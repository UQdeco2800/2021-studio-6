package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import org.slf4j.LoggerFactory;


/**
 * This class listens to events relevant to a NPCs state and plays the animation when one
 * of the events is triggered. 
 */
public class FriendlyNPCAnimationController extends Component {
  private static org.slf4j.Logger logger = LoggerFactory.getLogger(FriendlyNPCAnimationController.class);
  private static final String[] ANIMATIONS_LEFT = {"left", "left-run"};
  private static final String[] ANIMATIONS_RIGHT = {"right", "right-run"};
  private static final String[] ANIMATIONS_FRONT = {"front", "front-run"};
  private static final String[] ANIMATIONS_BACK = {"back", "back-run"};
  private static final int WALKING = 1;
  private String currentDirection;
  private Vector2 currentWalkingTarget;
  AnimationRenderComponent animator;
  private String lastAnimation;

  /**
   * Creates the animator and sets default animation.
   */
  @Override
  public void create() {
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    animator.startAnimation("front");
  }

  @Override
  public void update() {
    PhysicsMovementComponent npcMovement = entity.getComponent(PhysicsMovementComponent.class);
    Vector2 walkTarget = npcMovement.getTarget();

    if (walkTarget != null && walkTarget != currentWalkingTarget) {
      currentWalkingTarget = walkTarget;
      updateAnimationDirection(walkTarget, WALKING);
    }

    if (!npcMovement.getMoving()) {
      setDirection(currentDirection);
    }
  }

  public void updateAnimationDirection(Vector2 lookPosition, int state) {
    Vector2 myPos = entity.getPosition();
    float xOffset = myPos.x - lookPosition.x;
    float yOffset = myPos.y - lookPosition.y;

    if (yOffset > 0 && yOffset > -xOffset && yOffset > xOffset) {
      setDirection(ANIMATIONS_FRONT[state]);
      currentDirection = "front";
    }
    else if (yOffset < 0 && yOffset < -xOffset && yOffset < xOffset) {
      setDirection(ANIMATIONS_BACK[state]);
      currentDirection = "back";
    }
    else if (xOffset > 0 && yOffset >= -xOffset && yOffset <= xOffset) {
      setDirection(ANIMATIONS_LEFT[state]);
      currentDirection = "left";
    }
    else if (xOffset < 0 && yOffset <= -xOffset && yOffset >= xOffset) {
      setDirection(ANIMATIONS_RIGHT[state]);
      currentDirection = "right";
    }
  }

  public void setDirection(String direction) {
    if (!direction.equals(lastAnimation)) {
      animator.startAnimation(direction);
      lastAnimation = direction;
    }
  }
}
