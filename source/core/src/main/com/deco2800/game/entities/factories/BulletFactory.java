package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.BulletCollisionComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
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
    private static final PlayerConfig stats =
            FileLoader.readClass(PlayerConfig.class, "configs/PlayerState.json");
    private static final Vector2 HIDDEN_COORD = new Vector2(-10,-10);

    /**
     * Bullets are created here before player fires it in game
     * @return Bullet that comes from a pool of Bullet objects
     */
    public static Entity createBullet() {
        Entity bullet = new Entity()
                .addComponent(new TextureRenderComponent("images/playeritems/shootingammo.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(5f, 5f)))
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new BulletCollisionComponent())
                .addComponent(new DisposingComponent())
                .addComponent(new PlayerCombatStatsComponent(stats.health, stats.baseAttack, stats.woundState,
                        stats.baseRangedAttack, stats.defenceLevel));

        // hide bullet out of game screen
        bullet.setPosition(HIDDEN_COORD);
        bullet.setScale(0.7f, 0.7f);
        return bullet;
    }
}
