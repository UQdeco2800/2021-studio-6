package com.deco2800.game.memento;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class PlayerTest {
    Player playerState;
    PlayerMemento playerMemento, anotherPlayerMemento = null;
    int ANOTHER_PLAYER_ID, PLAYER_ID, AMMO, MAGAZINE, GOLD, BANDAGE, DEFENCE_LEVEL, WOUND_STATE,
            RANGE_ATTACK, BASE_ATTACK, HEALTH;
    double GAME_LEVEL;
    String ABILITY, MELEE_WEAPON, ARMOR_TYPE, MELEE_FILE_PATH;

    @BeforeEach
    void beforeEach() {
        PLAYER_ID = 0;
        ANOTHER_PLAYER_ID = 1;
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
    }

    @Test
    void shouldSetAndGetEverything() {
        playerState = new Player(PLAYER_ID).setBaseRangedAttack(RANGE_ATTACK).setBaseAttack(BASE_ATTACK)
                .setHealth(HEALTH).setAmmo(AMMO).setBandage(BANDAGE).setGold(GOLD).setWoundState(WOUND_STATE)
                .setDefenceLevel(DEFENCE_LEVEL).setAbility(ABILITY).setMeleeFilePath(MELEE_FILE_PATH)
                .setMeleeWeaponType(MELEE_WEAPON).setArmorType(ARMOR_TYPE).setCurrentGameLevel(GAME_LEVEL)
                .setBulletMagazine(MAGAZINE);

        assertEquals(PLAYER_ID, playerState.getId());
        assertEquals(AMMO, playerState.getAmmo());
        assertEquals(MAGAZINE, playerState.getBulletMagazine());
        assertEquals(GOLD, playerState.getGold());
        assertEquals(BANDAGE, playerState.getBandage());
        assertEquals(DEFENCE_LEVEL, playerState.getDefenceLevel());
        assertEquals(WOUND_STATE, playerState.getWoundState());
        assertEquals(BASE_ATTACK, playerState.getBaseAttack());
        assertEquals(RANGE_ATTACK, playerState.getBaseRangedAttack());
        assertEquals(HEALTH, playerState.getHealth());
        assertEquals(GAME_LEVEL, playerState.getCurrentGameLevel());
        assertEquals(ABILITY, playerState.getAbility());
        assertEquals(MELEE_WEAPON, playerState.getMeleeWeaponType());
        assertEquals(ARMOR_TYPE, playerState.getArmorType());
        assertEquals(MELEE_FILE_PATH, playerState.getMeleeFilePath());
    }

    @Test
    void shouldGetRightString() {
        playerState = new Player(PLAYER_ID).setBaseRangedAttack(RANGE_ATTACK).setBaseAttack(BASE_ATTACK)
                .setHealth(HEALTH).setAmmo(AMMO).setBandage(BANDAGE).setGold(GOLD).setWoundState(WOUND_STATE)
                .setDefenceLevel(DEFENCE_LEVEL).setAbility(ABILITY).setMeleeFilePath(MELEE_FILE_PATH)
                .setMeleeWeaponType(MELEE_WEAPON).setArmorType(ARMOR_TYPE).setCurrentGameLevel(GAME_LEVEL)
                .setBulletMagazine(MAGAZINE);
        String EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + AMMO + ", magazine = " + MAGAZINE +
                ", gold = " + GOLD +
                ", bandage = " + BANDAGE + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + GAME_LEVEL;
        assertEquals(EXPECTED_STRING, playerState.toString());
    }

    @Test
    void shouldCreatePlayerMemento() {
        playerState = new Player(PLAYER_ID).setBaseRangedAttack(RANGE_ATTACK).setBaseAttack(BASE_ATTACK)
                .setHealth(HEALTH).setAmmo(AMMO).setBandage(BANDAGE).setGold(GOLD).setWoundState(WOUND_STATE)
                .setDefenceLevel(DEFENCE_LEVEL).setAbility(ABILITY).setMeleeFilePath(MELEE_FILE_PATH)
                .setMeleeWeaponType(MELEE_WEAPON).setArmorType(ARMOR_TYPE).setCurrentGameLevel(GAME_LEVEL)
                .setBulletMagazine(MAGAZINE);
        playerMemento = playerState.createMemento();

        String EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + AMMO + ", magazine = " + MAGAZINE +
                ", gold = " + GOLD +
                ", bandage = " + BANDAGE + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + GAME_LEVEL;
        assertEquals(EXPECTED_STRING, playerMemento.toString());
    }

    @Test
    void shouldNotRestorePlayer() {
        playerState = new Player(PLAYER_ID).setBaseRangedAttack(RANGE_ATTACK).setBaseAttack(BASE_ATTACK)
                .setHealth(HEALTH).setAmmo(AMMO).setBandage(BANDAGE).setGold(GOLD).setWoundState(WOUND_STATE)
                .setDefenceLevel(DEFENCE_LEVEL).setAbility(ABILITY).setMeleeFilePath(MELEE_FILE_PATH)
                .setMeleeWeaponType(MELEE_WEAPON).setArmorType(ARMOR_TYPE).setCurrentGameLevel(GAME_LEVEL)
                .setBulletMagazine(MAGAZINE);
        playerState.restore(playerMemento);
        assertEquals(PLAYER_ID, playerState.getId());
    }

    @Test
    void shouldRestorePlayer() {
        anotherPlayerMemento = playerMemento = new PlayerMemento(ANOTHER_PLAYER_ID, AMMO, GOLD, BANDAGE,
                DEFENCE_LEVEL, WOUND_STATE, RANGE_ATTACK, BASE_ATTACK, HEALTH, MAGAZINE,
                ABILITY, MELEE_FILE_PATH, MELEE_WEAPON, ARMOR_TYPE, GAME_LEVEL);

        playerState = new Player(PLAYER_ID).setBaseRangedAttack(RANGE_ATTACK).setBaseAttack(BASE_ATTACK)
                .setHealth(HEALTH).setAmmo(AMMO).setBandage(BANDAGE).setGold(GOLD).setWoundState(WOUND_STATE)
                .setDefenceLevel(DEFENCE_LEVEL).setAbility(ABILITY).setMeleeFilePath(MELEE_FILE_PATH)
                .setMeleeWeaponType(MELEE_WEAPON).setArmorType(ARMOR_TYPE).setCurrentGameLevel(GAME_LEVEL)
                .setBulletMagazine(MAGAZINE);

        playerState.restore(anotherPlayerMemento);
        assertEquals(ANOTHER_PLAYER_ID, playerState.getId());
    }
}
