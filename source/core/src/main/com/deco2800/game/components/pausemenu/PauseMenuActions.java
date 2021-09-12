package com.deco2800.game.components.pausemenu;

import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Pause Menu overlay and does something when one of the
 * events is triggered.
 */
public class PauseMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(PauseMenuActions.class);
  private static final float BUTTON_CLICK_DURATION = 0.3f;
  private GdxGame game;
  private boolean gameReturningToMenu = false;
  private boolean gameQuiting = false;

  public PauseMenuActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("continue", this::onContinue);
    entity.getEvents().addListener("exit-to-menu", this::onExitToMenu);
    entity.getEvents().addListener("exit-game", this::onExitGame);
  }

  /**
   * Unpauses the game
   */
  private void onContinue() {
    logger.info("Continue game");
    if (!gameReturningToMenu && !gameQuiting) {
      entity.getEvents().trigger("togglepause");
    }
  }

  /**
   * Swtiches back to the main menu screen
   */
  private void onExitToMenu() {
    logger.info("Exiting to menu");
    if (!gameReturningToMenu && !gameQuiting) {
      gameReturningToMenu = true;
      // starts the game after the button click sound has finished
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
  private void onExitGame() {
    logger.info("Exit game");
    if (!gameReturningToMenu && !gameQuiting) {
      gameQuiting = true;
      // starts the game after the button click sound has finished
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          game.exit();
        }
      }, BUTTON_CLICK_DURATION);
    }
  }
}
