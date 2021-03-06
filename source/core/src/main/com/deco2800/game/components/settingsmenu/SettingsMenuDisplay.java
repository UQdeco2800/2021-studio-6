package com.deco2800.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.GdxGame;
import com.deco2800.game.GdxGame.ScreenType;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.files.UserSettings.DisplaySettings;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.StringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * DECO2800Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class SettingsMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(SettingsMenuDisplay.class);
  private static final String BUTTON_LABEL_SKIN = "white";
  private final GdxGame game;

  private Table rootTable;
  private TextField fpsText;
  private CheckBox fullScreenCheck;
  private CheckBox vsyncCheck;
  private Slider sfxVolumeSlider;
  private Slider musicVolumeSlider;
  private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;
  private Boolean changeBackButton = false;
  private Boolean changeTable = false;
  private TextButton exitBtn;

  public SettingsMenuDisplay(GdxGame game) {
    super();
    this.game = game;
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  public void setTable(Table table) {
    rootTable = table;
  }

  public void changeBackLocation(Boolean changeBackButton) {
    this.changeBackButton = changeBackButton;
  }
  public void changeTableLocation(Boolean changeTable) {
    this.changeTable = changeTable;
  }

  public TextButton getExitBtn() {
    return exitBtn;
  }

  private void addActors() {
    Label title = new Label("Settings", skin, "title");
    Table settingsTable = makeSettingsTable();
    Table menuBtns = makeMenuBtns();

    if (Boolean.FALSE.equals(changeTable)) {
      rootTable = new Table();
      rootTable.setFillParent(true);
    }

    rootTable.align(Align.center);
    rootTable.add(title).expandX().bottom();

    rootTable.row().padTop(30f);
    rootTable.add(settingsTable).expandX().bottom();

    rootTable.row();
    rootTable.add(menuBtns).padTop(30f);

    stage.addActor(rootTable);
  }

  private Table makeSettingsTable() {
    // Get current values
    UserSettings.Settings settings = UserSettings.get();

    // Create components
    Label fpsLabel = new Label("FPS Cap:", skin, BUTTON_LABEL_SKIN);
    fpsText = new TextField(Integer.toString(settings.fps), skin);

    Label fullScreenLabel = new Label("Fullscreen:", skin, BUTTON_LABEL_SKIN);
    fullScreenCheck = new CheckBox("", skin);
    fullScreenCheck.setChecked(settings.fullscreen);

    Label vsyncLabel = new Label("VSync:", skin, BUTTON_LABEL_SKIN);
    vsyncCheck = new CheckBox("", skin);
    vsyncCheck.setChecked(settings.vsync);

    Label sfxVolumeLabel = new Label("SFX Volume:", skin, BUTTON_LABEL_SKIN);
    sfxVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
    sfxVolumeSlider.setValue(settings.sfxVolume);
    Label sfxVolumeValue = new Label(String.format("%.2fx", settings.sfxVolume), skin, BUTTON_LABEL_SKIN);

    Label musicVolumeLabel = new Label("Music Volume:", skin, BUTTON_LABEL_SKIN);
    musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
    musicVolumeSlider.setValue(settings.musicVolume);
    Label musicVolumeValue = new Label(String.format("%.2fx", settings.musicVolume), skin, BUTTON_LABEL_SKIN);

    Label displayModeLabel = new Label("Resolution:", skin, BUTTON_LABEL_SKIN);
    displayModeSelect = new SelectBox<>(skin);
    Monitor selectedMonitor = Gdx.graphics.getMonitor();
    displayModeSelect.setItems(getDisplayModes(selectedMonitor));
    displayModeSelect.setSelected(getActiveMode(displayModeSelect.getItems()));

    // Position Components on table
    Table table = new Table();

    table.add(fpsLabel).right().padRight(15f);
    table.add(fpsText).width(100).left();

    table.row().padTop(10f);
    table.add(fullScreenLabel).right().padRight(15f);
    table.add(fullScreenCheck).left();

    table.row().padTop(10f);
    table.add(vsyncLabel).right().padRight(15f);
    table.add(vsyncCheck).left();

    table.row().padTop(10f);
    Table sfxVolumeTable = new Table();
    sfxVolumeTable.add(sfxVolumeSlider).width(100).left();
    sfxVolumeTable.add(sfxVolumeValue).left().padLeft(5f).expandX();
    table.add(sfxVolumeLabel).right().padRight(15f);
    table.add(sfxVolumeTable).left();

    table.row().padTop(10f);
    Table musicVolumeTable = new Table();
    musicVolumeTable.add(musicVolumeSlider).width(100).left();
    musicVolumeTable.add(musicVolumeValue).left().padLeft(5f).expandX();
    table.add(musicVolumeLabel).right().padRight(15f);
    table.add(musicVolumeTable).left();

    table.row().padTop(10f);
    table.add(displayModeLabel).right().padRight(15f);
    table.add(displayModeSelect).left();

    // Events on inputs
    sfxVolumeSlider.addListener(
            (Event event) -> {
              float value = sfxVolumeSlider.getValue();
              sfxVolumeValue.setText(String.format("%.2fx", value));
              return true;
            });

    musicVolumeSlider.addListener(
            (Event event) -> {
              float value = musicVolumeSlider.getValue();
              musicVolumeValue.setText(String.format("%.2fx", value));
              return true;
            });

    return table;
  }

  private StringDecorator<DisplayMode> getActiveMode(Array<StringDecorator<DisplayMode>> modes) {
    DisplayMode active = Gdx.graphics.getDisplayMode();

    for (StringDecorator<DisplayMode> stringMode : modes) {
      DisplayMode mode = stringMode.object;
      if (active.width == mode.width
          && active.height == mode.height
          && active.refreshRate == mode.refreshRate) {
        return stringMode;
      }
    }
    return null;
  }

  private Array<StringDecorator<DisplayMode>> getDisplayModes(Monitor monitor) {
    DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);
    Array<StringDecorator<DisplayMode>> arr = new Array<>();

    for (DisplayMode displayMode : displayModes) {
      arr.add(new StringDecorator<>(displayMode, this::prettyPrint));
    }

    return arr;
  }

  private String prettyPrint(DisplayMode displayMode) {
    return displayMode.width + "x" + displayMode.height + ", " + displayMode.refreshRate + "hz";
  }

  private Table makeMenuBtns() {
    exitBtn = new TextButton("Exit", skin);
    TextButton applyBtn = new TextButton("Apply", skin);

    if (Boolean.FALSE.equals(changeBackButton)) {

      exitBtn.addListener(
          new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
              logger.debug("Exit button clicked");
              exitMenu();
            }
          });
    }

    applyBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Apply button clicked");
            applyChanges();
          }
        });

    Table table = new Table();
    table.add(exitBtn).left().pad(0f, 0f, 0f, 30f);
    table.add(applyBtn).right().pad(0f, 30f, 0f, 0f);
    return table;
  }

  private void applyChanges() {
    UserSettings.Settings settings = UserSettings.get();

    Integer fpsVal = parseOrNull(fpsText.getText());
    if (fpsVal != null) {
      settings.fps = fpsVal;
    }
    settings.fullscreen = fullScreenCheck.isChecked();
    settings.sfxVolume = sfxVolumeSlider.getValue();
    settings.musicVolume = musicVolumeSlider.getValue();
    settings.displayMode = new DisplaySettings(displayModeSelect.getSelected().object);
    settings.vsync = vsyncCheck.isChecked();

    UserSettings.set(settings, true);
  }

  private void exitMenu() {
    game.setScreen(ScreenType.MAIN_MENU);
  }

  private Integer parseOrNull(String num) {
    try {
      return Integer.parseInt(num, 10);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  protected void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public void update() {
    stage.act(ServiceLocator.getTimeSource().getDeltaTime());
  }

  @Override
  public void dispose() {
    rootTable.clear();
    super.dispose();
  }
}
