package com.deco2800.game.entities.configs;

import com.deco2800.game.items.Abilities;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends BaseEntityConfig  {
  public int gold;
  public String favouriteColour = "none";
  public int woundState;
  public int defenceLevel;
  public int baseRangedAttack;
  public int baseAttack;
  public int health;
  public int ammo;
  public int bandages;
  public String ability;
  public String meleeFilePath;
  public String meleeWeaponType;
  public String armorType;
}
