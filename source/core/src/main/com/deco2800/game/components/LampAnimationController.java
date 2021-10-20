package com.deco2800.game.components;

import com.deco2800.game.rendering.AnimationRenderComponent;

import java.util.Random;

public class LampAnimationController extends Component{
  AnimationRenderComponent animator;
  private int mode = 0;
  private Random rand;

  @Override
  public void update() {
    if (mode == 3 && animator.isFinished()) {
      animator.startAnimation("onlamp");
      mode = 0;
    } else if (rand.nextInt(300) == 1 && mode == 0) {
      mode = 1;
      animator.startAnimation("startflicker");
    } else if (mode == 1 && animator.isFinished()) {
      mode = 2;
      animator.startAnimation("rapidflicker");
    } else if (mode == 2 && animator.isFinished()) {
      mode = 3;
      animator.startAnimation("endflicker");
    }
  }

  @Override
  public void create() {
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    rand = new Random();
    animator.startAnimation("onlamp");
  }
}
