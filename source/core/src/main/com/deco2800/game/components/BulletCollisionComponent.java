package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to listen for collisions. Collisions for obstacles and NPCs (causing damage to it) will
 * cause bullet to be kept and reuse again and since bullet spawns in the same location as player,
 * player layer will be ignored.
 */
public class BulletCollisionComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(BulletCollisionComponent.class);
    short targetLayer = PhysicsLayer.NPC;
    short playerLayer = PhysicsLayer.PLAYER;
    short obstacleLayer = PhysicsLayer.OBSTACLE;
    short wallLayer = PhysicsLayer.WALL;
    private boolean launchStatus = false;
    private PlayerCombatStatsComponent bulletCombatStats;

    public BulletCollisionComponent() {
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::bulletHit);
        entity.getEvents().addListener("rockDone", this::disposeRock);
        bulletCombatStats = entity.getComponent(PlayerCombatStatsComponent.class);
    }

    @Override
    public void update() {
    }

    /**
     * Sets current launch status of bullet. False being bullets not fired yet by user
     *
     * @param launched is to let system know whether bullet has been launched by system
     */
    public void setBulletLaunchStatus(boolean launched) {
        this.launchStatus = launched;
    }

    /**
     * Gives current launch status of bullet Used later on to check if collisions are detected in
     * game before launching bullets
     * @return the launch status of bullets
     */
    public boolean getBulletLaunchStatus() {
        return this.launchStatus;
    }

    /**
     * Dictates what happens to bullets when it collides with different objects
     * in the game world
     * @param me the bullet that has been launched and moving in game world
     * @param other object which bullet collide with
     */
    private void bulletHit(Fixture me, Fixture other) {

        // Get data of current bullet for checking
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);

        // collision can occur twice for 1 bullet and ensure that bullet has not been launched yet
        if (this.launchStatus) {
            if (PhysicsLayer.contains(playerLayer, other.getFilterData().categoryBits)) {
                logger.debug("Bullet may have collided with player's layer");
                // bullet collide with obstacles
            } else if ((PhysicsLayer.contains(obstacleLayer, other.getFilterData().categoryBits)) ||
                    (PhysicsLayer.contains(wallLayer, other.getFilterData().categoryBits))) {
                this.entity.getComponent(PhysicsMovementComponent.class).setMoving(false);
                entity.getEvents().trigger("breakRock");
                logger.debug("Bullet collided with obstacle's or level wall's layer");

                //entity.getComponent(DisposingComponent.class).toBeReused();

            } else if (PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
                this.entity.getComponent(PhysicsMovementComponent.class).setMoving(false);
                entity.getEvents().trigger("breakRock");
                //entity.getEvents().trigger("stopRock", this);
                // bullet collides with NPC
                logger.debug("Bullet collided with NPC's layer");

                if (targetStats != null) {
                    targetStats.hit(bulletCombatStats.getBaseRangedAttack());

                    if (targetStats.isDead()) {
                        ServiceLocator.getGameArea().despawnEntity(target);
                    }
                }
            }
        }
    }

    /**
     * Function called when the rock is ready to be disposed, after the animation is finished
     */
    private void disposeRock() {
        entity.getComponent(DisposingComponent.class).toBeReused();
    }
}
