package com.deco2800.game.components.gameover;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.MenuUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A ui component for displaying the Main menu.
 */
public class GameOverDisplay extends UIComponent {
  private static final String MENU_BUTTON_STYLE = "menu-button-large";
  private static final float BUTTON_PADDING_FOR_LARGE_FONT = 30;
  private static final float CELL_PADDING_LARGE = 20;
  private static final String MUSIC_FILE_PATH = "sounds/game-over-music.mp3";
  private static final String BACKGROUND = "images/placeholder.png";
  private static final Logger logger = LoggerFactory.getLogger(GameOverDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;

  @Override
  public void create() {
    super.create();
    logger.debug("Creating Game Over menu screen");
    addActors();
    logger.debug("Finished creating Game Over menu screen");
  }

  /**
   * Adds all the assets (buttons, background, sound, music) for the game over menu into the stage
   */
  private void addActors() {
    table = new Table();

    playMusic();

    Image background = new Image(ServiceLocator.getResourceService().getAsset(BACKGROUND, Texture.class));

    TextButton restartBtn = new TextButton("Restart Game", skin, MENU_BUTTON_STYLE);
    TextButton menuBtn = new TextButton("Return to Menu", skin, MENU_BUTTON_STYLE);
    TextButton exitBtn = new TextButton("Exit", skin, MENU_BUTTON_STYLE);

    restartBtn.pad(BUTTON_PADDING_FOR_LARGE_FONT);
    menuBtn.pad(BUTTON_PADDING_FOR_LARGE_FONT);
    exitBtn.pad(BUTTON_PADDING_FOR_LARGE_FONT);

    // Triggers an event when the button is pressed
    MenuUtility.addButtonSelectListener(entity, restartBtn, "restart", "Restart button clicked");
    MenuUtility.addButtonSelectListener(entity, menuBtn, "menu", "Menu button clicked");
    MenuUtility.addButtonSelectListener(entity, exitBtn, "exit", "Exit button clicked");

    // Triggers an event when the user has triggered the button rollover
    MenuUtility.addButtonRolloverListener(restartBtn);
    MenuUtility.addButtonRolloverListener(menuBtn);
    MenuUtility.addButtonRolloverListener(exitBtn);

    table.align(Align.center);
    table.add(restartBtn).pad(CELL_PADDING_LARGE);
    table.row();
    table.add(menuBtn).pad(CELL_PADDING_LARGE);
    table.row();
    table.add(exitBtn).pad(CELL_PADDING_LARGE);
    table.setFillParent(true);

    stage.addActor(background);
    stage.addActor(table);
  }

  private void playMusic() {
    Music gameOverSong = ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class);
    gameOverSong.setLooping(true);
    gameOverSong.setVolume(0.1f);
    gameOverSong.play();
  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }


  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  /**
   * Disposes all the assets related to the main menu display
   */
  @Override
  public void dispose() {
    table.clear();
    table.remove();
    ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class).stop();
    super.dispose();
  }
}
