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
  private Image heartImage;
  private Label woundLabel;
  private Label healthLabel;

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

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

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
