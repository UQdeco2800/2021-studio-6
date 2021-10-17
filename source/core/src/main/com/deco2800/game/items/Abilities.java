package com.deco2800.game.items;

public enum Abilities {
    NONE,
    LONG_DASH,
    INVINCIBILITY,
    FIRE_CRACKER;

    public static Abilities getAbility(String ability) {
        switch (ability) {
            case "LONG_DASH":
                return LONG_DASH;
            case "INVINCIBILITY":
                return INVINCIBILITY;
            case "FIRE_CRACKER":
                return FIRE_CRACKER;
            default:
                return NONE;
        }
    }

    public static boolean checkAbility(String ability) {
        switch (ability) {
            case "LONG_DASH":
            case "INVINCIBILITY":
            case "FIRE_CRACKER":
                return true;
            default:
                return false;
        }
    }
}
