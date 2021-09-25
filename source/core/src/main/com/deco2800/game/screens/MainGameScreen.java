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
  private static boolean revert = false;
  private GameTime timeSource;
  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private final TerrainFactory terrainFactory;
  private final Lighting lighting;
  private final boolean LIGHTINGON = false;
  private GameArea gameArea = null;
  private Entity ui;
  private final double LEVEL_INCREMENT = 0.5;

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

    manageGameLevel(gameType);
  }

  /**
   * This method does logic to decide which level would be generated based on whether game is restarted, checkpoint
   * is being reverted or game will proceed to a new level
   *
   * @param
   */
  private void manageGameLevel(GdxGame.GameType gameType) {
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

    } else {

      // revert player to closest checkpoint - will take player to most recent safehouse and if player is still
      // in level 1, player will be spawned in level 1. PLAYER SHOULD NEVER DIE IN SAFEHOUSE
      if (gameType == GdxGame.GameType.REVERT_CLOSEST_CHECKPOINT) {
        revert = true;
        PlayerStateManager.getInstance().restorePlayerStateToClosestSafehouse();
        logger.info("Player state is reverted");
      } else {
        revert = false;
        logger.info("Player state is carried forward");
      }

      Player currentPlayerState = PlayerStateManager.getInstance().currentPlayerState();
      gameLevel = currentPlayerState.getCurrentGameLevel();
      generateNewLevel(revert);
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

  /**
   * This method generates a new level whenever player enters or leaves safehouse. It is used to not only
   * generate specific levels with different terrains etc. but it is also used to manage logic of when to
   * revert player back to the latest saved state checkpoint
   * @param reverting true when user decided to revert game to latest saved checkpoint and false when game
   *                  is to generate next new level
   */
  public void generateNewLevel(boolean reverting) {
    // before disposing everything, update and store player's state - this only occurs when game
    // is not reverting to player's most recent checkpoint (going back in game time in a way)
    if (!reverting) {
      gameLevel += LEVEL_INCREMENT;
      PlayerStateManager.getInstance().addAndUpdatePlayerState(gameArea.player, gameLevel);
    }
    logger.info("Generating game level " + gameLevel);

    // TODO: This should not be here as this should be for boss fight
    int LEVEL_4 = 4;
    if (gameLevel == LEVEL_4) {
      victory();
      return;
    }

    // when game reverts to closest checkpoint, gameArea will already be disposed of
    if (gameArea != null) {
      gameArea.player.getEvents().trigger("dispose");
      gameArea.dispose();
    }

    // user may want to revert to closest checkpoint on level 1
    int LEVEL_3 = 3;
    int LEVEL_2 = 2;
    int LEVEL_1 = 1;
    if (gameLevel == LEVEL_1) {
      gameArea = new Level1(terrainFactory);

    } else if (gameLevel == LEVEL_2) {
      gameArea = new Level2(terrainFactory);

    } else if (gameLevel == LEVEL_3) {
      gameArea = new Level3(terrainFactory);

    // for safehouse - created in between every level
    // #TODO: Will need to have specific else if statement right after final boss fight level that will call
    // #TODO: victory() method
    } else {
      gameArea = new SafehouseGameArea(terrainFactory);
    }
    gameArea.create();
    gameArea.player.getEvents().trigger("resetPlayerMovements");
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
