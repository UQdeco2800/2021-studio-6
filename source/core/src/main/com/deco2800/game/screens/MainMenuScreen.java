package com.deco2800.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.mainmenu.MainMenuActions;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main menu.
 */
public class MainMenuScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);
  private final GdxGame game;
  private final Renderer renderer;
  private MainMenuDisplay mainMenuDisplay;

  private static final String[] mainMenuTextures = {};
  private static final String[] mainMenuSounds = {"sounds/rollover.mp3"};
  // Transitional sounds can play between screens by not unloading on screen change
  private static final String[] mainMenuTransitionalSounds = {"sounds/click.mp3"};
  private static final String[] mainMenuMusic = {"sounds/title-screen-music.mp3"};
  private static final String[] mainMenuTextureAtlases = {"images/title-screen.atlas"};


  public MainMenuScreen(GdxGame game) {
    this.game = game;

    // Sets background to black
    Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);

    logger.debug("Initialising main menu screen services");
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerPhysicsService(new PhysicsService());


    renderer = RenderFactory.createRenderer();

    loadAssets();
    createUI();
  }

  @Override
  public void render(float delta) {
    mainMenuDisplay.render(delta);
    ServiceLocator.getEntityService().update();
    renderer.render();
    renderer.renderUI();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    mainMenuDisplay.resize(width, height);
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
    logger.debug("Disposing main menu screen");
    mainMenuDisplay.dispose();
    renderer.dispose();
    unloadAssets();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getEntityService().dispose();
    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainMenuTextures);
    resourceService.loadSounds(mainMenuSounds);
    resourceService.loadSounds(mainMenuTransitionalSounds);
    resourceService.loadMusic(mainMenuMusic);
    resourceService.loadTextureAtlases(mainMenuTextureAtlases);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainMenuTextures);
    resourceService.unloadAssets(mainMenuSounds);
    resourceService.unloadAssets(mainMenuMusic);
    resourceService.unloadAssets(mainMenuTextureAtlases);
  }

  /**
   * Creates the main menu's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    mainMenuDisplay = new MainMenuDisplay();
    ui.addComponent(mainMenuDisplay)
        .addComponent(new InputDecorator(stage, 10))
        .addComponent(new MainMenuActions(game));
    ServiceLocator.getEntityService().register(ui);
  }
}
