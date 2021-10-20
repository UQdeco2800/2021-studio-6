package com.deco2800.game.components;

import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.awt.*;
import java.util.Random;

public class LampAnimationController extends Component{
  AnimationRenderComponent animator;
  private boolean animationFinished = true;
  private Random rand;
  private PointLightComponent pointLightComponent;
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private long startTime = 0;
  private int mode = 0;
  private int repeat = 0;
  private static final int SHORT_FLICKER = 220;
  private static final int LONG_FLICKER = 400;
  private static final int VERY_LONG_FLICKER = 1600;
  private static final int COOLDOWN = 5000;

  public void giveLightComponent(PointLightComponent pointLightComponent) {
    this.pointLightComponent = pointLightComponent;
  }

  @Override
  public void update() {
    if (mode == 4) {
      animator.startAnimation("on");
      this.pointLightComponent.turnOnLight();
      mode = 0;
    } else if (mode == 0 && timeSource.getTimeSince(startTime) > COOLDOWN &&
        rand.nextInt(50) == 1) {
      mode = 1;
      animator.startAnimation("off");
      this.pointLightComponent.turnOffLight();
      startTime = ServiceLocator.getTimeSource().getTime();
    } else if (mode == 1 && repeat < 3 &&
        timeSource.getTimeSince(startTime) > SHORT_FLICKER) {
      animator.startAnimation("on");
      mode = 2;
      this.pointLightComponent.turnOnLight();
      startTime = ServiceLocator.getTimeSource().getTime();
      repeat += 1;
    } else if (mode == 2 && repeat < 3 &&
        timeSource.getTimeSince(startTime) > SHORT_FLICKER) {
      mode = 1;
      animator.startAnimation("off");
      this.pointLightComponent.turnOffLight();
      startTime = ServiceLocator.getTimeSource().getTime();
    } else if (mode == 2 && timeSource.getTimeSince(startTime) > VERY_LONG_FLICKER) {
      mode = 3;
      animator.startAnimation("off");
      this.pointLightComponent.turnOffLight();
      startTime = ServiceLocator.getTimeSource().getTime();
    } else if (mode == 3 && timeSource.getTimeSince(startTime) > LONG_FLICKER) {
      mode = 4;
      repeat = 0;
      startTime = ServiceLocator.getTimeSource().getTime();
    }
  }

  @Override
  public void create() {
    startTime = ServiceLocator.getTimeSource().getTime() - 10000;
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    rand = new Random();
    animator.startAnimation("on");
  }
}
