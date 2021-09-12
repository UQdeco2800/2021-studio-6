package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.player.hud.PlayerHealthAnimationController;
import com.deco2800.game.components.player.hud.PlayerHudAnimationController;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerInterfaceDisplay extends UIComponent {
  Table table, tableCoin, tableBandage, tableAmmo, tableGunMagazine, tableHealth, tableReload;
  private Image heartImage;
  private Label woundLabel;
  private Label healthLabel;
  private Image healthBar;
  private IndependentAnimator dashAnimator;
  private IndependentAnimator healthAnimator;
  private ArrayList<Image> bulletImages = new ArrayList<>();

  private Image bandageImage, ammoImage, coinImage, bulletImage1, bulletImage2, bulletImage3, bulletImage4,
          bulletImage5;
  private Label bandageLabel, ammoLabel, coinLabel, bulletMagazineLabel, reloadLabel;
  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    healthAnimator =
        new IndependentAnimator(
            ServiceLocator.getResourceService()
                .getAsset("images/hud/health.atlas", TextureAtlas.class));
    healthAnimator.setCamera(true);
    healthAnimator.setPositions(10, (float) -2.7);
    healthAnimator.setScale( 3, 1);
    healthAnimator.addAnimation("health1", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health2", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health3", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health4", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health5", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health6", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health7", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health8", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health9", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health10", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health11", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health12", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health13", 0.1f, Animation.PlayMode.NORMAL);

    dashAnimator =
        new IndependentAnimator(
            ServiceLocator.getResourceService()
                .getAsset("images/hud/dashbar.atlas", TextureAtlas.class));
    dashAnimator.addAnimation("dashbar", 0.04f, Animation.PlayMode.NORMAL);
    dashAnimator.addAnimation("dashbarFull", 0.1f, Animation.PlayMode.NORMAL);

    dashAnimator.setCamera(true);
    dashAnimator.setPositions(10, (float) -2);
    dashAnimator.setScale( 3, (float) 0.5);

    entity.getEvents().addListener("updateWound", this::updatePlayerWoundUI);
    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
    entity.getEvents().addListener("firstHit", this::removeHealth);
    entity.getEvents().addListener("updateBandageHUD", this::updatePlayerBandageUI);
    entity.getEvents().addListener("updateAmmoHUD", this::updatePlayerAmmoUI);
    entity.getEvents().addListener("updateGunMagImageHUD", this::updatePlayerGunMagazineImages);
    entity.getEvents().addListener("updateCoinHUD", this::updatePlayerCoinUI);
    addActors();
    setAnimations();
  }

  public void setAnimations() {
    PlayerHudAnimationController setHud = this.entity.getComponent(PlayerHudAnimationController.class);
    PlayerHealthAnimationController setHealth = this.entity.getComponent(PlayerHealthAnimationController.class);
    setHud.setter();
    setHealth.setter();
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f);

    tableHealth = new Table();
    tableHealth.top().left();
    tableHealth.setFillParent(true);
    tableHealth.padTop(150f);
    tableHealth.add(healthBar).size(110f, 32f);

    // Heart image
    float imageSideLength = 30f;
    float bandageSideLength = 50f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));


    healthBar = new Image(ServiceLocator.getResourceService().getAsset
        ("images/hud/healthFull.png", Texture.class));

    // Health text
    int wounds = entity.getComponent(PlayerCombatStatsComponent.class).getWoundState();
    int health = entity.getComponent(PlayerCombatStatsComponent.class).getHealth();
    CharSequence woundText = String.format("Wounds: %d", wounds);
    CharSequence healthText = String.format("Health: %d", health);
    woundLabel = new Label(woundText, skin, "large");
    healthLabel = new Label(healthText, skin, "large");

    table.row().padLeft(-100f);
    table.add(healthLabel);
    table.add(heartImage).size(imageSideLength).pad(6);
    table.add(woundLabel);
    table.padLeft(6f);

    // Relevant images used alongside labels
    bandageImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/bandage/bandage01.png", Texture.class));
    ammoImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/shootingammo.png", Texture.class));
    coinImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/coin.png", Texture.class));
    bulletImage1 = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/shootingammo.png", Texture.class));
    bulletImage2 = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/shootingammo.png", Texture.class));
    bulletImage3 = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/shootingammo.png", Texture.class));
    bulletImage4 = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/shootingammo.png", Texture.class));
    bulletImage5 = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/shootingammo.png", Texture.class));
    List<Image> imageList = Arrays.asList( bulletImage1, bulletImage2, bulletImage3, bulletImage4, bulletImage5);
    bulletImages.addAll(imageList);

    // components with relevant data variables
    InventoryComponent inventory = entity.getComponent(InventoryComponent.class);

    // Data variables shown on HUD
    int bandages = inventory.getBandages();
    int ammo = inventory.getAmmo();
    int coins = inventory.getGold();

    CharSequence bandageText = String.format(": %d", bandages);
    CharSequence ammoText = String.format(" : %d", ammo);
    CharSequence cointText = String.format(" : %d", coins);
    CharSequence magazineText = " Left";
    CharSequence reloadText = "No ammo! Press R to reload!";

    bandageLabel = new Label(bandageText, skin, "large");
    ammoLabel = new Label(ammoText, skin, "large");
    coinLabel = new Label(cointText, skin, "large");
    bulletMagazineLabel = new Label(magazineText, skin, "large");
    reloadLabel = new Label(reloadText, skin, "large");

    tableCoin = new Table();
    tableCoin.padTop(200f);
    tableCoin.add(coinImage);
    tableCoin.add(coinLabel);

    tableBandage = new Table();
    tableBandage.padLeft(-10);
    tableBandage.add(bandageImage).size(bandageSideLength);
    tableBandage.add(bandageLabel);

    tableAmmo = new Table();
    tableAmmo.add(ammoImage);
    tableAmmo.add(ammoLabel);

    tableGunMagazine = new Table();
    tableGunMagazine.add(bulletImage1);
    tableGunMagazine.add(bulletImage2);
    tableGunMagazine.add(bulletImage3);
    tableGunMagazine.add(bulletImage4);
    tableGunMagazine.add(bulletImage5);
    tableGunMagazine.add(bulletMagazineLabel);

    tableReload = new Table();
