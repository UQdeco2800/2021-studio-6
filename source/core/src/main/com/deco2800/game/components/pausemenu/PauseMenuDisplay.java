package com.deco2800.game.components.pausemenu;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.settingsmenu.SettingsMenuDisplay;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A ui component for displaying the pause menu.
 */
public class PauseMenuDisplay extends UIComponent {
  private static final String MENU_BUTTON_STYLE = "menu-button-large";
  private static final String CLICK_SOUND_FILE_PATH = "sounds/click.mp3";
  private static final String ROLLOVER_SOUND_FILE_PATH = "sounds/rollover.mp3";
  private static final float MAX_PAUSE_MENU_WIDTH_MEDIUM_FONT = 300;
  private static final float MAX_PAUSE_MENU_HEIGHT_MEDIUM_FONT = 300;
  private static final float MAX_PAUSE_MENU_WIDTH_LARGE_FONT = 400;
  private static final float MAX_PAUSE_MENU_HEIGHT_LARGE_FONT = 400;
  private static final float PADDING_FOR_SMALL_FONT = 5;
  private static final float PADDING_FOR_MEDIUM_FONT = 15;
  private static final float PADDING_FOR_LARGE_FONT = 25;
  private static final float MAX_PAUSE_MENU_WIDTH = 500;
  private static final float MAX_PAUSE_MENU_HEIGHT = 600;
  private static final float PAUSE_MENU_WIDTH_TO_SCREEN_RATIO = 2.7f / 3f;
  private static final float PAUSE_MENU_HEIGHT_TO_SCREEN_RATIO = 2.7f / 3f;
  private static final Logger logger = LoggerFactory.getLogger(PauseMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private final GdxGame game;
  private Table mainTable;
  private Table settingsTable;
  private Window pauseWindow;
  private Boolean rolloverActivated = false;
  private boolean isEnabled = false;

  public PauseMenuDisplay(GdxGame game) {
    this.game = game;
  }
  @Override
  public void create() {
    super.create();
    addActors();
  }

  public void togglePauseScreen() {
    GameTime timeSource = ServiceLocator.getTimeSource();

    if (!isEnabled) {
      timeSource.pause();
      pauseWindow.setVisible(true);
      pauseWindow.toFront();
      isEnabled = true;
    } else {
      pauseWindow.setVisible(false);
      timeSource.unpause();
      isEnabled = false;
    }
  }

  /**
   * Adds a pause menu into the stage that contain the pause menu buttons
   */
  private void addActors() {

    pauseWindow = new Window("", skin, "pausemenu");
    createMainTable();
    createSettingsTable();
    pauseWindow.add(mainTable);
    // adding and removing moves the table from being drawn on the screen but default
    pauseWindow.add(settingsTable);
    pauseWindow.removeActor(settingsTable);
    pauseWindow.padTop(0);
    pauseWindow.padBottom(0);
    setPauseMenuSize();
    pauseWindow.setMovable(false);
    pauseWindow.setResizable(false);
    pauseWindow.setVisible(false);

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
    addButtonSelectListener(continueBtn, "continue", "Continue button clicked");
    addButtonSelectListener(settingsBtn, "pause-settings", "Settings button clicked");
    addButtonSelectListener(menuBtn, "exit-to-menu", "Exit to menu button clicked");
    addButtonSelectListener(exitBtn, "exit-game", "Exit game button clicked");

    // Triggers an event when the user has triggered the button rollover
    addButtonRolloverListener(continueBtn);
    addButtonRolloverListener(settingsBtn);
    addButtonRolloverListener(menuBtn);
    addButtonRolloverListener(exitBtn);

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
    addButtonSelectListener(backBtn, "back", "Back button clicked");
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
    pauseWindow.setPosition(stage.getWidth() / 2 - pauseWindow.getWidth() / 2,
        stage.getHeight() / 2 - pauseWindow.getHeight() / 2);

    adjustMenuButtonsFontSize(pauseMenuWidth, pauseMenuHeight);
  }


  /**
   * Adds a listener to a button that triggers an event when the user selects the button
   * @param button the button to add the select listener to
   * @param eventTrigger the event to be triggered when button is selected
   * @param debugCommand the command to be printed to debug when the button is selected
   */
  private void addButtonSelectListener(Button button, String eventTrigger, String debugCommand) {
    button.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug(debugCommand);
            Sound buttonClickSound = ServiceLocator.getResourceService().getAsset(CLICK_SOUND_FILE_PATH, Sound.class);
            long soundClickId = buttonClickSound.play();
            buttonClickSound.setVolume(soundClickId,0.6f);

            entity.getEvents().trigger(eventTrigger);
          }
        });
  }

  /**
   * Adds a listener to a button that triggers an event when the user activates the button rollover state
   * @param button the button to add the rollover listener to
   */
  private void addButtonRolloverListener(TextButton button) {
    ClickListener rollOverListener = new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

        // check that the rollover even is the text button and not the button label
        if (Boolean.FALSE.equals(rolloverActivated) && event.getRelatedActor() != null && (!event.getRelatedActor().toString().contains("Label:")
                || event.getRelatedActor().toString().contains("TextButton"))) {
          rolloverActivated = true;
          Sound rolloverClickSound = ServiceLocator.getResourceService().getAsset(ROLLOVER_SOUND_FILE_PATH, Sound.class);
          long soundRolloverId = rolloverClickSound.play();
          rolloverClickSound.setVolume(soundRolloverId,0.5f);
        }
      }
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
      {
        // check that the rollover even is the text button and not the button label
        if (event.getRelatedActor() != null && (!event.getRelatedActor().toString().contains("Label:")
                || event.getRelatedActor().toString().contains("TextButton"))) {
          rolloverActivated = false;
        }
      }
    };

    button.addListener(rollOverListener);
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
      changeMenuButtonStyles(MENU_BUTTON_STYLE, PADDING_FOR_LARGE_FONT);
    } else if (menuWidth >= MAX_PAUSE_MENU_WIDTH_MEDIUM_FONT && menuHeight >= MAX_PAUSE_MENU_HEIGHT_MEDIUM_FONT) {
      changeMenuButtonStyles("menu-button-medium", PADDING_FOR_MEDIUM_FONT);
    } else {
      changeMenuButtonStyles("menu-button-small", PADDING_FOR_SMALL_FONT);
    }
  }

  /**
   * Sets the style for all the buttons within the menu table
   * @param style - The LibGDX style to set the button to
   * @param padding - The padding for each cell of the table
   */
  private void changeMenuButtonStyles(String style, float padding) {
    for (Cell<Actor> cell : mainTable.getCells()) {
      cell.pad(padding);
      Actor cellContents = cell.getActor();
      if (Button.class.isInstance(cellContents)) {
        Button cellButton = (Button) cellContents;
        Button.ButtonStyle newButtonStyle = skin.get(style, TextButton.TextButtonStyle.class);
        cellButton.setStyle(newButtonStyle);
      }
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