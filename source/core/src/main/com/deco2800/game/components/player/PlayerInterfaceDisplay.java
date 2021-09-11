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

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerInterfaceDisplay extends UIComponent {
  Table table, tableCoin, tableBandage, tableAmmo, tableHealth;
  private Image heartImage;
  private Label woundLabel;
  private Label healthLabel;
  private Image healthBar;
  private IndependentAnimator dashAnimator;
  private IndependentAnimator healthAnimator;

  private Image bandageImage, ammoImage, coinImage;
  private Label bandageLabel, ammoLabel, coinLabel, bulletMagazineLabel;
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

    table.add(healthLabel);
    table.add(heartImage).size(imageSideLength).pad(6);
    table.add(woundLabel);
    table.padLeft(6f);

    // Relevant images used alongside labels
    bandageImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/bandage/bandage01.png", Texture.class));
    ammoImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/shootingammo.png", Texture.class));
    coinImage = new Image(ServiceLocator.getResourceService().getAsset("images/playeritems/coin.png", Texture.class));

    // components with relevant data variables
    InventoryComponent inventory = entity.getComponent(InventoryComponent.class);
    PlayerRangeAttackComponent rangeAttackComponent = entity.getComponent(PlayerRangeAttackComponent.class);

    // Data variables shown on HUD
    int bandages = inventory.getBandages();
    int ammo = inventory.getAmmo();
    int coins = inventory.getGold();
    int bulletsInMagazine = rangeAttackComponent.getGunMagazine();

    CharSequence bandageText = String.format(": %d", bandages);
    CharSequence ammoText = String.format(" : %d", ammo);
    CharSequence cointText = String.format(" : %d", coins);
    CharSequence magazineText = String.format("Bullets left: %d/5", bulletsInMagazine);

    bandageLabel = new Label(bandageText, skin, "large");
    ammoLabel = new Label(ammoText, skin, "large");
    coinLabel = new Label(cointText, skin, "large");
    bulletMagazineLabel = new Label(magazineText, skin, "large");

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

    table.row();
    table.add(tableCoin).left();
    table.row();
    table.add(tableBandage).left();
    table.row();
    table.add(tableAmmo).left();

    stage.addActor(table);
  }

  /**
   *
   */
  public void updatePlayerGunMagazine(int bullet) {

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
    heartImage.remove();
    healthLabel.remove();
    healthBar.remove();

    coinLabel.remove();
    bandageLabel.remove();
    ammoLabel.remove();
    bulletMagazineLabel.remove();

    bandageImage.remove();
    ammoImage.remove();
    coinImage.remove();

    tableHealth.remove();
    table.remove();
  }
}
