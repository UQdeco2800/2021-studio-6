package com.deco2800.game.entities.configs;

public class ShopItemInfoConfig {
    // for all items - this information is a must have in shop
    public String itemName;
    public int price;
    public String description;

    // for melee weapons
    public String attackLength;
    public String knockback;
    public int attackDamage;

    // for armor
    public String defenceLevel;

    // for abilities #TODO: include description and other essential details
    public String abilityEffect;

    // others - torches, bandage #TODO: include description and other essential details
}
