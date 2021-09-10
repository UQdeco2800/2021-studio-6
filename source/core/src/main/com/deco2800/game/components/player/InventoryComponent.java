package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component intended to be used by the player to track their inventory.
 *
 * Currently only stores the gold and ammo amount
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private int gold;
  private int ammo;
  private int bandages;

  public InventoryComponent(int gold, int ammo, int bandages) {
    this.gold = gold;
    this.ammo = ammo;
    this.bandages = bandages;
  }

  @Override
  public void create() {
    super.create();
    entity.getEvents().trigger("updateBandageHUD",this.bandages);
  }

  /**
   * Returns the player's ammo.
   *
   * @return entity's ammmo
   */
  public int getAmmo() {
    return this.ammo;
  }

  /**
   * Returns if the player has a certain amount of ammo.
   * @param ammo required amount of ammo
   * @return player has greater than or equal to the required amount of ammo
   */
  public Boolean hasAmmo(int ammo) {
    return this.ammo >= ammo;
  }

  /**
   * Sets the player's ammo. Ammo has a minimum bound of 0.
   *
   * @param ammo to be set for player
   */
  public void setAmmo(int ammo) {
    this.ammo = Math.max(ammo, 0);
    logger.debug("Setting ammo to {}", this.ammo);
  }

  /**
   * Adds to the player's ammo count . The amount added can be negative.
   * @param ammo ammo to add
   */
  public void addAmmo(int ammo) {
    setAmmo(this.ammo + ammo);
  }

  /**
   * Returns the player's gold.
   *
   * @return entity's health
   */
  public int getGold() {
    return this.gold;
  }

  /**
   * Returns if the player has a certain amount of gold.
   * @param gold required amount of gold
   * @return player has greater than or equal to the required amount of gold
   */
  public Boolean hasGold(int gold) {
    return this.gold >= gold;
  }

  /**
   * Sets the player's gold. Gold has a minimum bound of 0.
   *
   * @param gold gold
   */
  public void setGold(int gold) {
    if (gold >= 0) {
      this.gold = gold;
    } else {
      this.gold = 0;
    }
    logger.debug("Setting gold to {}", this.gold);
  }

  /**
   * Adds to the player's gold. The amount added can be negative.
   * @param gold gold to add
   */
  public void addGold(int gold) {
    setGold(this.gold + gold);
  }

  /**
   * Returns the player's bandage number left to use.
   *
   * @return entity's bandage item quantity
   */
  public int getBandages() {
    return this.bandages;
  }

  /**
   * Returns if the player has a certain amount of bandages.
   * @param bandages required amount of bandages to be checked against
   * @return player has greater than or equal to the required amount of bandages
   */
  public Boolean hasBandages(int bandages) {
    return this.bandages >= bandages;
  }

  /**
   * Sets the player's bandage. Bandage has a minimum bound of 0.
   *
   * @param bandages bandage to be set
   */
  public void setBandages(int bandages) {
    this.bandages = Math.max(bandages, 0);
    logger.debug("Setting ammo to {}", this.bandages);
    entity.getEvents().trigger("updateBandageHUD",bandages);
  }

  /**
   * Adds to the player's bandage. The amount added can be negative.
   * @param bandages to add or subtract
   */
  public void addBandages(int bandages) {
    setBandages(this.bandages + bandages);
  }
}
