package com.deco2800.game.components.mainmenu;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
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

  private TextureAtlas textureAtlas;
  private Animation<TextureRegion> backgroundAnimation;
  private float elapsedTime = 0f;


  @Override
  public void create() {
    super.create();
    addActors();

  }


  /**
   * Adds all the assets for the menu into the stage
   */
  private void addActors() {
    table = new Table();

    textureAtlas = new TextureAtlas(Gdx.files.internal("images/title-screen.atlas"));
    backgroundAnimation = new Animation(1f/3f, textureAtlas.getRegions());
    background = new Image(backgroundAnimation.getKeyFrame(elapsedTime,true));

    TextButton startBtn = new TextButton("Start", skin, "menu-button-large");
    TextButton loadBtn = new TextButton("Load", skin, "menu-button-large");
    TextButton settingsBtn = new TextButton("Settings", skin, "menu-button-large");
    TextButton exitBtn = new TextButton("Exit", skin, "menu-button-large");

    // Adds all the text buttons into a list to be accessed elsewhere in the class
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
    //table.add(loadBtn);
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

  /**
   * Renders the current menu screen related to the current time elapsed
   * @param delta - the time that has elapsed
   */
  public void render(float delta) {

      elapsedTime += delta;
      Image nextBackgroundImage = new Image(backgroundAnimation.getKeyFrame(elapsedTime,true));
      background.setDrawable(nextBackgroundImage.getDrawable());

  }

  /**
   * Resizes all the menu assets based on the current stage size
   * @param width - The current width of the stage
   * @param height - The current height of the stage
   */
  public void resize(int width, int height) {

      // determine whether the height or width is the restricting factor for background size
      boolean restrictedByHeight = (width * BACKGROUND_IMAGE_ASPECT > height) ? true : false;

      resizeBackground(width, height, restrictedByHeight);
      resizeTable(width, height, restrictedByHeight);
      adjustMenuButtonsFontSize();
  }

    /**
     * Resizes the background based on the current stage width and height.
     * @param width - The current width of the stage
     * @param height - The current height of the stage
     * @param restrictedByHeight - whether the height or width is the restricting factor for background size
     */
    private void resizeBackground(int width, int height, boolean restrictedByHeight) {
        float backgroundWidth = width;
        float backgroundHeight = width * BACKGROUND_IMAGE_ASPECT;
        float backgroundX = width - backgroundWidth;
        float backgroundY = height - backgroundHeight;

        if (restrictedByHeight) {
            backgroundWidth = height * (1f / BACKGROUND_IMAGE_ASPECT);
            backgroundHeight = height;
            backgroundX = width /2 - backgroundWidth/2;
            backgroundY = height /2 - backgroundHeight/2;
        }

        background.setWidth(backgroundWidth);
        background.setHeight(backgroundHeight);
        background.setPosition(backgroundX, backgroundY);
    }

    /**
     * Resizes the table containing the menu buttons based on the current stage width and height.
     * @param width - The current width of the stage
     * @param height - The current height of the stage
     * @param restrictedByHeight - whether the height or width is the restricting factor for background size
     */
    private void resizeTable(int width, int height, boolean restrictedByHeight) {
        float tableWidth = background.getWidth() - (background.getWidth() * MENU_TABLE_RIGHT_OFFSET);
        float tableHeight = background.getHeight() * MENU_TABLE_HEIGHT_RATIO;
        float tableX = width - background.getWidth();
        float tableY = height - background.getHeight();

        if (restrictedByHeight) {
            tableX = background.getX();
            tableY = background.getY();
        }

        table.setWidth(tableWidth);
        table.setHeight(tableHeight);
        table.setPosition(tableX, tableY);
    }

    /**
     * Adjusts the menu button font size based on the size of the table containing the menu buttons
     */
    private void adjustMenuButtonsFontSize() {

      float tableWidth = table.getWidth();

      if (tableWidth < WIDTH_MAX_FOR_SMALL_FONT) {
          changeMenuButtonStyles("menu-button-small", PADDING_FOR_SMALL_FONT);
      } else if (tableWidth < WIDTH_MAX_FOR_MEDIUM_FONT) {
          changeMenuButtonStyles("menu-button-medium", PADDING_FOR_MEDIUM_FONT);
      } else {
          changeMenuButtonStyles("menu-button-large", PADDING_FOR_LARGE_FONT);
      }
  }

  /**
   * Sets the style for all the buttons within the menu button table
   * @param style - The LibGDX style to set the button to
   * @param padding - The padding for each cell of the table
   */
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
