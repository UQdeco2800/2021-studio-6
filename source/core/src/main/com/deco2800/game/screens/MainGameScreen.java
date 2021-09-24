package com.deco2800.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.*;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.KeyboardLevelInputComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.pausemenu.PauseMenuActions;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.components.shopmenu.ShopMenuDisplay;
import com.deco2800.game.components.story.StoryInputComponent;
import com.deco2800.game.components.story.StoryManager;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.lighting.Lighting;
import com.deco2800.game.memento.Player;
import com.deco2800.game.memento.PlayerStateManager;
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
  private static final String[] mainGameTextures = {"images/placeholder.png", "images/heart.png","images/hud/22highbar6.png",
  "images/hud/22highbar1.png","images/hud/27highbar7.png","images/hud/27highbar6.png","images/hud/27highbar1.png",
  "images/hud/32highbar8.png","images/hud/32highbar7.png","images/hud/32highbar6.png","images/hud/32highbar1.png",
          "images/Player_Sprite/front01.png", "images/playeritems/sword/sword2.png", "images/playeritems/sword/sword3.png",
  "images/playeritems/dagger/dagger.png", "images/playeritems/dagger/dagger.png", "images/playeritems/axe/axe_left2.png", "images/playeritems/axe/axe_right4.png",
  "images/playeritems/tourch/tourch.png", "images/playeritems/armour.png", "images/playeritems/halmet.png",
  "images/playeritems/shootingammo.png", "images/playeritems/firecracker/firecracker8.png", "images/playeritems/firecracker/firecracker7.png",
  "images/playeritems/bandage/bandage01.png", "images/playeritems/bandage/bandage02.png", "images/playeritems/coin/money bag.png",
  "images/playeritems/coin/coin1.png"};
  private static final String[] menuSounds = {"sounds/rollover.mp3","sounds/click.mp3"};

  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
  private double gameLevel = 1;
  public static boolean levelChange = false;
  private GameTime timeSource;
  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private final TerrainFactory terrainFactory;
  private final Lighting lighting;
  private final boolean LIGHTINGON = false;
  private GameArea gameArea;
  private Entity ui;



  public MainGameScreen(GdxGame game, GdxGame.GameType gameType) {
    this.game = game;

    // Sets background to light yellow
    Gdx.gl.glClearColor(50/255f, 50/255f, 50/255f, 1);

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    ServiceLocator.registerLightingService(new Lighting(ServiceLocator.getPhysicsService().getPhysics().getWorld()));

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    lighting = ServiceLocator.getLightingService();

    loadAssets();
    createUI();

    logger.debug("Initialising main game screen entities");
    this.terrainFactory = new TerrainFactory(renderer.getCamera());

    doGameLevelLogic(gameType);
  }

  /**
   * This method does logic to decide which level would be generated based on whether game is restarted, checkpoint
   * is being reverted or game will proceed to a new level
   *
   * @param
   */
  private void doGameLevelLogic(GdxGame.GameType gameType) {
    // when game is started for the first time or it is restarted by user
    if (gameType == GdxGame.GameType.RESTART_OR_START) {

      // ensure player states are restored to its original first state if it has been created before
      if (PlayerStateManager.getInstance().currentPlayerState() != null) {
        PlayerStateManager.getInstance().restorePlayerState();
      }
      gameArea = new Level1(terrainFactory);
      gameArea.create();
      ServiceLocator.registerGameArea(gameArea);
      this.gameArea.player.getEvents().addListener("dead", this::checkGameOver);
      this.gameArea.player.getEvents().addListener("toggleShopBox", this::createShopBox);

      // revert checkpoint initiated
    } else {
      Player currentPlayerState = PlayerStateManager.getInstance().currentPlayerState();
      logger.info("Reverting to closest checkpoint with this information - " + currentPlayerState);

      // level where most recent checkpoint was saved for player will be used to determine which level will be
      // generated
      gameLevel = currentPlayerState.getCurrentGameLevel();
      generateNewLevel(true);
    }
  }

  /**
   * This creates a shop box popup for player to interact and purchase items
   */
  private void createShopBox() {
    logger.info("Shop box created");
    ui.getComponent(ShopMenuDisplay.class).toggleShopBox();
  }

  private void checkGameOver() {
    logger.info("Game Over");
    timeSource = ServiceLocator.getTimeSource();
    timeSource.pause();

    Timer.schedule(new Timer.Task() {
      @Override
      public void run() {
        game.setScreen(GdxGame.ScreenType.GAME_OVER);
      }
    }, 1f);
  }

  @Override
  public void render(float delta) {
    if (levelChange) {
      timeSource = ServiceLocator.getTimeSource();
      timeSource.pause();
      generateNewLevel(false);
      timeSource.unpause();
    }
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
    lighting.update();
    renderer.render();
    if(LIGHTINGON){
      lighting.setCamera(renderer.getCamera().getCamera());
      lighting.render();
    }

    renderer.renderUI();



    if (!gameArea.player.getComponent(PlayerCombatStatsComponent.class).isDead()) {
      CAMERA_POSITION.set(gameArea.player.getPosition());
      ServiceLocator.getRenderService().setPos(CAMERA_POSITION);
      renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    }
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
        .addComponent(new KeyboardLevelInputComponent())
        .addComponent(new PauseMenuActions(this.game))
        .addComponent(new PauseMenuDisplay(this.game))
        .addComponent(new ShopMenuDisplay(this.game))
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay())
        .addComponent(StoryManager.getInstance())
        .addComponent(new StoryInputComponent());

    ServiceLocator.getEntityService().register(ui);
  }

  public static void changeLevel() {
    levelChange = true;
  }

  public void generateNewLevel(boolean reverting) {
    Vector2 walkingDirection
            = gameArea.player.getComponent(KeyboardPlayerInputComponent.class).walkDirection;

    // before disposing everything, update and store player's state - this only occurs when game
    // is not reverting to player's most recent checkpoint (going back in game time in a way)
    if (!reverting) {
      gameLevel += 0.5;
      PlayerStateManager.getInstance().addAndUpdatePlayerState(gameArea.player, gameLevel);
    }

    if (gameLevel == 4) {
      victory();
      return;
    }

    gameArea.player.getEvents().trigger("dispose");
    gameArea.dispose();
    if (gameLevel == 2) {
      gameArea = new Level2(terrainFactory);
      gameArea.create();
      gameArea.player.getComponent(KeyboardPlayerInputComponent.class)
              .walkDirection.add(walkingDirection);
    } else if (gameLevel == 3) {
      gameArea = new Level3(terrainFactory);
      gameArea.create();
      gameArea.player.getComponent(KeyboardPlayerInputComponent.class)
              .walkDirection.add(walkingDirection);
    } else if (gameLevel % 1 == 0.5){
      gameArea = new SafehouseGameArea(terrainFactory);
      gameArea.create();
      gameArea.player.getComponent(KeyboardPlayerInputComponent.class)
              .walkDirection.add(walkingDirection);
    }
    ServiceLocator.registerGameArea(gameArea);
    this.gameArea.player.getEvents().addListener("dead", this::checkGameOver);
    levelChange = false;
  }

  private void victory() {
    timeSource.pause();
    spawnOutroDialogue();
  }

  private void spawnOutroDialogue(){
    StoryManager.getInstance().loadCutScene(StoryNames.EPILOGUE);
    StoryManager.getInstance().displayStory();
    StoryManager.getInstance().getEntity().getEvents().addListener("story-finished:" + StoryNames.EPILOGUE,
            this::onOutroFinish);
  }

  private void onOutroFinish() {
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }
}
