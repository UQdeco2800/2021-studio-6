package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private Image background;
  private static final float BACKGROUND_IMAGE_ASPECT = 9f / 16f;
  private static final float MENU_TABLE_HEIGHT_RATIO = 1f / 2f;
  private static final float MENU_TABLE_RIGHT_OFFSET = 1f / 8f;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();

    background =
        new Image(
            ServiceLocator.getResourceService()
                .getAsset("images/background-menu-screen.png", Texture.class));

    resize((int) stage.getWidth(), (int) stage.getHeight());

    TextButton startBtn = new TextButton("Start", skin, "menu-button");
    TextButton loadBtn = new TextButton("Load", skin, "menu-button");
    TextButton settingsBtn = new TextButton("Settings", skin, "menu-button");
    TextButton exitBtn = new TextButton("Exit", skin, "menu-button");

    // Triggers an event when the button is pressed
    startBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Start button clicked");
            entity.getEvents().trigger("start");
          }
        });

    loadBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Load button clicked");
            entity.getEvents().trigger("load");
          }
        });

    settingsBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Settings button clicked");
            entity.getEvents().trigger("settings");
          }
        });

    exitBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {

            logger.debug("Exit button clicked");
            entity.getEvents().trigger("exit");
          }
        });

    table.align(Align.center);
    table.add(startBtn).padRight(50f);

    // load button is removed until the save/load functionality has been added
    //table.add(loadBtn).padRight(50f);
    table.add(settingsBtn).padRight(50f);
    table.add(exitBtn);

    stage.addActor(background);
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  public void resize(int width, int height) {
      float backgroundWidth = width;
      float backgroundHeight = width * BACKGROUND_IMAGE_ASPECT;
      background.setWidth(backgroundWidth);
      background.setHeight(backgroundHeight);
      background.setPosition(width - backgroundWidth, height - backgroundHeight);

      float tableWidth = backgroundWidth - (backgroundWidth * MENU_TABLE_RIGHT_OFFSET);
      float tableHeight = backgroundHeight * MENU_TABLE_HEIGHT_RATIO;
      table.setWidth(tableWidth);
      table.setHeight(tableHeight);
      table.setPosition(width - backgroundWidth, height - backgroundHeight);
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
