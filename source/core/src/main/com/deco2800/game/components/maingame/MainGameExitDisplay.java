package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

  @Override
  public void create() {
    super.create();
    addActors();
  }

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
    Window pause = new Window("PAUSE - Placeholder Screen", skin);
    TextButton continueButton = new TextButton("Continue", skin);
    TextButton exitButton = new TextButton ("Exit", skin);

    pause.add(continueButton);
    pause.add(exitButton);

    // Click Events for continue & exit button
    continueButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        pause.setVisible(false);
      }
    });
    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        entity.getEvents().trigger("exit");
      }
    });

    // Set and position pause window screen
    pause.setSize(stage.getWidth() / 3f, stage.getHeight() / 2f);
    pause.setMovable(false);
    pause.setPosition(stage.getWidth() / 2 - pause.getWidth() / 2,
            stage.getHeight() / 2 - pause.getHeight() / 2);

    stage.addActor(pause);
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
