package com.deco2800.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.gameover.GameOverActions;
import com.deco2800.game.components.gameover.GameOverDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.entities.factories.WispFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.lighting.Lighting;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main menu.
 */
public class GameOverScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(GameOverScreen.class);
  private static final String[] gameOverTextures = {"images/gameover.png"};
  private static final String[] gameOverSounds = {"sounds/rollover.mp3"};
  // Transitional sounds can play between screens by not unloading on screen change
  private static final String[] gameOverTransitionalSounds = {"sounds/click.mp3"};
  private static final String[] gameOverMusic = {"sounds/game-over-music.mp3"};
  private static final String[] gameOverTextureAtlases = {};
  private final GdxGame game;
  private final Renderer renderer;
  private final Lighting lighting;
  private GameOverDisplay gameOverDisplay;



  public GameOverScreen(GdxGame game) {
    this.game = game;

    // Sets background to black
    Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);

    logger.debug("Initialising game over screen services");
    
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerTimeSource(new GameTime());
    ServiceLocator.registerPhysicsService(new PhysicsService());
    ServiceLocator.registerLightingService(new Lighting(ServiceLocator.getPhysicsService().getPhysics().getWorld()));

    renderer = RenderFactory.createRenderer();
    lighting = ServiceLocator.getLightingService();

    loadAssets();
    createUI();
    ServiceLocator.getEntityService().register(WispFactory.createWisp(0));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(3));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(5));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(7));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(10));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(13));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(15));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(17));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(20));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(23));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(25));
    ServiceLocator.getEntityService().register(WispFactory.createWisp(27));

  }

  @Override
  public void render(float delta) {
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    renderer.render();
    lighting.update();
    lighting.render();
    renderer.renderUI();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    gameOverDisplay.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    logger.info("Game paused");
  }

  @Override
  public void resume() {
    logger.info("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing game over screen");

    renderer.dispose();


    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getPhysicsService().getPhysics().dispose();
    lighting.dispose();
    unloadAssets();
    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(gameOverTextures);
    resourceService.loadSounds(gameOverSounds);
    resourceService.loadSounds(gameOverTransitionalSounds);
    resourceService.loadMusic(gameOverMusic);
    resourceService.loadTextureAtlases(gameOverTextureAtlases);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(gameOverTextures);
    resourceService.unloadAssets(gameOverSounds);
    resourceService.unloadAssets(gameOverMusic);
    resourceService.unloadAssets(gameOverTextureAtlases);
  }

  /**
   * Creates the main menu's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    gameOverDisplay = new GameOverDisplay();
    ui.addComponent(gameOverDisplay)
        .addComponent(new InputDecorator(stage, 10))
        .addComponent(new GameOverActions(game));
    ServiceLocator.getEntityService().register(ui);
  }

  public void createWisps() {

  }
}
