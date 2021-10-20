package com.deco2800.game.items;

import org.junit.jupiter.api.Test;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbilitiesTest {
    // Note: tests enum value switching so full of many hardcoded returns which are tested here

    @Test
    void shouldGetAbility() {
        assertEquals(Abilities.LONG_DASH, Abilities.getAbility("LONG_DASH"));
        assertEquals(Abilities.FIRE_CRACKER, Abilities.getAbility("FIRE_CRACKER"));
        assertEquals(Abilities.INVINCIBILITY, Abilities.getAbility("INVINCIBILITY"));
        assertEquals(Abilities.NONE, Abilities.getAbility("ArbitraryOther"));
    }

    @Test
    void shouldCheckAbility() {
        assertEquals(TRUE, Abilities.checkAbility("LONG_DASH"));
        assertEquals(FALSE, Abilities.checkAbility("ArbitraryOther"));
    }
}
