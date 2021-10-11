package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.BulletAnimationController;
import com.deco2800.game.components.BulletCollisionComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

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

        AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/playeritems/rock/rock.atlas", TextureAtlas.class));
        animator.addAnimation("rock", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("left", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("right", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("down", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("up", 0.1f, Animation.PlayMode.NORMAL);

        Entity bullet = new Entity()
                //.addComponent(new TextureRenderComponent("images/playeritems/rock/thrown.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(5f, 5f)))
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new BulletCollisionComponent())
                .addComponent(new DisposingComponent())
                .addComponent(animator)
                .addComponent(new BulletAnimationController())
                .addComponent(new PlayerCombatStatsComponent(stats.health, stats.baseAttack, stats.woundState,
                        stats.baseRangedAttack, stats.defenceLevel));

        // hide bullet out of game screen
        bullet.setPosition(HIDDEN_COORD);
        bullet.setScale(1f, 1f);
        return bullet;
    }
}
