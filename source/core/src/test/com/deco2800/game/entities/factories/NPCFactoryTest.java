package com.deco2800.game.entities.factories;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.MultiAITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.DarknessDetectionComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.npc.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class NPCFactoryTest {
  @Mock
  GameArea gameArea;
  @Mock
  Entity player;

  static final String[] ENEMY_ATLAS = {
      "images/Enemy_Assets/LargeEnemy/largeEnemy.atlas",
      "images/Enemy_Assets/SmallEnemy/small_enemy.atlas",
      "images/Enemy_Assets/SpawnerEnemy/spawnerEnemy.atlas",
      "images/Enemy_Assets/ToughLongRangeEnemy/toughLongRangeEnemy.atlas",
      "images/Enemy_Assets/LongRangeEnemy/longRangeEnemy.atlas",
  };

  static final String[] ENEMY_SOUNDS = {
      "sounds/enemies/ToughLongRangeEnemy/hit.mp3",
      "sounds/enemies/ToughLongRangeEnemy/dead.mp3",
      "sounds/enemies/ToughLongRangeEnemy/shoot.wav",
      "sounds/enemies/LongRangeEnemy/dead.mp3",
      "sounds/enemies/LongRangeEnemy/shoot.mp3",
      "sounds/enemies/LargeEnemy/hit.mp3",
      "sounds/enemies/LargeEnemy/dead.mp3",
      "sounds/enemies/LargeEnemy/detectPlayer.mp3",
      "sounds/enemies/LargeEnemy/meleeAttack.mp3",
      "sounds/enemies/SmallEnemy/dead.mp3",
      "sounds/enemies/SmallEnemy/detectPlayer.wav",
      "sounds/enemies/SmallEnemy/meleeAttack.mp3",
      "sounds/enemies/SpawnerEnemy/hit.mp3",
      "sounds/enemies/SpawnerEnemy/dead.mp3",
      "sounds/enemies/SpawnerEnemy/spawn.wav"
  };

  @BeforeEach
  void beforeEach() {
    // Setup resource locator to load speech bubble texture and npc movement atlas
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.getResourceService().loadTextureAtlases(ENEMY_ATLAS);
    ServiceLocator.getResourceService().loadSounds(ENEMY_SOUNDS);
    ServiceLocator.getResourceService().loadAll();

    // Register the physics service
    ServiceLocator.registerPhysicsService(new PhysicsService());

    //Setup lighting service
    ServiceLocator.registerUnlitRenderService(new RenderService());
    ServiceLocator.registerRenderService(new RenderService());

  }

  @AfterEach
  void afterEach() {
    ServiceLocator.getResourceService().unloadAssets(ENEMY_ATLAS);
    ServiceLocator.getResourceService().unloadAssets(ENEMY_SOUNDS);
  }

  @Test
  void createNewSmallEnemyContainsAllComponentsTest() {
    Entity enemy = NPCFactory.createSmallEnemy(player);
    
    assertNotNull(enemy.getComponent(PhysicsComponent.class));
    assertNotNull(enemy.getComponent(HitboxComponent.class));
    assertNotNull(enemy.getComponent(DisposingComponent.class));
    assertNotNull(enemy.getComponent(ColliderComponent.class));
    assertNotNull(enemy.getComponent(PhysicsMovementComponent.class));
    assertNotNull(enemy.getComponent(AnimationRenderComponent.class));
    assertNotNull(enemy.getComponent(NPCAnimationController.class));
    assertNotNull(enemy.getComponent(NPCSoundComponent.class));
    assertNotNull(enemy.getComponent(CombatStatsComponent.class));
    
    assertNotNull(enemy.getComponent(EnemyMeleeAttackComponent.class));
    assertNotNull(enemy.getComponent(DarknessDetectionComponent.class));
    assertNotNull(enemy.getComponent(EnemyDarknessController.class));
    assertNotNull(enemy.getComponent(AITaskComponent.class));
    assertNotNull(enemy.getComponent(LootComponent.class));
    assertNotNull(enemy.getComponent(GlowingEyesComponent.class));
  }
  
  @Test
  void createNewLargeEnemyContainsAllComponentsTest() {
    Entity enemy = NPCFactory.createLargeEnemy(player);
    
    assertNotNull(enemy.getComponent(PhysicsComponent.class));
    assertNotNull(enemy.getComponent(HitboxComponent.class));
    assertNotNull(enemy.getComponent(DisposingComponent.class));
    assertNotNull(enemy.getComponent(ColliderComponent.class));
    assertNotNull(enemy.getComponent(PhysicsMovementComponent.class));
    assertNotNull(enemy.getComponent(AnimationRenderComponent.class));
    assertNotNull(enemy.getComponent(NPCAnimationController.class));
    assertNotNull(enemy.getComponent(NPCSoundComponent.class));
    assertNotNull(enemy.getComponent(CombatStatsComponent.class));
    
    assertNotNull(enemy.getComponent(EnemyMeleeAttackComponent.class));
    assertNotNull(enemy.getComponent(DarknessDetectionComponent.class));
    assertNotNull(enemy.getComponent(EnemyDarknessController.class));
    assertNotNull(enemy.getComponent(AITaskComponent.class));
    assertNotNull(enemy.getComponent(LootComponent.class));
    assertNotNull(enemy.getComponent(GlowingEyesComponent.class));
  }  
  
  @Test
  void createNewSpawnerContainsAllComponentsTest() {
    Entity enemy = NPCFactory.createSpawnerEnemy();
    
    assertNotNull(enemy.getComponent(PhysicsComponent.class));
    assertNotNull(enemy.getComponent(HitboxComponent.class));
    assertNotNull(enemy.getComponent(DisposingComponent.class));
    assertNotNull(enemy.getComponent(ColliderComponent.class));
    assertNotNull(enemy.getComponent(PhysicsMovementComponent.class));
    assertNotNull(enemy.getComponent(AnimationRenderComponent.class));
    assertNotNull(enemy.getComponent(NPCAnimationController.class));
    assertNotNull(enemy.getComponent(NPCSoundComponent.class));
    assertNotNull(enemy.getComponent(CombatStatsComponent.class));
    
    assertNotNull(enemy.getComponent(LootComponent.class));
    assertNotNull(enemy.getComponent(PointLightComponent.class));
    assertNotNull(enemy.getComponent(AITaskComponent.class));
  }

  @Test
  void createNewLongRangeContainsAllComponentsTest() {
    Entity enemy = NPCFactory.createLongRangeEnemy(player, gameArea);

    assertNotNull(enemy.getComponent(PhysicsComponent.class));
    assertNotNull(enemy.getComponent(HitboxComponent.class));
    assertNotNull(enemy.getComponent(DisposingComponent.class));
    assertNotNull(enemy.getComponent(ColliderComponent.class));
    assertNotNull(enemy.getComponent(PhysicsMovementComponent.class));
    assertNotNull(enemy.getComponent(AnimationRenderComponent.class));
    assertNotNull(enemy.getComponent(NPCAnimationController.class));
    assertNotNull(enemy.getComponent(NPCSoundComponent.class));
    assertNotNull(enemy.getComponent(CombatStatsComponent.class));

    assertNotNull(enemy.getComponent(FireBulletListener.class));
    assertNotNull(enemy.getComponent(DarknessDetectionComponent.class));
    assertNotNull(enemy.getComponent(EnemyDarknessController.class));
    assertNotNull(enemy.getComponent(MultiAITaskComponent.class));
    assertNotNull(enemy.getComponent(LootComponent.class));
    assertNotNull(enemy.getComponent(GlowingEyesComponent.class));
  }

  @Test
  void createNewToughLongRangeContainsAllComponentsTest() {
    Entity enemy = NPCFactory.createToughLongRangeEnemy(player, gameArea);

    assertNotNull(enemy.getComponent(PhysicsComponent.class));
    assertNotNull(enemy.getComponent(HitboxComponent.class));
    assertNotNull(enemy.getComponent(DisposingComponent.class));
    assertNotNull(enemy.getComponent(ColliderComponent.class));
    assertNotNull(enemy.getComponent(PhysicsMovementComponent.class));
    assertNotNull(enemy.getComponent(AnimationRenderComponent.class));
    assertNotNull(enemy.getComponent(NPCAnimationController.class));
    assertNotNull(enemy.getComponent(NPCSoundComponent.class));
    assertNotNull(enemy.getComponent(CombatStatsComponent.class));

    assertNotNull(enemy.getComponent(ToughFireBulletListener.class));
    assertNotNull(enemy.getComponent(DarknessDetectionComponent.class));
    assertNotNull(enemy.getComponent(EnemyDarknessController.class));
    assertNotNull(enemy.getComponent(MultiAITaskComponent.class));
    assertNotNull(enemy.getComponent(LootComponent.class));
    assertNotNull(enemy.getComponent(GlowingEyesComponent.class));
  }


  @Test
  void createEnemyContainsAllAnimationsTest() {
    Entity enemy = NPCFactory.createSmallEnemy(player);
    AnimationRenderComponent animator = enemy.getComponent(AnimationRenderComponent.class);
    assertTrue(animator.hasAnimation("left"));
    assertTrue(animator.hasAnimation("right"));
    assertTrue(animator.hasAnimation("back"));
    assertTrue(animator.hasAnimation("front"));
    assertTrue(animator.hasAnimation("left-run"));
    assertTrue(animator.hasAnimation("right-run"));
    assertTrue(animator.hasAnimation("back-run"));
    assertTrue(animator.hasAnimation("front-run"));
    assertTrue(animator.hasAnimation("left-hit"));
    assertTrue(animator.hasAnimation("right-hit"));
    assertTrue(animator.hasAnimation("back-hit"));
    assertTrue(animator.hasAnimation("front-hit"));
    assertTrue(animator.hasAnimation("left-run-hit"));
    assertTrue(animator.hasAnimation("right-run-hit"));
    assertTrue(animator.hasAnimation("back-run-hit"));
    assertTrue(animator.hasAnimation("front-run-hit"));
    assertTrue(animator.hasAnimation("left-damaged"));
    assertTrue(animator.hasAnimation("right-damaged"));
    assertTrue(animator.hasAnimation("back-damaged"));
    assertTrue(animator.hasAnimation("front-damaged"));
    assertTrue(animator.hasAnimation("left-run-damaged"));
    assertTrue(animator.hasAnimation("right-run-damaged"));
    assertTrue(animator.hasAnimation("back-run-damaged"));
    assertTrue(animator.hasAnimation("front-run-damaged"));
    assertTrue(animator.hasAnimation("left-damaged-hit"));
    assertTrue(animator.hasAnimation("right-damaged-hit"));
    assertTrue(animator.hasAnimation("back-damaged-hit"));
    assertTrue(animator.hasAnimation("front-damaged-hit"));
    assertTrue(animator.hasAnimation("left-run-damaged-hit"));
    assertTrue(animator.hasAnimation("right-run-damaged-hit"));
    assertTrue(animator.hasAnimation("back-run-damaged-hit"));
    assertTrue(animator.hasAnimation("front-run-damaged-hit"));
    assertTrue(animator.hasAnimation("dead"));
  }

  @Test
  void createSpanwerContainsAdditionalAnimationsTest() {
    Entity enemy = NPCFactory.createSpawnerEnemy();
    AnimationRenderComponent animator = enemy.getComponent(AnimationRenderComponent.class);
    assertTrue(animator.hasAnimation("spawn"));
  }

  @Test
  void createNewFriendlyNPCHitboxLayerTest() {
    Entity enemy = NPCFactory.createSmallEnemy(player);
    HitboxComponent hitboxComponent = enemy.getComponent(HitboxComponent.class);
    assertEquals(PhysicsLayer.NPC, hitboxComponent.getLayer());
  }
}