package com.deco2800.game.entities.factories;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.MultiAITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.*;
import com.deco2800.game.components.npc.*;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.lighting.FlickerLightComponent;
import com.deco2800.game.lighting.PointLightComponent;
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
 * "NPCConfigs".</p>
 *
 */
public class NPCFactory {
  private static final NPCConfigs configs =
          FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  /**
   *
   * Creates a spawner enemy entity.
   *
   * @return entity
   */
  public static Entity createSpawnerEnemy() {
    // Get the base config and standard entity
    BaseEnemyConfig config = configs.spawnerEnemy;
    String atlasFileName = "images/Enemy_Assets/SpawnerEnemy/spawnerEnemy.atlas";
    Entity spawnerNPC = createBaseNPC(config, atlasFileName);

    // Add additional specific components
    spawnerNPC.addComponent(new LootComponent("ammo",3, 5, 1));
    spawnerNPC.addComponent(new PointLightComponent(new Color(0x93ff9eaa), 0.5f, 0, 0));
    spawnerNPC.addComponent(new AITaskComponent());

    // Make this entity completely static
    spawnerNPC.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);

    // Add the spawn animation
    spawnerNPC.getComponent(AnimationRenderComponent.class).addAnimation("spawn", 0.1f, Animation.PlayMode.LOOP);;

    // Add the entity sound effects
    NPCSoundComponent npcSoundComponent = spawnerNPC.getComponent(NPCSoundComponent.class);
    npcSoundComponent.setHit(ServiceLocator.getResourceService().getAsset("sounds/enemies/SpawnerEnemy/hit.mp3", Sound.class));
    npcSoundComponent.setDead(ServiceLocator.getResourceService().getAsset("sounds/enemies/SpawnerEnemy/dead.mp3", Sound.class));
    npcSoundComponent.setSpawn(ServiceLocator.getResourceService().getAsset("sounds/enemies/SpawnerEnemy/spawn.wav", Sound.class));

    // Change size of entity and size of hitbox
    spawnerNPC.getComponent(AnimationRenderComponent.class).scaleEntity();
    spawnerNPC.setScale(1f, 1f);
    PhysicsUtils.setEntityPhysics(spawnerNPC, 2000f, 0.8f, 0.8f, 0f, 0f);

