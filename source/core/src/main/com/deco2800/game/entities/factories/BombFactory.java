package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.BombCollisionComponent;
import com.deco2800.game.components.BulletCollisionComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.components.BombColliderComponent;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Bombs used as ammo for player to shoot
 *
 * <p> These are created before even shooting and are placed in the background for player's use
 */
public class BombFactory {
    private static final PlayerConfig stats =
            FileLoader.readClass(PlayerConfig.class, "configs/PlayerState.json");
    private static final Vector2 HIDDEN_COORD = new Vector2(-10,-10);

    /**
     * Bombs are created here before player fires it in game
     * @return Bomb that comes from a pool of Bombs objects
     */
    public static Entity createBomb() {
        Entity bomb = new Entity()
                .addComponent(new TextureRenderComponent("images/playeritems/firecracker/firecracker.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(5f, 5f)))
                .addComponent(new BombColliderComponent().setSensor(true))
                .addComponent(new BulletCollisionComponent())
                .addComponent(new DisposingComponent())
                .addComponent(new PlayerCombatStatsComponent(stats.health, stats.baseAttack, stats.woundState,
                        stats.baseRangedAttack, stats.defenceLevel));

        // hide bomb out of game screen
        bomb.setPosition(HIDDEN_COORD);
        bomb.setScale(1.5f, 1.5f);
        return bomb;
    }
}
