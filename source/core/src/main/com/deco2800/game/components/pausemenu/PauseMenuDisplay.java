package com.deco2800.game.components.pausemenu;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.settingsmenu.SettingsMenuDisplay;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.MenuUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A ui component for displaying the pause menu.
 */
public class PauseMenuDisplay extends UIComponent {
  private static final String MENU_BUTTON_STYLE = "menu-button-large";
  private static final float MAX_PAUSE_MENU_WIDTH_MEDIUM_FONT = 300;
  private static final float MAX_PAUSE_MENU_HEIGHT_MEDIUM_FONT = 300;
  private static final float MAX_PAUSE_MENU_WIDTH_LARGE_FONT = 400;
  private static final float MAX_PAUSE_MENU_HEIGHT_LARGE_FONT = 400;
  private static final float CELL_PADDING_LARGE = 20;
  private static final float CELL_PADDING_MEDIUM = 12;
  private static final float CELL_PADDING_SMALL = 6;
  private static final float BUTTON_PADDING_FOR_SMALL_FONT = 5;
  private static final float BUTTON_PADDING_FOR_MEDIUM_FONT = 15;
  private static final float BUTTON_PADDING_FOR_LARGE_FONT = 25;
  private static final float MAX_PAUSE_MENU_WIDTH = 500;
  private static final float MAX_PAUSE_MENU_HEIGHT = 600;
  private static final float PAUSE_MENU_WIDTH_TO_SCREEN_RATIO = 2.7f / 3f;
  private static final float PAUSE_MENU_HEIGHT_TO_SCREEN_RATIO = 2.7f / 3f;
  private static final String BACKGROUND_FILE_PATH = "images/placeholder.png";
  private static final Logger logger = LoggerFactory.getLogger(PauseMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private final GdxGame game;
  private Table mainTable;
  private Table settingsTable;
  private Table pauseWindow;
  private boolean isEnabled = false;
  private Image background;

  public PauseMenuDisplay(GdxGame game) {
    this.game = game;
  }
  @Override
  public void create() {
    super.create();
    logger.debug("Creating Pause menu screen");
    addActors();
    logger.debug("Finished creating Pause menu screen");
  }

  public void togglePauseScreen() {
    GameTime timeSource = ServiceLocator.getTimeSource();
    ServiceLocator.getGameArea().player.getEvents().trigger("resetPlayerMovements");

    if (!isEnabled) {
      timeSource.pause();
      pauseWindow.setVisible(true);
      background.setVisible(true);
      background.toFront();
      pauseWindow.toFront();
      isEnabled = true;
    } else {
      pauseWindow.setVisible(false);
      background.setVisible(false);
      timeSource.unpause();
      isEnabled = false;
    }
  }

  /**
   * Adds a pause menu into the stage that contain the pause menu buttons
   */
  private void addActors() {

    background = new Image(ServiceLocator.getResourceService().getAsset(BACKGROUND_FILE_PATH, Texture.class));

    pauseWindow = new Table();

    createMainTable();
    createSettingsTable();
    pauseWindow.add(mainTable);

    // adding and removing moves the table from being drawn on the screen but default
    pauseWindow.add(settingsTable);
    pauseWindow.removeActor(settingsTable);
    pauseWindow.padTop(0);
    pauseWindow.padBottom(0);
    setPauseMenuSize();
    pauseWindow.setVisible(false);
    background.setVisible(false);

    stage.addActor(background);
    stage.addActor(pauseWindow);

    entity.getEvents().addListener("togglepause", this::togglePauseScreen);
    entity.getEvents().addListener("pause-settings", this::onSettings);

  }

  private void createMainTable() {
    TextButton continueBtn = new TextButton("Continue", skin, MENU_BUTTON_STYLE);
    TextButton settingsBtn = new TextButton("Settings", skin, MENU_BUTTON_STYLE);
    TextButton menuBtn = new TextButton("Exit to Menu", skin, MENU_BUTTON_STYLE);
    TextButton exitBtn = new TextButton("Exit Game", skin, MENU_BUTTON_STYLE);

    // Triggers an event when the button is pressed
    MenuUtility.addButtonSelectListener(entity, continueBtn, "continue");
    MenuUtility.addButtonSelectListener(entity, settingsBtn, "pause-settings");
    MenuUtility.addButtonSelectListener(entity, menuBtn, "exit-to-menu");
    MenuUtility.addButtonSelectListener(entity, exitBtn, "exit-game");

    // Triggers an event when the user has triggered the button rollover
    MenuUtility.addButtonRolloverListener(continueBtn);
    MenuUtility.addButtonRolloverListener(settingsBtn);
    MenuUtility.addButtonRolloverListener(menuBtn);
    MenuUtility.addButtonRolloverListener(exitBtn);

    mainTable = new Table();
    mainTable.add(continueBtn);
    mainTable.row();
    mainTable.add(settingsBtn);
    mainTable.row();
    mainTable.add(menuBtn);
    mainTable.row();
    mainTable.add(exitBtn);
  }

  private void createSettingsTable() {
    settingsTable = new Table();

    SettingsMenuDisplay settingsMenu = new SettingsMenuDisplay(game);
    settingsMenu.setTable(settingsTable);
    settingsMenu.changeBackLocation(true);
    settingsMenu.changeTableLocation(true);
    settingsMenu.create();
    TextButton backBtn = settingsMenu.getExitBtn();
    MenuUtility.addButtonSelectListener(entity, backBtn, "back");
    entity.getEvents().addListener("back", this::closeSettings);

  }

  /**
   * Shows the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
    pauseWindow.removeActor(mainTable);
    pauseWindow.add(settingsTable);
  }

  /**
   * Closing the settings screen
   */
  private void closeSettings() {
    logger.info("Closing settings window");
    pauseWindow.removeActor(settingsTable);
    pauseWindow.add(mainTable);
  }
  /**
   * Sets the size of the pause menu dependent on the stage current size
   */
  private void setPauseMenuSize() {
    float stageWidth = stage.getWidth();
    float stageHeight = stage.getHeight();
    float pauseMenuWidth = stageWidth * PAUSE_MENU_WIDTH_TO_SCREEN_RATIO;
    float pauseMenuHeight = stageHeight * PAUSE_MENU_HEIGHT_TO_SCREEN_RATIO;

    // Checks if pause menu should be a max width and height
    if (pauseMenuWidth > MAX_PAUSE_MENU_WIDTH) {
      pauseMenuWidth = MAX_PAUSE_MENU_WIDTH;
    }
    if (pauseMenuHeight > MAX_PAUSE_MENU_HEIGHT) {
      pauseMenuHeight = MAX_PAUSE_MENU_HEIGHT;
    }

    // Set and position pause window screen
    pauseWindow.setSize(pauseMenuWidth, pauseMenuHeight);
    background.setSize(pauseMenuWidth, pauseMenuHeight);
    pauseWindow.setPosition(stage.getWidth() / 2 - pauseWindow.getWidth() / 2,
        stage.getHeight() / 2 - pauseWindow.getHeight() / 2);
    background.setPosition(stage.getWidth() / 2 - pauseWindow.getWidth() / 2,
        stage.getHeight() / 2 - pauseWindow.getHeight() / 2);

    adjustMenuButtonsFontSize(pauseMenuWidth, pauseMenuHeight);
  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
    setPauseMenuSize();
  }

  /**
   * Adjusts the menu button font size based on the size of the table containing the menu buttons
   */
  private void adjustMenuButtonsFontSize(float menuWidth, float menuHeight) {

    if (menuWidth >= MAX_PAUSE_MENU_WIDTH_LARGE_FONT && menuHeight >= MAX_PAUSE_MENU_HEIGHT_LARGE_FONT) {
      MenuUtility.changeMenuButtonStyles(mainTable, skin, MENU_BUTTON_STYLE, BUTTON_PADDING_FOR_LARGE_FONT,
          CELL_PADDING_LARGE);
    } else if (menuWidth >= MAX_PAUSE_MENU_WIDTH_MEDIUM_FONT && menuHeight >= MAX_PAUSE_MENU_HEIGHT_MEDIUM_FONT) {
      MenuUtility.changeMenuButtonStyles(mainTable, skin, "menu-button-medium", BUTTON_PADDING_FOR_MEDIUM_FONT,
          CELL_PADDING_MEDIUM);
    } else {
      MenuUtility.changeMenuButtonStyles(mainTable, skin, "menu-button-small", BUTTON_PADDING_FOR_SMALL_FONT,
          CELL_PADDING_SMALL);
    }
  }


  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  /**
   * Disposes all the assets related to the pause menu display
   */
  @Override
  public void dispose() {
    mainTable.clear();
    mainTable.remove();
    settingsTable.clear();
    settingsTable.remove();

    super.dispose();
  }

}