package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.utils.math.Vector2Utils;

public class BulletAnimationController extends Component{
  AnimationRenderComponent animator;
  private boolean started = false;

  @Override
  public void update() {
    if (started && animator.isFinished()) {
      entity.getEvents().trigger("rockDone");
      started = false;
    }
  }

  /**
   * Sets up relevant event trigger for getting hurt and starts default animation.
   */
  @Override
  public void create() {
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("breakRock",this::breakRock);
  }

  /**
   * Gets triggered when the rock collides with an object to start the rock breaking animation
   */
  void breakRock() {
    animator.startAnimation("rock");
    started = true;
  }

  /**
   * Used to start the appropriate animation for the rock
   * @param direction the direction that the rock was thrown, corresponding to the set animation
   */
  public void setDirection(Vector2 direction) {
    if (Vector2Utils.RIGHT.equals(direction)) {
      animator.startAnimation("right");
    } else if (Vector2Utils.LEFT.equals(direction)) {
      animator.startAnimation("left");
    } else if (Vector2Utils.UP.equals(direction)) {
      animator.startAnimation("up");
    } else if (Vector2Utils.DOWN.equals(direction)) {
      animator.startAnimation("down");
    }
  }

}
