package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * This class listens to events relevant to a NPCs state and plays the animation when one
 * of the events is triggered. 
 */
public class FriendlyNPCAnimationController extends Component {
  // arrays are used to easily index into the stationary and running states of the animation
  private static final String[] ANIMATIONS_LEFT = {"left", "left-run"};
  private static final String[] ANIMATIONS_RIGHT = {"right", "right-run"};
  private static final String[] ANIMATIONS_FRONT = {"front", "front-run"};
  private static final String[] ANIMATIONS_BACK = {"back", "back-run"};
  private static final int STATIONARY = 0;
  private static final int WALKING = 1;
  private String[] currentDirection;
  private Vector2 currentWalkingTarget;
  AnimationRenderComponent animator;
  private String lastAnimation;
  private GameTime gameTime;

  /**
   * Creates the animator and sets default animation.
   */
  @Override
  public void create() {
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    animator.startAnimation(ANIMATIONS_FRONT[STATIONARY]);
    currentDirection = ANIMATIONS_FRONT;
    gameTime = ServiceLocator.getTimeSource();
  }

  /**
   * Extracts the current target coordinates and changes the animation to face that direction. For example if the
   * target coordinates are to the north than we will see the npc back so update the animation to the back animation.
   */
  @Override
  public void update() {
    if (gameTime.isPaused()) {
      return;
    }

    PhysicsMovementComponent npcMovement = entity.getComponent(PhysicsMovementComponent.class);
    Vector2 walkTarget = npcMovement.getTarget();
    Vector2 myPosition = entity.getPosition();



    // Account for rounding errors, if the entity is close enough to the walk position then stop
    if (Math.abs(walkTarget.x - myPosition.x) < 0.1 && Math.abs(walkTarget.y - myPosition.y) < 0.1) {
      npcMovement.setMoving(false);
    }

    // if the npc is not moving then set the animation as the stationary animation facing the same direction
    if (!npcMovement.getMoving()) {
      setDirection(currentDirection[STATIONARY]);
    // if there is target coordinates then update the animation direction to face it
    } else if (walkTarget != null && walkTarget != currentWalkingTarget) {
      currentWalkingTarget = walkTarget;
      updateAnimationDirection(walkTarget, WALKING);
    }
  }

  /**
   * Updates the direction the animation is facing based on the coordinates the animation should be facing towards.
   * Chooses the correct animation state that should be set as well.
   * @param lookPosition the coordinates that the animation should face
   * @param state whether the npc is currently walking (value of 1) or stationary (value of 0)
   */
  public void updateAnimationDirection(Vector2 lookPosition, int state) {
    Vector2 myPos = entity.getPosition();
    float xOffset = lookPosition.x - myPos.x;
    float yOffset = lookPosition.y - myPos.y;

    // if the coordinates are in a 45 degree cone above the NPC then set the animation to be the back
    if (yOffset > 0 && yOffset > -xOffset && yOffset > xOffset) {
      setDirection(ANIMATIONS_BACK[state]);
      currentDirection = ANIMATIONS_BACK;
    }
    // if the coordinates are in a 45 degree cone below the NPC then set the animation to be the front
    else if (yOffset <= 0 && yOffset <= -xOffset && yOffset <= xOffset) {
      setDirection(ANIMATIONS_FRONT[state]);
      currentDirection = ANIMATIONS_FRONT;
    }
    // if the coordinates are in a 45 degree cone to the right the NPC then set the animation to be the right
    else if (xOffset > 0 && yOffset > -xOffset && yOffset <= xOffset) {
      setDirection(ANIMATIONS_RIGHT[state]);
      currentDirection = ANIMATIONS_RIGHT;
    }
    // if the coordinates are in a 45 degree cone to the left of the NPC then set the animation to be the left
    else if (xOffset < 0 && yOffset <= -xOffset && yOffset > xOffset) {
      setDirection(ANIMATIONS_LEFT[state]);
      currentDirection = ANIMATIONS_LEFT;
    }
  }

  /**
   * Starts a directional animation and stores as the last animation played
   * @param direction the animation state to set the direction to
   */
  public void setDirection(String direction) {
    if (!direction.equals(lastAnimation)) {
      animator.startAnimation(direction);
      lastAnimation = direction;
    }
  }
}
