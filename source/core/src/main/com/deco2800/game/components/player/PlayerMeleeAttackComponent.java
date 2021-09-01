package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsComponent.AlignX;
import com.deco2800.game.physics.components.PhysicsComponent.AlignY;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashSet;
import java.util.Set;

/**
 * When entity is within range of enemy and melee attack button is clicked, damage is dealt
 * to the NPC only.
 *
 * <p>Requires PlayerCombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class PlayerMeleeAttackComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerMeleeAttackComponent.class);

    private final FixtureDef fixtureDef;
    private Fixture fixture;
    private PlayerCombatStatsComponent playerCombatStats;
    private boolean meleeAttackClicked;
    private boolean closeToAttack;
    private Vector2 directionMove;
    short targetLayer = PhysicsLayer.NPC;
    private final Set<Fixture> closeEnemies = new HashSet<>();
    private final Set<Fixture> removingEnemies = new HashSet<>();

    public PlayerMeleeAttackComponent() {
        fixtureDef = new FixtureDef();
        directionMove = new Vector2((float)0.0,(float)0.0);
    }

    @Override
    public void create() {

        // event listeners to check if enemy is within range of melee attack or not
        entity.getEvents().addListener("collisionStart", this::onEnemyClose);
        entity.getEvents().addListener("collisionEnd", this::onEnemyFar);
        entity.getEvents().addListener("attack", this::Attack);
        entity.getEvents().addListener("walk", this::Walk);

        playerCombatStats = entity.getComponent(PlayerCombatStatsComponent.class);

    }

    /**
     * Triggered when the player walks in a direction.
     *
     * @param walkDirection direction that the player walked
     */
    private void Walk(Vector2 walkDirection) {
        if (fixture != null) {
            dispose();
        }
        setDirection(walkDirection);
    }

    /**
     * Getter for direction of player movement.
     *
     * @return player last walked direction
     */
    private Vector2 getDirection() {
        return directionMove;
    }

    /**
     * Setter for direction of player movement.
     *
     * @param direction the direction that the player last walked in.
     */
    private void setDirection(Vector2 direction) {
        this.directionMove = direction.cpy();
    }


    /**
     * When player and enemy no longer in contact, reset variable closeToAttack. Only resets closeToAttack
     * variable - there is a bug where player can click melee button before colliding with enemy and upon
     * collision, enemy receives damage immediately
     */
    private void onEnemyFar(Fixture me, Fixture other) {
        closeEnemies.remove(other);
        if (closeEnemies.size() == 0) {
            this.closeToAttack = false;
        }
    }

    /**
     * Called when collision with any fixture has occurred and sets closeToAttack
     * to be True. Event system will continuously check collision between objects in world
     * If enemy is close and player has clicked the attack melee button, player
     * will be allowed to damage them.
     *
     * @param me is the fixture of current component
     * @param other is the other fixture which current component is in contact
     *              with
     */
    private void onEnemyClose(Fixture me, Fixture other) {

        // By default, should only try detect NPC layers only
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore - could be obstacle but not NPC
            return;
        }

        // Try to detect enemy.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            closeToAttack = true;
            closeEnemies.add(other);
        }
    }

    /**
     * Function to handle actually hitting an enemy. Gets called after the melee
     * attack has been triggered and the collision and fixtures are set.
     *
     * Goes through each enemy close to the player and damages them individually.
     * Afterwards, goes through the set of killed enemies and removes them.
     */
    private void damage() {
        for (Fixture enemy: closeEnemies) {
            Entity target = ((BodyUserData) enemy.getBody().getUserData()).entity;
            CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
            // enemy within range and player clicked melee attack button
            if (closeToAttack && meleeAttackClicked && targetStats != null) {
                targetStats.hit(playerCombatStats);
                // freezes enemy - will need to be replaced to despawn enemy entity
                if (targetStats.isDead()) {
                    removingEnemies.add(enemy);
                    target.getComponent(DisposingComponent.class).toBeDisposed();
                }
            }
        }
        this.meleeAttackClicked = false;
        for (Fixture removable: removingEnemies) {
            closeEnemies.removeIf(removable::equals);
        }
        removingEnemies.clear();
    }
    /**
     * Triggered when the player presses the attack button. Handles creating
     * the fixtures for the attack and prepping before actual the damage/hit.
     */
    private void Attack() {
        if (fixtureDef.shape == null) {
            logger.trace("{} Setting default bounding box", this);
            fixtureDef.shape = makeBoundingBox();
        }

        this.meleeAttackClicked = true;
        Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
        fixture = physBody.createFixture(fixtureDef);

        // if sensor is false, NPC will not be able to collide with player's fixture
        setSensor(true);
        damage();
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
        if (physBody.getFixtureList().contains(fixture, true) && fixture != null) {
            physBody.destroyFixture(fixture);
        }
        fixtureDef.shape = null;
    }

    /**
     * This is an indication that the melee attack button has been clicked
     *
     * @param clicked is the melee button clicked by player
     * */
    public void meleeAttackClicked(boolean clicked) {
        if (closeToAttack) {
            this.meleeAttackClicked = clicked;
        }
    }

    /**
     * Set physics as a box with a given size. Box is centered around the entity.
     *
     * @param size size of the box
     * @return self
     */
    public PlayerMeleeAttackComponent setAsBox(Vector2 size) {
        return setAsBox(size, entity.getCenterPosition());
    }

    /**
     * Set physics as a box with a given size. Box is aligned based on alignment.
     *
     * @param size size of the box
     * @param alignX how to align x relative to entity
     * @param alignY how to align y relative to entity
     * @return self
     */
    public PlayerMeleeAttackComponent setAsBoxAligned(Vector2 size, AlignX alignX, AlignY alignY) {
        Vector2 position = new Vector2();
        switch (alignX) {
            case LEFT:
                position.x = size.x / 2;
                break;
            case CENTER:
                position.x = entity.getCenterPosition().x;
                break;
            case RIGHT:
                position.x = entity.getScale().x - (size.x / 2);
                break;
        }

        switch (alignY) {
            case BOTTOM:
                position.y = size.y / 2;
                break;
            case CENTER:
                position.y = entity.getCenterPosition().y;
                break;
            case TOP:
                position.y = entity.getScale().y - (size.y / 2);
                break;
        }

        return setAsBox(size, position);
    }

    /**
     * Set physics as a box with a given size and local position. Box is centered around the position.
     *
     * @param size size of the box
     * @param position position of the box center relative to the entity.
     * @return self
     */
    public PlayerMeleeAttackComponent setAsBox(Vector2 size, Vector2 position) {
        PolygonShape bbox = new PolygonShape();
        bbox.setAsBox(size.x / 2, size.y / 2, position, 0f);
        setShape(bbox);
        return this;
    }

    /**
     * Set whether this physics component is a sensor. Sensors don't collide with other objects but
     * still trigger collision events. See: https://www.iforce2d.net/b2dtut/sensors
     *
     * @param isSensor true if sensor, false if not. default = false.
     * @return self
     */
    public PlayerMeleeAttackComponent setSensor(boolean isSensor) {
        if (fixture == null) {
            fixtureDef.isSensor = isSensor;
        } else {
            fixture.setSensor(isSensor);
        }
        return this;
    }

    /**
     * Set shape
     *
     * @param shape shape, default = bounding box the same size as the entity
     * @return self
     */
    public PlayerMeleeAttackComponent setShape(Shape shape) {
        if (fixture == null) {
            fixtureDef.shape = shape;
        } else {
            logger.error("{} Cannot set Collider shape after create(), ignoring.", this);
        }
        return this;
    }

    /** @return Physics fixture of this collider. Null before created() */
    public Fixture getFixture() {
        return fixture;
    }

    /**
     * @return The {@link PhysicsLayer} this collider belongs to
     */
    public short getLayer() {
        if (fixture == null) {
            return fixtureDef.filter.categoryBits;
        }
        return fixture.getFilterData().categoryBits;
    }

    /**
     * Enlarge shape of fixture based on fixture size of entity
     *
     * @return enlarge fixture and places it on the center of the entity
     */
    private Shape makeBoundingBox() {

        PolygonShape bbox = new PolygonShape();
        Vector2 center = entity.getScale().scl(0.5f);
        // width and height enlarge - this is the range of melee attack for player

        if (directionMove.epsilonEquals(Vector2Utils.UP)){
            bbox.setAsBox(center.x * 2, center.y*(float)0.5, center.add( 0 , (float)0.7), 0f);
        }

        else if (directionMove.epsilonEquals(Vector2Utils.DOWN)){
            bbox.setAsBox(center.x * 2, center.y*(float)0.5, center.add( 0 , (float)-0.7), 0f);
        }

        else if (directionMove.epsilonEquals(Vector2Utils.LEFT)){
            bbox.setAsBox(center.x * (float)0.5, center.y*2, center.add( (float)-0.7 , 0), 0f);
        }

        else if (directionMove.epsilonEquals(Vector2Utils.RIGHT)){
            bbox.setAsBox(center.x * (float)0.5, center.y*2, center.add( (float) 0.7 , 0), 0f);
        }
        return bbox;
    }



}