package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.items.Directions;
import com.deco2800.game.rendering.IndependentAnimator;

public class PlayerTorchAnimationController extends Component {

  private IndependentAnimator torchAnimator;
  private PlayerLightingComponent lightingComponent;
  private Directions previous;
  private boolean on = true;

  @Override
  public void create() {
    super.create();
  }

  /**
   * Setter has to be called after creation from wherever the Controller was
   * instanced to get around some annoying issues.
   */
  public void setter() {
    lightingComponent = this.entity.getComponent(PlayerLightingComponent.class);
    torchAnimator = lightingComponent.getTorchAnimator();
    entity.getEvents().addListener("toggleTorch",this::stop);
    torchAnimator.startAnimation("front");
    previous = Directions.MOVE_UP;
  }

  /**
   * Checks to stop animation once it is finished
   */
  @Override
  public void update() {
    if (on) {
      torchAnimator.setPositions(0, 0);
      KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
      Directions direct = key.getDirection();
      if (previous != direct) {
        switch (direct) {
          case MOVE_DOWN:
            torchAnimator.startAnimation("front");
            break;
          case MOVE_LEFT:
            torchAnimator.startAnimation("left");
            break;
          case MOVE_UP:
            torchAnimator.startAnimation("back");
            break;
          case MOVE_RIGHT:
            torchAnimator.startAnimation("right");
            break;
        }
        previous = direct;
      }
    } else {
      torchAnimator.stopAnimation();
    }
  }

  void stop() {
    on = !on;
    torchAnimator.stopAnimation();
  }

  void disposeAnimation() {
    torchAnimator.dispose();
  }
}
