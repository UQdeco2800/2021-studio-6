package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.LoggerFactory;


/**
 * This class listens to events relevant to a NPCs state and plays the animation when one
 * of the events is triggered. 
 */
public class FriendlyNPCAnimationController extends Component {
  private static org.slf4j.Logger logger = LoggerFactory.getLogger(FriendlyNPCAnimationController.class);
  AnimationRenderComponent animator;
  Vector2 currentWalkTarget;
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
    Vector2 walkTarget = entity.getComponent(PhysicsMovementComponent.class).getTarget();
    if (walkTarget != null && walkTarget != currentWalkTarget) {
      currentWalkTarget = walkTarget;
      updateAnimationDirection(walkTarget);
    }
  }

  public void updateAnimationDirection(Vector2 lookPosition) {
    Vector2 myPos = entity.getPosition();
    float xOffset = myPos.x - lookPosition.x;
    float yOffset = myPos.y - lookPosition.y;

    if (yOffset > 0 && yOffset > -xOffset && yOffset > xOffset) { setDirection("front"); }
    else if (yOffset < 0 && yOffset < -xOffset && yOffset < xOffset) { setDirection("back"); }
    else if (xOffset > 0 && yOffset >= -xOffset && yOffset <= xOffset) { setDirection("left"); }
    else if (xOffset < 0 && yOffset <= -xOffset && yOffset >= xOffset) { setDirection("right"); }
  }

  public void setDirection(String direction) {
    if (!direction.equals(lastAnimation)) {
      animator.startAnimation(direction);
      lastAnimation = direction;
    }
  }
}
