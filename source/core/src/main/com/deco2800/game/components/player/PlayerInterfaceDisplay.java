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
  Table table, tableCoin, tableBandage, tableAmmo, tableGunMagazine, tableHealth, tableReload, tableTemp;
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
    healthAnimator.setPositions(9, (float) 4);
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
    dashAnimator.addAnimation("dashbar", 1.2f, Animation.PlayMode.NORMAL);
    dashAnimator.addAnimation("dashbarFull", 0.1f, Animation.PlayMode.NORMAL);

    dashAnimator.setCamera(true);
    dashAnimator.setPositions(9, (float) 4.7);
    dashAnimator.setScale( 3, (float) 0.5);

    entity.getEvents().addListener("firstHit", this::removeHealth);
    entity.getEvents().addListener("updateBandageHUD", this::updatePlayerBandageUI);
    entity.getEvents().addListener("updateAmmoHUD", this::updatePlayerAmmoUI);
    entity.getEvents().addListener("updateGunMagImageHUD", this::updatePlayerGunMagazineImages);
    entity.getEvents().addListener("updateCoinHUD", this::updatePlayerCoinUI);
    entity.getEvents().addListener("gunMagazineEmpty", this::displayGunMagEmpty);
    entity.getEvents().addListener("gunMagazineReloading", this::displayGunMagReloading);
    entity.getEvents().addListener("hideReloadingStatus", this::hideReloadingStatus);
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
    tableHealth.padTop(250f);
    tableHealth.add(healthBar).size(110f, 32f);


    float bandageSideLength = 50f;


    healthBar = new Image(ServiceLocator.getResourceService().getAsset
        ("images/hud/healthFull.png", Texture.class));

    table.row().left();
    table.add(tableTemp);
    table.padLeft(6f);

    // Relevant images used alongside labels
    bandageImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/bandage/bandage01.png", Texture.class));
    ammoImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/shootingammo.png", Texture.class));
    coinImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/coin/coin1.png", Texture.class));
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

    CharSequence bandageText = String.format("x %d", bandages);
    CharSequence ammoText = String.format(" x %d", ammo);
    CharSequence cointText = String.format(" x %d", coins);
    CharSequence reloadText = "No ammo! Press R to reload!";

    bandageLabel = new Label(bandageText, skin, "large");
    ammoLabel = new Label(ammoText, skin, "large");
    coinLabel = new Label(cointText, skin, "large");
    reloadLabel = new Label(reloadText, skin, "large");

    tableCoin = new Table();
    tableCoin.padTop(500f).padLeft(270f);
    tableCoin.add(coinImage);
    tableCoin.add(coinLabel);

    tableBandage = new Table();
    tableBandage.padLeft(260f);
    tableBandage.add(bandageImage).size(bandageSideLength);
    tableBandage.add(bandageLabel);

    tableAmmo = new Table();
    tableAmmo.padLeft(270f);
    tableAmmo.add(ammoImage);
    tableAmmo.add(ammoLabel);

    tableGunMagazine = new Table();
    tableGunMagazine.padLeft(270f).padTop(-20f);
    tableGunMagazine.add(bulletImage1);
    tableGunMagazine.add(bulletImage2);
    tableGunMagazine.add(bulletImage3);
    tableGunMagazine.add(bulletImage4);
    tableGunMagazine.add(bulletImage5);

    tableReload = new Table();
    tableReload.add(reloadLabel);
    tableReload.setVisible(false);

    table.row();
    table.add(tableCoin).left();
    table.row();
    table.add(tableBandage).left();
    table.row();
    table.add(tableAmmo).left();
    table.row();
    table.add(tableReload).left().padLeft(600f);
    table.row();
    table.add(tableGunMagazine).left();

    stage.addActor(table);
  }

  /**
   * Updates images of bullet left in magazine on player's HUD
   * @param index to acquire which image to hide on screen for user to visibly see
   *              bullet has been launched
   * @param reload when reloading, could possibly reload multiple bullets, will need
   *               to set multiple images to be visible on screen
   */
  private void updatePlayerGunMagazineImages(int index, boolean reload) {
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

  /**
   * Displays text that tells user that it is time to reload weapon
   */
  private void displayGunMagEmpty() {
    tableReload.setVisible(true);
    String text = "No ammo! Press R to reload!";
    reloadLabel.setText(text);
  }

  /**
   * Displays text that tells user that currently, gun is reloading
   */
  private void displayGunMagReloading() {
    tableReload.setVisible(true);
    String text = "Reloading...";
    reloadLabel.setText(text);
  }

  /**
   * Hides reloading text once gun has been reloaded successfully after a certain period of time
   * in game
   */
  private void hideReloadingStatus() {
    tableReload.setVisible(false);
  }


  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's bandage quantity left on the ui.
   * @param bandages number left in player's inventory
   */
  public void updatePlayerBandageUI(int bandages) {
    CharSequence text = String.format(" x %d", bandages);
    bandageLabel.setText(text);
  }

  /**
   * Updates the player's ammo quantity left on the ui.
   * @param ammo number left in player's inventory
   */
  public void updatePlayerAmmoUI(int ammo) {
    CharSequence text = String.format(" x %d", ammo);
    ammoLabel.setText(text);
  }

  /**
   * Updates the player's coin quantity left on the ui.
   * @param coin number left in player's inventory
   */
  public void updatePlayerCoinUI(int coin) {
    CharSequence text = String.format(" x %d", coin);
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
