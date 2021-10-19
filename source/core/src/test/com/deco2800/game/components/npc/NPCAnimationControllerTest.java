package com.deco2800.game.components.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class NPCAnimationControllerTest {
  @Mock
  AnimationRenderComponent friendlyAnimator;
  @Mock
  AnimationRenderComponent enemyAnimator;
  @Mock
  TextureAtlas textureAtlas;

  private static final float NPC_X_POS = 0;
  private static final float NPC_Y_POS = 0;
  private static final String ANIMATION_DEFAULT = "front";
  private static final String[] ANIMATIONS_LEFT = {"left", "left-run"};
  private static final String[] ANIMATIONS_RIGHT = {"right", "right-run"};
  private static final String[] ANIMATIONS_FRONT = {"front", "front-run"};
  private static final String[] ANIMATIONS_BACK = {"back", "back-run"};
  private static final int STATIONARY = 0;
  private static final int WALKING = 1;
  private Entity friendlyNpc;
  private Entity enemyNpc;
  private TouchAttackComponent touchAttackComponentSpy;
  private GlowingEyesComponent glowingEyesComponentSpy;
  private GameTime time;
  @Spy
  NPCAnimationController friendlyNpcAnimationController;
  @Spy
  NPCAnimationController enemyAnimationController;
  @Spy
  PhysicsMovementComponent friendlyNpcMovement;
  @Spy
  PhysicsMovementComponent enemyNpcMovement;
  @Spy
  NPCSoundComponent npcSoundComponent;
  @Spy
  AITaskComponent aiTaskComponent;
  @Spy
  ColliderComponent colliderComponent;
  @Spy
  HitboxComponent hitboxComponent;


  @BeforeEach
  void beforeEach() {
    // Register a physics and time service
    time = mock(GameTime.class);
    ServiceLocator.registerTimeSource(time);
    ServiceLocator.registerPhysicsService(new PhysicsService());
    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("images/Enemy_Assets/SmallEnemy/small_enemy.atlas"));



    // Create a new entity with minimal components
    friendlyNpcAnimationController = Mockito.spy(new NPCAnimationController());
    friendlyNpcMovement = Mockito.spy(new PhysicsMovementComponent());
    friendlyNpc = new Entity()
        .addComponent(friendlyAnimator)
        .addComponent(friendlyNpcAnimationController)
        .addComponent(friendlyNpcMovement)
        .addComponent(new PhysicsComponent());


    TouchAttackComponent touchAttackComponent = new TouchAttackComponent((short) 0);
    touchAttackComponentSpy = Mockito.spy(touchAttackComponent);

    GlowingEyesComponent glowingEyesComponent = new GlowingEyesComponent(textureAtlas);
    glowingEyesComponentSpy = Mockito.spy(glowingEyesComponent);

    enemyNpc = new Entity()
        .addComponent(enemyAnimator)
        .addComponent(enemyAnimationController)
        .addComponent(enemyNpcMovement)
        .addComponent(new CombatStatsComponent(4, 1))
        .addComponent(npcSoundComponent)
        .addComponent(aiTaskComponent)
        .addComponent(colliderComponent)
        .addComponent(hitboxComponent)
        .addComponent(glowingEyesComponentSpy)
        .addComponent(touchAttackComponentSpy)
        .addComponent(new PhysicsComponent());

    // Set the initial position of the npc
    friendlyNpc.setPosition(new Vector2(NPC_X_POS, NPC_Y_POS));

    // Run the initial setup method of components
    friendlyNpcAnimationController.create();
    friendlyNpcMovement.create();
    enemyAnimationController.create();
    enemyNpcMovement.create();
  }

  @Test
  void createAnimationWithDefaultTest() {
    verify(friendlyAnimator).startAnimation(ANIMATION_DEFAULT);
  }

  @Test
  void updateWalkingToTargetTest() {
    // checks that getTarget is still called even if no target set
    friendlyNpcAnimationController.update();
    verify(friendlyNpcMovement).getTarget();

    // when a new direction is set check that the direction matches
    Vector2 newDirection = new Vector2(1, 1);
    friendlyNpcMovement.setTarget(newDirection);
    friendlyNpcAnimationController.update();
    verify(friendlyNpcAnimationController).updateAnimationDirection(newDirection, WALKING);
  }


  @Test
  void updateWalkingToSameTargetTwiceTest() {
    // set a new direction and verify that it updated the animation direction
    Vector2 newDirection = new Vector2(10, 0);
    friendlyNpcMovement.setTarget(newDirection);
    friendlyNpcAnimationController.update();
    verify(friendlyNpcAnimationController, Mockito.times(1)).updateAnimationDirection(newDirection, WALKING);

    // keep the same direction and verify that it doesn't updated the animation direction again
    friendlyNpcAnimationController.update();
    verify(friendlyNpcAnimationController, Mockito.times(1)).updateAnimationDirection(newDirection, WALKING);
  }

  @Test
  void updateNotMovingDefaultDirectionTest() {
    friendlyNpcMovement.setMoving(false);
    friendlyNpcAnimationController.update();
    verify(friendlyNpcAnimationController, Mockito.times(1)).setDirection(ANIMATION_DEFAULT);
  }

  @Test
  void updateNotMovingDifferentDirectionTest() {
    // Set a target direction to the right
    Vector2 newDirection = new Vector2(10, 0);
    friendlyNpcMovement.setTarget(newDirection);
    friendlyNpcAnimationController.update();

    // Stop all movement, check that the direction is stationary facing the right
    friendlyNpcMovement.setMoving(false);
    friendlyNpcAnimationController.update();
    verify(friendlyNpcAnimationController, Mockito.times(1)).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
  }

  @Test
  void updateAnimationDirectionDefaultEntityPositionOneUnitLookPositionTest() {
    Vector2 lookPositionNorth = new Vector2(0, 1);
    Vector2 lookPositionEast = new Vector2(1, 0);
    Vector2 lookPositionSouth = new Vector2(0, -1);
    Vector2 lookPositionWest = new Vector2(-1, 0);

    // check the stationary and walking animations are set if looking to the north
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionNorth, STATIONARY);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_BACK[STATIONARY]);
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionNorth, WALKING);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_BACK[WALKING]);

    // check the stationary and walking animations are set if looking to the east
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionEast, STATIONARY);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionEast, WALKING);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_RIGHT[WALKING]);

    // check the stationary and walking animations are set if looking to the south
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionSouth, STATIONARY);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionSouth, WALKING);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_FRONT[WALKING]);

    // check the stationary and walking animations are set if looking to the west
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionWest, STATIONARY);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_LEFT[STATIONARY]);
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionWest, WALKING);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_LEFT[WALKING]);
  }

  @Test
  void updateAnimationDirectionDefaultEntityPositionOriginLookPositionTest() {
    // Set the look position as the same position as the entity
    Vector2 lookPositionOrigin = new Vector2(0, 0);

    // Check that updating the direction keeps the animation facing the front
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionOrigin, STATIONARY);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionOrigin, WALKING);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_FRONT[WALKING]);
  }

  @Test
  void updateAnimationDirectionDefaultEntityPositionDiagonalLookPositionTest() {
    Vector2 lookPositionNorthEast = new Vector2(5, 5);
    Vector2 lookPositionNorthWest = new Vector2(-5, 5);
    Vector2 lookPositionSouthEast = new Vector2(5, -5);
    Vector2 lookPositionSouthWest = new Vector2(-5, -5);

    // If facing the northeast then the animation should face the right
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionNorthEast, STATIONARY);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionNorthEast, WALKING);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_RIGHT[WALKING]);

    // If facing the northwest then the animation should face the left
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionNorthWest, STATIONARY);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_LEFT[STATIONARY]);
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionNorthWest, WALKING);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_LEFT[WALKING]);

    // If facing the southeast then the animation should face the front
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionSouthEast, STATIONARY);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionSouthEast, WALKING);
    verify(friendlyNpcAnimationController).setDirection(ANIMATIONS_FRONT[WALKING]);

    // If facing the southwest then the animation should face the front
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionSouthWest, STATIONARY);
    verify(friendlyNpcAnimationController, Mockito.times(2)).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNpcAnimationController.updateAnimationDirection(lookPositionSouthWest, WALKING);
    verify(friendlyNpcAnimationController, Mockito.times(2)).setDirection(ANIMATIONS_FRONT[WALKING]);
  }

  @Test
  void updateAnimationDirectionDefaultEntityPositionVariableUnitLookPositionTest() {
    int count = 1;
    // Try different X and Y coordinates for the looking direction, where i is the dominant direction and j is always less
    for (int i = 0; i < 10; i++) {
      for (int j = -i + 1; j < i; j++) {
        Vector2 lookPositionNorth = new Vector2(j, i);
        Vector2 lookPositionEast = new Vector2(i, j);
        Vector2 lookPositionSouth = new Vector2(j, -i);
        Vector2 lookPositionWest = new Vector2(-i, j);

        // check the stationary and walking animations are set if looking to the north
        friendlyNpcAnimationController.updateAnimationDirection(lookPositionNorth, STATIONARY);
        verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_BACK[STATIONARY]);
        friendlyNpcAnimationController.updateAnimationDirection(lookPositionNorth, WALKING);
        verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_BACK[WALKING]);

        // check the stationary and walking animations are set if looking to the east
        friendlyNpcAnimationController.updateAnimationDirection(lookPositionEast, STATIONARY);
        verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
        friendlyNpcAnimationController.updateAnimationDirection(lookPositionEast, WALKING);
        verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[WALKING]);

        // check the stationary and walking animations are set if looking to the south
        friendlyNpcAnimationController.updateAnimationDirection(lookPositionSouth, STATIONARY);
        verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[STATIONARY]);
        friendlyNpcAnimationController.updateAnimationDirection(lookPositionSouth, WALKING);
        verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[WALKING]);

        // check the stationary and walking animations are set if looking to the west
        friendlyNpcAnimationController.updateAnimationDirection(lookPositionWest, STATIONARY);
        verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_LEFT[STATIONARY]);
        friendlyNpcAnimationController.updateAnimationDirection(lookPositionWest, WALKING);
        verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_LEFT[WALKING]);

        count++;
      }
    }
  }

  @Test
  void updateAnimationDirectionVariableEntityPositionVariableUnitLookPositionTest() {
    int count = 1;
    // Set different X and Y coordinates for the entity
    for (int k = -2; k <= 2; k++) {
      for (int l = -2; l <= 2; l++) {
        friendlyNpc.setPosition(new Vector2(k, l));
    // Try different X and Y coordinates for the looking direction, where i is the dominant direction and j is always less
        for (int i = 0; i <= 2; i++) {
          for (int j = -i + 1; j < i; j++) {
            Vector2 lookPositionNorth = new Vector2(k + j, l + i);
            Vector2 lookPositionEast = new Vector2(k + i, l + j);
            Vector2 lookPositionSouth = new Vector2(k + j, l + -i);
            Vector2 lookPositionWest = new Vector2(k + -i, l + j);

            // check the stationary and walking animations are set if looking to the north
            friendlyNpcAnimationController.updateAnimationDirection(lookPositionNorth, STATIONARY);
            verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_BACK[STATIONARY]);
            friendlyNpcAnimationController.updateAnimationDirection(lookPositionNorth, WALKING);
            verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_BACK[WALKING]);

            // check the stationary and walking animations are set if looking to the east
            friendlyNpcAnimationController.updateAnimationDirection(lookPositionEast, STATIONARY);
            verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
            friendlyNpcAnimationController.updateAnimationDirection(lookPositionEast, WALKING);
            verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[WALKING]);

            // check the stationary and walking animations are set if looking to the south
            friendlyNpcAnimationController.updateAnimationDirection(lookPositionSouth, STATIONARY);
            verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[STATIONARY]);
            friendlyNpcAnimationController.updateAnimationDirection(lookPositionSouth, WALKING);
            verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[WALKING]);

            // check the stationary and walking animations are set if looking to the west
            friendlyNpcAnimationController.updateAnimationDirection(lookPositionWest, STATIONARY);
            verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_LEFT[STATIONARY]);
            friendlyNpcAnimationController.updateAnimationDirection(lookPositionWest, WALKING);
            verify(friendlyNpcAnimationController, times(count)).setDirection(ANIMATIONS_LEFT[WALKING]);

            count++;
          }
        }
      }
    }
  }

  @Test
  void setDirectionTest() {
    // Check the animation direction is set correctly for the back direction
    friendlyNpcAnimationController.setDirection(ANIMATIONS_BACK[STATIONARY]);
    verify(friendlyAnimator, Mockito.times(1)).startAnimation(ANIMATIONS_BACK[STATIONARY]);
    friendlyNpcAnimationController.setDirection(ANIMATIONS_BACK[WALKING]);
    verify(friendlyAnimator, Mockito.times(1)).startAnimation(ANIMATIONS_BACK[WALKING]);

    // Check the animation direction is set correctly for the right direction
    friendlyNpcAnimationController.setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    verify(friendlyAnimator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[STATIONARY]);
    friendlyNpcAnimationController.setDirection(ANIMATIONS_RIGHT[WALKING]);
    verify(friendlyAnimator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[WALKING]);

    // Check the animation direction is set correctly for the front direction
    friendlyNpcAnimationController.setDirection(ANIMATIONS_FRONT[STATIONARY]);
    // Note this runs two times as one is for the default direction
    verify(friendlyAnimator, Mockito.times(2)).startAnimation(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNpcAnimationController.setDirection(ANIMATIONS_FRONT[WALKING]);
    verify(friendlyAnimator, Mockito.times(1)).startAnimation(ANIMATIONS_FRONT[WALKING]);

    // Check the animation direction is set correctly for the left direction
    friendlyNpcAnimationController.setDirection(ANIMATIONS_LEFT[STATIONARY]);
    verify(friendlyAnimator, Mockito.times(1)).startAnimation(ANIMATIONS_LEFT[STATIONARY]);
    friendlyNpcAnimationController.setDirection(ANIMATIONS_LEFT[WALKING]);
    verify(friendlyAnimator, Mockito.times(1)).startAnimation(ANIMATIONS_LEFT[WALKING]);
  }

  @Test
  void setDirectionSameAsPreviousTest() {
    friendlyNpcAnimationController.setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    verify(friendlyAnimator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[STATIONARY]);

    // Setting the same direction twice should only change the animation once
    friendlyNpcAnimationController.setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    verify(friendlyAnimator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[STATIONARY]);
  }

  @Test
  void checkMethodsForWhenHitTest() {
    CombatStatsComponent combatStatsComponent = enemyNpc.getComponent(CombatStatsComponent.class);
    combatStatsComponent.hit(1);

    when(time.isPaused()).thenReturn(false);
    when(enemyNpcMovement.getMoving()).thenReturn(false);

    // Verify that hit animation was played
    when(time.getTimeSince(anyLong())).thenReturn(100L);
    enemyAnimationController.update();
    verify(enemyAnimator, Mockito.times(1)).startAnimation("front-hit");
    verify(npcSoundComponent, Mockito.times(1)).playHit();

    // Verify that hit animation was not played as time is past duration
    when(time.getTimeSince(anyLong())).thenReturn(1000L);
    enemyAnimationController.update();
    verify(enemyAnimator, Mockito.times(1)).startAnimation("front-hit");
    verify(npcSoundComponent, Mockito.times(1)).playHit();
  }

  @Test
  void checkMethodsForWhenDamagedTest() {
    CombatStatsComponent combatStatsComponent = enemyNpc.getComponent(CombatStatsComponent.class);
    combatStatsComponent.hit(2);

    when(time.isPaused()).thenReturn(false);
    when(enemyNpcMovement.getMoving()).thenReturn(false);

    // Verify that hit damaged animation was played
    when(time.getTimeSince(anyLong())).thenReturn(100L);
    enemyAnimationController.update();
    verify(enemyAnimator, Mockito.times(1)).startAnimation("front-damaged-hit");
    verify(enemyAnimator, Mockito.times(0)).startAnimation("front-damaged");

    // Verify that hit animation was not played as time is past duration, just play damaged animation
    when(time.getTimeSince(anyLong())).thenReturn(1000L);
    enemyAnimationController.update();
    verify(enemyAnimator, Mockito.times(1)).startAnimation("front-damaged-hit");
    verify(enemyAnimator, Mockito.times(1)).startAnimation("front-damaged");
  }

  @Test
  void checkMethodsForSpawningTest() {
    enemyNpc.getEvents().trigger("spawn");

    when(time.isPaused()).thenReturn(false);
    when(enemyNpcMovement.getMoving()).thenReturn(false);

    // Verify that spawn animation was played
    when(time.getTimeSince(anyLong())).thenReturn(100L);
    enemyAnimationController.update();
    verify(enemyAnimator, Mockito.times(1)).startAnimation("spawn");

    // Verify that spawn animation was not played as time is past duration
    when(time.getTimeSince(anyLong())).thenReturn(1000L);
    enemyAnimationController.update();
    verify(enemyAnimator, Mockito.times(1)).startAnimation("spawn");

    // Verify that spawn animation was not played as spawner is damaged
    CombatStatsComponent combatStatsComponent = enemyNpc.getComponent(CombatStatsComponent.class);
    combatStatsComponent.hit(2);
    enemyNpc.getEvents().trigger("spawn");
    when(time.getTimeSince(anyLong())).thenReturn(100L);
    enemyAnimationController.update();
    verify(enemyAnimator, Mockito.times(1)).startAnimation("spawn");
  }

  @Test
  void checkMethodsForWhenDeadTest() {
    CombatStatsComponent combatStatsComponent = enemyNpc.getComponent(CombatStatsComponent.class);
    combatStatsComponent.hit(4);

    when(time.isPaused()).thenReturn(false);
    enemyAnimationController.update();
    enemyAnimationController.update();

    // Check that all components were deactived and dead methods called
    verify(glowingEyesComponentSpy, Mockito.times(1)).deactivate();
    verify(enemyAnimator, Mockito.times(1)).startAnimation("dead");
    verify(npcSoundComponent, Mockito.times(1)).playDead();
    verify(npcSoundComponent, Mockito.times(0)).playHit();
    verify(aiTaskComponent, Mockito.times(1)).addTask(any(PriorityTask.class));
    verify(touchAttackComponentSpy, Mockito.times(1)).disable();
    verify(colliderComponent, Mockito.times(1)).setLayer(PhysicsLayer.NONE);
    verify(hitboxComponent, Mockito.times(1)).setLayer(PhysicsLayer.NONE);
  }
}