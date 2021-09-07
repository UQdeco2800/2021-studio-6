package com.deco2800.game.components.gameover;

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
 * A ui component for displaying the Main menu.
 */
public class GameOverDisplay extends UIComponent {
  private static final String MENU_BUTTON_STYLE = "menu-button-large";
  private static final float PADDING_FOR_LARGE_FONT = 30;
  private static final String clickSoundFilePath = "sounds/click.mp3";
  private static final String rolloverSoundFilePath = "sounds/rollover.mp3";
  private static final Logger logger = LoggerFactory.getLogger(GameOverDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private ArrayList<TextButton> menuButtons;
  private Sound buttonClickSound;
  private Sound rolloverClickSound;
  private Boolean rolloverActivated = false;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  /**
   * Adds all the assets (buttons, background, sound, music) for the game over menu into the stage
   */
  private void addActors() {
    table = new Table();

    buttonClickSound = ServiceLocator.getResourceService().getAsset(clickSoundFilePath, Sound.class);
    rolloverClickSound = ServiceLocator.getResourceService().getAsset(rolloverSoundFilePath, Sound.class);

    TextButton restartBtn = new TextButton("Restart Game", skin, MENU_BUTTON_STYLE);
    TextButton menuBtn = new TextButton("Return to Menu", skin, MENU_BUTTON_STYLE);
    TextButton exitBtn = new TextButton("Exit", skin, MENU_BUTTON_STYLE);

    // Adds all the text buttons into a list to be accessed elsewhere in the class
    menuButtons = new ArrayList<>();
    menuButtons.add(restartBtn);
    menuButtons.add(menuBtn);
    menuButtons.add(exitBtn);

    // Triggers an event when the button is pressed
    addButtonSelectListener(restartBtn, "restart", "Restart button clicked");
    addButtonSelectListener(menuBtn, "menu", "Menu button clicked");
    addButtonSelectListener(exitBtn, "exit", "Exit button clicked");

    // Triggers an event when the user has triggered the button rollover
    addButtonRolloverListener(restartBtn);
    addButtonRolloverListener(menuBtn);
    addButtonRolloverListener(exitBtn);

    table.align(Align.center);
    table.add(restartBtn).pad(PADDING_FOR_LARGE_FONT);
    table.row();
    table.add(menuBtn).pad(PADDING_FOR_LARGE_FONT);;
    table.row();
    table.add(exitBtn).pad(PADDING_FOR_LARGE_FONT);;
    table.setFillParent(true);

    stage.addActor(table);
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
            buttonClickSound.setVolume(soundClickId,0.8f);

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

        if (!rolloverActivated && (event.getRelatedActor() == null || !event.getRelatedActor().toString().contains("Label:"))) {
          rolloverActivated = true;
          long soundRolloverId = rolloverClickSound.play();
          rolloverClickSound.setVolume(soundRolloverId,0.8f);
        }
      }
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
      {
        if (event.getRelatedActor() == null || !event.getRelatedActor().toString().contains("Label:")) {
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

  /**
   * Disposes all the assets related to the main menu display
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
