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

  public InventoryComponent(int gold, int ammo) {
    setGold(gold);
    setAmmo(ammo);
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
}
