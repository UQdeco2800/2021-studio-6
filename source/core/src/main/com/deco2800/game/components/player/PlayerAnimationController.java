package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * This class listens to events relevant to a player's state and plays the animation when one
 * of the events is triggered. Currently not implemented as the animations do
 * not yet exist. Can be expanded later on to include other animation events.
 */
public class PlayerAnimationController extends Component {
  AnimationRenderComponent animator;
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private static final long hurtDuration = 1000;
  private boolean hurtActive = false;
  private long hurtTime;
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

  @Override
  public void update() {
    if (timeSource.getTimeSince(hurtTime) >= hurtDuration) {
      hurtActive = false;
      if (!entity.getComponent(PlayerActions.class).isMoving()) {
        animateIdle();
      }
    }
  }

  private boolean checkHurt() {
    return hurtActive;
  }

  void animateDead() {
    animator.startAnimation("dead-right");
  }

  void animateMoveDown() {
    if (checkHurt()) {
      animator.startAnimation("front-run-hurt");
    } else {
      animator.startAnimation("front-run");
    }
    lastDirection = 1;
  }

  void animateMoveRight() {
    if (checkHurt()) {
      animator.startAnimation("right-run-hurt");
    } else {
      animator.startAnimation("right-run");
    }
    lastDirection = 2;
  }

  void animateMoveUp() {
    if (checkHurt()) {
      animator.startAnimation("back-run-hurt");
    } else {
      animator.startAnimation("back-run");
    }
    lastDirection = 3;
  }

  void animateMoveLeft() {
    if (checkHurt()) {
      animator.startAnimation("left-run-hurt");
    } else {
      animator.startAnimation("left-run");
    }
    lastDirection = 4;
  }

  void animateHurt() {
    hurtActive = true;
    hurtTime = timeSource.getTime() + hurtDuration;
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
    if(checkHurt()) {
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
    } else {
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
}
