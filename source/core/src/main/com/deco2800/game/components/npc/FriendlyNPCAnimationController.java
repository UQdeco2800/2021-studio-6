package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;


/**
 * This class listens to events relevant to a NPCs state and plays the animation when one
 * of the events is triggered. 
 */
public class FriendlyNPCAnimationController extends Component {
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

  /**
   * Friendly NPC animations are updated and checked each update call. 
   * Also checks if the identical animation is already in progress.
   */
  @Override
  public void update() { 
    String anim;
    anim = "front";

    if (!anim.equals(lastAnimation)) {
      animator.startAnimation(anim);
      lastAnimation = anim;
    }
  }
}
