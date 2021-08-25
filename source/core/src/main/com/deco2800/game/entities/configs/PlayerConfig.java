package com.deco2800.game.entities.configs;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends BaseEntityConfig  {
  public int gold = 1;
  public String favouriteColour = "none";
  public int woundState = 3;
  public int defenceLevel = 1;
  public int baseRangedAttack = 2;
  public int health = 3;
}
