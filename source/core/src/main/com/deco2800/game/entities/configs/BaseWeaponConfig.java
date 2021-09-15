package com.deco2800.game.entities.configs;

public class BaseWeaponConfig {
  public int attackLength;
  public float length;
  public float height;
  public float animationLength; //Animation length = (attackLength/number of animation frames)/1000
  public int knockback;
  public int attackDamage;
  public String atlasPath;
  public Float[][] animationCord;
  public Float[] scale;
}

