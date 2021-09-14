package com.deco2800.game.rendering;

import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class IndependentAnimatorTest {

  @Mock
  RenderService service;

  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);


  @Test
  void shouldRegisterSelf() {
    ServiceLocator.registerRenderService(service);
    assertNotNull(ServiceLocator.getRenderService());
    TextureAtlas atlas = createMockAtlas("test_name", 1);
    IndependentAnimator animator = new IndependentAnimator(atlas);
    verify(service).register(animator);
  }

  @Test
  void shouldSetPositions() {
    ServiceLocator.registerRenderService(service);
    TextureAtlas atlas = createMockAtlas("test_name", 1);
    IndependentAnimator animator = new IndependentAnimator(atlas);
    animator.setPositions(1.50f, (float) 2.5);
    float[] testArray = new float[]{1.50f, 2.5f};
    float[] getArray = animator.getPositions();
    assertEquals(testArray[0], getArray[0]);
    assertEquals(testArray[1], getArray[1]);
  }

  @Test
  void shouldDefaultSetPositions() {
    ServiceLocator.registerRenderService(service);
    TextureAtlas atlas = createMockAtlas("test_name", 1);
    IndependentAnimator animator = new IndependentAnimator(atlas);
    float[] testArray = new float[]{0f, 0f};
    float[] getArray = animator.getPositions();
    assertEquals(testArray[0], getArray[0]);
    assertEquals(testArray[1], getArray[1]);
  }

  @Test
  void shouldSetScale() {
    ServiceLocator.registerRenderService(service);
    TextureAtlas atlas = createMockAtlas("test_name", 1);
    IndependentAnimator animator = new IndependentAnimator(atlas);
    animator.setScale(1.50f, (float) 2.5);
    float[] testArray = new float[]{1.50f, 2.5f};
    float[] getArray = animator.getScale();
    assertEquals(testArray[0], getArray[0]);
    assertEquals(testArray[1], getArray[1]);
  }

  @Test
  void shouldDefaultSetScale() {
    ServiceLocator.registerRenderService(service);
    TextureAtlas atlas = createMockAtlas("test_name", 1);
    IndependentAnimator animator = new IndependentAnimator(atlas);
    float[] testArray = new float[]{1f, 1f};
    float[] getArray = animator.getScale();
    assertEquals(testArray[0], getArray[0]);
    assertEquals(testArray[1], getArray[1]);
  }
  @Test
  void shouldPlayAnimationCamera() {
    RenderService mockedServe = mock(RenderService.class);
    when(mockedServe.getPos()).thenReturn(CAMERA_POSITION);
    ServiceLocator.registerRenderService(mockedServe);
    assertNotNull(ServiceLocator.getRenderService());
    ServiceLocator.getRenderService().setPos(CAMERA_POSITION);
    assertEquals(new Vector2(7.5f,7.5f), ServiceLocator.getRenderService().getPos());

    int numFrames = 5;
    String animName = "test_name";
    float frameTime = 1f;

    // Mock texture atlas
    TextureAtlas atlas = createMockAtlasRegions(animName, numFrames);
    Array<AtlasRegion> regions = atlas.findRegions(animName);
    SpriteBatch batch = mock(SpriteBatch.class);

    // Mock game time
    GameTime gameTime = mock(GameTime.class);
    ServiceLocator.registerTimeSource(gameTime);
    when(gameTime.getDeltaTime()).thenReturn(frameTime);

    // Start animation
    IndependentAnimator animator = new IndependentAnimator(atlas);
    animator.setCamera(true);
    animator.setPositions(4f, 2f);
    animator.setScale(1.50f, (float) 2.5);
    animator.addAnimation(animName, frameTime);
    animator.startAnimation(animName);

    MockGraphics e = new MockGraphics();
    for (int i = 0; i < 5; i++) {
      // Each draw advances 1 frame, check that it matches for each
      assertNotNull(animator.getFullAnimation());
      animator.draw(batch);
      float[] getArrayScl = animator.getScale();
      float[] getArrayPos = animator.getPositions();
      verify(batch).draw(
          regions.get(i),
         3.5f,
          5.5f,
          getArrayScl[0],
          getArrayScl[1]
      );
    }
  }

  @Test
  void shouldPlayAnimation() {
    ServiceLocator.registerRenderService(service);
    int numFrames = 5;
    String animName = "test_name";
    float frameTime = 1f;

    // Mock texture atlas
    TextureAtlas atlas = createMockAtlasRegions(animName, numFrames);
    Array<AtlasRegion> regions = atlas.findRegions(animName);
    SpriteBatch batch = mock(SpriteBatch.class);

    // Mock game time
    GameTime gameTime = mock(GameTime.class);
    ServiceLocator.registerTimeSource(gameTime);
    when(gameTime.getDeltaTime()).thenReturn(frameTime);

    // Start animation
    IndependentAnimator animator = new IndependentAnimator(atlas);
    animator.setCamera(false);
    animator.setPositions(3.50f, (float) 7.5);
    animator.setScale(1.50f, (float) 2.5);
    animator.addAnimation(animName, frameTime);
    animator.startAnimation(animName);

    MockGraphics e = new MockGraphics();
    for (int i = 0; i < 5; i++) {
      // Each draw advances 1 frame, check that it matches for each
      assertNotNull(animator.getFullAnimation());
      animator.draw(batch);
      float[] getArrayScl = animator.getScale();
      float[] getArrayPos = animator.getPositions();
      verify(batch).draw(
          regions.get(i),
          getArrayPos[0],
          getArrayPos[1],
          getArrayScl[0],
          getArrayScl[1]
      );
    }
  }

  static TextureAtlas createMockAtlas(String animationName, int numRegions) {
    TextureAtlas atlas = mock(TextureAtlas.class);
    Array<AtlasRegion> regions = new Array<>(numRegions);
    for (int i = 0; i < numRegions; i++) {
      regions.add(mock(AtlasRegion.class));
    }
    return atlas;
  }

  static TextureAtlas createMockAtlasRegions(String animationName, int numRegions) {
    TextureAtlas atlas = mock(TextureAtlas.class);
    Array<AtlasRegion> regions = new Array<>(numRegions);
    for (int i = 0; i < numRegions; i++) {
      regions.add(mock(AtlasRegion.class));
    }
    when(atlas.findRegions(animationName)).thenReturn(regions);
    return atlas;
  }
}
