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
    KATANA,
    GREATAXE,
    DUALDAGGER,

    // used in shop popup box for categorization
    MELEE_WEAPONS,
    SHIELDS,
    OTHERS;


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

    public boolean checkMeleeWeapon(String meleeWeapon) {
        switch (meleeWeapon) {
            case "SWORD":
            case "AXE":
            case "DAGGER":
                return true;
            default:
                return false;
        }
    }
}