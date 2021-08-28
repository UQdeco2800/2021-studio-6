package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
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
    private boolean launchStatus = false;
    private boolean collideStatus = false;
    private PlayerCombatStatsComponent bulletCombatStats;

    public BulletCollisionComponent() {
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::bulletHit);
        bulletCombatStats = entity.getComponent(PlayerCombatStatsComponent.class);
    }

    @Override
    public void update() {
    }

    /**
     * To update collision status of bullet
     *
     * @param collided is to let system know whether bullet has collided. Depending on how frame
     *                 is refreshed, bullet can sometimes collide twice
     */
    public void setBulletCollisionStatus(boolean collided) {
        this.collideStatus = false;
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

    public void bulletHit(Fixture me, Fixture other) {

        // Get data of current bullet for checking
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);


        // collision can occur twice for 1 bullet and ensure that bullet has not been launched yet
        if (this.launchStatus && !this.collideStatus) {
            System.out.println("Bullet entity : " + entity);
            if (PhysicsLayer.contains(playerLayer, other.getFilterData().categoryBits)) {
                logger.debug("Bullet may have collided with player's layer");
                // bullet collide with obstacles
            } else if (PhysicsLayer.contains(obstacleLayer, other.getFilterData().categoryBits)) {
                logger.debug("Bullet collided with obstacle's layer");
                setBulletCollisionStatus(true);

                entity.getComponent(DisposingComponent.class).toBeReused();

            } else if (PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
                // bullet collides with NPC
                logger.debug("Bullet collided with NPC's layer");
                setBulletCollisionStatus(true);

                if (targetStats != null) {
                    targetStats.hit(bulletCombatStats);

                    if (targetStats.isDead()) {
                        target.getComponent(DisposingComponent.class).toBeDisposed();
                    }
                }
                entity.getComponent(DisposingComponent.class).toBeReused();
            }
        }
    }
}
