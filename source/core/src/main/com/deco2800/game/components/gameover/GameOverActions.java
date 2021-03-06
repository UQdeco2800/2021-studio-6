package com.deco2800.game.components.gameover;

import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class GameOverActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(GameOverActions.class);
  private static final float BUTTON_CLICK_DURATION = 0.3f;
  private GdxGame game;
  private boolean loading = false;


  public GameOverActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("restart", this::onRestart);
    entity.getEvents().addListener("menu", this::onMenu);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("revertLastCheckpoint", this::onRevertLastCheckpoint);
  }

  /**
   * Swaps to the main game screen
   */
  private void onRevertLastCheckpoint() {
    logger.info("Revert game to last checkpoint");
    if (!loading) {
      loading = true;
      // restarts the game after the button click sound has finished
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          game.setScreen(GdxGame.ScreenType.REVERT_CHECKPOINT);
        }
      }, BUTTON_CLICK_DURATION);
    }
  }

  /**
   * Swaps to the main game screen
   */
  private void onRestart() {
    logger.info("Start game");
    if (!loading) {
      loading = true;
      // restarts the game after the button click sound has finished
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        }
      }, BUTTON_CLICK_DURATION);
    }
  }

  /**
   * Swaps to the menu screen
   */
  private void onMenu() {
    logger.info("Start game");
    if (!loading) {
      loading = true;
      // displays the main menu after the button click sound has finished
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          game.setScreen(GdxGame.ScreenType.MAIN_MENU);
        }
      }, BUTTON_CLICK_DURATION);
    }
  }

  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    if (!loading) {
      loading = true;
      // exits the game after the button click sound has finished
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          game.exit();
        }
      }, BUTTON_CLICK_DURATION);
    }
  }
}
