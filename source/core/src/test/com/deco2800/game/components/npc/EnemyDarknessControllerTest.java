package com.deco2800.game.components.npc;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.DarknessDetectionComponent;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.DistanceFireBulletTask;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class EnemyDarknessControllerTest {
  EnemyDarknessController enemyDarknessController;
  @Mock
  GameArea gameArea;
  @Mock
  DarknessDetectionComponent darknessDetectionComponent;
  @Mock
  GlowingEyesComponent glowingEyesComponent;

  static final float DEFAULT_SPEED = 1;
  static final float DARKNESS_SPEED = DEFAULT_SPEED * 3;
  static final float DARKNESS_FIRE_DURATION = 10;
  static final float LIGHT_FIRE_DURATION = 10F/2F;
  static final float PLAYER_LIGHT_CHASE_DISTANCE = 10;
  static final float PLAYER_DARK_CHASE_DISTANCE = (float) (10 * 1.7);
  Entity npc;
  Entity player;
  PhysicsMovementComponent physicsMovementComponent;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerGameArea(gameArea);
    ServiceLocator.registerTimeSource(new GameTime());
    ServiceLocator.registerPhysicsService(new PhysicsService());
    ServiceLocator.registerRenderService(new RenderService());

    player = new Entity();
    player.addComponent(darknessDetectionComponent);
    npc = new Entity();
    npc.addComponent(new EnemyDarknessController());
    npc.addComponent(new PhysicsMovementComponent());
    npc.addComponent(glowingEyesComponent);

    physicsMovementComponent = npc.getComponent(PhysicsMovementComponent.class);
    physicsMovementComponent.setSpeed(new Vector2(DEFAULT_SPEED, DEFAULT_SPEED));

    when(gameArea.getPlayer()).thenReturn(player);
    enemyDarknessController = npc.getComponent(EnemyDarknessController.class);
  }

  @Test
  void shootingEntityInAndOutOfDarknessTest() {
    DistanceFireBulletTask distanceFireBulletTask = null;

    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new DistanceFireBulletTask(player, DARKNESS_FIRE_DURATION, 1, 1));
    npc.addComponent(aiTaskComponent);

    List<Task> aiTasks = aiTaskComponent.getTasks();
    for (Task task : aiTasks) {
      if (task instanceof DistanceFireBulletTask) {
        distanceFireBulletTask = (DistanceFireBulletTask) task;
      }
    }
    assertNotNull(distanceFireBulletTask);

    assertEquals(new Vector2(DEFAULT_SPEED,DEFAULT_SPEED), physicsMovementComponent.getSpeed());
    assertEquals(DARKNESS_FIRE_DURATION, distanceFireBulletTask.getFireDuration());

    enemyDarknessController.create();
    enemyDarknessController.update();

    // Initially set to darkness speeds and attack
    assertEquals(new Vector2(DARKNESS_SPEED,DARKNESS_SPEED), physicsMovementComponent.getSpeed());
    assertEquals(LIGHT_FIRE_DURATION, distanceFireBulletTask.getFireDuration());
    verify(glowingEyesComponent, Mockito.times(1)).displayOn();

    // Back to original speeds when in light
    npc.getEvents().trigger("inLight");
    assertEquals(new Vector2(DEFAULT_SPEED,DEFAULT_SPEED), physicsMovementComponent.getSpeed());
    assertEquals(DARKNESS_FIRE_DURATION, distanceFireBulletTask.getFireDuration());
    verify(glowingEyesComponent, Mockito.times(1)).displayOff();

    // Darkness speeds when in the dark
    npc.getEvents().trigger("inShadow");
    assertEquals(new Vector2(DARKNESS_SPEED,DARKNESS_SPEED), physicsMovementComponent.getSpeed());
    assertEquals(LIGHT_FIRE_DURATION, distanceFireBulletTask.getFireDuration());
    verify(glowingEyesComponent, Mockito.times(2)).displayOn();

  }

  @Test
  void meleeEntityInAndOutOfDarknessTest() {
    ChaseTask chaseTask = null;

    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ChaseTask(player, 1, PLAYER_LIGHT_CHASE_DISTANCE, PLAYER_LIGHT_CHASE_DISTANCE));
    npc.addComponent(aiTaskComponent);

    List<Task> aiTasks = aiTaskComponent.getTasks();
    for (Task task : aiTasks) {
      if (task instanceof ChaseTask) {
        chaseTask = (ChaseTask) task;
      }
    }
    assertNotNull(chaseTask);

    assertEquals(new Vector2(DEFAULT_SPEED,DEFAULT_SPEED), physicsMovementComponent.getSpeed());

    enemyDarknessController.create();
    enemyDarknessController.update();

    // Initially set to darkness speeds
    assertEquals(new Vector2(DARKNESS_SPEED,DARKNESS_SPEED), physicsMovementComponent.getSpeed());
    verify(glowingEyesComponent, Mockito.times(1)).displayOn();

    // Back to original speeds when in light
    npc.getEvents().trigger("inLight");
    assertEquals(new Vector2(DEFAULT_SPEED,DEFAULT_SPEED), physicsMovementComponent.getSpeed());
    verify(glowingEyesComponent, Mockito.times(1)).displayOff();

    // Darkness speeds when in the dark
    npc.getEvents().trigger("inShadow");
    assertEquals(new Vector2(DARKNESS_SPEED,DARKNESS_SPEED), physicsMovementComponent.getSpeed());
    verify(glowingEyesComponent, Mockito.times(2)).displayOn();

  }

  @Test
  void playerStartInLightTest() {
    ChaseTask chaseTask = null;

    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ChaseTask(player, 1, PLAYER_LIGHT_CHASE_DISTANCE, PLAYER_LIGHT_CHASE_DISTANCE));
    npc.addComponent(aiTaskComponent);

    List<Task> aiTasks = aiTaskComponent.getTasks();
    for (Task task : aiTasks) {
      if (task instanceof ChaseTask) {
        chaseTask = (ChaseTask) task;
      }
    }
    assertNotNull(chaseTask);

    assertEquals(PLAYER_LIGHT_CHASE_DISTANCE, chaseTask.getMaxChaseDistance());

    when(darknessDetectionComponent.isInLight()).thenReturn(true);

    // Initially set to player in light aggro distance
    enemyDarknessController.create();
    assertEquals(PLAYER_LIGHT_CHASE_DISTANCE, chaseTask.getMaxChaseDistance());
  }

  @Test
  void playerStartInDarkTest() {
    ChaseTask chaseTask = null;

    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ChaseTask(player, 1, PLAYER_LIGHT_CHASE_DISTANCE, PLAYER_LIGHT_CHASE_DISTANCE));
    npc.addComponent(aiTaskComponent);

    List<Task> aiTasks = aiTaskComponent.getTasks();
    for (Task task : aiTasks) {
      if (task instanceof ChaseTask) {
        chaseTask = (ChaseTask) task;
      }
    }
    assertNotNull(chaseTask);

    assertEquals(PLAYER_LIGHT_CHASE_DISTANCE, chaseTask.getMaxChaseDistance());
    assertEquals(PLAYER_LIGHT_CHASE_DISTANCE, chaseTask.getViewDistance());

    when(darknessDetectionComponent.isInLight()).thenReturn(false);

    // Initially set to player in light aggro distance
    enemyDarknessController.create();
    assertEquals(PLAYER_DARK_CHASE_DISTANCE, chaseTask.getMaxChaseDistance());
    assertEquals(PLAYER_DARK_CHASE_DISTANCE, chaseTask.getViewDistance());
  }

  @Test
  void playerInAndOutOfDarknessTest() {
    ChaseTask chaseTask = null;

    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ChaseTask(player, 1, PLAYER_LIGHT_CHASE_DISTANCE, PLAYER_LIGHT_CHASE_DISTANCE));
    npc.addComponent(aiTaskComponent);

    List<Task> aiTasks = aiTaskComponent.getTasks();
    for (Task task : aiTasks) {
      if (task instanceof ChaseTask) {
        chaseTask = (ChaseTask) task;
      }
    }
    assertNotNull(chaseTask);

    assertEquals(PLAYER_LIGHT_CHASE_DISTANCE, chaseTask.getMaxChaseDistance());
    assertEquals(PLAYER_LIGHT_CHASE_DISTANCE, chaseTask.getViewDistance());

    when(darknessDetectionComponent.isInLight()).thenReturn(true);

    // Initially set to player in light aggro distance
    enemyDarknessController.create();
    enemyDarknessController.update();

    assertEquals(PLAYER_LIGHT_CHASE_DISTANCE, chaseTask.getMaxChaseDistance());
    assertEquals(PLAYER_LIGHT_CHASE_DISTANCE, chaseTask.getViewDistance());

    // When player in dark set to darkest aggro distance
    player.getEvents().trigger("inShadow");
    assertEquals(PLAYER_DARK_CHASE_DISTANCE, chaseTask.getMaxChaseDistance());
    assertEquals(PLAYER_DARK_CHASE_DISTANCE, chaseTask.getViewDistance());

    // Back to original aggro distance when in light
    player.getEvents().trigger("inLight");
    assertEquals(PLAYER_LIGHT_CHASE_DISTANCE, chaseTask.getMaxChaseDistance());
    assertEquals(PLAYER_LIGHT_CHASE_DISTANCE, chaseTask.getViewDistance());
  }

}