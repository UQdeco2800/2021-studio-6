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
  private static final Logger logger = LoggerFactory.getLogger(PauseMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private Window pauseWindow;
  private float elapsedTime = 0f;
  private Sound buttonClickSound;
  private Sound rolloverClickSound;
  private Boolean rolloverActivated = false;

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
    } else {
      pauseWindow.setVisible(false);
    }
  }

  /**
   * Adds all the assets (buttons, background, sound, music) for the menu into the stage
   */
  private void addActors() {


    buttonClickSound = ServiceLocator.getResourceService().getAsset(clickSoundFilePath, Sound.class);
    rolloverClickSound = ServiceLocator.getResourceService().getAsset(rolloverSoundFilePath, Sound.class);

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

    pauseWindow = new Window("Game Paused", skin);

    table = new Table();

    table.add(continueBtn);
    table.row();
    table.add(settingsBtn);
    table.row();
    table.add(menuBtn);
    table.row();
    table.add(exitBtn);

    pauseWindow.add(table);

    // Set and position pause window screen
    pauseWindow.setSize(stage.getWidth() / 3f, stage.getHeight() / 2f);
    pauseWindow.setMovable(false);
    pauseWindow.setPosition(stage.getWidth() / 2 - pauseWindow.getWidth() / 2,
        stage.getHeight() / 2 - pauseWindow.getHeight() / 2);

    stage.addActor(pauseWindow);
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
            System.out.println(debugCommand);
            long soundClickId = buttonClickSound.play();
            buttonClickSound.setVolume(soundClickId,0.8f);

            // disposes the sound after the sound has finished to allow the sound playing after menu screen is disposed
            Timer.schedule(new Timer.Task() {
              @Override
              public void run() {
                ServiceLocator.getResourceService().unloadAssets(new String[] {clickSoundFilePath});
              }
            }, 0.2f);

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
        if (!rolloverActivated && event.getRelatedActor() != null && !event.getRelatedActor().toString().contains("Label:")) {
          rolloverActivated = true;
          long soundRolloverId = rolloverClickSound.play();
          rolloverClickSound.setVolume(soundRolloverId,0.8f);
        }
      }
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
      {
        if (event.getRelatedActor() != null && !event.getRelatedActor().toString().contains("Label:")) {
          rolloverActivated = false;
        }
      }
    };

    button.addListener(rollOverListener);
  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    table.clear();
    super.dispose();
  }
}