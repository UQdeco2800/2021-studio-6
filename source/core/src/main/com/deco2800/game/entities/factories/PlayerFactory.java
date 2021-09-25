package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.player.*;
import com.deco2800.game.components.player.hud.PlayerHealthAnimationController;
import com.deco2800.game.components.player.hud.PlayerHudAnimationController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.lighting.ChainLightComponent;
import com.deco2800.game.lighting.ConeLightComponent;
import com.deco2800.game.lighting.DirectionalLightComponent;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final PlayerConfig stats =
          FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  private static String sword = "configs/Sword.json";

  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer() {
    InputComponent inputComponent =
            ServiceLocator.getInputService().getInputFactory().createForPlayer();

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/Player_Animations/player_movement.atlas", TextureAtlas.class));
    animator.addAnimation("dead-left", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("dead-right", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-hurt", 1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-hurt", 1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-hurt", 1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-hurt", 1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-hurt", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-hurt", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-hurt", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-hurt", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-helmet", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-helmet", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-helmet", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-helmet", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-helmet", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-helmet", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-helmet", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-helmet", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-armour", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-armour", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-armour", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-armour", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-armour", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-armour", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-armour", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-armour", 0.1f, Animation.PlayMode.LOOP);


    Entity player = new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new PlayerMeleeAttackComponent(sword))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                    .addComponent(new PlayerActions(stats.woundState))
                    .addComponent(new PlayerCombatStatsComponent(stats.health, stats.baseAttack, stats.woundState,
                            stats.baseRangedAttack, stats.defenceLevel))
                    .addComponent(new InventoryComponent(stats.gold, stats.ammo, stats.bandages))
                    .addComponent(new PlayerAbilitiesComponent(stats.ability))
                    .addComponent(inputComponent)
                    .addComponent(new PlayerRangeAttackComponent())
                    .addComponent(new PlayerRangeAOEComponent())
                    .addComponent(new PlayerReusableComponent())
                    .addComponent(new DisposingComponent())
                    .addComponent(new PlayerInterfaceDisplay())
                    .addComponent(new PlayerPickupComponent(PhysicsLayer.ITEM))
                    .addComponent(animator)
                    .addComponent(new PlayerAnimationController())
                    .addComponent(new PlayerHudAnimationController())
                    .addComponent(new PlayerWeaponAnimationController())
                    .addComponent(new PlayerHealthAnimationController())
                    .addComponent(new PointLightComponent(Colors.get("BLACK"), 4f, 0, 0));
    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    return player;
  }
  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
