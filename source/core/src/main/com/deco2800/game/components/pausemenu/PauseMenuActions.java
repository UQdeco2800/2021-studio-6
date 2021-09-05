package com.deco2800.game.components.pausemenu;

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
  private GdxGame game;

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
   * Swaps to the Main Game screen.
   */
  private void onContinue() {
    logger.info("Continue game");
    timeSource.togglePause();

  }

  /**
   * Shows the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
  }

  /**
   * Intended for loading a saved game state.
   * Load functionality is not actually implemented.
   */
  private void onExitToMenu() {
    logger.info("Exiting to menu");
  }

  /**
   * Exits the game.
   */
  private void onExitGame() {
    logger.info("Exit game");
    game.exit();
  }

}
