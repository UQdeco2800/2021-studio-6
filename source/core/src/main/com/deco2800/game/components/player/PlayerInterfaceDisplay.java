package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.player.hud.PlayerHealthAnimationController;
import com.deco2800.game.components.player.hud.PlayerHudAnimationController;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class  PlayerInterfaceDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(PlayerInterfaceDisplay.class);
  Table table, tableCoin, tableBandage, tableAmmo, tableGunMagazine, tableHealth, tableReload, tableTemp;
  private Image healthBar;
  private IndependentAnimator dashAnimator;
  private IndependentAnimator healthAnimator;
  private ArrayList<Image> bulletImages = new ArrayList<>();
  private final int MAGAZINE_FULL_COUNT = 5;

  private Image bandageImage, ammoImage, coinImage, bulletImage1, bulletImage2, bulletImage3, bulletImage4,
          bulletImage5;
  private Label bandageLabel, ammoLabel, coinLabel, bulletMagazineLabel, reloadLabel;
  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();

    double level = MainGameScreen.getGameLevel();

    healthAnimator =
        new IndependentAnimator(
            ServiceLocator.getResourceService()
                .getAsset("images/hud/health.atlas", TextureAtlas.class), false);

    healthAnimator.setCamera(true);
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
                .getAsset("images/hud/dashbar.atlas", TextureAtlas.class), false);
    dashAnimator.addAnimation("dashbar", 0.4f, Animation.PlayMode.NORMAL);
    dashAnimator.addAnimation("dashbarFull", 0.1f, Animation.PlayMode.NORMAL);
    dashAnimator.setCamera(true);
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

    int bulletCount = entity.getComponent(PlayerRangeAttackComponent.class).getGunMagazine();
    updateBulletImageHUD(bulletCount);

    if (level != 4) {
      healthAnimator.setPositions(9, (float) 4);
      healthAnimator.setScale( 3, 1);
      dashAnimator.setPositions(9, (float) 4.7);
      dashAnimator.setScale( 3, (float) 0.5);
    } else {
      healthAnimator.setPositions(12, (float) 6);
      healthAnimator.setScale( 4, 1.5f);
      dashAnimator.setPositions(12, (float) 6.5);
      dashAnimator.setScale( 4, (float) 0.4);
    }




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
    tableHealth.padTop(0f);
    tableHealth.add(healthBar).size(110f, 32f);


    float bandageSideLength = 50f;


    healthBar = new Image(ServiceLocator.getResourceService().getAsset
        ("images/hud/healthFull.png", Texture.class));

    table.row().left();
    table.add(tableTemp);
    table.padLeft(6f);

    // Relevant images used alongside labels
    bandageImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/bandage/bandage01.png", Texture.class));
    ammoImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/rock/ammo.png", Texture.class));
    coinImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/coin/coin1.png", Texture.class));
    bulletImage1 = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/rock/ammo.png", Texture.class));
    bulletImage2 = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/rock/ammo.png", Texture.class));
    bulletImage3 = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/rock/ammo.png", Texture.class));
    bulletImage4 = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/rock/ammo.png", Texture.class));
    bulletImage5 = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/rock/ammo.png", Texture.class));
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
    CharSequence coinText = String.format(" x %d", coins);
    CharSequence reloadText = "No ammo! Press R to reload!";

    bandageLabel = new Label(bandageText, skin, "large");
    ammoLabel = new Label(ammoText, skin, "large");
    coinLabel = new Label(coinText, skin, "large");
    reloadLabel = new Label(reloadText, skin, "large");

    tableCoin = new Table();
    tableCoin.padRight(70f);
    tableCoin.add(coinImage);
    tableCoin.add(coinLabel);

    tableBandage = new Table();
    tableBandage.padRight(85f);
    tableBandage.add(bandageImage).size(bandageSideLength);
    tableBandage.add(bandageLabel);

    tableAmmo = new Table();
    tableAmmo.padRight(85f);
    tableAmmo.add(ammoImage);
    tableAmmo.add(ammoLabel);

    tableGunMagazine = new Table();
    tableGunMagazine.padRight(35f);
    tableGunMagazine.add(bulletImage1);
    tableGunMagazine.add(bulletImage2);
    tableGunMagazine.add(bulletImage3);
    tableGunMagazine.add(bulletImage4);
    tableGunMagazine.add(bulletImage5);

    tableReload = new Table();
    tableReload.add(reloadLabel);
    tableReload.setVisible(false);

    table.row();
    table.add(tableCoin).width(Value.percentWidth(.45F, table));
    table.row();
    table.add(tableBandage).width(Value.percentWidth(.45F, table));
    table.row();
    table.add(tableAmmo).width(Value.percentWidth(.45F, table));
    table.row();
    table.add(tableReload).width(Value.percentWidth(.45F, table));
    table.row();
    table.add(tableGunMagazine).width(Value.percentWidth(.55F, table));
    table.bottom().padBottom(30F);
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
   * Updates images of bullet left in magazine on player's HUD
   * @param bulletCount used to display number of bullets left on player's magazine's HUD
   */
  public void updateBulletImageHUD(int bulletCount) {
    // for when bullets are shot
    for (int i = MAGAZINE_FULL_COUNT - 1; i >= bulletCount; i--) {
      bulletImages.get(i).setVisible(false);
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
