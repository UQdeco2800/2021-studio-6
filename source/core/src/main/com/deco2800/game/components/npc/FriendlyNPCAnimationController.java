package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;
import org.slf4j.LoggerFactory;


/**
 * This class listens to events relevant to a NPCs state and plays the animation when one
 * of the events is triggered. 
 */
public class FriendlyNPCAnimationController extends Component {
  private static org.slf4j.Logger logger = LoggerFactory.getLogger(FriendlyNPCAnimationController.class);
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

  public void setDirection(String direction) {
    if (!direction.equals(lastAnimation)) {
      animator.startAnimation(direction);
      lastAnimation = direction;
    }
  }
}
