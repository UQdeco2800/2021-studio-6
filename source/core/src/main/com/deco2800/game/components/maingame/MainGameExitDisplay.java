package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameExitDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
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

    // If game is paused then turn off screen
    if (ServiceLocator.getTimeSource().getDeltaTime() == 0f) {
      pauseWindow.setVisible(true);
    } else {
      pauseWindow.setVisible(false);
    }
  }

  /**
   * Adds the exit button and pause display, ideally the pause display should be
   * in its own MainGamePauseDisplay component tbh, maybe update code and javadoc for future sprints
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
          entity.getEvents().trigger("exit");
        }
      });

    // =======================  Window Pause test  =============================
    // TODO: For UI team, read below in multicomment section
    /*
     This section below should really be in its own MainGamePauseDisplay.java component
     but I had no success with the timeframe I had as it crashes when I click
     the exit button on pause display, this may be fixed in future sprints by
     someone smarter than me.

     Also may need to update the skin json file for the display to suit the theme?
     Pause Window is also not resizable at the moment.
     Overall this part is really messy.
     */
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
        entity.getEvents().trigger("exit");
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
