package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.lighting.PointLightComponent;
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
    private PlayerCombatStatsComponent playerCombatStatsComponent;
    private HitboxComponent hitboxComponent;
    private ColliderComponent colliderComponent;
    private PhysicsMovementComponent physicsComponent;
    private final GameTime timeSource = ServiceLocator.getTimeSource();
    private static long endTimeFlameAOE = 0;
    private static long intervalFlameAOE = 0;
    private static final Vector2 HIDDEN_COORD = new Vector2(-10,-10);

    // duration lasts for 6s but only damages enemies every 2s
    private static final long AOE_DURATION = 6000;
    private static final long DAMAGE_INTERVAL = 2000;

    // enemies within area will be inflicted damage and will be removed upon leaving
    private final Set<Fixture> effectedEnemies = new HashSet<>();
    private final Set<Fixture> uneffectedEnemies = new HashSet<>();
    private int light = 8;

    private final int EXPLOSION_MULTIPLIER = 2;
    private final int DAMAGE_MULTIPLIER = 1;

    public FireCrackerCollisionComponent() {
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::fireCrackerOrExplosionCollide);
        entity.getEvents().addListener("collisionEnd", this::outsideOfAOE);
        playerCombatStatsComponent = entity.getComponent(PlayerCombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
        colliderComponent = entity.getComponent(ColliderComponent.class);
        physicsComponent = entity.getComponent(PhysicsMovementComponent.class);
    }

    @Override
    public void update() {
        // get rid of registered NPCs which should no longer be tracked
        if (!uneffectedEnemies.isEmpty()) {
            unregisterEnemies();
        }

        // fire cracker has exploded
        if (explosionStatus) {
            this.getEntity().getComponent(PointLightComponent.class).changeDistance(light);
            // enemies will receive damage the moment fire cracker explodes
            if (inflictDamageOnce) {
                inflictDamage(EXPLOSION_MULTIPLIER);
                inflictDamageOnce = false;
            }

            // AOE is still around after explosion
            if (timeSource.getTime() < endTimeFlameAOE) {


                if (timeSource.getTime() ==(intervalFlameAOE/2)) {
                    this.getEntity().getComponent(PointLightComponent.class).changeDistance(light -= 1);
                }

                // AOE flame damages NPCs every 2 seconds
                if (timeSource.getTime() > intervalFlameAOE) {
                    this.getEntity().getComponent(PointLightComponent.class).changeDistance(light -= 1);
                    logger.debug("Has been 2 seconds in game, damage will be dealt");
                    inflictDamage(DAMAGE_MULTIPLIER);
                    intervalFlameAOE = timeSource.getTime() + DAMAGE_INTERVAL;
                }

            } else {
                this.getEntity().getComponent(PointLightComponent.class).changeDistance(0);
                this.getEntity().getEvents().trigger("endFirecracker");
                offExplosion();
            }
        }
    }

    /**
     * Sets current launch status of fire cracker. True will set explosion to occur which continuously attacks NPCs
     * within the area of fixture AOE
     *
     * @param explode is set to start explosion fixture to continuously attack NPCs within the vicinity
     */
    public void setExplosion(boolean explode) {
        this.explosionStatus = explode;
        endTimeFlameAOE = timeSource.getTime() + AOE_DURATION;
        intervalFlameAOE = timeSource.getTime() + DAMAGE_INTERVAL;
    }

    /**
     * This resets fire cracker to its original state by turning off explosion (which prevents system from checking
     * and damaging entities within vicinity of AOE) and hides fire cracker entity once again
     */
    private void offExplosion() {
        this.explosionStatus = false;
        this.inflictDamageOnce = true;

        entity.setPosition(HIDDEN_COORD);
        entity.getComponent(ColliderComponent.class).setSensor(true);
        entity.getComponent(PhysicsMovementComponent.class).setMoving(false);
        effectedEnemies.clear();
    }

    /**
     * Checks if explosion has occured, if it has, fire cracker should not be seen in game
     * @return explosion status of fire cracker
     */
    public boolean getExplosionStatus() {
        return this.explosionStatus;
    }

    /**
     * Dictates what happens to fire cracker upon colliding with invisible wall in game and when nay entity collides
     * with the AOE flaming fixture. Fire cracker should not be able to easily bypass tall objects (like buildings or
     * lamps perhaps - these are designated with wall physic layer which prevents fire cracker from flying over).
     * When fire cracker stops moving, system starts to track and find target layer (enemies) and upon explosion, if
     * enemy NPCs are still within flaming AOE fixture, they will recceive damage every 2s for 10s.
     * @param me - fire cracker fixture that has been launched and moving in game world along with AOE flaming fixture
     * @param other object which fire cracker fixture or AOE flaming fixture has collide with
     */
    private void fireCrackerOrExplosionCollide(Fixture me, Fixture other) {

        // this is for when fire cracker body itself hits a wall layer - this can include buildings, invisible walls
        // around levels and etc
        if (colliderComponent.getFixture() == me) {
            if (PhysicsLayer.contains(wallLayer, other.getFilterData().categoryBits)) {
                this.getEntity().getEvents().trigger("pauseFirecracker");
                logger.debug("Fire cracker collided with a very large game object");
                entity.getComponent(PhysicsMovementComponent.class).setMoving(false);
            }
            return;
        }

        // this will only be activated once fire cracker stops moving - no damage will be dealt to enemy NPCs but
        // will track anything that is within the vicinity of AOE
        if (hitboxComponent.getFixture() == me) {

            // if enemy NPC is not registered yet, register it for damage inflicting later
            if (PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits) && !
                    effectedEnemies.contains(other)) {
                logger.debug("Enemy should be added to be inflicted with damage");
                effectedEnemies.add(other);
            }
        }
    }

    /**
     * This is important when an NPC (which has been registered when it entered AOE flaming fixture upon explosion or
     * before) as it will unregister NPC from list as it should not be receiving damage when it leaves flaming AOE
     * fixture
     * @param me - fire cracker fixture that has been launched and moving in game world along with AOE flaming fixture
     * @param other object which fire cracker fixture or AOE flaming fixture has ended collide with
     */
    private void outsideOfAOE(Fixture me, Fixture other) {
        // this will remove any registered enemy NPC from register that tracks NPCs that will need to be inflicted
        // with damage every 2 seconds when npc is no longer colliding with fixture
        if (hitboxComponent.getFixture() == me) {
            // enemy NPC has left AOE fixture, unregister it in update method
            if (PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits) &&
                    effectedEnemies.contains(other)) {
                logger.debug("NPC is no longer within AOE fixture, will be removed");
                uneffectedEnemies.add(other);
            }
        }
    }

    /**
     * This inflicts damage to enemy fixture registered and despawns them when enemy health reaches zero
     *
     * @param multiplier used to increase damage output when fire cracker explodes initially
     */
    private void inflictDamage(int multiplier) {
        for (Fixture fixture : effectedEnemies) {
            logger.debug("Damage will now be dealt to enemies in flaming AOE fixture");
            Entity enemy = ((BodyUserData) fixture.getBody().getUserData()).entity;
            CombatStatsComponent targetStats = enemy.getComponent(CombatStatsComponent.class);

            targetStats.hit(playerCombatStatsComponent.getBaseRangedAttack() * multiplier);

            if (targetStats.isDead()) {
                uneffectedEnemies.add(fixture);
                ServiceLocator.getGameArea().despawnEntity(enemy);
            }
        }
        unregisterEnemies();
    }

    /**
     * Method used to remove fixtures that should no longer be continuously track for damage inflicting purposes and
     * clears unaffected enemies set as well
     */
    private void unregisterEnemies() {
        for (Fixture fixture : uneffectedEnemies) {
            effectedEnemies.remove(fixture);
        }
        uneffectedEnemies.clear();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.getEntity().getEvents().trigger("dispose");
    }
}
