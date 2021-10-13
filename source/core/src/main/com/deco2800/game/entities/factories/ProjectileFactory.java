package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
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
public class ProjectileFactory {
    private static final PlayerConfig stats =
            FileLoader.readClass(PlayerConfig.class, "configs/PlayerState.json");
    private static final Vector2 HIDDEN_COORD = new Vector2(-10,-10);
    private static final Vector2 SCALE_LARGER_BOX = new Vector2(2, 2);
    private static final Vector2 SCALE_SMALLER_BOX = new Vector2(0.5f, 0.5f);
    private static final Vector2 SCALE_REL_COORD = new Vector2(0.5f,0.5f);

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

    /**
     * Fire cracker is created here before player fires it in game as well as its
     * @return fire cracker that is launched based on player's selected ability
     */
    public static Entity createFireCracker() {
        Entity fireCracker = new Entity()
                .addComponent(new TextureRenderComponent("images/playeritems/firecracker/firecracker.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(5f, 5f)))
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new HitboxComponent())
//                .addComponent(new FireCrackerCollisionComponent())
                .addComponent(new DisposingComponent())
                .addComponent(new PlayerCombatStatsComponent(stats.health, stats.baseAttack, stats.woundState,
                        stats.baseRangedAttack, stats.defenceLevel));

        // hide fire cracker out of game screen
        fireCracker.setPosition(HIDDEN_COORD);
        fireCracker.getComponent(HitboxComponent.class).setAsBox(SCALE_LARGER_BOX, SCALE_REL_COORD);
        fireCracker.getComponent(ColliderComponent.class).setAsBox(SCALE_SMALLER_BOX, SCALE_REL_COORD);
        return fireCracker;
    }
}
