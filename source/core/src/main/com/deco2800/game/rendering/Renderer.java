package com.deco2800.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Core rendering system for the game. Controls the game's camera and runs rendering on all
 * renderables each frame.
 */
public class Renderer implements Disposable {
  private static final float GAME_SCREEN_WIDTH = 20f;
  private static final Logger logger = LoggerFactory.getLogger(Renderer.class);

  private CameraComponent camera;
  private float gameWidth;
  private SpriteBatch batch;
  private Stage stage;
  private RenderService renderService;
  private DebugRenderer debugRenderer;
  private boolean lighting;

  /**
   * Create a new renderer with default settings. Can create two separate renderers,
   * one for lit assets and one for unlit assets, depending on the boolean lighting.
   *
   * @param camera camera to render to
   * @param lighting Boolean for whether this renderer deals with lighting (true) or not
   * @param existingStage A given stage if a stage already exists in the environment
   */
  public Renderer(CameraComponent camera, boolean lighting, Stage existingStage) {
    SpriteBatch spriteBatch = new SpriteBatch();
    DebugRenderer debugRenderer = new DebugRenderer();
    debugRenderer.setActive(true);
    this.lighting = lighting;
    if (lighting) {
      init(
          camera,
          GAME_SCREEN_WIDTH,
          spriteBatch,
          new Stage(new ScreenViewport(), spriteBatch),
          ServiceLocator.getRenderService(),
          debugRenderer);
    } else {
      init(
          camera,
          GAME_SCREEN_WIDTH,
          spriteBatch,
          existingStage,
          ServiceLocator.getRenderUnlitService(),
          debugRenderer);
    }
  }

  /**
   * Create a renderer
   *
   * @param camera Camera to use for rendering.
   * @param gameWidth Desired game width in metres the screen should show. Height is then based on *
   *     the aspect ratio.
   * @param batch Batch to render to.
   * @param stage Scene2D stage for UI rendering
   * @param renderService Render service to use
   * @param debugRenderer Debug renderer to render
   */
  public Renderer(
      CameraComponent camera,
      float gameWidth,
      SpriteBatch batch,
      Stage stage,
      RenderService renderService,
      DebugRenderer debugRenderer) {
    init(camera, gameWidth, batch, stage, renderService, debugRenderer);
  }

  private void init(
      CameraComponent camera,
      float gameWidth,
      SpriteBatch batch,
      Stage stage,
      RenderService renderService,
      DebugRenderer debugRenderer) {

    this.camera = camera;
    this.gameWidth = gameWidth;
    this.batch = batch;
    this.stage = stage;
    this.renderService = renderService;
    this.debugRenderer = debugRenderer;

    renderService.setStage(stage);
    renderService.setDebug(debugRenderer);
    resizeCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  /**
   * Gets the camera specified for this renderer
   * @return The camera attached to this renderer
   */
  public CameraComponent getCamera() {
    return camera;
  }

  /** Render everything to the render service. Has a different render procedure
   * depending on whether it is for lit or unlit assets.
   */
  public void render() {
    Matrix4 projMatrix = camera.getProjectionMatrix();
    batch.setProjectionMatrix(projMatrix);
    if (this.lighting) {
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    batch.begin();
    renderService.render(batch);
    batch.end();
    debugRenderer.render(projMatrix);
  }

  public void setZoom(int zoom) {
    this.gameWidth = zoom;
    camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), zoom);
  }

  public void renderUI() {
    stage.act();
    stage.draw();
  }

  /**
   * Resize the renderer to a new screen size.
   *
   * @param width new screen width
   * @param height new screen height
   */
  public void resize(int width, int height) {
    resizeCamera(width, height);
    resizeStage(width, height);
    logger.debug("Resizing to ({}x{})", width, height);
  }

  /** @return The debug renderer attached to this renderer */
  public DebugRenderer getDebug() {
    return debugRenderer;
  }

  private void resizeCamera(int screenWidth, int screenHeight) {
    camera.resize(screenWidth, screenHeight, gameWidth);
  }

  private void resizeStage(int screenWidth, int screenHeight) {
    stage.getViewport().update(screenWidth, screenHeight, true);
  }

  @Override
  public void dispose() {
    stage.dispose();
    batch.dispose();
  }

  public Stage getStage() {
    return stage;
  }
}
