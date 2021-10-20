package com.deco2800.game.items;

import org.junit.jupiter.api.Test;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemsTest {
    // Note: tests enum value switching so full of many hardcoded returns which are tested here

    @Test
    void shouldGetArmourType() {
        assertEquals("ARMOUR", Items.getArmorType(5));
        assertEquals("CHEST", Items.getArmorType(3));
        assertEquals("HELMET", Items.getArmorType(2));
        assertEquals("NONE", Items.getArmorType(20));
        assertEquals("NONE", Items.getArmorType(0));
    }

    @Test
    void shouldGetDefenceLevelNumber() {
        assertEquals(5, Items.getDefenceLevel("ARMOUR"));
        assertEquals(3, Items.getDefenceLevel("CHEST"));
        assertEquals(2, Items.getDefenceLevel("HELMET"));
        assertEquals(0, Items.getDefenceLevel("arbitraryOther"));
    }

    @Test
    void shouldGetWeaponFilepath() {
        assertEquals("configs/Axe.json", Items.getWeaponFilepath("AXE"));
        assertEquals("configs/Dagger.json", Items.getWeaponFilepath("DAGGER"));
        assertEquals("configs/Sledge.json", Items.getWeaponFilepath("SLEDGE"));
        assertEquals("configs/Baseball.json", Items.getWeaponFilepath("BAT"));
        assertEquals("configs/Machete.json", Items.getWeaponFilepath("MACHETE"));
        assertEquals("configs/Crowbar.json", Items.getWeaponFilepath("CROWBAR"));
        assertEquals("", Items.getWeaponFilepath("arbitraryOther"));
    }

    @Test
    void shouldGetMeleeWeapon() {
        assertEquals(Items.AXE, Items.getMeleeWeapon("AXE"));
        assertEquals(Items.DAGGER, Items.getMeleeWeapon("DAGGER"));
        assertEquals(Items.SLEDGE, Items.getMeleeWeapon("SLEDGE"));
        assertEquals(Items.BAT, Items.getMeleeWeapon("BAT"));
        assertEquals(Items.MACHETE, Items.getMeleeWeapon("MACHETE"));
        assertEquals(Items.CROWBAR, Items.getMeleeWeapon("CROWBAR"));
        assertEquals(null, Items.getMeleeWeapon("arbitraryOther"));
    }

    @Test
    void shouldCheckMeleeWeapon() {
        assertEquals(TRUE, Items.checkMeleeWeapon("CROWBAR"));
        assertEquals(FALSE, Items.checkMeleeWeapon("arbitraryOther"));
    }

    @Test
    void shouldCheckShieldType() {
        assertEquals(TRUE, Items.checkShieldType("HELMET"));
        assertEquals(FALSE, Items.checkShieldType("arbitraryOther"));
    }
}
