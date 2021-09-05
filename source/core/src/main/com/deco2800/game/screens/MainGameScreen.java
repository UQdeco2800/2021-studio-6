package com.deco2800.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.*;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.pausemenu.PauseMenuActions;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import com.deco2800.game.components.pausemenu.PauseMenuDisplay;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {"images/heart.png","images/hud/22highbar6.png",
  "images/hud/22highbar1.png","images/hud/27highbar7.png","images/hud/27highbar6.png","images/hud/27highbar1.png",
  "images/hud/32highbar8.png","images/hud/32highbar7.png","images/hud/32highbar6.png","images/hud/32highbar1.png"};
  private static final String[] menuSounds = {"sounds/rollover.mp3","sounds/click.mp3"};
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
  private double CurrentLevel = 1;
  public static boolean levelChange = false;
  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private final TerrainFactory terrainFactory;
  private GameArea gameArea;
  private Entity ui;
  public MainGameScreen(GdxGame game) {
    this.game = game;

    // Sets background to light yellow
    Gdx.gl.glClearColor(248f/255f, 249/255f, 178/255f, 1);

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    createUI();

    logger.debug("Initialising main game screen entities");
    this.terrainFactory = new TerrainFactory(renderer.getCamera());
    gameArea = new ForestGameArea(terrainFactory);
    gameArea.create();
  }

  @Override
  public void render(float delta) {
    if (levelChange) {
      generateNewLevel();
    }
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
    renderer.render();

    CAMERA_POSITION.set(gameArea.player.getPosition());
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  /**
   * Function is called when you minimise the game program.
   */
  @Override
  public void pause() {
    logger.info("Game paused");
  }

  /**
   * Function is called when you open the game program when it was minimised.
   */
  @Override
  public void resume() {
    logger.info("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main game screen");

    renderer.dispose();
    unloadAssets();

    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getResourceService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainGameTextures);
    resourceService.loadSounds(menuSounds);

    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);
    resourceService.unloadAssets(menuSounds);
  }

  /**
   * Creates the main game's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForTerminal();

    this.ui = new Entity();
    ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new PauseMenuActions(this.game))
        .addComponent(new PauseMenuDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());

    ServiceLocator.getEntityService().register(ui);
  }

  public static void changeLevel() {
    levelChange = true;
  }

  public void generateNewLevel() {
    CurrentLevel += 0.5;
    Vector2 walkingDirection
            = gameArea.player.getComponent(KeyboardPlayerInputComponent.class).walkDirection;
    gameArea.dispose();
    if (CurrentLevel == 2) {
      gameArea = new Level2(terrainFactory);
      gameArea.create();
      gameArea.player.getComponent(KeyboardPlayerInputComponent.class)
              .walkDirection.add(walkingDirection);
    } else if (CurrentLevel == 3) {
      gameArea = new Level3(terrainFactory);
      gameArea.create();
      gameArea.player.getComponent(KeyboardPlayerInputComponent.class)
              .walkDirection.add(walkingDirection);
    } else if (CurrentLevel % 1 == 0.5){
      gameArea = new SafehouseGameArea(terrainFactory);
      gameArea.create();
      gameArea.player.getComponent(KeyboardPlayerInputComponent.class)
              .walkDirection.add(walkingDirection);
    } else if (CurrentLevel == 4) {
      System.out.println("You win");
      ui.getEvents().trigger("exit");
    }
    levelChange = false;
  }
}
