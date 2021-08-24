package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private Image background;
  private ArrayList<TextButton> menuButtons;
  private static final float BACKGROUND_IMAGE_ASPECT = 9f / 16f;
  private static final float MENU_TABLE_HEIGHT_RATIO = 1f / 2f;
  private static final float MENU_TABLE_RIGHT_OFFSET = 1f / 8f;
  private static final float WIDTH_MAX_FOR_SMALL_FONT = 500;
  private static final float WIDTH_MAX_FOR_MEDIUM_FONT = 750;
  private static final float PADDING_FOR_SMALL_FONT = 0;
  private static final float PADDING_FOR_MEDIUM_FONT = 15;
  private static final float PADDING_FOR_LARGE_FONT = 25;

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

    TextButton startBtn = new TextButton("Start", skin, "menu-button-large");
    TextButton loadBtn = new TextButton("Load", skin, "menu-button-large");
    TextButton settingsBtn = new TextButton("Settings", skin, "menu-button-large");
    TextButton exitBtn = new TextButton("Exit", skin, "menu-button-large");

    menuButtons = new ArrayList<TextButton>();
    menuButtons.add(startBtn);
    menuButtons.add(loadBtn);
    menuButtons.add(settingsBtn);
    menuButtons.add(exitBtn);


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
    table.add(startBtn);

    // load button is removed until the save/load functionality has been added
    //table.add(loadBtn).padRight(50f);
    table.add(settingsBtn);
    table.add(exitBtn);

    resize((int) stage.getWidth(), (int) stage.getHeight());

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
      float backgroundX = width - backgroundWidth;
      float backgroundY = height - backgroundHeight;
      boolean restrictedByHeight = (backgroundHeight > height) ? true : false;

      if (restrictedByHeight) {
          backgroundWidth = height * (1f / BACKGROUND_IMAGE_ASPECT);
          backgroundHeight = height;
          backgroundX = width/2 - backgroundWidth/2;
          backgroundY = height/2 - backgroundHeight/2;
      }

      background.setWidth(backgroundWidth);
      background.setHeight(backgroundHeight);
      background.setPosition(backgroundX, backgroundY);

      float tableWidth = backgroundWidth - (backgroundWidth * MENU_TABLE_RIGHT_OFFSET);
      float tableHeight = backgroundHeight * MENU_TABLE_HEIGHT_RATIO;
      float tableX = width - backgroundWidth;
      float tableY = height - backgroundHeight;

      if (restrictedByHeight) {
          tableX = backgroundX;
          tableY = backgroundY;
      }

      table.setWidth(tableWidth);
      table.setHeight(tableHeight);
      table.setPosition(tableX, tableY);

      adjustMenuButtonsOnResize();
  }

  private void adjustMenuButtonsOnResize() {
      float tableWidth = table.getWidth();

      if (tableWidth < WIDTH_MAX_FOR_SMALL_FONT) {
          changeMenuButtonStyles("menu-button-small", PADDING_FOR_SMALL_FONT);
      } else if (tableWidth < WIDTH_MAX_FOR_MEDIUM_FONT) {
          changeMenuButtonStyles("menu-button-medium", PADDING_FOR_MEDIUM_FONT);
      } else {
          changeMenuButtonStyles("menu-button-large", PADDING_FOR_LARGE_FONT);
      }
  }

  private void changeMenuButtonStyles(String style, float padding) {
      for (TextButton menuButton : menuButtons) {
          Button.ButtonStyle newButtonStyle = skin.get( style, TextButton.TextButtonStyle.class );
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

  @Override
  public void dispose() {
    table.clear();
    super.dispose();
  }
}
