package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  private Image heartImage;
  private Label healthLabel;
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

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
//    greenShortHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hudelements/22highbar6.png", Texture.class));
//    emptyShortHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hudelements/22highbar1.png", Texture.class));
//    greenMediumHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hudelements/27highbar7.png", Texture.class));
//    orangeMediumHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hudelements/27highbar6.png", Texture.class));
//    emptyMediumHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hudelements/27highbar1.png", Texture.class));
//    greenLongHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hudelements/32highbar8.png", Texture.class));
//    orangeLongHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hudelements/32highbar7.png", Texture.class));
//    redLongHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hudelements/32highbar6.png", Texture.class));
//    emptyLongHealthBar = new Image(ServiceLocator.getResourceService().getAsset
//            ("images/hudelements/32highbar1.png", Texture.class));

    // Health text
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "large");

    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel);
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * @param health player health
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
