package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Used to listen for collisions. This component is used to watch collision of 2 fixtures. The first being the
 * collision of the explosion AOE itself after the fire cracker goes off and the second being the fire cracker itself
 * as it should not bypass certain objects in the game world
 */
public class FireCrackerCollisionComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(FireCrackerCollisionComponent.class);
    short targetLayer = PhysicsLayer.NPC;
    short playerLayer = PhysicsLayer.PLAYER;
    short wallLayer = PhysicsLayer.WALL;
    private boolean explosionStatus = false;
    private boolean inflictDamageOnce = true;
    private PlayerCombatStatsComponent fireCrackerCombatStats;
    private HitboxComponent hitboxComponent;
    private ColliderComponent colliderComponent;
    private PhysicsMovementComponent physicsComponent;
    private final GameTime timeSource = ServiceLocator.getTimeSource();
    private static long endTimeFlameAOE = 0;
    private static long intervalFlameAOE = 0;
    private static final Vector2 HIDDEN_COORD = new Vector2(-10,-10);

    // duration lasts for 10s but only damages enemies every 2s
    private static final long AOE_DURATION = 10000;
    private static final long DAMAGE_INTERVAL = 2000;

    // enemies within area
    private final Set<Fixture> effectedEnemies = new HashSet<>();

    public FireCrackerCollisionComponent() {
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::fireCrackerOrExplosionCollide);
        entity.getEvents().addListener("collisionEnd", this::outsideOfAOE);
        fireCrackerCombatStats = entity.getComponent(PlayerCombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
        colliderComponent = entity.getComponent(ColliderComponent.class);
        physicsComponent = entity.getComponent(PhysicsMovementComponent.class);
    }

    @Override
    public void update() {
        if (explosionStatus) {
            // enemies will receive damage the moment fire cracker explodes
            if (inflictDamageOnce) {
                logger.info("Damage inflict first time in AOE!");
                inflictDamage();
                inflictDamageOnce = false;
            }

            // AOE is still around after explosion
            if (timeSource.getTime() < endTimeFlameAOE) {

                // AOE flame damages NPCs every 2 seconds
                if (timeSource.getTime() > intervalFlameAOE) {
                    logger.info("Damage inflict next 2 seconds in AOE!");
                    inflictDamage();
                    intervalFlameAOE = timeSource.getTime() + DAMAGE_INTERVAL;
                }

            } else {
                offExplosion();
            }
        }
    }

    /**
     * Sets current launch status of bomb. True will set explosion to occur which continuously attacks NPCs
     * within the area of fixture
     *
     * @param explode is set to start explosion fixture to continuously attack NPCs within the vicinity
     */
    public void setExplosion(boolean explode) {
        this.explosionStatus = explode;
        endTimeFlameAOE = timeSource.getTime() + AOE_DURATION;
        intervalFlameAOE = timeSource.getTime() + DAMAGE_INTERVAL;
    }

    /**
     * This resets fire cracker to its original state but turning off explosion (which prevents system from checking
     * and damaging entities within vicinity of AOE) and hides fire cracker entity once again
     */
    private void offExplosion() {
        this.explosionStatus = false;
        this.inflictDamageOnce = true;

        entity.setPosition(HIDDEN_COORD);
        entity.getComponent(ColliderComponent.class).setSensor(true);
        entity.getComponent(PhysicsMovementComponent.class).setMoving(false);
    }

    /**
     * Checks if explosion has occured, if it has, fire cracker should not be seen in game
     * @return explosion status of fire cracker
     */
    public boolean getExplosionStatus() {
        return this.explosionStatus;
    }

    /**
     * Dictates what happens to fire cracker upon colliding with invisible wall in game only. An additional
     * @param me the bullet that has been launched and moving in game world
     * @param other object which bullet collide with
     */
    private void fireCrackerOrExplosionCollide(Fixture me, Fixture other) {

        // this is for when fire cracker body itself hits a wall layer - this can include buildings, invisible walls
        // around levels and etc
        if (colliderComponent.getFixture() == me) {
            if (PhysicsLayer.contains(wallLayer, other.getFilterData().categoryBits)) {
                logger.info("Fire cracker hit something really big");
                entity.getComponent(PhysicsMovementComponent.class).setMoving(false);
            }
            return;
        }

        // this will only be activated once fire cracker stops moving - no damage will be dealt to enemy NPCs but
        // will track anything that is within the vicinity of AOE
        if (hitboxComponent.getFixture() == me && !physicsComponent.getMoving()) {
            // Not triggered by hitbox, ignore
            logger.info("FIRE CRACKER STOP MOVING, TRACKING ANY NPCS NEARBY");
            if (PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
                logger.info("ADD NPC NEARBY");
            }
        }


        // Get data of current bullet for checking
//        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
//        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
//
//        // collision can occur twice for 1 bullet and ensure that bullet has not been launched yet
//        if (this.explosionStatus) {
//            if (PhysicsLayer.contains(playerLayer, other.getFilterData().categoryBits)) {
//                logger.debug("Bullet may have collided with player's layer");
//                // bullet collide with obstacles
//            } else if (PhysicsLayer.contains(wallLayer, other.getFilterData().categoryBits)) {
//                logger.debug("Fire cracker collided with wall layer");
//
//                entity.getComponent(DisposingComponent.class).toBeReused();
//
//            } else if (PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
//                // bullet collides with NPC
//                logger.debug("Bullet collided with NPC's layer");
//
//                if (targetStats != null) {
//                    targetStats.hit(fireCrackerCombatStats.getBaseAttack());
//
//                    if (targetStats.isDead()) {
//                        ServiceLocator.getGameArea().despawnEntity(target);
//                    }
//                }
//                entity.getComponent(DisposingComponent.class).toBeReused();
//            }
//        }
    }

    private void outsideOfAOE(Fixture me, Fixture other) {

    }

    private void inflictDamage() {
        logger.info("Damage inflict in AOE!");
    }
}
