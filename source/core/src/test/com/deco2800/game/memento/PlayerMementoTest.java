package com.deco2800.game.memento;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class PlayerMementoTest {
    PlayerMemento playerMemento;
    int PLAYER_ID, AMMO, MAGAZINE, GOLD, BANDAGE, DEFENCE_LEVEL, WOUND_STATE,
            RANGE_ATTACK, BASE_ATTACK, HEALTH, TORCH_TIMER;
    double GAME_LEVEL;
    String ABILITY, MELEE_WEAPON, ARMOR_TYPE, MELEE_FILE_PATH;

    @BeforeEach
    void beforeEach() {
        PLAYER_ID = 0;
        AMMO = 5;
        MAGAZINE = 5;
        GOLD = 10;
        BANDAGE = 3;
        DEFENCE_LEVEL = 0;
        WOUND_STATE = 3;
        RANGE_ATTACK = 5;
        BASE_ATTACK = 10;
        HEALTH = 3;
        GAME_LEVEL = 1;
        TORCH_TIMER = 1;

        ABILITY = "LONG_DASH";
        MELEE_WEAPON = "AXE";
        ARMOR_TYPE = "NONE";
        MELEE_FILE_PATH = "configs/Axe.json";

        playerMemento = new PlayerMemento(PLAYER_ID, AMMO, GOLD, BANDAGE,
                DEFENCE_LEVEL, WOUND_STATE, RANGE_ATTACK, BASE_ATTACK, HEALTH, MAGAZINE,
                ABILITY, MELEE_FILE_PATH, MELEE_WEAPON, ARMOR_TYPE, GAME_LEVEL, TORCH_TIMER);
    }

    @Test
    void shouldGetEverything() {
        assertEquals(PLAYER_ID, playerMemento.getId());
        assertEquals(AMMO, playerMemento.getAmmo());
        assertEquals(MAGAZINE, playerMemento.getBulletMagazine());
        assertEquals(GOLD, playerMemento.getGold());
        assertEquals(BANDAGE, playerMemento.getBandage());
        assertEquals(DEFENCE_LEVEL, playerMemento.getDefenceLevel());
        assertEquals(WOUND_STATE, playerMemento.getWoundState());
        assertEquals(BASE_ATTACK, playerMemento.getBaseAttack());
        assertEquals(RANGE_ATTACK, playerMemento.getBaseRangedAttack());
        assertEquals(HEALTH, playerMemento.getHealth());
        assertEquals(GAME_LEVEL, playerMemento.getCurrentGameLevel());
        assertEquals(ABILITY, playerMemento.getAbility());
        assertEquals(MELEE_WEAPON, playerMemento.getMeleeWeaponType());
        assertEquals(ARMOR_TYPE, playerMemento.getArmorType());
        assertEquals(MELEE_FILE_PATH, playerMemento.getMeleeFilePath());
    }

    @Test
    void shouldGetRightString() {
        String EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + AMMO + ", magazine = " + MAGAZINE +
                ", gold = " + GOLD +
                ", bandage = " + BANDAGE + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + GAME_LEVEL;
        assertEquals(EXPECTED_STRING, playerMemento.toString());
    }
}
