package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class GhostAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("wanderStart", this::animateWander);
    entity.getEvents().addListener("chaseStart", this::animateChase);
    entity.getEvents().addListener("attackStart", this::animateAttack);
  }

  void animateWander() {
    animator.startAnimation("float");
  }

  void animateChase() {
    animator.startAnimation("angry_float");
  }

  void animateAttack() {
    animator.startAnimation("default"); //change to attack later, make a default or idle animator
  } //yet to be implemented, will be needed tho
}
