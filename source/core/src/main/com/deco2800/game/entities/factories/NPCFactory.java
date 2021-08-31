package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.npc.FireBulletListener;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.GhostKingConfig;
import com.deco2800.game.entities.configs.LargeEnemyConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class NPCFactory {
  private static final NPCConfigs configs =
          FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");


  /**
   * 
   * Creates a small enemy entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createSmallEnemy(Entity target) {
    Entity smallEnemy = createBaseNPC(target);
    BaseEntityConfig config = configs.smallEnemy;
    Vector2 speed = new Vector2(config.speed, config.speed);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/small_enemy.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    smallEnemy
            .addComponent(new GhostAnimationController())
            .addComponent(new PhysicsMovementComponent(speed))
            .addComponent(animator)
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack));

    smallEnemy.getComponent(AnimationRenderComponent.class).scaleEntity();

    return smallEnemy;
  }

  /**
   * Creates an enemy with slower speed but deals more damage to the target
   *
   * @param target entity to chase
   * @return the large enemy entity
   */
  public static Entity createLargeEnemy(Entity target) {
    Entity largeEnemy = createBaseNPC(target);
    LargeEnemyConfig config = configs.largeEnemy;

    //Movement speed of large enemy
    Vector2 speed = new Vector2(config.speed_x, config.speed_y);

//    Vector2 hitBox = new Vector2(2,2);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/largeEnemy.atlas", TextureAtlas.class));

    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    largeEnemy
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new PhysicsMovementComponent(speed))
            .addComponent(new GhostAnimationController());

    //Increase the size of the enemy
    largeEnemy.setScale(2,2);

    //Change size of hit box
//    largeEnemy.getComponent(ColliderComponent.class).setAsBox(hitBox);

    return largeEnemy;
  }

  /**
   * Creates a long range enemy, fires bullet towards a target
   * @param target the target entity to fire at
   * @param gameArea the game area spawned in need for despawning bullets
   * @return returns the long range enemy entity
   */
  public static Entity createLongRangeEnemy(Entity target, GameArea gameArea) {

    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                    .addTask(new DistanceFireBulletTask(target, 1, 10, 5f));
    Entity longRange = new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
                    .addComponent(new TextureRenderComponent("images/eye.png"))
                    .addComponent(new CombatStatsComponent(1, 1))
                    .addComponent(aiComponent)
                    .addComponent(new FireBulletListener(target, gameArea));
    longRange.setScale(new Vector2(1.5f, 1.5f));
    return longRange;
  }



  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC(Entity target) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                    .addTask(new ChaseTask(target, 10, 3f, 4f));
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }




  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
