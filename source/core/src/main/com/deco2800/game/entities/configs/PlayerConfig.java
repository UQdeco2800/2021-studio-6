package com.deco2800.game.entities.configs;

import com.deco2800.game.items.Abilities;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends BaseEntityConfig  {
  public int gold = 1;
  public String favouriteColour = "none";
  public int woundState = 3;
  public int defenceLevel = 0;
  public int baseRangedAttack = 2;
  public int health = 3;
  public int ammo = 10;
  public int bandages = 3;
  public Abilities ability = Abilities.LONG_DASH;
}
