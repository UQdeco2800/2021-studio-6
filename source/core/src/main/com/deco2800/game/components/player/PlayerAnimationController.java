package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class PlayerAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("still", this::animateStill);
    entity.getEvents().addListener("moveLeft", this::animateLeft);
    entity.getEvents().addListener("moveRight", this::animateRight);
    entity.getEvents().addListener("moveUp", this::animateUp);
    entity.getEvents().addListener("moveDown", this::animateDown);
  }

  void animateStill() {
    //animator.startAnimation("standing");
  }

  void animateLeft() {
    //animator.startAnimation("movingLeft");
  }

  void animateRight() {
    //animator.startAnimation("movingRight");
  }

  void animateUp() {
    //animator.startAnimation("movingUp");
  }

  void animateDown() {
    //animator.startAnimation("movingDown");
  }

}
