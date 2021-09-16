package com.deco2800.game.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuUtility {
  private static final Logger logger = LoggerFactory.getLogger(MenuUtility.class);
  private static final String CLICK_SOUND_FILE_PATH = "sounds/click.mp3";
  private static final String ROLLOVER_SOUND_FILE_PATH = "sounds/rollover.mp3";
  private static final float BUTTON_PADDING_DEFAULT = 10;
  private static final float CLICK_VOLUME = 0.4f;
  private static final float ROLLOVER_VOLUME = 0.25f;
  private static Boolean rolloverActivated = false;

  /**
   * This private constructor stops other classes from instantiating the static utility class
   */
  private MenuUtility() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Adds a listener to a button that triggers an event when the user selects the button
   * @param entity the entity that the trigger will be called to
   * @param button the button to add the select listener to
   * @param eventTrigger the event to be triggered when button is selected
   */
  public static void addButtonSelectListener(Entity entity, Button button, String eventTrigger) {
    button.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug(String.format("Button clicked with trigger of: %s", eventTrigger));

            Sound buttonClickSound = ServiceLocator.getResourceService().getAsset(CLICK_SOUND_FILE_PATH, Sound.class);
            buttonClickSound.stop();
            buttonClickSound.play(CLICK_VOLUME);

            entity.getEvents().trigger(eventTrigger);
          }
        });
  }

  /**
   * Adds a listener to a button that triggers an event when the user activates the button rollover state
   * @param button the button to add the rollover listener to
   */
  public static void addButtonRolloverListener(TextButton button) {
    ClickListener rollOverListener = new ClickListener() {

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

        // check that the rollover event is the text button and not the button label

        if (Boolean.FALSE.equals(rolloverActivated) && event.getRelatedActor() != null &&
            event.getRelatedActor().toString().contains("TextureRegionDrawable") &&
            event.getRelatedActor().getParent().toString().contains("TextButton")) {
          rolloverActivated = true;
          Sound rolloverClickSound = ServiceLocator.getResourceService().getAsset(ROLLOVER_SOUND_FILE_PATH, Sound.class);
          rolloverClickSound.play(ROLLOVER_VOLUME);

        }
      }
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
      {
        // check that the rollover even is the text button and not the button label
        if (event.getRelatedActor() != null && event.getRelatedActor().toString().contains("TextureRegionDrawable") &&
            event.getRelatedActor().getParent().toString().contains("TextButton")) {
          rolloverActivated = false;
        }
      }
    };

    button.addListener(rollOverListener);
  }

  /**
   * Sets the style for all the buttons within the menu's table
   * @param table - the table of menu buttons to alter
   * @param skin - the default skin config file
   * @param style - The LibGDX style to set the button to
   * @param buttonPad - The padding for each button of the table
   * @param cellPad - The padding for each cell of the table
   */
  public static void changeMenuButtonStyles(Table table, Skin skin, String style, float buttonPad, float cellPad) {
    for (Cell<Actor> cell : table.getCells()) {
      cell.pad(cellPad);
      Actor cellContents = cell.getActor();
      if (cellContents instanceof Button) {
        Button cellButton = (Button) cellContents;
        Button.ButtonStyle newButtonStyle = skin.get(style, TextButton.TextButtonStyle.class);
        cellButton.setStyle(newButtonStyle);
        cellButton.pad(BUTTON_PADDING_DEFAULT, buttonPad, BUTTON_PADDING_DEFAULT, buttonPad);
      }
    }
  }
}
