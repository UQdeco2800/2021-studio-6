package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.screens.GameOverScreen;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.screens.MainMenuScreen;
import com.deco2800.game.screens.SettingsScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
  private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);
  public static boolean win = false;

  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to black
    Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);

    setScreen(ScreenType.MAIN_MENU);
  }

  /**
   * Loads the game's settings.
   */
  private void loadSettings() {
    logger.debug("Loading game settings");
    UserSettings.Settings settings = UserSettings.get();
    UserSettings.applySettings(settings);
  }

  /**
   * Sets the game's screen to a new screen of the provided type.
   * @param screenType screen type
   */
  public void setScreen(ScreenType screenType) {
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(screenType));
  }

  @Override
  public void dispose() {
    logger.debug("Disposing of current screen");
    getScreen().dispose();
  }

  /**
   * Create a new screen of the provided type.
   * @param screenType screen type
   * @return new screen
   */
  private Screen newScreen(ScreenType screenType) {
    switch (screenType) {
      case MAIN_MENU:
        return new MainMenuScreen(this);
      case MAIN_GAME:
        return new MainGameScreen(this, GameType.RESTART_OR_START);
      case REVERT_CHECKPOINT:
        return new MainGameScreen(this, GameType.REVERT_CLOSEST_CHECKPOINT);
      case SETTINGS:
        return new SettingsScreen(this);
      case GAME_OVER:
        return new GameOverScreen(this);
      default:
        return null;
    }
  }

  public enum ScreenType {
    MAIN_MENU, MAIN_GAME, SETTINGS, GAME_OVER, REVERT_CHECKPOINT
  }

  public enum GameType {
    RESTART_OR_START, REVERT_CLOSEST_CHECKPOINT, GENERATE_NEXT_LEVEL
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}
