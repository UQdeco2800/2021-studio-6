package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  Table tableHealth;
  private Image heartImage;
  private Label woundLabel;
  private Label healthLabel;
  //temporary health images until I(Chris Kang) figure out how to use
  //animation for UI
  private Image greenShortHealthBar1;
  private Image greenShortHealthBar2;
  private Image greenShortHealthBar3;
  private Image emptyShortHealthBar1;
  private Image emptyShortHealthBar2;
  private Image emptyShortHealthBar3;
  private Image greenMediumHealthBar1;
  private Image orangeMediumHealthBar1;
  private Image emptyMediumHealthBar1;
  private Image greenMediumHealthBar2;
  private Image orangeMediumHealthBar2;
  private Image emptyMediumHealthBar2;
  private Image greenMediumHealthBar3;
  private Image orangeMediumHealthBar3;
  private Image emptyMediumHealthBar3;
  private Image greenMediumHealthBar4;
  private Image orangeMediumHealthBar4;
  private Image emptyMediumHealthBar4;
  private Image greenLongHealthBar1;
  private Image orangeLongHealthBar1;
  private Image redLongHealthBar1;
  private Image emptyLongHealthBar1;
  private Image greenLongHealthBar2;
  private Image orangeLongHealthBar2;
  private Image redLongHealthBar2;
  private Image emptyLongHealthBar2;
  private Image greenLongHealthBar3;
  private Image orangeLongHealthBar3;
  private Image redLongHealthBar3;
  private Image emptyLongHealthBar3;
  private Image greenLongHealthBar4;
  private Image orangeLongHealthBar4;
  private Image redLongHealthBar4;
  private Image emptyLongHealthBar4;
  private Image greenLongHealthBar5;
  private Image orangeLongHealthBar5;
  private Image redLongHealthBar5;
  private Image emptyLongHealthBar5;


  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateWound", this::updatePlayerWoundUI);
    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    tableHealth = new Table();
    tableHealth.top().left();
    tableHealth.setFillParent(true);
    tableHealth.padTop(150f).padLeft(5f);

    // Heart image
    float heartSideLength = 30f;
