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
  private static final Logger logger = LoggerFactory.getLogger(PauseMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private Window pauseWindow;

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
    table = new Table();
    table.top().right();
    table.setFillParent(true);

    TextButton mainMenuBtn = new TextButton("Exit", skin);

    // Triggers an event when the button is pressed.
    mainMenuBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Exit button clicked");
            entity.getEvents().trigger("exit-game");
          }
        });

    pauseWindow = new Window("PAUSE - Placeholder Screen", skin);
    TextButton continueButton = new TextButton("Continue", skin);
    TextButton exitButton = new TextButton ("Exit", skin);

    pauseWindow.add(continueButton);
    pauseWindow.add(exitButton);

    // Click Events for continue & exit button
    continueButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // do nothing for now
      }
    });
    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        entity.getEvents().trigger("exit-game");
      }
    });

    // Set and position pause window screen
    pauseWindow.setSize(stage.getWidth() / 3f, stage.getHeight() / 2f);
    pauseWindow.setMovable(false);
    pauseWindow.setPosition(stage.getWidth() / 2 - pauseWindow.getWidth() / 2,
        stage.getHeight() / 2 - pauseWindow.getHeight() / 2);

    stage.addActor(pauseWindow);
    table.add(mainMenuBtn).padTop(10f).padRight(10f);
    stage.addActor(table);
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