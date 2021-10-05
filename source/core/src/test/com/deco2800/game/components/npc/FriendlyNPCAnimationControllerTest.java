package com.deco2800.game.components.npc;

import com.deco2800.game.physics.PhysicsService;
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
    // Register a physics and time service
    ServiceLocator.registerPhysicsService(new PhysicsService());
    ServiceLocator.registerTimeSource(new GameTime());

    // Create a new entity with minimal components
    friendlyNPCAnimationController = Mockito.spy(new FriendlyNPCAnimationController());
    npcMovement = Mockito.spy(new PhysicsMovementComponent());
    npc = new Entity()
        .addComponent(animator)
        .addComponent(friendlyNPCAnimationController)
        .addComponent(npcMovement)
        .addComponent(new PhysicsComponent());

    // Set the initial position of the npc
    npc.setPosition(new Vector2(NPC_X_POS, NPC_Y_POS));

    // Run the initial setup method of components
    friendlyNPCAnimationController.create();
    npcMovement.create();
  }

  @Test
  void createAnimationWithDefaultTest() {
    verify(animator).startAnimation(ANIMATION_DEFAULT);
  }

  @Test
  void updateWalkingToTargetTest() {
    // checks that getTarget is still called even if no target set
    friendlyNPCAnimationController.update();
    verify(npcMovement).getTarget();

    // when a new direction is set check that the direction matches
    Vector2 newDirection = new Vector2(1, 1);
    npcMovement.setTarget(newDirection);
    friendlyNPCAnimationController.update();
    verify(friendlyNPCAnimationController).updateAnimationDirection(newDirection, WALKING);
  }


  @Test
  void updateWalkingToSameTargetTwiceTest() {
    // set a new direction and verify that it updated the animation direction
    Vector2 newDirection = new Vector2(10, 0);
    npcMovement.setTarget(newDirection);
    friendlyNPCAnimationController.update();
    verify(friendlyNPCAnimationController, Mockito.times(1)).updateAnimationDirection(newDirection, WALKING);

    // keep the same direction and verify that it doesn't updated the animation direction again
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
    // Set a target direction to the right
    Vector2 newDirection = new Vector2(10, 0);
    npcMovement.setTarget(newDirection);
    friendlyNPCAnimationController.update();

    // Stop all movement, check that the direction is stationary facing the right
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

    // check the stationary and walking animations are set if looking to the north
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_BACK[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_BACK[WALKING]);

    // check the stationary and walking animations are set if looking to the east
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_RIGHT[WALKING]);

    // check the stationary and walking animations are set if looking to the south
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_FRONT[WALKING]);

    // check the stationary and walking animations are set if looking to the west
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionWest, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_LEFT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionWest, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_LEFT[WALKING]);
  }

  @Test
  void updateAnimationDirectionDefaultEntityPositionOriginLookPositionTest() {
    // Set the look position as the same position as the entity
    Vector2 lookPositionOrigin = new Vector2(0, 0);

    // Check that updating the direction keeps the animation facing the front
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

    // If facing the northeast then the animation should face the right
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorthEast, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorthEast, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_RIGHT[WALKING]);

    // If facing the northwest then the animation should face the left
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorthWest, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_LEFT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorthWest, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_LEFT[WALKING]);

    // If facing the southeast then the animation should face the front
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouthEast, STATIONARY);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouthEast, WALKING);
    verify(friendlyNPCAnimationController).setDirection(ANIMATIONS_FRONT[WALKING]);

    // If facing the southwest then the animation should face the front
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouthWest, STATIONARY);
    verify(friendlyNPCAnimationController, Mockito.times(2)).setDirection(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouthWest, WALKING);
    verify(friendlyNPCAnimationController, Mockito.times(2)).setDirection(ANIMATIONS_FRONT[WALKING]);
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
        friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, STATIONARY);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_BACK[STATIONARY]);
        friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, WALKING);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_BACK[WALKING]);

        // check the stationary and walking animations are set if looking to the east
        friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, STATIONARY);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
        friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, WALKING);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[WALKING]);

        // check the stationary and walking animations are set if looking to the south
        friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, STATIONARY);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[STATIONARY]);
        friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, WALKING);
        verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[WALKING]);

        // check the stationary and walking animations are set if looking to the west
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
    // Set different X and Y coordinates for the entity
    for (int k = -2; k <= 2; k++) {
      for (int l = -2; l <= 2; l++) {
        npc.setPosition(new Vector2(k, l));
// Try different X and Y coordinates for the looking direction, where i is the dominant direction and j is always less
        for (int i = 0; i <= 2; i++) {
          for (int j = -i + 1; j < i; j++) {
            Vector2 lookPositionNorth = new Vector2(k + j, l + i);
            Vector2 lookPositionEast = new Vector2(k + i, l + j);
            Vector2 lookPositionSouth = new Vector2(k + j, l + -i);
            Vector2 lookPositionWest = new Vector2(k + -i, l + j);

            // check the stationary and walking animations are set if looking to the north
            friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, STATIONARY);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_BACK[STATIONARY]);
            friendlyNPCAnimationController.updateAnimationDirection(lookPositionNorth, WALKING);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_BACK[WALKING]);

            // check the stationary and walking animations are set if looking to the east
            friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, STATIONARY);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[STATIONARY]);
            friendlyNPCAnimationController.updateAnimationDirection(lookPositionEast, WALKING);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_RIGHT[WALKING]);

            // check the stationary and walking animations are set if looking to the south
            friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, STATIONARY);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[STATIONARY]);
            friendlyNPCAnimationController.updateAnimationDirection(lookPositionSouth, WALKING);
            verify(friendlyNPCAnimationController, times(count)).setDirection(ANIMATIONS_FRONT[WALKING]);

            // check the stationary and walking animations are set if looking to the west
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
    // Check the animation direction is set correctly for the back direction
    friendlyNPCAnimationController.setDirection(ANIMATIONS_BACK[STATIONARY]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_BACK[STATIONARY]);
    friendlyNPCAnimationController.setDirection(ANIMATIONS_BACK[WALKING]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_BACK[WALKING]);

    // Check the animation direction is set correctly for the right direction
    friendlyNPCAnimationController.setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[STATIONARY]);
    friendlyNPCAnimationController.setDirection(ANIMATIONS_RIGHT[WALKING]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[WALKING]);

    // Check the animation direction is set correctly for the front direction
    friendlyNPCAnimationController.setDirection(ANIMATIONS_FRONT[STATIONARY]);
    // Note this runs two times as one is for the default direction
    verify(animator, Mockito.times(2)).startAnimation(ANIMATIONS_FRONT[STATIONARY]);
    friendlyNPCAnimationController.setDirection(ANIMATIONS_FRONT[WALKING]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_FRONT[WALKING]);

    // Check the animation direction is set correctly for the left direction
    friendlyNPCAnimationController.setDirection(ANIMATIONS_LEFT[STATIONARY]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_LEFT[STATIONARY]);
    friendlyNPCAnimationController.setDirection(ANIMATIONS_LEFT[WALKING]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_LEFT[WALKING]);
  }

  @Test
  void setDirectionSameAsPreviousTest() {
    friendlyNPCAnimationController.setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[STATIONARY]);

    // Setting the same direction twice should only change the animation once
    friendlyNPCAnimationController.setDirection(ANIMATIONS_RIGHT[STATIONARY]);
    verify(animator, Mockito.times(1)).startAnimation(ANIMATIONS_RIGHT[STATIONARY]);
  }
}