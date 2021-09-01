package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
//  private Image greenShortHealthBar;
//  private Image emptyShortHealthBar;
//  private Image greenMediumHealthBar;
//  private Image orangeMediumHealthBar;
//  private Image emptyMediumHealthBar;
//  private Image greenLongHealthBar;
//  private Image orangeLongHealthBar;
//  private Image redLongHealthBar;
//  private Image emptyLongHealthBar;

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
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
//    greenShortHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hud/22highbar6.png", Texture.class));
//    emptyShortHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/22highbar1.png", Texture.class));
//    greenMediumHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hud/27highbar7.png", Texture.class));
//    orangeMediumHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hud/27highbar6.png", Texture.class));
//    emptyMediumHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hud/27highbar1.png", Texture.class));
//    greenLongHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hud/32highbar8.png", Texture.class));
//    orangeLongHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hud/32highbar7.png", Texture.class));
//    redLongHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hud/32highbar6.png", Texture.class));
//    emptyLongHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hud/32highbar1.png", Texture.class));

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
    stage.addActor(table);
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
  }
}
