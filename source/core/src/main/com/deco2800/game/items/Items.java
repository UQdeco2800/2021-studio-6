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

    public static String getArmorType(int defenceLevel) {
        switch (defenceLevel) {
            case 2:
                return "ARMOUR";
            case 1:
                return "HELMET";
            default:
                return "";
        }
    }

    public static int getDefenceLevel(String shield) {
        switch (shield) {
            case "ARMOUR":
                return 2;
            case "HELMET":
                return 1;
            default:
                return 0;
        }
    }

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

    public static Items getMeleeWeapon(String meleeWeapon) {
        switch (meleeWeapon) {
            case "SWORD":
                return SWORD;
            case "AXE":
                return AXE;
            case "DAGGER":
                return DAGGER;
            case "KATANA":
                return KATANA;
            case "GREATAXE":
                return GREATAXE;
            case "DUALDAGGER":
                return DUALDAGGER;
            default:
                return null;
        }
    }

    public static boolean checkMeleeWeapon(String meleeWeapon) {
        switch (meleeWeapon) {
            case "SWORD":
            case "AXE":
            case "DAGGER":
            case "KATANA":
            case "GREATAXE":
            case "DUALDAGGER":
                return true;
            default:
                return false;
        }
    }

    public static boolean checkShieldType(String shield) {
        switch (shield) {
            case "ARMOUR":
            case "HELMET":
                return true;
            default:
                return false;
        }
    }
}