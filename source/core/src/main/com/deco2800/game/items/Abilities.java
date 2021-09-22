package com.deco2800.game.items;

public enum Abilities {
    NONE,
    LONG_DASH,
    INVINCIBILITY;

    public static Abilities getAbility(String ability) {
        switch (ability) {
            case "LONG_DASH":
                return LONG_DASH;
            case "INVINCIBILITY":
                return INVINCIBILITY;
            default:
                return NONE;
        }
    }
}
