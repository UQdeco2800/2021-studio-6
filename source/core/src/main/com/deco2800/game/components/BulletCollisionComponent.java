package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.PlayerRangeAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
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
    private final Vector2 ORIGIN = new Vector2(0,0);
    private boolean launchStatus = false;

    public BulletCollisionComponent() {
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::bulletHit);
    }

    @Override
    public void update() {
    }

    /**
     * Sets current launch status of bullet. False being bullets not fired yet by user
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
//        System.out.println("Target layer " +  target.getComponent(ColliderComponent.class).getLayer());

        if (this.launchStatus) {
            if (PhysicsLayer.contains(playerLayer, other.getFilterData().categoryBits)) {
                return;

                // bullet collide with obstacles
            } else if (PhysicsLayer.contains(obstacleLayer, other.getFilterData().categoryBits)) {
                System.out.println("COLLIDE WITH SOMETHING NOT NPC");
                this.entity.setPosition(ORIGIN);
                this.entity.getComponent(PhysicsMovementComponent.class).setTarget(ORIGIN);
                PlayerRangeAttackComponent.restockBulletShot(this.entity);

                // bullet collides with NPC
            } else if (PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
                System.out.println("COLLIDE WITH NPC");
                System.out.println("Bullet collided");
//            this.entity.setPosition(ORIGIN);
//            this.entity.getComponent(PhysicsMovementComponent.class).setTarget(ORIGIN);
//            PlayerRangeAttackComponent.restockBulletShot(this.entity);
            }

            // Try to detect enemy.
//        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
//        if (targetStats != null) {
//            System.out.println("COLLIDE WITH NPC");
//            System.out.println("Bullet collided");
//            keepShotBullet.setPosition(ORIGIN);
//            keepShotBullet.getComponent(PhysicsMovementComponent.class).setTarget(ORIGIN);
//            activeBullets.add(keepShotBullet);
//        }
        }
    }
}
