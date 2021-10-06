package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.player.*;
import com.deco2800.game.components.player.hud.PlayerHealthAnimationController;
import com.deco2800.game.components.player.hud.PlayerHudAnimationController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.items.Abilities;
import com.deco2800.game.items.Items;
import com.deco2800.game.lighting.ChainLightComponent;
import com.deco2800.game.lighting.ConeLightComponent;
import com.deco2800.game.lighting.DirectionalLightComponent;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.memento.Player;
import com.deco2800.game.memento.PlayerStateManager;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final PlayerConfig stats =
          FileLoader.readClass(PlayerConfig.class, "configs/PlayerState.json");
  private static String meleeFilePath = stats.meleeFilePath;
  private static int baseRangedAttack, baseAttack, health, ammo, bandages, gold, woundState, defenceLevel, bulletMagazine, torch;
  private static String ability, meleeWeaponType, armorType;

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
    animator.addAnimation("dead-left", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("dead-right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("left", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("back", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("front", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-hurt", 1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-hurt", 1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-hurt", 1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-hurt", 1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-hurt", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-hurt", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-hurt", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-hurt", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-helmet", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-helmet", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-helmet", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-helmet", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-helmet", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-helmet", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-helmet", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-helmet", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-armour", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-armour", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-armour", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-armour", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-armour", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-armour", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-armour", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-armour", 0.2f, Animation.PlayMode.LOOP);
    animator.setUnlit();
    // when changing melee weapon file path, remember to also change weapon type player currently holds
    loadPlayerData();
    Entity player = new Entity()
          .addComponent(new PhysicsComponent())
          .addComponent(new ColliderComponent())
          .addComponent(new PlayerMeleeAttackComponent(meleeFilePath))
          .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
          .addComponent(new PlayerActions(woundState))
          .addComponent(new PlayerCombatStatsComponent(health, baseAttack, woundState,
                  baseRangedAttack, defenceLevel))
          .addComponent(new InventoryComponent(gold, ammo, bandages, torch))
          .addComponent(new PlayerAbilitiesComponent(Abilities.getAbility(ability)))
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
          .addComponent(new PlayerTorchAnimationController())
          .addComponent(new PlayerWeaponAnimationController())
          .addComponent(new PlayerHealthAnimationController())
          .addComponent(new PlayerLightingComponent(Colors.get("ORANGE"), 10f, 0, 0));


    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(PlayerMeleeAttackComponent.class).setMeleeWeaponType(Items.getMeleeWeapon(meleeWeaponType));
    player.getComponent(PlayerRangeAttackComponent.class).setBulletMagazine(bulletMagazine);
    return player;
  }

  private static void loadPlayerData() {
    // manages player states for carrying over and restoring in game
    PlayerStateManager playerManager = PlayerStateManager.getInstance();

    if (playerManager.currentPlayerState() == null) {
      // set initial state of player when game starts for the very first time, load from config file
      playerManager.createStartingPlayerState(stats.baseRangedAttack, stats.baseAttack, stats.health, stats.ammo,
              stats.bandages, stats.gold, stats.woundState, stats.defenceLevel, stats.bulletMagazine, stats.ability,
              stats.meleeFilePath, stats.meleeWeaponType, stats.armorType, stats.currentGameLevel, stats.torch);

      baseAttack = stats.baseAttack;
      baseRangedAttack = stats.baseAttack;
      health = stats.health;
      ammo = stats.ammo;
      bandages = stats.bandages;
      torch = stats.torch;
      gold = stats.gold;
      woundState = stats.woundState;
      defenceLevel = stats.defenceLevel;
      ability = stats.ability;
      meleeFilePath = stats.meleeFilePath;
      meleeWeaponType = stats.meleeWeaponType;
      armorType = stats.armorType;
      bulletMagazine = stats.bulletMagazine;

    } else {
      Player currentPlayerState = playerManager.currentPlayerState();
      baseAttack = currentPlayerState.getBaseAttack();
      baseRangedAttack = currentPlayerState.getBaseRangedAttack();
      health = currentPlayerState.getHealth();
      ammo = currentPlayerState.getAmmo();
      bandages = currentPlayerState.getBandage();
      torch = currentPlayerState.getTorch();
      gold = currentPlayerState.getGold();
      woundState = currentPlayerState.getWoundState();
      defenceLevel = currentPlayerState.getDefenceLevel();
      ability = currentPlayerState.getAbility();
      meleeFilePath = currentPlayerState.getMeleeFilePath();
      meleeWeaponType = currentPlayerState.getMeleeWeaponType();
      armorType = currentPlayerState.getArmorType();
      bulletMagazine = currentPlayerState.getBulletMagazine();
    }
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
