package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerInventoryDisplay extends UIComponent {
  Table table;
  private Image bandageImage;
  private Label bandageLabel;
  private Image ammoImage;
  private Label ammoLabel;
  private Image torchImage;
  private Label torchLabel;



  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("useBandage", this::updatePlayerBandageUI);
    entity.getEvents().addListener("useAmmo", this::updatePlayerAmmoUI);
    entity.getEvents().addListener("useTorch", this::updatePlayerTorchesUI);

  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(280f).padLeft(5f);

    // Heart image
    float heartSideLength = 30f;
    bandageImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
    ammoImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
    torchImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

    // Health text
    int bandages = 0;
    int ammo = 0;
    int torches = 0;
    //entity.getComponent(PlayerCombatStatsComponent.class).getWoundState();

    CharSequence bandageText = String.format("%d", bandages);
    CharSequence ammoText = String.format("%d", ammo);
    CharSequence torchText = String.format("%d", torches);

    bandageLabel = new Label(bandageText, skin, "large");
    ammoLabel = new Label(ammoText, skin, "large");
    torchLabel = new Label(torchText, skin, "large");

    table.add(bandageImage).size(heartSideLength).padTop(80).padLeft(10);
    table.add(bandageLabel).padTop(80);
    table.add(ammoImage).size(heartSideLength).padTop(160);
    table.add(ammoLabel).padTop(160);
    table.add(torchImage).size(heartSideLength).padRight(10);
    table.add(torchLabel);

    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's wound state on the ui.
   * @param  bandages player bandage number
   */
  public void updatePlayerBandageUI(int bandages) {
    CharSequence text = String.format("%d", bandages);
    bandageLabel.setText(text);
  }

  /**
   * Updates the player's wound state on the ui.
   * @param  ammo player bandage number
   */
  public void updatePlayerAmmoUI(int ammo) {
    CharSequence text = String.format("%d", ammo);
    ammoLabel.setText(text);
  }

  /**
   * Updates the player's wound state on the ui.
   * @param  torch player bandage number
   */
  public void updatePlayerTorchesUI(int torch) {
    CharSequence text = String.format("%d", torch);
    torchLabel.setText(text);
  }


  @Override
  public void dispose() {
    super.dispose();

    torchLabel.remove();
    bandageLabel.remove();
    ammoLabel.remove();

    bandageImage.remove();
    ammoImage.remove();
    torchImage.remove();

  }
}
