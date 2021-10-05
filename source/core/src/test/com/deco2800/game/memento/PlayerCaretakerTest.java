package com.deco2800.game.memento;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class PlayerCaretakerTest {
    PlayerMemento playerMemento;
    PlayerCaretaker playerCaretaker;
    int PLAYER_ID, AMMO, MAGAZINE, GOLD, BANDAGE, DEFENCE_LEVEL, WOUND_STATE,
            RANGE_ATTACK, BASE_ATTACK, HEALTH;
    double GAME_LEVEL;
    String ABILITY, MELEE_WEAPON, ARMOR_TYPE, MELEE_FILE_PATH, MEMENTO_MESSAGE_INITIAL_STATE, EMPTY_MESSAGE,
            DIFF_MESSAGE;

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
        ABILITY = "LONG_DASH";
        MELEE_WEAPON = "AXE";
        ARMOR_TYPE = "NONE";
        MELEE_FILE_PATH = "configs/Axe.json";
        MEMENTO_MESSAGE_INITIAL_STATE = "Player state at level 1";
        DIFF_MESSAGE = "Testing";
        EMPTY_MESSAGE = "";

        playerMemento = new PlayerMemento(PLAYER_ID, AMMO, GOLD, BANDAGE,
                DEFENCE_LEVEL, WOUND_STATE, RANGE_ATTACK, BASE_ATTACK, HEALTH, MAGAZINE,
                ABILITY, MELEE_FILE_PATH, MELEE_WEAPON, ARMOR_TYPE, GAME_LEVEL);
        playerCaretaker = new PlayerCaretaker();
    }

    @Test
    void shouldNotGetMemento() {
        assertNull(playerCaretaker.getMemento(PLAYER_ID, EMPTY_MESSAGE));
    }

    @Test
    void shouldAddAndGetMemento() {
        playerCaretaker.addMemento(PLAYER_ID, MEMENTO_MESSAGE_INITIAL_STATE, playerMemento);

        String EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + AMMO + ", magazine = " + MAGAZINE +
                ", gold = " + GOLD +
                ", bandage = " + BANDAGE + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + GAME_LEVEL;
        assertEquals(EXPECTED_STRING, playerCaretaker.getMemento(PLAYER_ID, MEMENTO_MESSAGE_INITIAL_STATE).toString());
    }

    @Test
    void shouldNotAddMemento() {
        playerCaretaker.addMemento(PLAYER_ID, EMPTY_MESSAGE, playerMemento);
        assertNull(playerCaretaker.getMemento(PLAYER_ID, MEMENTO_MESSAGE_INITIAL_STATE));
    }

    @Test
    void shouldNotGetMementoWithWrongMessage() {
        playerCaretaker.addMemento(PLAYER_ID, MEMENTO_MESSAGE_INITIAL_STATE, playerMemento);
        assertNull(playerCaretaker.getMemento(PLAYER_ID, DIFF_MESSAGE));
    }
}
