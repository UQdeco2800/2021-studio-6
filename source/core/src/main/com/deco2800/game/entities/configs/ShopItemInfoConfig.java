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

    // anythin other than melee weapon will have effects in json file - inclusive of armor
    public String effects;

    // for armor
    public int defenceLevel;
}
