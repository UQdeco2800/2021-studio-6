package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.PlayerRangeAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
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
    short ignoreLayer = PhysicsLayer.PLAYER;
    short obstacleLayer = PhysicsLayer.OBSTACLE;
    private final Vector2 ORIGIN = new Vector2(0,0);

    public BulletCollisionComponent() {
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::bulletHit);
    }

    @Override
    public void update() {
    }

    public void bulletHit(Fixture me, Fixture other) {

        // Try to detect enemy.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (PhysicsLayer.contains(ignoreLayer, target.getComponent(ColliderComponent.class).getLayer())) {
            System.out.println("PLAYER'S STUFF");

            // bullet collide with obstacles
        } else if (PhysicsLayer.contains(obstacleLayer, target.getComponent(ColliderComponent.class).getLayer())) {
            System.out.println("COLLIDE WITH SOMETHING NOT NPC");
//            this.entity.setPosition(ORIGIN);
//            this.entity.getComponent(PhysicsMovementComponent.class).setTarget(ORIGIN);
//            PlayerRangeAttackComponent.restockBulletShot(this.entity);

            // bullet collides with NPC
        } else if (PhysicsLayer.contains(targetLayer, target.getComponent(ColliderComponent.class).getLayer())) {
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
