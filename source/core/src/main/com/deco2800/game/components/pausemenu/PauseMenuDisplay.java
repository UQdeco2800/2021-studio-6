package com.deco2800.game.components.pausemenu;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * A ui component for displaying the pause menu.
 */
public class PauseMenuDisplay extends UIComponent {
  private static final String MENU_BUTTON_STYLE = "menu-button-large";
  private static final String clickSoundFilePath = "sounds/click.mp3";
  private static final String rolloverSoundFilePath = "sounds/rollover.mp3";
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
  private Table table;
  private Window pauseWindow;
  private float elapsedTime = 0f;
  private Sound buttonClickSound;
  private Sound rolloverClickSound;
  private Boolean rolloverActivated = false;
  private ArrayList<TextButton> menuButtons;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  /**
   * Checks to see if the game is paused by ESC key, if it is display the pause window
   * Game resumes by pressing ESC key again and the pause window goes away
   */
  @Override
  public void update() {
    super.update();

    // If game is paused then turn off the screen
    if (ServiceLocator.getTimeSource().getDeltaTime() == 0f) {
      pauseWindow.setVisible(true);
      pauseWindow.toFront();
    } else {
      pauseWindow.setVisible(false);
    }
  }

  /**
   * Adds a pause menu into the stage that contain the pause menu buttons
   */
  private void addActors() {


    buttonClickSound = ServiceLocator.getResourceService().getAsset(clickSoundFilePath, Sound.class);
    rolloverClickSound = ServiceLocator.getResourceService().getAsset(rolloverSoundFilePath, Sound.class);

    TextButton continueBtn = new TextButton("Continue", skin, MENU_BUTTON_STYLE);
    TextButton settingsBtn = new TextButton("Settings", skin, MENU_BUTTON_STYLE);
    TextButton menuBtn = new TextButton("Exit to Menu", skin, MENU_BUTTON_STYLE);
    TextButton exitBtn = new TextButton("Exit Game", skin, MENU_BUTTON_STYLE);

    // Adds all the text buttons into a list to be accessed elsewhere in the class
    menuButtons = new ArrayList<>();
    menuButtons.add(continueBtn);
    menuButtons.add(settingsBtn);
    menuButtons.add(menuBtn);
    menuButtons.add(exitBtn);

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

    pauseWindow = new Window("", skin, "pausemenu");
    pauseWindow.padTop(0);
    pauseWindow.padBottom(0);
    table = new Table();
    table.add(continueBtn);
    table.row();
    //table.add(settingsBtn);
    //table.row();
    table.add(menuBtn);
    table.row();
    table.add(exitBtn);
    pauseWindow.add(table);

    setPauseMenuSize();
    pauseWindow.setMovable(false);
    pauseWindow.setResizable(false);

    stage.addActor(pauseWindow);
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
        if (!rolloverActivated && event.getRelatedActor() != null && (!event.getRelatedActor().toString().contains("Label:")
                || event.getRelatedActor().toString().contains("TextButton"))) {
          rolloverActivated = true;
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
      changeMenuButtonStyles("menu-button-large", PADDING_FOR_LARGE_FONT);
    } else if (menuWidth >= MAX_PAUSE_MENU_WIDTH_MEDIUM_FONT && menuHeight >= MAX_PAUSE_MENU_HEIGHT_MEDIUM_FONT) {
      changeMenuButtonStyles("menu-button-medium", PADDING_FOR_MEDIUM_FONT);
    } else {
      changeMenuButtonStyles("menu-button-small", PADDING_FOR_SMALL_FONT);
    }
  }

  /**
   * Sets the style for all the buttons within the menu button table
   * @param style - The LibGDX style to set the button to
   */
  private void changeMenuButtonStyles(String style, float padding) {
    for (TextButton menuButton : menuButtons) {
      Button.ButtonStyle newButtonStyle = skin.get(style, TextButton.TextButtonStyle.class);
      menuButton.setStyle(newButtonStyle);
      Array<Cell> cells = table.getCells();

      for (Cell cell : cells) {
        cell.pad(padding);
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
    table.clear();
    table.remove();
    for (Button button : menuButtons) {
      button.remove();
    }
    menuButtons.clear();

    super.dispose();
  }

}