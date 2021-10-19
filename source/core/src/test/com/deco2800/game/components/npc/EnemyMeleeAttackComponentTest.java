package com.deco2800.game.components.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.MultiAITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.DarknessDetectionComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.npc.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.memento.Player;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class EnemyMeleeAttackComponentTest {
  @Mock
  GameArea gameArea;
  @Mock
  NPCSoundComponent npcSoundComponent;
  @Mock
  Fixture fixture;
  @Mock
  Filter filter;

  GameTime time;
  Entity npc;

  EnemyMeleeAttackComponent enemyMeleeAttackComponent;
  NPCSoundComponent soundComponent;
  PlayerCombatStatsComponent playerCombatStatsComponent;
  static final int MAX_PLAYER_HEALTH = 5;
  static final int ENEMY_ATTACK = 1;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerGameArea(gameArea);
    time = mock(GameTime.class);
    ServiceLocator.registerTimeSource(time);
    when(time.isPaused()).thenReturn(false);
    ServiceLocator.registerPhysicsService(new PhysicsService());


    Entity player = new Entity();
    player.addComponent(new PlayerCombatStatsComponent(MAX_PLAYER_HEALTH, 1, 1, 1, 1));

    when(gameArea.getPlayer()).thenReturn(player);

    npc = new Entity();
    npc.addComponent(new EnemyMeleeAttackComponent());
    npc.addComponent(new CombatStatsComponent(1, ENEMY_ATTACK));
    npc.addComponent(npcSoundComponent);

    soundComponent = npc.getComponent(NPCSoundComponent.class);
    enemyMeleeAttackComponent = npc.getComponent(EnemyMeleeAttackComponent.class);
    enemyMeleeAttackComponent.create();
    playerCombatStatsComponent = player.getComponent(PlayerCombatStatsComponent.class);
  }

  @Test
  void playerInRangeTest() {
    when(fixture.getFilterData()).thenReturn(filter);
    filter.categoryBits = PhysicsLayer.PLAYER;

    assertEquals(MAX_PLAYER_HEALTH , playerCombatStatsComponent.getHealth());

    npc.getEvents().trigger("collisionStart", fixture, fixture);

    when(time.getTime()).thenReturn(0L);
    enemyMeleeAttackComponent.update();

    verify(soundComponent, Mockito.times(1)).playMeleeAttack();
    assertEquals(MAX_PLAYER_HEALTH - ENEMY_ATTACK, playerCombatStatsComponent.getHealth());

    when(time.getTime()).thenReturn(3000L);
    playerCombatStatsComponent.update();
    enemyMeleeAttackComponent.update();
    verify(soundComponent, Mockito.times(2)).playMeleeAttack();
    assertEquals(MAX_PLAYER_HEALTH - ENEMY_ATTACK*2, playerCombatStatsComponent.getHealth());

  }

  @Test
  void playerOutRangeTest() {
    when(fixture.getFilterData()).thenReturn(filter);
    filter.categoryBits = PhysicsLayer.PLAYER;

    assertEquals(MAX_PLAYER_HEALTH , playerCombatStatsComponent.getHealth());

    npc.getEvents().trigger("collisionStart", fixture, fixture);

    when(time.getTime()).thenReturn(0L);
    enemyMeleeAttackComponent.update();

    verify(soundComponent, Mockito.times(1)).playMeleeAttack();
    assertEquals(MAX_PLAYER_HEALTH - ENEMY_ATTACK, playerCombatStatsComponent.getHealth());

    npc.getEvents().trigger("collisionEnd", fixture, fixture);

    when(time.getTime()).thenReturn(3000L);
    playerCombatStatsComponent.update();
    enemyMeleeAttackComponent.update();
    verify(soundComponent, Mockito.times(1)).playMeleeAttack();
    assertEquals(MAX_PLAYER_HEALTH - ENEMY_ATTACK, playerCombatStatsComponent.getHealth());

  }

}