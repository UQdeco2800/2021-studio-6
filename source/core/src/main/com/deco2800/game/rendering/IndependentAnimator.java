package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.services.ServiceLocator;

public class IndependentAnimator  extends AnimationRenderComponent {
  private final float xPos;
  private final float yPos;
  private final float xScale;
  private final float yScale;

  /**
   * Create the Independent Animator for the given TextureAtlas. Extends the
   * AnimationRenderComponent but doesn't need to be attached to an entity.
   * Instead, makes use of given x and y offsets to position the animation in
   * relation to the screen. Also sizes according to given scales.
   *
   * @param atlas libGDX-supported texture atlas containing desired animations
   */
  public IndependentAnimator(TextureAtlas atlas, float xOffset, float yOffset, float xScl, float yScl) {
    super(atlas);
    ServiceLocator.getRenderService().register(this);
    xPos = xOffset;
    yPos = yOffset;
    xScale = xScl;
    yScale = yScl;
  }

  @Override
  protected void draw(SpriteBatch batch) {
    if (super.getFullAnimation() != null) {
      TextureRegion region = super.getFullAnimation().getKeyFrame(super.getAnimTime());
      Vector2 current = ServiceLocator.getRenderService().getPos();
      batch.draw(region, current.x - xPos, current.y - yPos, xScale, yScale);
      super.setTime(super.getAnimTime() + super.getTime().getDeltaTime());
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
