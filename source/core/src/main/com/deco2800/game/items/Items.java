package com.deco2800.game.items;

public enum Items {
    COINS,
    AMMO,
    BANDAGE,
    ARMOUR,
    HELMET,
    CROWBAR,
    AXE,
    DAGGER,
    SHOP,
    SLEDGE,
    MACHETE,
    BAT,

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
                return "NONE";
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
            case "SLEDGE":
                return "configs/Sledge.json";
            case "BAT":
                return "configs/Baseball.json";
            case "MACHETE":
                return "configs/Machete.json";
            case "CROWBAR":
                return "configs/Crowbar.json";
            default:
                return "";
        }
    }

    public static String getShopWeaponConfig(String meleeWeapon) {
        switch (meleeWeapon) {
            case "AXE":
                return "configs/ShopAxeInfo.json";
            case "DAGGER":
                return "configs/ShopDaggerInfo.json";
            case "SLEDGE":
                return "configs/ShopHelmetInfo.json";
            case "BAT":
                return "configs/ShopBaseballInfo.json";
            case "MACHETE":
                return "configs/ShopMacheteInfo.json";
            case "CROWBAR":
                return "configs/Crowbar.json";
            default:
                return "";
        }
    }

    public static Items getMeleeWeapon(String meleeWeapon) {
        switch (meleeWeapon) {
            case "CROWBAR":
                return CROWBAR;
            case "AXE":
                return AXE;
            case "DAGGER":
                return DAGGER;
            case "SLEDGE":
                return SLEDGE;
            case "BAT":
                return BAT;
            case "MACHETE":
                return MACHETE;
            default:
                return null;
        }
    }

    public static boolean checkMeleeWeapon(String meleeWeapon) {
        switch (meleeWeapon) {
            case "CROWBAR":
            case "AXE":
            case "DAGGER":
            case "SLEDGE":
            case "BAT":
            case "MACHETE":
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