package com.deco2800.game.items;

public enum Abilities {
    NONE,
    LONG_DASH,
    INVINCIBILITY;

    public static Abilities getAbility(String ability) {
        if (ability.equals("LONG_DASH")) {
            return LONG_DASH;
        } else if (ability.equals("INVINCIBILITY")) {
            return INVINCIBILITY;
        }
        return NONE;
    }
}
