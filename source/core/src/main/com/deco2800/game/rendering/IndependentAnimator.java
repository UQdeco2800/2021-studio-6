package com.deco2800.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class IndependentAnimator  implements Renderable {
  private static final Logger logger = LoggerFactory.getLogger(IndependentAnimator.class);
  private final GameTime timeSource;
  private final TextureAtlas atlas;
  private final Map<String, Animation<TextureRegion>> animations;
  private Animation<TextureRegion> currentAnimation;
  private String currentAnimationName;
  private float animationPlayTime;
  private final float xPos;
  private final float yPos;
  private final float xScale;
  private final float yScale;


  /**
   * Create the component for a given texture atlas.
   * @param atlas libGDX-supported texture atlas containing desired animations
   */
  public IndependentAnimator(TextureAtlas atlas, float xOffset, float yOffset, float xScl, float yScl) {

    ServiceLocator.getRenderService().register(this);
    System.out.println("atlas stuff");
    xPos = xOffset;
    yPos = yOffset;
    xScale = xScl;
    yScale = yScl;
    this.atlas = atlas;
    this.animations = new HashMap<>(4);
    timeSource = ServiceLocator.getTimeSource();
  }

  /**
   * Register an animation from the texture atlas. Will play once when called with startAnimation()
   * @param name Name of the animation. Must match the name of this animation inside the texture
   *             atlas.
   * @param frameDuration How long, in seconds, to show each frame of the animation for when playing
   * @return true if added successfully, false otherwise
   */
  public boolean addAnimation(String name, float frameDuration) {
    return addAnimation(name, frameDuration, Animation.PlayMode.NORMAL);
  }

  /**
   * Register an animation from the texture atlas.
   * @param name Name of the animation. Must match the name of this animation inside the texture
   *             atlas.
   * @param frameDuration How long, in seconds, to show each frame of the animation for when playing
   * @param playMode How the animation should be played (e.g. looping, backwards)
   * @return true if added successfully, false otherwise
   */
  public boolean addAnimation(String name, float frameDuration, Animation.PlayMode playMode) {
    Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(name);
    if (regions == null || regions.size == 0) {
      logger.warn("Animation {} not found in texture atlas", name);
      return false;
    } else if (animations.containsKey(name)) {
      logger.warn(
          "Animation {} already added in texture atlas. Animations should only be added once.",
          name);
      return false;
    }

    Animation<TextureRegion> animation = new Animation<>(frameDuration, regions, playMode);
    animations.put(name, animation);
    logger.debug("Adding animation {}", name);
    return true;
  }

  /** Scale the entity to a width of 1 and a height matching the texture's ratio */
  public void scaleEntity() {
    TextureRegion defaultTexture = this.atlas.findRegion("default");
   // entity.setScale(1f, (float) defaultTexture.getRegionHeight() / defaultTexture.getRegionWidth());
  }

  /**
   * Remove an animation from this animator. This is not required before disposing.
   * @param name Name of the previously added animation.
   * @return true if removed, false if animation was not found.
   */
  public boolean removeAnimation(String name) {
    logger.debug("Removing animation {}", name);
    return animations.remove(name) != null;
  }

  /**
   * Whether the animator has added the given animation.
   * @param name Name of the added animation.
   * @return true if added, false otherwise.
   */
  public boolean hasAnimation(String name) {
    return animations.containsKey(name);
  }

  /**
   * Start playback of an animation. The animation must have been added using addAnimation().
   * @param name Name of the animation to play.
   */
  public void startAnimation(String name) {
    System.out.println("starting");
    Animation<TextureRegion> animation = animations.getOrDefault(name, null);
    if (animation == null) {
      logger.error(
          "Attempted to play unknown animation {}. Ensure animation is added before playback.",
          name);
      return;
    }

    currentAnimation = animation;
    currentAnimationName = name;
    animationPlayTime = 0f;
    logger.debug("Starting animation {}", name);
  }

  /**
   * Stop the currently running animation. Does nothing if no animation is playing.
   * @return true if animation was stopped, false if no animation is playing.
   */
  public boolean stopAnimation() {
    if (currentAnimation == null) {
      return false;
    }

    logger.debug("Stopping animation {}", currentAnimationName);
    currentAnimation = null;
    currentAnimationName = null;
    animationPlayTime = 0f;
    return true;
  }

  /**
   * Get the name of the animation currently being played.
   * @return current animation name, or null if not playing.
   */
  public String getCurrentAnimation() {
    return currentAnimationName;
  }

  /**
   * Has the playing animation finished? This will always be false for looping animations.
   * @return true if animation was playing and has now finished, false otherwise.
   */
  public boolean isFinished() {
    return currentAnimation != null && currentAnimation.isAnimationFinished(animationPlayTime);
  }

  protected void draw(SpriteBatch batch) {
    if (currentAnimation == null) {
      return;
    } else {
      TextureRegion region = currentAnimation.getKeyFrame(animationPlayTime);
      //Vector2 pos = entity.getPosition();
      //Vector2 scale = entity.getScale();
      Vector2 current = ServiceLocator.getRenderService().getPos();
      batch.draw(region, current.x-xPos, current.y-yPos, xScale, yScale);
      animationPlayTime += timeSource.getDeltaTime();
    }
  }

  public void dispose() {
    // THIS DELETES ALL ANIMATION INSTANCE REGARDLESS OF WHICH ANIMATION COMPONENT WAS ASSIGNED TO. If one enemy dies
    // all animation will be removed - based on structure of code
//    atlas.dispose();
    System.out.println("dispose");
    //super.dispose();
  }

  @Override
  public void render(SpriteBatch batch) {
    draw(batch);
  }

  @Override
  public float getZIndex() {
    // The smaller the Y value, the higher the Z index, so that closer entities are drawn in front
    return 5;
  }

  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public int compareTo(Renderable o) {
    return 0;
  }
}
