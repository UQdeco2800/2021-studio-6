package com.deco2800.game.entities.factories;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.*;
import com.deco2800.game.components.npc.*;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.physics.PhysicsUtils;

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
   * Creates a spawner enemy entity.
   *
   * @param target entity to chase
   * @param gameArea the current game area
   * @return entity
   */
  public static Entity createSpawnerEnemy(Entity target, GameArea gameArea) {
    BaseEnemyConfig config = configs.spawnerEnemy;
    Vector2 speed = new Vector2(config.speed_x, config.speed_y);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/Enemy_Assets/SpawnerEnemy/spawnerEnemy.atlas", TextureAtlas.class));
    animator.addAnimation("left", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-hit", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-hit", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("dead", 0.1f, Animation.PlayMode.LOOP);

    Entity spawnerEnemy =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new DisposingComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new PhysicsMovementComponent(speed))
                    .addComponent(animator)
                    .addComponent(new NPCAnimationController())
                    .addComponent(new NPCSoundComponent())
                    .addComponent(new AITaskComponent())
                    .addComponent(new LootComponent("ammo",3, 5, 1))
                    .addComponent(new CombatStatsComponent(config.health, config.baseAttack));
    
    spawnerEnemy.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);

    NPCSoundComponent npcSoundComponent = spawnerEnemy.getComponent(NPCSoundComponent.class);
    npcSoundComponent.setHit(ServiceLocator.getResourceService().getAsset("sounds/enemies/SpawnerEnemy/hit.wav", Sound.class));
    npcSoundComponent.setDead(ServiceLocator.getResourceService().getAsset("sounds/enemies/SpawnerEnemy/dead.wav", Sound.class));
    npcSoundComponent.setDetectPlayer(ServiceLocator.getResourceService().getAsset("sounds/enemies/SpawnerEnemy/detectPlayer.mp3", Sound.class));
    npcSoundComponent.setSpawn(ServiceLocator.getResourceService().getAsset("sounds/enemies/SpawnerEnemy/spawn.wav", Sound.class));

    spawnerEnemy.getComponent(AnimationRenderComponent.class).scaleEntity();
    spawnerEnemy.setScale(1f, 1f);

    return spawnerEnemy;
  }

  /**
   * 
   * Creates a small enemy entity.
   *
   * @param target entity to chase
   * @return entity
   */

  public static Entity createSmallEnemy(Entity target) {
    BaseEnemyConfig config = configs.smallEnemy;
    Vector2 speed = new Vector2(config.speed_x, config.speed_y);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/Enemy_Assets/SmallEnemy/small_enemy.atlas", TextureAtlas.class));
    animator.addAnimation("left", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-hit", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-hit", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("dead", 0.1f, Animation.PlayMode.LOOP);

    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                    .addTask(new ChaseTask(target, 10, 3f, 4f));

    Entity smallEnemy =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 2f))
                    .addComponent(aiComponent)
                    .addComponent(new GlowingEyesComponent("images/Enemy_Assets/SmallEnemy/small_enemy_redeyes.png"))
                    .addComponent(new DarknessDetectionComponent())
                    .addComponent(new EnemyDarknessController())
                    .addComponent(new PhysicsMovementComponent(speed))
                    .addComponent(animator)
                    .addComponent(new NPCAnimationController())
                    .addComponent(new NPCSoundComponent())
                    .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                    .addComponent(new LootComponent("coins", 1,2, 0.3f))
                    .addComponent(new DisposingComponent());

    ColliderComponent colliderComponent = smallEnemy.getComponent(ColliderComponent.class);
    colliderComponent.setDensity(2f);

    GlowingEyesComponent glowingEyesComponent = smallEnemy.getComponent(GlowingEyesComponent.class);
    glowingEyesComponent.initialise();
    glowingEyesComponent.setUnlit();

    NPCSoundComponent npcSoundComponent = smallEnemy.getComponent(NPCSoundComponent.class);
    npcSoundComponent.setHit(ServiceLocator.getResourceService().getAsset("sounds/enemies/SmallEnemy/hit.wav", Sound.class));
    npcSoundComponent.setDead(ServiceLocator.getResourceService().getAsset("sounds/enemies/SmallEnemy/dead.wav", Sound.class));
    npcSoundComponent.setDetectPlayer(ServiceLocator.getResourceService().getAsset("sounds/enemies/SmallEnemy/detectPlayer.wav", Sound.class));
    npcSoundComponent.setMeleeAttack(ServiceLocator.getResourceService().getAsset("sounds/enemies/SmallEnemy/meleeAttack.mp3", Sound.class));

    smallEnemy.getComponent(AnimationRenderComponent.class).scaleEntity();
    smallEnemy.setScale(1.2f, 1.2f);
    PhysicsUtils.setScaledCollider(smallEnemy, 0.6f, 0.3f);
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
    System.out.println(configs);
    BaseEnemyConfig config = configs.largeEnemy;

    //Movement speed of large enemy
    Vector2 speed = new Vector2(config.speed_x, config.speed_y);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/Enemy_Assets/LargeEnemy/largeEnemy.atlas", TextureAtlas.class));
    animator.addAnimation("left", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-hit", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-hit", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("dead", 0.1f, Animation.PlayMode.LOOP);

    largeEnemy
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 4f))
            .addComponent(new PhysicsMovementComponent(speed))
            .addComponent(new GlowingEyesComponent("images/Enemy_Assets/SmallEnemy/small_enemy_redeyes.png"))
            .addComponent(new DarknessDetectionComponent())
            .addComponent(new NPCSoundComponent())
            .addComponent(new EnemyDarknessController())
            .addComponent(new LootComponent("coins",5, 10, 1))
            .addComponent(new NPCAnimationController());

    ColliderComponent colliderComponent = largeEnemy.getComponent(ColliderComponent.class);
    colliderComponent.setDensity(50f);

    GlowingEyesComponent glowingEyesComponent = largeEnemy.getComponent(GlowingEyesComponent.class);
    glowingEyesComponent.initialise();
    glowingEyesComponent.setUnlit();

    NPCSoundComponent npcSoundComponent = largeEnemy.getComponent(NPCSoundComponent.class);
    npcSoundComponent.setHit(ServiceLocator.getResourceService().getAsset("sounds/enemies/LargeEnemy/hit.wav", Sound.class));
    npcSoundComponent.setDead(ServiceLocator.getResourceService().getAsset("sounds/enemies/LargeEnemy/dead.wav", Sound.class));
    npcSoundComponent.setDetectPlayer(ServiceLocator.getResourceService().getAsset("sounds/enemies/LargeEnemy/detectPlayer.mp3", Sound.class));
    npcSoundComponent.setMeleeAttack(ServiceLocator.getResourceService().getAsset("sounds/enemies/LargeEnemy/meleeAttack.mp3", Sound.class));

    //Increase the size of the enemy
    largeEnemy.setScale(2f,2f);
    PhysicsUtils.setScaledCollider(largeEnemy, 0.8f, 0.3f);

    return largeEnemy;
  }

  /**
   * Creates a long range enemy, fires bullet towards a target
   * @param target the target entity to fire at
   * @param gameArea the game area spawned in need for despawning bullets
   * @return returns the long range enemy entity
   */
  public static Entity createLongRangeEnemy(Entity target, GameArea gameArea) {
    BaseEnemyConfig config = configs.rangedEnemy;

    Vector2 speed = new Vector2(config.speed_x, config.speed_y);

    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                    .addTask(new DistanceFireBulletTask(target, 2, 10, 8f));


    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/Enemy_Assets/LongRangeEnemy/longRangeEnemy.atlas", TextureAtlas.class));
    animator.addAnimation("left", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-hit", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-hit", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("dead", 0.1f, Animation.PlayMode.LOOP);

    Entity longRange = new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent(speed))
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                    .addComponent(aiComponent)
                    .addComponent(new FireBulletListener(target, gameArea, "images/Enemy_Assets/LongRangeEnemy/blood_ball.png"))
                    .addComponent(new DarknessDetectionComponent())
                    .addComponent(animator)
                    .addComponent(new NPCAnimationController())
                    .addComponent(new NPCSoundComponent())
                    .addComponent(new GlowingEyesComponent("images/Enemy_Assets/SmallEnemy/small_enemy_redeyes.png"))
                    .addComponent(new EnemyDarknessController())
                    .addComponent(new LootComponent("coins",1, 3, 0.5f))
                    .addComponent(new DisposingComponent());

    ColliderComponent colliderComponent = longRange.getComponent(ColliderComponent.class);
    colliderComponent.setDensity(6f);

    GlowingEyesComponent glowingEyesComponent = longRange.getComponent(GlowingEyesComponent.class);
    glowingEyesComponent.initialise();
    glowingEyesComponent.setUnlit();

    NPCSoundComponent npcSoundComponent = longRange.getComponent(NPCSoundComponent.class);
    npcSoundComponent.setHit(ServiceLocator.getResourceService().getAsset("sounds/enemies/LongRangeEnemy/hit.mp3", Sound.class));
    npcSoundComponent.setDead(ServiceLocator.getResourceService().getAsset("sounds/enemies/LongRangeEnemy/dead.mp3", Sound.class));
    npcSoundComponent.setDetectPlayer(ServiceLocator.getResourceService().getAsset("sounds/enemies/LongRangeEnemy/detectPlayer.mp3", Sound.class));
    npcSoundComponent.setShoot(ServiceLocator.getResourceService().getAsset("sounds/enemies/LongRangeEnemy/shoot.wav", Sound.class));

    longRange.setScale(new Vector2(1f, 1f));
    return longRange;
  }

  public static Entity createToughLongRangeEnemy(Entity target, GameArea gameArea) {
    BaseEnemyConfig config = configs.rangedToughEnemy;

    //Movement speed of large enemy
    Vector2 speed = new Vector2(config.speed_x, config.speed_y);

    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                    .addTask(new DistanceFireBulletTask(target, 4, 10, 8f));

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/Enemy_Assets/ToughLongRangeEnemy/toughLongRangeEnemy.atlas", TextureAtlas.class));
    animator.addAnimation("left", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-hit", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-hit", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-damaged", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("dead", 0.1f, Animation.PlayMode.LOOP);

    Entity toughLongRangeEnemy = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent(speed))
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(aiComponent)
            .addComponent(new ToughFireBulletListener(target, gameArea, "images/Enemy_Assets/ToughLongRangeEnemy/tough-projectile.png"))
            .addComponent(new DarknessDetectionComponent())
            .addComponent(animator)
            .addComponent(new NPCAnimationController())
            .addComponent(new NPCSoundComponent())
            .addComponent(new GlowingEyesComponent("images/Enemy_Assets/SmallEnemy/small_enemy_redeyes.png"))
            .addComponent(new EnemyDarknessController())
            .addComponent(new LootComponent("coins",3, 7, 1))
            .addComponent(new DisposingComponent());

    ColliderComponent colliderComponent = toughLongRangeEnemy.getComponent(ColliderComponent.class);
    colliderComponent.setDensity(20f);

    GlowingEyesComponent glowingEyesComponent = toughLongRangeEnemy.getComponent(GlowingEyesComponent.class);
    glowingEyesComponent.initialise();
    glowingEyesComponent.setUnlit();

    NPCSoundComponent npcSoundComponent = toughLongRangeEnemy.getComponent(NPCSoundComponent.class);
    npcSoundComponent.setHit(ServiceLocator.getResourceService().getAsset("sounds/enemies/ToughLongRangeEnemy/hit.mp3", Sound.class));
    npcSoundComponent.setDead(ServiceLocator.getResourceService().getAsset("sounds/enemies/ToughLongRangeEnemy/dead.mp3", Sound.class));
    npcSoundComponent.setDetectPlayer(ServiceLocator.getResourceService().getAsset("sounds/enemies/ToughLongRangeEnemy/detectPlayer.mp3", Sound.class));
    npcSoundComponent.setShoot(ServiceLocator.getResourceService().getAsset("sounds/enemies/ToughLongRangeEnemy/shoot.wav", Sound.class));

    toughLongRangeEnemy.setScale(new Vector2(2f, 2f));

    return toughLongRangeEnemy;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   * @param target the target entity to attack
   * @return entity
   */
  private static Entity createBaseNPC(Entity target) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                    .addTask(new ChaseTask(target, 10, 4f, 5f));
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            //.addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 3f))
            .addComponent(aiComponent)
            .addComponent(new DisposingComponent());

    //PhysicsUtils.setScaledCollider(npc, 0.8f, 0.3f);
    return npc;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
