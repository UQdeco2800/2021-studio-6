package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private static final float BUTTON_CLICK_DURATION = 0.3f;
  private GdxGame game;
  private boolean loading = false;


  public MainMenuActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");
    if (!loading) {
      loading = true;
      // starts the game after the button click sound has finished
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          game.setScreen(GdxGame.ScreenType.MAIN_GAME);
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
      // starts the game after the button click sound has finished
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          game.exit();
        }
      }, BUTTON_CLICK_DURATION);
    }
  }

  /**
   * Swaps to the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
    if (!loading) {
      game.setScreen(GdxGame.ScreenType.SETTINGS);
    }
  }
}
