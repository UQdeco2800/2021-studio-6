package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.player.hud.PlayerHealthAnimationController;
import com.deco2800.game.components.player.hud.PlayerHudAnimationController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseWeaponConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.items.Directions;
import com.deco2800.game.items.Items;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsComponent.AlignX;
import com.deco2800.game.physics.components.PhysicsComponent.AlignY;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.*;

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

    private FixtureDef fixtureDef;
    private FixtureDef fixtureDefLast;
    private final FixtureDef fixtureDefW;
    private final FixtureDef fixtureDefA;
    private final FixtureDef fixtureDefS;
    private final FixtureDef fixtureDefD;
    private Fixture fixture;
    private boolean meleeAttackClicked;
    private boolean closeToAttack;
    short targetLayer = PhysicsLayer.NPC;
    private final Set<Fixture> closeEnemies = new HashSet<>();
    private final Set<Fixture> removingEnemies = new HashSet<>();


    private final GameTime timeSource = ServiceLocator.getTimeSource();
    private boolean canAttack = true;
    private boolean setShapes = false;

    private int damage;
    private int knockback;
    private float length;
    private float height;
    private int attackLength; // in milliseconds
    private String atlasFile;
    private String weaponFile;
    private Float[][] animationCords;
    private long disposeTime = 0;
    private IndependentAnimator weaponAnimator;
    private boolean gotWeapon = false;
    private long attackEnd = 0;
    private Items weaponType;

    public PlayerMeleeAttackComponent(String weaponConfig) {
        fixtureDef = new FixtureDef();
        fixtureDefW = new FixtureDef();
        fixtureDefA = new FixtureDef();
        fixtureDefS = new FixtureDef();
        fixtureDefD = new FixtureDef();
        fixtureDefLast = fixtureDefW;
        weaponFile = weaponConfig;
    }

    @Override
    public void create() {
        // event listeners to check if enemy is within range of melee attack or not
        entity.getEvents().addListener("collisionStart", this::onEnemyClose);
        entity.getEvents().addListener("collisionEnd", this::onEnemyFar);
        entity.getEvents().addListener("attackStart", this::attack);
        entity.getEvents().addListener("walk", this::walk);
        setWeapon(weaponFile);
    }

    /**
     * Used to set the weapon stats and animations. Called either when the component is first instanced
     * or later when the player changes weapon. Has to check if the player already
     * had a weapon to cancel and remove any previous lingering elements.
     * @param weaponConfig The config file for the specified weapon to be set to
     */
    public void setWeapon(String weaponConfig) {
        weaponFile = weaponConfig;
        BaseWeaponConfig stats =
            FileLoader.readClass(BaseWeaponConfig.class, weaponConfig);

        damage = stats.attackDamage;
        knockback = stats.knockback;
        length = stats.length;
        height = stats.height;
        attackLength = stats.attackLength; // in milliseconds
        atlasFile = stats.atlasPath;
        animationCords = stats.animationCord;
        PlayerWeaponAnimationController setWeapon = this.entity.getComponent(PlayerWeaponAnimationController.class);
        if (gotWeapon) {
            setWeapon.stop();
        }
        if (setWeapon != null) {
            weaponAnimator =
                new IndependentAnimator(
                    ServiceLocator.getResourceService()
                        .getAsset(atlasFile, TextureAtlas.class), false);
            weaponAnimator.addAnimation("attackUp", stats.animationLength, Animation.PlayMode.NORMAL);
            weaponAnimator.addAnimation("attackDown", stats.animationLength, Animation.PlayMode.NORMAL);
            weaponAnimator.addAnimation("attackLeft", stats.animationLength, Animation.PlayMode.NORMAL);
            weaponAnimator.addAnimation("attackRight", stats.animationLength, Animation.PlayMode.NORMAL);
            weaponAnimator.setCamera(true);
            weaponAnimator.setScale(length * stats.scale[0], height * stats.scale[1]);
            setAnimations(setWeapon);
            gotWeapon = true;
        }
        attackEnd = 0;
        setShapes();
    }

    /**
     * Returns the config filepath that player currently uses for melee weapon.
     * @return file path of weapon config file
     */
    public String getWeapon() {
        return weaponFile;
    }

    /**
     * Keeps track of the type of weapon player is currently holding using ENUMs created. Used for player
     * state tracking and shop popup box differentiation of different types of melee weapons
     * @return type of weapon player is currently holding
     */
    public Items getMeleeWeaponType() {
        return weaponType;
    }

    /**
     * Set melee weapon type that player is currently holding
     * @param weaponType that player is holding
     */
    public void setMeleeWeaponType(Items weaponType) {
        this.weaponType = weaponType;
    }

    /**
     * Sets the animations for the weaponAnimator after a certain point to allow assets to load
     * @param setWeapon the reference to the animation controller to be set on
     */
    public void setAnimations(PlayerWeaponAnimationController setWeapon) {
        setWeapon.setter(animationCords);
    }

    /**
     * Gets the animator for the weapons
     * @return IndependentAnimator for the weapon attack
     */
    public IndependentAnimator getAnimator() {
        return weaponAnimator;
    }


    /**
     * Triggered when the player walks in a direction.
     *
     * @param walkDirection direction that the player walked
     */
    private void walk(Vector2 walkDirection) {
        if (fixture != null) {
            toggleFixtures();
        }
    }

    /**
     * Getter for direction of player movement.
     *
     * @return FixtureDef for the last walked direction
     */
    private FixtureDef getFixDirection() {
        KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
        if (key != null) {
            Directions direct = key.getDirection();
            switch (direct) {
                case MOVE_UP:
                    fixtureDefLast = fixtureDefW;
                    return fixtureDefW;
                case MOVE_DOWN:
                    fixtureDefLast = fixtureDefS;
                    return fixtureDefS;
                case MOVE_LEFT:
                    fixtureDefLast = fixtureDefA;
                    return fixtureDefA;
                case MOVE_RIGHT:
                    fixtureDefLast = fixtureDefD;
                    return fixtureDefD;
            }
        }
        return fixtureDefLast;
    }

    /**
     * Sets the bounding boxes of the weapon collision based on the weapons stats
     */
    private void setShapes() {
        PolygonShape bbox = new PolygonShape();
        Vector2 center = entity.getScale().scl(0.5f);
        bbox.setAsBox(center.x * length, center.y*height, center.add( 0 , (float)0.8), 0f);
        fixtureDefW.shape = bbox;
        bbox = new PolygonShape();
        center = entity.getScale().scl(0.5f);
        bbox.setAsBox(center.x * height, center.y*length, center.add( (float)-0.8 , 0), 0f);
        fixtureDefA.shape = bbox;
        bbox = new PolygonShape();
        center = entity.getScale().scl(0.5f);
        bbox.setAsBox(center.x * height, center.y*length, center.add( (float) 0.8 , 0), 0f);
        fixtureDefD.shape = bbox;
        bbox = new PolygonShape();
        center = entity.getScale().scl(0.5f);
        bbox.setAsBox(center.x * length, center.y*height, center.add( 0 , (float)-0.8), 0f);
        fixtureDefS.shape = bbox;
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
     * @param other is the other fixture which current component is in contact with
     */
    private void onEnemyClose(Fixture me, Fixture other) {
        // By default, should only try detect NPC layers only
        if (!PhysicsLayer.contains(PhysicsLayer.WEAPON, me.getFilterData().categoryBits)) {
            return;
        }
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
            damage();
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
            for (Fixture enemy : closeEnemies) {
                Entity target = ((BodyUserData) enemy.getBody().getUserData()).entity;
                CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
                // enemy within range and player clicked melee attack button
                if (closeToAttack && meleeAttackClicked && targetStats != null) {
                    targetStats.hit(damage);

                    if (targetStats.isDead()) {
                        removingEnemies.add(enemy);
                        this.getEntity().getComponent(PlayerAbilitiesComponent.class).reduceCooldown();
                    } else {
                        // Apply knockback
                        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
                        if (physicsComponent != null && knockback > 0f) {
                            Body targetBody = physicsComponent.getBody();
                            Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
                            Vector2 impulse = direction.setLength(knockback * 100f);
                            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
                        }
                    }
                }
            }
            this.meleeAttackClicked = false;
            for (Fixture removable : removingEnemies) {
                closeEnemies.removeIf(removable::equals);
            }
            removingEnemies.clear();
    }

    /**
     * Triggered when the player presses the attack button. Handles creating the
     * fixtures for the attack and prepping before actual the damage/hit. Also handles
     * attack length and duration.
     */
    private void attack() {
        if (timeSource.getTime() >= attackEnd) {
            if (canAttack) {
                fixtureDef = getFixDirection();
                if (fixtureDef != null) {
                    canAttack = false;
                    this.meleeAttackClicked = true;
                    Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
                    fixture = physBody.createFixture(fixtureDef);
                    fixture.getFilterData().categoryBits = PhysicsLayer.WEAPON;
                    // if sensor is false, NPC will not be able to collide with player's fixture
                    setSensor(true);
                    disposeTimeSet();
                    attackEnd = attackLength + timeSource.getTime();
                    entity.getEvents().trigger("attack");
                }
            }
        }
    }

    /**
     * Timed used to dispose of the weapon fixture based on attack length.
     */
    private void disposeTimeSet() {
        disposeTime = timeSource.getTime() + attackLength;
    }

    /**
     * Checks to dispose of the weapon fixture.
     */
    @Override
    public void update() {
        if (disposeTime < timeSource.getTime() && fixture != null) {
            toggleFixtures();
        }
    }

    /**
     * Similar to dispose() but also destroys the weapon fixture.
     * Doing so in dispose() crashes things on account of update locks.
     */
    public void toggleFixtures() {
        if (weaponAnimator != null) {
            weaponAnimator.stopAnimation();
        }
        Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
        if (physBody.getFixtureList().contains(fixture, true) && fixture != null) {
            fixture.getFilterData().categoryBits = PhysicsLayer.DEFAULT;
            physBody.destroyFixture(fixture);
            fixture = null;
        }
        canAttack = true;
    }

    /**
     * Dispose for the weapon requires stopping the animation (visual element)
     * and changing/disposing the attack fixture.
     */
    @Override
    public void dispose() {
        if (weaponAnimator != null) {
            weaponAnimator.stopAnimation();
        }
        Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
        if (physBody.getFixtureList().contains(fixture, true) && fixture != null) {
            fixture.getFilterData().categoryBits = PhysicsLayer.DEFAULT;
            fixture = null;
        }
        canAttack = true;
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
}