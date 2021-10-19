package com.deco2800.game.components;

import com.deco2800.game.rendering.AnimationRenderComponent;

import java.util.Random;

public class CampfireAnimationController  extends Component {
  AnimationRenderComponent animator;
  private boolean started = false;
  private Random rand;

  @Override
  public void update() {
    if (started && animator.isFinished()) {
      animator.startAnimation("inactive");
      started = false;
    } else if ((rand.nextInt(400) == 1) && !started) {
      started = true;
      animator.startAnimation("active");
    }
  }

  @Override
  public void create() {
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    rand = new Random();
    animator.startAnimation("active");
  }
}