//    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
    greenShortHealthBar1 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/22highbar6.png", Texture.class));
    greenShortHealthBar2 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/22highbar6.png", Texture.class));
    greenShortHealthBar3 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/22highbar6.png", Texture.class));
    emptyShortHealthBar1 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/22highbar1.png", Texture.class));
    emptyShortHealthBar2 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/22highbar1.png", Texture.class));
    emptyShortHealthBar3 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/22highbar1.png", Texture.class));
    greenMediumHealthBar1 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar7.png", Texture.class));
    greenMediumHealthBar2 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar7.png", Texture.class));
    greenMediumHealthBar3 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar7.png", Texture.class));
    greenMediumHealthBar4 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar7.png", Texture.class));
    orangeMediumHealthBar1 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar6.png", Texture.class));
    orangeMediumHealthBar2 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar6.png", Texture.class));
    orangeMediumHealthBar3 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar6.png", Texture.class));
    orangeMediumHealthBar4 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar6.png", Texture.class));
    emptyMediumHealthBar1 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar1.png", Texture.class));
    emptyMediumHealthBar2 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar1.png", Texture.class));
    emptyMediumHealthBar3 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar1.png", Texture.class));
    emptyMediumHealthBar4 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/27highbar1.png", Texture.class));
    greenLongHealthBar1 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar8.png", Texture.class));
    greenLongHealthBar2 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar8.png", Texture.class));
    greenLongHealthBar3 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar8.png", Texture.class));
    greenLongHealthBar4 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar8.png", Texture.class));
    greenLongHealthBar5 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar8.png", Texture.class));
    orangeLongHealthBar1 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar7.png", Texture.class));
    orangeLongHealthBar2 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar7.png", Texture.class));
    orangeLongHealthBar3 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar7.png", Texture.class));
    orangeLongHealthBar4 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar7.png", Texture.class));
    orangeLongHealthBar4 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar7.png", Texture.class));
    orangeLongHealthBar5 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar8.png", Texture.class));
    redLongHealthBar1 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar6.png", Texture.class));
    redLongHealthBar2 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar6.png", Texture.class));
    redLongHealthBar3 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar6.png", Texture.class));
    redLongHealthBar4 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar6.png", Texture.class));
    redLongHealthBar5 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar6.png", Texture.class));
    emptyLongHealthBar1 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar1.png", Texture.class));
    emptyLongHealthBar2 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar1.png", Texture.class));
    emptyLongHealthBar3 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar1.png", Texture.class));
    emptyLongHealthBar4 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar1.png", Texture.class));
    emptyLongHealthBar5 = new Image(ServiceLocator.getResourceService().getAsset
            ("images/hud/32highbar1.png", Texture.class));


    // Health text
    int wounds = entity.getComponent(PlayerCombatStatsComponent.class).getWoundState();
    int health = entity.getComponent(PlayerCombatStatsComponent.class).getHealth();
    CharSequence woundText = String.format("Wounds: %d", wounds);
    CharSequence healthText = String.format("Health: %d", health);
    woundLabel = new Label(woundText, skin, "large");
    healthLabel = new Label(healthText, skin, "large");

    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel);
    table.add(heartImage).size(heartSideLength).pad(6);
    table.add(woundLabel);
    float barWidth = 16f;
    float barLong = 64f;
    float barMedium = 54f;
    float barShort = 44f;

    tableHealth.add(greenLongHealthBar1).size(barWidth, barLong).padTop(20);
    tableHealth.add(greenLongHealthBar2).size(barWidth, barLong).padTop(20);
    tableHealth.add(greenLongHealthBar3).size(barWidth, barLong).padTop(20);
    tableHealth.add(greenLongHealthBar4).size(barWidth, barLong).padTop(20);
    tableHealth.add(greenLongHealthBar5).size(barWidth, barLong).padTop(20);
    tableHealth.add(greenMediumHealthBar1).size(barWidth, barMedium).padTop(10);
    tableHealth.add(greenMediumHealthBar2).size(barWidth, barMedium).padTop(10);
    tableHealth.add(greenMediumHealthBar3).size(barWidth, barMedium).padTop(10);
    tableHealth.add(greenMediumHealthBar4).size(barWidth, barMedium).padTop(10);
    tableHealth.add(greenShortHealthBar1).size(barWidth, barShort);
    tableHealth.add(greenShortHealthBar2).size(barWidth, barShort);
    tableHealth.add(greenShortHealthBar3).size(barWidth, barShort);

    stage.addActor(table);
    stage.addActor(tableHealth);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's wound state on the ui.
   * @param wound player wound state
   */
  public void updatePlayerWoundUI(int wound) {
    CharSequence text = String.format("Wounds: %d", wound);
    woundLabel.setText(text);
  }

  /**
   * Updates the player's state health on the ui.
   * @param health player state health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    greenShortHealthBar1.remove();
    greenShortHealthBar2.remove();
    greenShortHealthBar3.remove();
    emptyShortHealthBar1.remove();
    emptyShortHealthBar2.remove();
    emptyShortHealthBar3.remove();
    greenMediumHealthBar1.remove();
    orangeMediumHealthBar1.remove();
    emptyMediumHealthBar1.remove();
    greenMediumHealthBar2.remove();
    orangeMediumHealthBar2.remove();
    emptyMediumHealthBar2.remove();
    greenMediumHealthBar3.remove();
    orangeMediumHealthBar3.remove();
    emptyMediumHealthBar3.remove();
    greenMediumHealthBar4.remove();
    orangeMediumHealthBar4.remove();
    emptyMediumHealthBar4.remove();
    greenLongHealthBar1.remove();
    orangeLongHealthBar1.remove();
    redLongHealthBar1.remove();
    emptyLongHealthBar1.remove();
    greenLongHealthBar2.remove();
    orangeLongHealthBar2.remove();
    redLongHealthBar2.remove();
    emptyLongHealthBar2.remove();
    greenLongHealthBar3.remove();
    orangeLongHealthBar3.remove();
    redLongHealthBar3.remove();
    emptyLongHealthBar3.remove();
    greenLongHealthBar4.remove();
    orangeLongHealthBar4.remove();
    redLongHealthBar4.remove();
    emptyLongHealthBar4.remove();
    greenLongHealthBar5.remove();
    orangeLongHealthBar5.remove();
    redLongHealthBar5.remove();
    emptyLongHealthBar5.remove();

  }
}
