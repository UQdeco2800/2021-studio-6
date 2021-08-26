package com.deco2800.game.entities.factories;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Bullets used as ammo for player to shoot
 *
 * <p> These are created before even shooting and are placed in the background for player's use
 */
public class BulletFactory {

    /**
     * Bullets are created here before player fires it in game
     * @return Bullet that comes from a pool of Bullet objects
     */
    public static Entity createBullet() {
        Entity bullet = new Entity()
                .addComponent(new TextureRenderComponent("images/player_placeholders/PROJECTILE.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent().setSensor(true));

        bullet.getComponent(TextureRenderComponent.class).scaleEntity();
        return bullet;
    }
}