    return spawnerNPC;
  }

  /**
   * 
   * Creates a small enemy entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createSmallEnemy(Entity target) {
    // Get the base config and standard entity
    BaseEnemyConfig config = configs.smallEnemy;
    String atlasFileName = "images/Enemy_Assets/SmallEnemy/small_enemy.atlas";
    Entity smallEnemy = createBaseNPC(config, atlasFileName);


    // Setup the entities AI component
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                    .addTask(new ChaseTask(target, 10, 7f, 7f));

    // Add additional specific components
    smallEnemy.addComponent(new EnemyMeleeAttackComponent());
    smallEnemy.addComponent(new DarknessDetectionComponent());
    smallEnemy.addComponent(new EnemyDarknessController());
    smallEnemy.addComponent(aiComponent);
    smallEnemy.addComponent(new LootComponent("coins", 1,2, 0.3f));
    smallEnemy.addComponent(new GlowingEyesComponent(
        ServiceLocator.getResourceService().getAsset(atlasFileName, TextureAtlas.class)));

    // Add the render component to display glowing features when they are in the dark
    GlowingEyesComponent glowingEyesComponent = smallEnemy.getComponent(GlowingEyesComponent.class);
    glowingEyesComponent.setUnlit();

    // Add the entity sound effects
    NPCSoundComponent npcSoundComponent = smallEnemy.getComponent(NPCSoundComponent.class);
    npcSoundComponent.setDead(ServiceLocator.getResourceService().getAsset("sounds/enemies/SmallEnemy/dead.mp3", Sound.class));
    npcSoundComponent.setDetectPlayer(ServiceLocator.getResourceService().getAsset("sounds/enemies/SmallEnemy/detectPlayer.wav", Sound.class));
    npcSoundComponent.setMeleeAttack(ServiceLocator.getResourceService().getAsset("sounds/enemies/SmallEnemy/meleeAttack.mp3", Sound.class));

    // Change size of entity and size of hitbox
    smallEnemy.getComponent(AnimationRenderComponent.class).scaleEntity();
    smallEnemy.setScale(1.2f, 1.2f);
    PhysicsUtils.setEntityPhysics(smallEnemy, 0.01f, 0.7f, 0.7f, 0f, 0f);

    return smallEnemy;
  }


  /**
   * Creates an enemy with slower speed but deals more damage to the target
   *
   * @param target entity to chase
   * @return the large enemy entity
   */
  public static Entity createLargeEnemy(Entity target) {
    // Get the base config and standard entity
    BaseEnemyConfig config = configs.largeEnemy;
    String atlasFileName = "images/Enemy_Assets/LargeEnemy/largeEnemy.atlas";
    Entity largeEnemy = createBaseNPC(config, atlasFileName);

    // Setup the entities AI component
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new ChaseTask(target, 10, 7f, 7f));

    // Add additional specific components
    largeEnemy.addComponent(new EnemyMeleeAttackComponent());
    largeEnemy.addComponent(new DarknessDetectionComponent());
    largeEnemy.addComponent(new EnemyDarknessController());
    largeEnemy.addComponent(aiComponent);
    largeEnemy.addComponent(new LootComponent("coins",5, 10, 1));
    largeEnemy.addComponent(new GlowingEyesComponent(
        ServiceLocator.getResourceService().getAsset(atlasFileName, TextureAtlas.class)));

    // Add the render component to display glowing features when they are in the dark
    GlowingEyesComponent glowingEyesComponent = largeEnemy.getComponent(GlowingEyesComponent.class);
    glowingEyesComponent.setUnlit();

    // Add the entity sound effects
    NPCSoundComponent npcSoundComponent = largeEnemy.getComponent(NPCSoundComponent.class);
    npcSoundComponent.setHit(ServiceLocator.getResourceService().getAsset("sounds/enemies/LargeEnemy/hit.mp3", Sound.class));
    npcSoundComponent.setDead(ServiceLocator.getResourceService().getAsset("sounds/enemies/LargeEnemy/dead.mp3", Sound.class));
    npcSoundComponent.setDetectPlayer(ServiceLocator.getResourceService().getAsset("sounds/enemies/LargeEnemy/detectPlayer.mp3", Sound.class));
    npcSoundComponent.setMeleeAttack(ServiceLocator.getResourceService().getAsset("sounds/enemies/LargeEnemy/meleeAttack.mp3", Sound.class));

    // Change size of entity and size of hitbox
    largeEnemy.setScale(2f,2f);
    PhysicsUtils.setEntityPhysics(largeEnemy, 30f, 0.8f, 1f, 0f, -0.4f);

    return largeEnemy;
  }

  /**
   * Creates a long range enemy, fires bullet towards a target
   * @param target the target entity to fire at
   * @param gameArea the game area spawned in need for despawning bullets
   * @return returns the long range enemy entity
   */
  public static Entity createLongRangeEnemy(Entity target, GameArea gameArea) {
    // Get the base config and standard entity
    BaseEnemyConfig config = configs.rangedEnemy;
    String atlasFileName = "images/Enemy_Assets/LongRangeEnemy/longRangeEnemy.atlas";
    Entity longRange = createBaseNPC(config, atlasFileName);

    MultiAITaskComponent aiComponent = new MultiAITaskComponent();
    aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));
    aiComponent.addTask(new DistanceFireBulletTask(target, 2, 10, 8f));

    // Add additional specific components
    longRange.addComponent(new FireBulletListener(target, gameArea, "images/Enemy_Assets/LongRangeEnemy/bloodball_purple.png"));
    longRange.addComponent(new DarknessDetectionComponent());
    longRange.addComponent(new EnemyDarknessController());
    longRange.addComponent(aiComponent);
    longRange.addComponent(new LootComponent("coins",1, 3, 0.5f));
    longRange.addComponent(new GlowingEyesComponent(
        ServiceLocator.getResourceService().getAsset(atlasFileName, TextureAtlas.class)));

    // Add the render component to display glowing features when they are in the dark
    GlowingEyesComponent glowingEyesComponent = longRange.getComponent(GlowingEyesComponent.class);
    glowingEyesComponent.setUnlit();

    // Add the entity sound effects
    NPCSoundComponent npcSoundComponent = longRange.getComponent(NPCSoundComponent.class);
    npcSoundComponent.setDead(ServiceLocator.getResourceService().getAsset("sounds/enemies/LongRangeEnemy/dead.mp3", Sound.class));
    npcSoundComponent.setShoot(ServiceLocator.getResourceService().getAsset("sounds/enemies/LongRangeEnemy/shoot.mp3", Sound.class));

    // Change size of entity and size of hitbox
    longRange.setScale(new Vector2(1f, 1f));
    PhysicsUtils.setEntityPhysics(longRange, 6f, 0.8f, 0.8f, 0f, 0f);

    return longRange;
  }

  public static Entity createToughLongRangeEnemy(Entity target, GameArea gameArea) {
    // Get the base config and standard entity
    BaseEnemyConfig config = configs.rangedToughEnemy;
    String atlasFileName = "images/Enemy_Assets/ToughLongRangeEnemy/toughLongRangeEnemy.atlas";
    Entity toughLongRangeEnemy = createBaseNPC(config, atlasFileName);

    // Create the ai component where both tasks run at the same time
    MultiAITaskComponent aiComponent = new MultiAITaskComponent();
    aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));
    aiComponent.addTask(new DistanceFireBulletTask(target, 4, 10, 10f));

    // Add additional specific components
    toughLongRangeEnemy.addComponent(new ToughFireBulletListener(target, gameArea, "images/Enemy_Assets/ToughLongRangeEnemy/tough-projectile.png"));
    toughLongRangeEnemy.addComponent(new DarknessDetectionComponent());
    toughLongRangeEnemy.addComponent(new EnemyDarknessController());
    toughLongRangeEnemy.addComponent(aiComponent);
    toughLongRangeEnemy.addComponent(new LootComponent("coins",3, 7, 1));
    toughLongRangeEnemy.addComponent(new GlowingEyesComponent(
        ServiceLocator.getResourceService().getAsset(atlasFileName, TextureAtlas.class)));

    // Add the render component to display glowing features when they are in the dark
    GlowingEyesComponent glowingEyesComponent = toughLongRangeEnemy.getComponent(GlowingEyesComponent.class);
    glowingEyesComponent.setUnlit();

    // Add the entity sound effects
    NPCSoundComponent npcSoundComponent = toughLongRangeEnemy.getComponent(NPCSoundComponent.class);
    npcSoundComponent.setHit(ServiceLocator.getResourceService().getAsset("sounds/enemies/ToughLongRangeEnemy/hit.mp3", Sound.class));
    npcSoundComponent.setDead(ServiceLocator.getResourceService().getAsset("sounds/enemies/ToughLongRangeEnemy/dead.mp3", Sound.class));
    npcSoundComponent.setShoot(ServiceLocator.getResourceService().getAsset("sounds/enemies/ToughLongRangeEnemy/shoot.wav", Sound.class));

    // Change size of entity and size of hitbox
    toughLongRangeEnemy.setScale(new Vector2(2f, 2f));
    PhysicsUtils.setEntityPhysics(toughLongRangeEnemy, 20f, 1.7f, 1.7f, 0f, 0f);

    return toughLongRangeEnemy;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   * @param config - the configuration file for the enemy
   * @param atlasFileName - the atlas filename for the enemy that contains all the animations
   * @return entity
   */
  private static Entity createBaseNPC(BaseEnemyConfig config, String atlasFileName) {
    Vector2 speed = new Vector2(config.speed_x, config.speed_y);

    // Add all the animations for the entity
    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(atlasFileName, TextureAtlas.class));
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
    animator.addAnimation("left-damaged-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-damaged-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-damaged-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-damaged-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left-run-damaged-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("right-run-damaged-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("back-run-damaged-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("front-run-damaged-hit", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("dead", 0.1f, Animation.PlayMode.LOOP);

    // Create the base entity with standard components in each NPC
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new DisposingComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new PhysicsMovementComponent(speed))
            .addComponent(animator)
            .addComponent(new NPCAnimationController())
            .addComponent(new NPCSoundComponent())
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack));

    // Set what the NPC should collide with
    ColliderComponent colliderComponent = npc.getComponent(ColliderComponent.class);
    colliderComponent.setLayer(PhysicsLayer.NPC,
        (short) (PhysicsLayer.PLAYER | PhysicsLayer.OBSTACLE | PhysicsLayer.NPC | PhysicsLayer.WALL | PhysicsLayer.FRIENDLY_NPC));

    return npc;
  }

  /**
   * Creates a firefly bug NPC that wanders around the map and also provide a small light source to the map.
   * @param speedX Movement speed in X direction
   * @param speedY Movement speed in Y direction
   * @param wanderX Proximity X distance to move around in
   * @param wanderY Proximity Y distance to move around in
   * @param waitTime Time in seconds to hold the position after wandering to a spot
   * @return Firefly entity
   */
  public static Entity createFireFlyBugNPC(float speedX, float speedY, float wanderX, float wanderY, float waitTime) {
    Vector2 speed = new Vector2(speedX, speedY);
    AITaskComponent aiComponent =
      new AITaskComponent()
        .addTask(new WanderTask(new Vector2(wanderX, wanderY), waitTime));

    TextureAtlas fireflyAtlas = ServiceLocator.getResourceService().getAsset("images/firefly/firefly.atlas", TextureAtlas.class);
    AnimationRenderComponent animator = new AnimationRenderComponent(fireflyAtlas);

    //animator.addAnimation("down", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("firefly-front", 0.05f, Animation.PlayMode.LOOP);
    animator.startAnimation("firefly-front");

    //animator.addAnimation("left", 0.2f, Animation.PlayMode.LOOP);
    //animator.addAnimation("right", 0.2f, Animation.PlayMode.LOOP);
    animator.setUnlit();

    Entity fireflyBugNPC =
      new Entity()
        //.addComponent(new TextureRenderComponent("images/level_2/fire-fly-bug-NPC.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new HitboxComponent())
        .addComponent(new PhysicsMovementComponent(speed))
        .addComponent(new DisposingComponent())
        .addComponent(aiComponent)
        .addComponent(new FlickerLightComponent(new Color(0xffa500aa), Color.ORANGE, Color.FIREBRICK,
                Color.SCARLET, 2f, 0, 0))
        .addComponent(animator);

    //fireflyBugNPC.getComponent(AnimationRenderComponent.class).scaleEntity();
    //fireflyBugNPC.scaleHeight(0.15f);

    return fireflyBugNPC;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
