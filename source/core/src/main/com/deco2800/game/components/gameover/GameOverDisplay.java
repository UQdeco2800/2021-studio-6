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
  private static final float BACKGROUND_IMAGE_ASPECT = 9f / 16f;
  private static final float MENU_TABLE_HEIGHT_RATIO = 1f / 2f;
  private static final float MENU_TABLE_TOP_OFFSET = 1f / 8f;
  private static final float WIDTH_MAX_FOR_SMALL_FONT = 500;
  private static final float WIDTH_MAX_FOR_MEDIUM_FONT = 750;
  private static final float BUTTON_PADDING_FOR_SMALL_FONT = 3;
  private static final float BUTTON_PADDING_FOR_MEDIUM_FONT = 15;
  private static final float BUTTON_PADDING_FOR_LARGE_FONT = 35;
  private static final float CELL_PADDING_LARGE = 20;
  private static final float CELL_PADDING_MEDIUM = 12;
  private static final float CELL_PADDING_SMALL = 2;
  private static final String MENU_BUTTON_STYLE = "menu-button-large";
  private static final String MUSIC_FILE_PATH = "sounds/game-over-music.mp3";
  private static final String BACKGROUND = "images/gameover.png";
  private static final Logger logger = LoggerFactory.getLogger(GameOverDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private Image background;

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

    background = new Image(ServiceLocator.getResourceService().getAsset(BACKGROUND, Texture.class));

    TextButton restartBtn = new TextButton("Restart Game", skin, MENU_BUTTON_STYLE);
    TextButton menuBtn = new TextButton("Return to Menu", skin, MENU_BUTTON_STYLE);
    TextButton exitBtn = new TextButton("Exit", skin, MENU_BUTTON_STYLE);

    restartBtn.pad(BUTTON_PADDING_FOR_LARGE_FONT);
    menuBtn.pad(BUTTON_PADDING_FOR_LARGE_FONT);
    exitBtn.pad(BUTTON_PADDING_FOR_LARGE_FONT);

    // Triggers an event when the button is pressed
    MenuUtility.addButtonSelectListener(entity, restartBtn, "restart");
    MenuUtility.addButtonSelectListener(entity, menuBtn, "menu");
    MenuUtility.addButtonSelectListener(entity, exitBtn, "exit");

    // Triggers an event when the user has triggered the button rollover
    MenuUtility.addButtonRolloverListener(restartBtn);
    MenuUtility.addButtonRolloverListener(menuBtn);
    MenuUtility.addButtonRolloverListener(exitBtn);

    table.add(restartBtn).pad(CELL_PADDING_LARGE);
    table.row();
    table.add(menuBtn).pad(CELL_PADDING_LARGE);
    table.row();
    table.add(exitBtn).pad(CELL_PADDING_LARGE);

    resize((int) stage.getWidth(), (int) stage.getHeight());
    stage.addActor(background);
    stage.addActor(table);
  }

  private void playMusic() {
    Music gameOverSong = ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class);
    gameOverSong.setLooping(true);
    gameOverSong.setVolume(0.1f);
    gameOverSong.play();
  }


  /**
   * Resizes all the menu assets based on the current stage size
   * @param width - The current width of the stage
   * @param height - The current height of the stage
   */
  public void resize(int width, int height) {

    // determine whether the height or width is the restricting factor for background size
    boolean restrictedByHeight = (width * BACKGROUND_IMAGE_ASPECT > height);

    resizeBackground(width, height, restrictedByHeight);
    resizeTable();
    adjustMenuButtonsFontSize();
  }

  /**
   * Resizes the background based on the current stage width and height.
   * @param width - The current width of the stage
   * @param height - The current height of the stage
   * @param restrictedByHeight - whether the height or width is the restricting factor for background size
   */
  private void resizeBackground(int width, int height, boolean restrictedByHeight) {
    float backgroundWidth = width;
    float backgroundHeight = width * BACKGROUND_IMAGE_ASPECT;
    float backgroundX = width - backgroundWidth;
    float backgroundY = height - backgroundHeight;

    if (restrictedByHeight) {
      backgroundWidth = height * (1f / BACKGROUND_IMAGE_ASPECT);
      backgroundHeight = height;
      backgroundX = (float) width / 2 - backgroundWidth / 2;
      backgroundY = (float) height / 2 - backgroundHeight / 2;
    }

    background.setWidth(backgroundWidth);
    background.setHeight(backgroundHeight);
    background.setPosition(backgroundX, backgroundY);
  }

  /**
   * Resizes the table containing the menu buttons based on the current stage width and height.
   */
  private void resizeTable() {
    float tableWidth = background.getWidth();
    float tableHeight = background.getHeight();

    float tableX = background.getX();
    float tableY = background.getY() - MENU_TABLE_TOP_OFFSET*tableHeight;

    table.setWidth(tableWidth);
    table.setHeight(tableHeight);
    table.setPosition(tableX, tableY);
  }

  /**
   * Adjusts the menu button font size based on the size of the table containing the menu buttons
   */
  private void adjustMenuButtonsFontSize() {

    float tableWidth = table.getWidth();

    if (tableWidth < WIDTH_MAX_FOR_SMALL_FONT) {
      MenuUtility.changeMenuButtonStyles(table, skin, "menu-button-small", BUTTON_PADDING_FOR_SMALL_FONT,
          CELL_PADDING_SMALL);
    } else if (tableWidth < WIDTH_MAX_FOR_MEDIUM_FONT) {
      MenuUtility.changeMenuButtonStyles(table, skin, "menu-button-medium", BUTTON_PADDING_FOR_MEDIUM_FONT,
          CELL_PADDING_MEDIUM);
    } else {
      MenuUtility.changeMenuButtonStyles(table, skin, MENU_BUTTON_STYLE, BUTTON_PADDING_FOR_LARGE_FONT,
          CELL_PADDING_LARGE);
    }
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
