package com.deco2800.game.components.mainmenu;

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
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
  private static final float BACKGROUND_IMAGE_ASPECT = 9f / 16f;
  private static final float MENU_TABLE_HEIGHT_RATIO = 1f / 2f;
  private static final float MENU_TABLE_RIGHT_OFFSET = 1f / 8f;
  private static final float WIDTH_MAX_FOR_SMALL_FONT = 500;
  private static final float WIDTH_MAX_FOR_MEDIUM_FONT = 750;
  private static final float PADDING_FOR_SMALL_FONT = 10;
  private static final float PADDING_FOR_MEDIUM_FONT = 30;
  private static final float PADDING_FOR_LARGE_FONT = 50;
  private static final String MENU_BUTTON_STYLE = "menu-button-large";
  private static final String MUSIC_FILE_PATH = "sounds/title-screen-music.mp3";
  private static final String CLICK_SOUND_FILE_PATH = "sounds/click.mp3";
  private static final String ROLLOVER_SOUND_FILE_PATH = "sounds/rollover.mp3";
  private static final String TITLE_SCREEN_ATLAS_FILE_PATH = "images/title-screen.atlas";
  private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private Image background;
  private Animation<TextureRegion> backgroundAnimation;
  private float elapsedTime = 0f;
  private Boolean rolloverActivated = false;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  /**
   * Adds all the assets (buttons, background, sound, music) for the menu into the stage
   */
  private void addActors() {
    table = new Table();

    playMusic();

    TextureAtlas backgroundTextureAtlas = ServiceLocator.getResourceService().getAsset(TITLE_SCREEN_ATLAS_FILE_PATH, TextureAtlas.class);

    backgroundAnimation = new Animation<>(1f/3f, backgroundTextureAtlas.getRegions());
    background = new Image(backgroundAnimation.getKeyFrame(elapsedTime,true));

    TextButton startBtn = new TextButton("Start", skin, MENU_BUTTON_STYLE);
    TextButton settingsBtn = new TextButton("Settings", skin, MENU_BUTTON_STYLE);
    TextButton exitBtn = new TextButton("Exit", skin, MENU_BUTTON_STYLE);

    // Triggers an event when the button is pressed
    addButtonSelectListener(startBtn, "start", "Start button clicked");
    addButtonSelectListener(settingsBtn, "settings", "Settings button clicked");
    addButtonSelectListener(exitBtn, "exit", "Exit button clicked");

    // Triggers an event when the user has triggered the button rollover
    addButtonRolloverListener(startBtn);
    addButtonRolloverListener(settingsBtn);
    addButtonRolloverListener(exitBtn);

    table.align(Align.center);
    table.add(startBtn);
    table.add(settingsBtn);
    table.add(exitBtn);

    resize((int) stage.getWidth(), (int) stage.getHeight());
    stage.addActor(background);
    stage.addActor(table);

  }

  private void playMusic() {
    Music menuSong = ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class);
    menuSong.setLooping(true);
    menuSong.setVolume(0.3f);
    menuSong.play();
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
            Sound buttonClickSound = ServiceLocator.getResourceService().getAsset(CLICK_SOUND_FILE_PATH, Sound.class);
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
        if (Boolean.FALSE.equals(rolloverActivated) && event.getRelatedActor() != null && !event.getRelatedActor().toString().contains("Label:")) {
          rolloverActivated = true;
          Sound rolloverClickSound = ServiceLocator.getResourceService().getAsset(ROLLOVER_SOUND_FILE_PATH, Sound.class);
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
      boolean restrictedByHeight = (width * BACKGROUND_IMAGE_ASPECT > height);

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
            backgroundX = (float) width / 2 - backgroundWidth / 2;
            backgroundY = (float) height / 2 - backgroundHeight / 2;
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
          changeMenuButtonStyles(MENU_BUTTON_STYLE, PADDING_FOR_LARGE_FONT);
      }
  }

  /**
   * Sets the style for all the buttons within the menu table
   * @param style - The LibGDX style to set the button to
   * @param padding - The padding for each cell of the table
   */
  private void changeMenuButtonStyles(String style, float padding) {
    for (Cell<Actor> cell : table.getCells()) {
      cell.pad(padding);
      Actor cellContents = cell.getActor();
      if (Button.class.isInstance(cellContents)) {
        Button cellButton = (Button) cellContents;
        Button.ButtonStyle newButtonStyle = skin.get(style, TextButton.TextButtonStyle.class);
        cellButton.setStyle(newButtonStyle);
      }
    }
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
    background.clear();
    background.remove();
    ServiceLocator.getResourceService().getAsset(MUSIC_FILE_PATH, Music.class).stop();
    super.dispose();
  }
}
