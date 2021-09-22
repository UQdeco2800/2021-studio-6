package com.deco2800.game.items;

public enum Items {
    COINS,
    AMMO,
    BANDAGE,
    ARMOUR,
    HELMET,
    SWORD,
    AXE,
    DAGGER,
    SHOP,

    // used in shop popup box for categorization
    MELEE_WEAPONS,
    SHIELDS,
    ABILITIES;

    public static String getWeaponFilepath(String meleeWeapon) {
        switch (meleeWeapon) {
            case "AXE":
                return "configs/Axe.json";
            case "DAGGER":
                return "configs/Dagger.json";
            case "SWORD":
                return "configs/Sword.json";
            default:
                return "";
        }
    }
}