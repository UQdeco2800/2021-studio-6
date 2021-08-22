package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsComponent.AlignX;
import com.deco2800.game.physics.components.PhysicsComponent.AlignY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerAttackComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerAttackComponent.class);

    private final FixtureDef fixtureDef;
    private Fixture fixture;
    private CombatStatsComponent combatStats;
    private PlayerAttackComponent playerAttackComponent;
    private boolean meleeAttackClicked;
    private boolean closeToAttack;
    short targetLayer = PhysicsLayer.NPC;

    public PlayerAttackComponent() {
        fixtureDef = new FixtureDef();
    }

    @Override
    public void create() {
        if (fixtureDef.shape == null) {
            logger.trace("{} Setting default bounding box", this);
            fixtureDef.shape = makeBoundingBox();
        }

        Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
        fixture = physBody.createFixture(fixtureDef);
        setSensor(true);

        // event listeners to check if enemy is within range of melee attack
        entity.getEvents().addListener("collisionStart", this::onEnemyClose);

        combatStats = entity.getComponent(CombatStatsComponent.class);
        playerAttackComponent = entity.getComponent(PlayerAttackComponent.class);
    }

    /**
     * Called when collision with any fixture has occurred and sets closeToAttack
     * to be True. Event system will continuously check collision between objects in world
     * If enemy is close and player has clicked the attack melee button, damage will be dealt to
     * the NPC
     *
     * @param me is the fixture of current component
     * @param other is the other fixture which current component is in contact
     *              with
     */
    private void onEnemyClose(Fixture me, Fixture other) {

//        if (playerAttackComponent.getFixture() != me) {
//            // Not triggered by hitbox, ignore
//            return;
//        }

        // By default, should only try detect NPC layers only
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore - could be obstacle but not NPC
            return;
        }

        // Try to detect enemy.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            System.out.println("===============================");
            System.out.println("ENEMY INSIDE RANGE");
            closeToAttack = true;
        }

        //
        if (closeToAttack && meleeAttackClicked) {
            System.out.println("======================================");
            System.out.println("CLICKED AND ENEMY IS INSIDE MELEE RANGE");
            targetStats.hit(combatStats);
            this.meleeAttackClicked = false;
        }
    }

    /**
     * This is an indication that the melee attack button has been clicked
     *
     * @param clicked is the melee button clicked by player
    * */
    public void meleeAttackClicked(boolean clicked) {
        this.meleeAttackClicked = clicked;
    }

    /**
     * Set physics as a box with a given size. Box is centered around the entity.
     *
     * @param size size of the box
     * @return self
     */
    public PlayerAttackComponent setAsBox(Vector2 size) {
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
    public PlayerAttackComponent setAsBoxAligned(Vector2 size, AlignX alignX, AlignY alignY) {
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
    public PlayerAttackComponent setAsBox(Vector2 size, Vector2 position) {
        PolygonShape bbox = new PolygonShape();
        bbox.setAsBox(size.x, size.y, position, 0f);
        setShape(bbox);
        return this;
    }

    /**
     * Set friction. This affects the object when touching other objects, but does not affect friction
     * with the ground.
     *
     * @param friction friction, default = 0
     * @return self
     */
    public PlayerAttackComponent setFriction(float friction) {
        if (fixture == null) {
            fixtureDef.friction = friction;
        } else {
            fixture.setFriction(friction);
        }
        return this;
    }

    /**
     * Set whether this physics component is a sensor. Sensors don't collide with other objects but
     * still trigger collision events. See: https://www.iforce2d.net/b2dtut/sensors
     *
     * @param isSensor true if sensor, false if not. default = false.
     * @return self
     */
    public PlayerAttackComponent setSensor(boolean isSensor) {
        if (fixture == null) {
            fixtureDef.isSensor = isSensor;
        } else {
            fixture.setSensor(isSensor);
        }
        return this;
    }

    /**
     * Set density
     *
     * @param density Density and size of the physics component determine the object's mass. default =
     *     0
     * @return self
     */
    public PlayerAttackComponent setDensity(float density) {
        if (fixture == null) {
            fixtureDef.density = density;
        } else {
            fixture.setDensity(density);
        }
        return this;
    }

    /**
     * Set restitution
     *
     * @param restitution restitution is the 'bounciness' of an object, default = 0
     * @return self
     */
    public PlayerAttackComponent setRestitution(float restitution) {
        if (fixture == null) {
            fixtureDef.restitution = restitution;
        } else {
            fixture.setRestitution(restitution);
        }
        return this;
    }

    /**
     * Set shape
     *
     * @param shape shape, default = bounding box the same size as the entity
     * @return self
     */
    public PlayerAttackComponent setShape(Shape shape) {
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
     * Set the collider layer, used in collision logic
     * @param layerMask Bitmask of {@link PhysicsLayer} this collider belongs to
     * @return self
     */
    public PlayerAttackComponent setLayer(short layerMask) {
        if (fixture == null) {
            fixtureDef.filter.categoryBits = layerMask;
        } else {
            Filter filter = fixture.getFilterData();
            filter.categoryBits = layerMask;
            fixture.setFilterData(filter);
        }
        return this;
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

    @Override
    public void dispose() {
        super.dispose();
        Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
        if (physBody.getFixtureList().contains(fixture, true)) {
            physBody.destroyFixture(fixture);
        }
    }

    private Shape makeBoundingBox() {
        PolygonShape bbox = new PolygonShape();
        Vector2 center = entity.getScale().scl(1f);
        bbox.setAsBox(center.x, center.y, center, 0f);
        return bbox;
    }


}
