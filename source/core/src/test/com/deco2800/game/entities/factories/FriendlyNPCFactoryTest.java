package com.deco2800.game.entities.factories;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.npc.NPCAnimationController;
import com.deco2800.game.components.npc.FriendlyNPCTriggerComponent;
import com.deco2800.game.components.npc.SpeechIconComponent;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.ResourceService;
import org.junit.jupiter.api.Test;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class FriendlyNPCFactoryTest {
  private Entity npc;
  private Entity nonWanderingNPC;

  @BeforeEach
  void beforeEach() {
    // Setup resource locator to load speech bubble texture and npc movement atlas
    String[] speechTextures = {"images/dialogue/raw/npc_indicator.png"};
    String[] npcMovementAtlas = {"images/npc_movement/atlas_for_testing.atlas"};
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.getResourceService().loadTextures(speechTextures);
    ServiceLocator.getResourceService().loadTextureAtlases(npcMovementAtlas);
    ServiceLocator.getResourceService().loadAll();

    // Register the physics service
    ServiceLocator.registerPhysicsService(new PhysicsService());

    //Setup lighting service
    ServiceLocator.registerUnlitRenderService(new RenderService());
    ServiceLocator.registerRenderService(new RenderService());

    // Create a wandering npc
    npc = FriendlyNPCFactory.createNewFriendlyNPC(
        StoryNames.PROLOGUE, "images/npc_movement/atlas_for_testing.atlas", true);

    // Create a non-wandering npc
    nonWanderingNPC = FriendlyNPCFactory.createNewFriendlyNPC(
        StoryNames.PROLOGUE, "images/npc_movement/atlas_for_testing.atlas", false);
  }

  @Test
  void createNewFriendlyNPCContainsAllComponentsTest() {
    assertNotNull(npc.getComponent(PhysicsComponent.class));
    assertNotNull(npc.getComponent(PhysicsMovementComponent.class));
    assertNotNull(npc.getComponent(HitboxComponent.class));
    assertNotNull(npc.getComponent(DisposingComponent.class));
    assertNotNull(npc.getComponent(FriendlyNPCTriggerComponent.class));
    assertNotNull(npc.getComponent(AnimationRenderComponent.class));
    assertNotNull(npc.getComponent(SpeechIconComponent.class));
    assertNotNull(npc.getComponent(NPCAnimationController.class));
    assertNotNull(npc.getComponent(AITaskComponent.class));
  }

  @Test
  void createNewFriendlyNPCContainsAllAnimationsTest() {
    AnimationRenderComponent animator = npc.getComponent(AnimationRenderComponent.class);
    assertTrue(animator.hasAnimation("left"));
    assertTrue(animator.hasAnimation("right"));
    assertTrue(animator.hasAnimation("back"));
    assertTrue(animator.hasAnimation("front"));
    assertTrue(animator.hasAnimation("left-run"));
    assertTrue(animator.hasAnimation("right-run"));
    assertTrue(animator.hasAnimation("back-run"));
    assertTrue(animator.hasAnimation("front-run"));
  }

  @Test
  void createNewFriendlyNPCNotWanderingPartialAnimationsTest() {
    AnimationRenderComponent animator = nonWanderingNPC.getComponent(AnimationRenderComponent.class);
    assertTrue(animator.hasAnimation("left"));
    assertTrue(animator.hasAnimation("right"));
    assertTrue(animator.hasAnimation("back"));
    assertTrue(animator.hasAnimation("front"));
    assertFalse(animator.hasAnimation("left-run"));
    assertFalse(animator.hasAnimation("right-run"));
    assertFalse(animator.hasAnimation("back-run"));
    assertFalse(animator.hasAnimation("front-run"));
  }

  @Test
  void createNewFriendlyNPCHitboxLayerTest() {
    HitboxComponent hitboxComponent = npc.getComponent(HitboxComponent.class);
    assertEquals(PhysicsLayer.FRIENDLY_NPC, hitboxComponent.getLayer());
  }
}