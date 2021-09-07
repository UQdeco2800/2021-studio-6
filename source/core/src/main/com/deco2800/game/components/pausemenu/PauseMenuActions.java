package com.deco2800.game.components.pausemenu;

import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Pause Menu overlay and does something when one of the
 * events is triggered.
 */
public class PauseMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(PauseMenuActions.class);
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private static final float buttonClickDuration = 0.3f;
  private GdxGame game;
  private boolean gameReturningToMenu = false;
  private boolean gameQuiting = false;

  public PauseMenuActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("continue", this::onContinue);
    entity.getEvents().addListener("pause-settings", this::onSettings);
    entity.getEvents().addListener("exit-to-menu", this::onExitToMenu);
    entity.getEvents().addListener("exit-game", this::onExitGame);
  }

  /**
   * Unpauses the game
   */
  private void onContinue() {
    logger.info("Continue game");
    entity.getEvents().trigger("togglepause");
  }

  /**
   * Shows the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
  }

  /**
   * Swtiches back to the main menu screen
   */
  private void onExitToMenu() {
    logger.info("Exiting to menu");
    if (!gameReturningToMenu) {
      gameReturningToMenu = true;
      // starts the game after the button click sound has finished
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          game.setScreen(GdxGame.ScreenType.MAIN_MENU);
        }
      }, buttonClickDuration);
    }
  }

  /**
   * Exits the game.
   */
  private void onExitGame() {
    logger.info("Exit game");
    if (!gameQuiting) {
      gameQuiting = true;
      // starts the game after the button click sound has finished
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          game.exit();
        }
      }, buttonClickDuration);
    }
  }
}
