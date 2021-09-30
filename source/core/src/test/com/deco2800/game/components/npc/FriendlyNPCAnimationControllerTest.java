package com.deco2800.game.components.npc;

import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
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
class FriendlyNPCAnimationControllerTest {
  @Mock AnimationRenderComponent animator;

  private static final float NPC_X_POS = 0;
  private static final float NPC_Y_POS = 0;
  private static final String ANIMATION_DEFAULT = "front";
  private static final String[] ANIMATIONS_LEFT = {"left", "left-run"};
  private static final String[] ANIMATIONS_RIGHT = {"right", "right-run"};
  private static final String[] ANIMATIONS_FRONT = {"front", "front-run"};
  private static final String[] ANIMATIONS_BACK = {"back", "back-run"};
  private static final int STATIONARY = 0;
  private static final int WALKING = 1;
  Entity npc;
  @Spy FriendlyNPCAnimationController friendlyNPCAnimationController;
  @Spy PhysicsMovementComponent npcMovement;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
    friendlyNPCAnimationController = Mockito.spy(new FriendlyNPCAnimationController());
    npcMovement = Mockito.spy(new PhysicsMovementComponent());
    npc = new Entity()
        .addComponent(animator)
        .addComponent(friendlyNPCAnimationController)
        .addComponent(npcMovement)
        .addComponent(new PhysicsComponent());
    npc.setPosition(new Vector2(NPC_X_POS, NPC_Y_POS));
    friendlyNPCAnimationController.create();
    npcMovement.create();
  }

  @Test
  void createAnimationWithDefaultTest() {
    verify(animator).startAnimation(ANIMATION_DEFAULT);
  }

  @Test
  void updateWalkingToTargetTest() {
    friendlyNPCAnimationController.update();
    verify(npcMovement).getTarget();

    Vector2 newDirection = new Vector2(1, 1);
    npcMovement.setTarget(newDirection);
    friendlyNPCAnimationController.update();
    verify(friendlyNPCAnimationController).updateAnimationDirection(newDirection, WALKING);
  }


  @Test
  void updateWalkingToSameTargetTwiceTest() {
    Vector2 newDirection = new Vector2(10, 0);
    npcMovement.setTarget(newDirection);
    friendlyNPCAnimationController.update();
    verify(friendlyNPCAnimationController, Mockito.times(1)).updateAnimationDirection(newDirection, WALKING);

    friendlyNPCAnimationController.update();
    verify(friendlyNPCAnimationController, Mockito.times(1)).updateAnimationDirection(newDirection, WALKING);
  }

  @Test
  void updateNotMovingDefaultDirectionTest() {
    npcMovement.setMoving(false);
    friendlyNPCAnimationController.update();
    verify(friendlyNPCAnimationController, Mockito.times(1)).setDirection(ANIMATION_DEFAULT);
  }

  @Test
  void updateNotMovingDifferentDirectionTest() {
    Vector2 newDirection = new Vector2(10, 0);
    npcMovement.setTarget(newDirection);
    friendlyNPCAnimationController.update();
    npcMovement.setMoving(false);
    friendlyNPCAnimationController.update();
    verify(friendlyNPCAnimationController, Mockito.times(1)).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
  }

  @Test
  void updateAnimationDirectionDefaultEntityPositionOneUnitLookPositionTest() {
    Vector2 lookPositionNorth = new Vector2(0, 1);
    Vector2 lookPositionEast = new Vector2(1, 0);
    Vector2 lookPositionSouth = new Vector2(0, -1);
    Vector2 lookPositionWest = new Vector2(-1, 0);

    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_BACK[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_BACK[WALKING]);

    friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_RIGHT[WALKING]);

    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_FRONT[WALKING]);

    friendlyNPCAnimationController.updateAnimationDirection(lookPositionWest, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_LEFT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionWest, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_LEFT[WALKING]);
  }

  @Test
  void updateAnimationDirectionDefaultEntityPositionOriginLookPositionTest() {
    Vector2 lookPositionOrigin = new Vector2(0, 0);

    friendlyNPCAnimationController.updateAnimationDirection(lookPositionOrigin, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionOrigin, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_FRONT[WALKING]);
  }

  @Test
  void updateAnimationDirectionDefaultEntityPositionDiagonalLookPositionTest() {
    Vector2 lookPositionNorthEast = new Vector2(5, 5);
    Vector2 lookPositionNorthWest = new Vector2(-5, 5);
    Vector2 lookPositionSouthEast = new Vector2(5, -5);
    Vector2 lookPositionSouthWest = new Vector2(-5, -5);

    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorthEast, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorthEast, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_RIGHT[WALKING]);

    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorthWest, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_LEFT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorthWest, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_LEFT[WALKING]);

    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouthEast, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouthEast, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_FRONT[WALKING]);

    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouthWest, STATIONARY);
    verify(friendlyNPCAnimationController, Mockito.times(2)).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouthWest, WALKING);
    verify(friendlyNPCAnimationController, Mockito.times(2)).setDirection(ANIMATIONS_FRONT[WALKING]);
  }

  @Test
  void updateAnimationDirectionDefaultEntityPositionVariableUnitLookPositionTest() {
    int count = 1;
    for (int i = 0; i < 10; i++) {
      for (int j = -i + 1; j < i; j++) {
        Vector2 lookPositionNorth = new Vector2(j, i);
        Vector2 lookPositionEast = new Vector2(i, j);
        Vector2 lookPositionSouth = new Vector2(j, -i);
        Vector2 lookPositionWest = new Vector2(-i, j);

        friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, STATIONARY);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_BACK[STATIONARY]);
        friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, WALKING);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_BACK[WALKING]);

        friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, STATIONARY);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
        friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, WALKING);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[WALKING]);

        friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, STATIONARY);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[STATIONARY]);
        friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, WALKING);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[WALKING]);

        friendlyNPCAnimationController.updateAnimationDirection(lookPositionWest, STATIONARY);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_LEFT[STATIONARY]);
        friendlyNPCAnimationController.updateAnimationDirection(lookPositionWest, WALKING);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_LEFT[WALKING]);

        count++;
      }
    }
  }

  @Test
  void updateAnimationDirectionVariableEntityPositionVariableUnitLookPositionTest() {
    int count = 1;
    for (int k = -2; k <= 2; k++) {
      for (int l = -2; l <= 2; l++) {
        npc.setPosition(new Vector2(k, l));
        for (int i = 0; i <= 2; i++) {
          for (int j = -i + 1; j < i; j++) {
            Vector2 lookPositionNorth = new Vector2(k + j, l + i);
            Vector2 lookPositionEast = new Vector2(k + i, l + j);
            Vector2 lookPositionSouth = new Vector2(k + j, l + -i);
            Vector2 lookPositionWest = new Vector2(k + -i, l + j);

            friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, STATIONARY);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_BACK[STATIONARY]);
            friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, WALKING);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_BACK[WALKING]);

            friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, STATIONARY);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
            friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, WALKING);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[WALKING]);

            friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, STATIONARY);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[STATIONARY]);
            friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, WALKING);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[WALKING]);

            friendlyNPCAnimationController.updateAnimationDirection(lookPositionWest, STATIONARY);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_LEFT[STATIONARY]);
            friendlyNPCAnimationController.updateAnimationDirection(lookPositionWest, WALKING);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_LEFT[WALKING]);

            count++;
          }
        }
      }
    }
  }

  @Test
  void setDirectionTest() {
    friendlyNPCAnimationController.setDirection(ANIMATIONS_BACK[STATIONARY]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_BACK[STATIONARY]);
    friendlyNPCAnimationController.setDirection(ANIMATIONS_BACK[WALKING]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_BACK[WALKING]);

    friendlyNPCAnimationController.setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[STATIONARY]);
    friendlyNPCAnimationController.setDirection(ANIMATIONS_RIGHT[WALKING]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[WALKING]);

    friendlyNPCAnimationController.setDirection(ANIMATIONS_FRONT[STATIONARY]);
    // Note this runs two times as one is for the default direction
    verify(animator, Mockito.times(2)).startAnimation(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNPCAnimationController.setDirection(ANIMATIONS_FRONT[WALKING]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_FRONT[WALKING]);

    friendlyNPCAnimationController.setDirection(ANIMATIONS_LEFT[STATIONARY]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_LEFT[STATIONARY]);
    friendlyNPCAnimationController.setDirection(ANIMATIONS_LEFT[WALKING]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_LEFT[WALKING]);
  }

  @Test
  void setDirectionSameAsPreviousTest() {
    friendlyNPCAnimationController.setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[STATIONARY]);

    friendlyNPCAnimationController.setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[STATIONARY]);
  }
}