package com.deco2800.game.components;
import com.deco2800.game.items.Directions;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.IndependentAnimator;

public class FirecrackerAnimationController extends Component {

  AnimationRenderComponent animator;
  IndependentAnimator explosionAnimator;
  boolean explosionStarted = false;

  @Override
  public void create() {
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("firecrackerStart",this::setDirection);
    entity.getEvents().addListener("explosionStart",this::explosion);
    entity.getEvents().addListener("explosionLoop",this::loop);
    entity.getEvents().addListener("endFirecracker", this::stopAnimations);
    entity.getEvents().addListener("dispose", this::dispose);
    entity.getEvents().addListener("pauseFirecracker", this::pauseThrow);
  }

  @Override
  public void update() {
    if (explosionStarted && explosionAnimator.isFinished()) {
      explosionStarted = false;
      loop();
    }
    if (explosionStarted) {
      explosionAnimator.setPositions(this.entity.getCenterPosition().x - (this.entity.getScale().x),
          this.entity.getCenterPosition().y - (this.entity.getScale().y));
    }
  }

  /**
   * Used to set which directional animation should be used i.e. if thrown right, should go clockwise
   * @param direct the direction the firecracker is thrown
   */
  public void setDirection(Directions direct) {
    switch (direct) {
      case MOVE_UP:
      case MOVE_LEFT:
        animator.startAnimation("firecrackerRev");
        break;
      case MOVE_DOWN:
      case MOVE_RIGHT:
        animator.startAnimation("firecracker");
        break;
    }
  }

  /**
   * Simple function used to initiate the explosion
   */
  void explosion() {
    animator.stopAnimation();
    animator.setPaused(false);
    explosionAnimator.setPositions(this.entity.getCenterPosition().x - (this.entity.getScale().x),
        this.entity.getCenterPosition().y - (this.entity.getScale().y));
    explosionAnimator.startAnimation("explosion");
    explosionStarted = true;
  }

  /**
   * Simple function used to loop through the 'Damage over Time' animation
   */
  void loop() {
    explosionAnimator.setPositions(this.entity.getCenterPosition().x - (this.entity.getScale().x),
        this.entity.getCenterPosition().y - (this.entity.getScale().y));
    explosionAnimator.startAnimation("explosionLoop");
  }

  /**
   * Sets the local independent animator for the explosion to enable upscaling
   * @param newAnimator the IndependentAnimator for the explosion animation
   */
  public void setAnimator(IndependentAnimator newAnimator) {
    this.explosionAnimator = newAnimator;
  }

  /**
   * Simple function used to pause the throw animation for when it hits a obstacle
   */
  void pauseThrow() {
    animator.setPaused(true);
  }

  /**
   * Stops all animations. Enables the firecracker to be reused.
   */
  public void stopAnimations() {
    this.animator.stopAnimation();
    this.explosionAnimator.stopAnimation();
  }

  @Override
  public void dispose() {
    //super.dispose();
    animator.dispose();

    // upon entering second safehouse after level 2 - it crashes cause explosionAnimator is null
    if (explosionAnimator != null) {
        explosionAnimator.dispose();
    }
  }
}
