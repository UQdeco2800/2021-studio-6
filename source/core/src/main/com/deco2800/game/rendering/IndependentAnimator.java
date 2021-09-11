package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.services.ServiceLocator;

public class IndependentAnimator  extends AnimationRenderComponent {
  private float xPos = 0;
  private float yPos = 0;
  private float xScale = 1;
  private float yScale = 1;
  private boolean cameraFollow = true;

  /**
   * Create the Independent Animator for the given TextureAtlas. Extends the
   * AnimationRenderComponent but doesn't need to be attached to an entity.
   * Instead, makes use of given x and y offsets to position the animation in
   * relation to the camera or the scene. Also sizes according to given scales.
   *
   * When using this, should first create the IndependentAnimator by giving it
   * the atlas file. After that, you need to set both the x and y position and
   * scale. Lastly, set whether it should follow the camera or not.
   *
   * @param atlas libGDX-supported texture atlas containing desired animations
   */
  public IndependentAnimator(TextureAtlas atlas) {
    super(atlas);
    ServiceLocator.getRenderService().register(this);
  }

  /**
   * Sets the position of where the associated animation should be played.
   * @param x the x coordinate of the animation location
   * @param y the y coordinate of the animation location
   */
  public void setPositions(float x, float y) {
    xPos = x;
    yPos = y;
  }

  /**
   * Gets the position of where the associated animation should be played.
   * @return Array corresponding to the x and y positions
   */
  public float[] getPositions() {
    return new float[]{xPos, yPos};
  }


  /**
   * Gets the scale of the associated animation.
   * @return Array corresponding to the x and y animation scales
   */
  public float[] getScale() {
    return new float[]{xScale, yScale};
  }

  /**
   * Sets the scale of the animation
   * @param x the x scale of the animation
   * @param y the y scale of the animation
   */
  public void setScale(float x, float y) {
    xScale = x;
    yScale = y;
  }

  /**
   * Sets whether or not the animation should be displayed in relation to the
   * camera or the scene. I.e. interface animations are for the camera and
   * follow the player.
   * True means that it follows the camera, false means the scene.
   * @param to the boolean value to set the animation camera to follow
   */
  public void setCamera(boolean to) {
    cameraFollow = to;
  }

  @Override
  protected void draw(SpriteBatch batch) {
    if (super.getFullAnimation() != null) {
      TextureRegion region = super.getFullAnimation().getKeyFrame(super.getAnimTime());
      if (cameraFollow) {
        Vector2 current = ServiceLocator.getRenderService().getPos();
        if (current != null) {
          batch.draw(region, current.x - xPos, current.y - yPos, xScale, yScale);
          super.setTime(super.getAnimTime() + super.getTime().getDeltaTime());
        }
      } else {
        batch.draw(region, xPos, yPos, xScale, yScale);
        super.setTime(super.getAnimTime() + super.getTime().getDeltaTime());
      }
    }
  }

  @Override
  public void dispose() {
    super.getAtlas().dispose();
    super.dispose();
  }

  @Override
  public float getZIndex() {
    // The smaller the Y value, the higher the Z index, so that closer entities are drawn in front
    //Placeholder value
    return 100;
  }
}
