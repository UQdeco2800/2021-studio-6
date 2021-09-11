package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a player's state and plays the animation when one
 * of the events is triggered. Currently not implemented as the animations do
 * not yet exist. Can be expanded later on to include other animation events.
 */
public class PlayerAnimationController extends Component {
  AnimationRenderComponent animator;
  private int lastDirection;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("moveLeft", this::animateMoveLeft);
    entity.getEvents().addListener("moveRight", this::animateMoveRight);
    entity.getEvents().addListener("moveUp", this::animateMoveUp);
    entity.getEvents().addListener("moveDown", this::animateMoveDown);
    entity.getEvents().addListener("walkStop", this::animateIdle);
    entity.getEvents().addListener("hurt",this::animateHurt);
    entity.getEvents().addListener("dead",this::animateDead);
    lastDirection = 1;
    animator.startAnimation("front");
  }

  void animateDead() {
    animator.startAnimation("dead-right");
  }

  void animateMoveDown() {
    animator.startAnimation("front-run");
    lastDirection = 1;
  }

  void animateMoveRight() {
    animator.startAnimation("right-run");
    lastDirection = 2;
  }

  void animateMoveUp() {
    animator.startAnimation("back-run");
    lastDirection = 3;
  }

  void animateMoveLeft() {
    animator.startAnimation("left-run");
    lastDirection = 4;
  }

  void animateHurt() {
    switch (lastDirection) {
      case 1:
        animator.startAnimation("front-hurt");
        break;
      case 2:
        animator.startAnimation("right-hurt");
        break;
      case 3:
        animator.startAnimation("back-hurt");
        break;
      case 4:
        animator.startAnimation("left-hurt");
        break;
    }
  }
  void animateIdle() {
    switch (lastDirection) {
      case 1:
        animator.startAnimation("front");
        break;
      case 2:
        animator.startAnimation("right");
        break;
      case 3:
        animator.startAnimation("back");
        break;
      case 4:
        animator.startAnimation("left");
        break;
    }
  }
}
