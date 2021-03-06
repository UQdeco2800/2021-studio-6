package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.items.Directions;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.rendering.TextureRenderComponent;

public class PlayerTorchAnimationController extends Component {

  private IndependentAnimator torchAnimator;
  private PlayerLightingComponent lightingComponent;
  private Directions previous;
  private boolean on = true;
  private Vector2 offsets = new Vector2(0, 0);

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
    entity.getEvents().addListener("torchOn",this::start);
    entity.getEvents().addListener("torchOff",this::stop);
    entity.getEvents().addListener("dispose", this::disposeAnimation);
    torchAnimator.startAnimation("front");
    previous = Directions.MOVE_UP;
  }

  /**
   * Updates and moves the torch animation based on the player's direction.
   * Follows a similar setup to the update function in PlayerAnimationController.
   * Will also only display if the torch is activated.
   */
  @Override
  public void update() {
    if (on) {
      torchAnimator.setPositions(
          offsets.x + this.entity.getPosition().x,
          offsets.y + this.entity.getPosition().y);

      KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
      Directions direct = key.getDirection();
      if (previous != direct) {
        switch (direct) {
          case MOVE_DOWN:
            torchAnimator.setZIndex(-this.getEntity().getCenterPosition().y + 100);
            torchAnimator.startAnimation("front");
            break;
          case MOVE_LEFT:
            torchAnimator.setZIndex(-this.getEntity().getCenterPosition().y + 100);
            torchAnimator.startAnimation("left");
            break;
          case MOVE_UP:
            torchAnimator.setZIndex(-this.getEntity().getCenterPosition().y - 100);
            torchAnimator.startAnimation("back");
            break;
          case MOVE_RIGHT:
            torchAnimator.setZIndex(-this.getEntity().getCenterPosition().y + 100);
            torchAnimator.startAnimation("right");
            break;
        }
        previous = direct;
      }
    } else {
      torchAnimator.stopAnimation();
    }
  }

  /**
   * Stops the torch animation status
   */
  void stop() {
    previous = null;
    on = false;
    torchAnimator.stopAnimation();
  }

  /**
   * Starts the torch animation status
   */
  void start() {
    previous = null;
    on = true;
  }


  /**
   * Removes and disposes the torch animation
   */
  void disposeAnimation() {
    super.dispose();
    torchAnimator.stopAnimation();
    torchAnimator.dispose();
  }
}
