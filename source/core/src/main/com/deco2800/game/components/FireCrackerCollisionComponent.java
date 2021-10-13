package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private PlayerCombatStatsComponent fireCrackerCombatStats;
    private HitboxComponent hitboxComponent;

    public FireCrackerCollisionComponent() {
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::explosionHit);
        fireCrackerCombatStats = entity.getComponent(PlayerCombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    @Override
    public void update() {
    }

    /**
     * Sets current launch status of bomb. True will set explosion to occur which continuously attacks NPCs
     * within the area of fixture
     *
     * @param explode is set to start explosion fixture to continuously attack NPCs within the vicinity
     */
    public void setExplosion(boolean explode) {
        this.explosionStatus = explode;
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
    private void explosionHit(Fixture me, Fixture other) {

        // Get data of current bullet for checking
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);

        // collision can occur twice for 1 bullet and ensure that bullet has not been launched yet
        if (this.explosionStatus) {
            if (PhysicsLayer.contains(playerLayer, other.getFilterData().categoryBits)) {
                logger.debug("Bullet may have collided with player's layer");
                // bullet collide with obstacles
            } else if (PhysicsLayer.contains(wallLayer, other.getFilterData().categoryBits)) {
                logger.debug("Fire cracker collided with wall layer");

                entity.getComponent(DisposingComponent.class).toBeReused();

            } else if (PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
                // bullet collides with NPC
                logger.debug("Bullet collided with NPC's layer");

                if (targetStats != null) {
                    targetStats.hit(fireCrackerCombatStats.getBaseAttack());

                    if (targetStats.isDead()) {
                        ServiceLocator.getGameArea().despawnEntity(target);
                    }
                }
                entity.getComponent(DisposingComponent.class).toBeReused();
            }
        }
    }
}