//    tableReload.center().bottom();
    tableReload.add(reloadLabel);

    table.row();
    table.add(tableCoin).left();
    table.row();
    table.add(tableBandage).left();
    table.row();
    table.add(tableAmmo).left();
    table.row();
    table.add(tableGunMagazine).left();
//    table.add(tableReload).center();

    stage.addActor(table);
  }

  /**
   * Updates images of bullet left in magazine on player's HUD
   * @param index to acquire which image to hide on screen for user to visibly see
   *              bullet has been launched
   * @param reload when reloading, could possibly reload multiple bullets, will need
   *               to set multiple images to be visible on screen
   */
  public void updatePlayerGunMagazineImages(int index, boolean reload) {
    // for when bullets are shot
    if (!reload) {
      bulletImages.get(index).setVisible(false);

    } else {
      // for reloading
      for (int i = 0; i < index; i++) {
        bulletImages.get(i).setVisible(true);
      }
    }
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

  /**
   * Updates the player's bandage quantity left on the ui.
   * @param bandages number left in player's inventory
   */
  public void updatePlayerBandageUI(int bandages) {
    CharSequence text = String.format(": %d", bandages);
    bandageLabel.setText(text);
  }

  /**
   * Updates the player's ammo quantity left on the ui.
   * @param ammo number left in player's inventory
   */
  public void updatePlayerAmmoUI(int ammo) {
    CharSequence text = String.format(" : %d", ammo);
    ammoLabel.setText(text);
  }

  /**
   * Updates the player's coin quantity left on the ui.
   * @param coin number left in player's inventory
   */
  public void updatePlayerCoinUI(int coin) {
    CharSequence text = String.format(" : %d", coin);
    coinLabel.setText(text);
  }

  /**
   * Gets the animator for the dash interface
   * @return IndependentAnimator for the dash
   */
  public IndependentAnimator getDashAnimator() {
    return dashAnimator;
  }

  /**
   * Gets the animator for the health interface
   * @return IndependentAnimator for the health bar
   */
  public IndependentAnimator getHealthAnimator() {
    return healthAnimator;
  }

  /**
   * Temp function to remove original health bar
   */
  void removeHealth() {
    if (healthBar != null) {
      healthBar.remove();
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    healthBar.remove();
    table.remove();
  }
}
