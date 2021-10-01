package com.deco2800.game.memento;

import com.deco2800.game.components.player.PlayerMeleeAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class PlayerStateManagerTest {
    PlayerStateManager playerManager;
    Entity player;
    Player playerState = null;
    int PLAYER_ID, AMMO, MAGAZINE, GOLD, BANDAGE, DEFENCE_LEVEL, WOUND_STATE,
            RANGE_ATTACK, BASE_ATTACK, HEALTH;
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
        ABILITY = "LONG_DASH";
        MELEE_WEAPON = "AXE";
        ARMOR_TYPE = "NONE";
        MELEE_FILE_PATH = "configs/Axe.json";

        player = new Entity();
        player.addComponent(new PlayerMeleeAttackComponent(MELEE_FILE_PATH));
        playerState = new Player(PLAYER_ID).setBaseRangedAttack(RANGE_ATTACK).setBaseAttack(BASE_ATTACK)
                .setHealth(HEALTH).setAmmo(AMMO).setBandage(BANDAGE).setGold(GOLD).setWoundState(WOUND_STATE)
                .setDefenceLevel(DEFENCE_LEVEL).setAbility(ABILITY).setMeleeFilePath(MELEE_FILE_PATH)
                .setMeleeWeaponType(MELEE_WEAPON).setArmorType(ARMOR_TYPE).setCurrentGameLevel(GAME_LEVEL)
                .setBulletMagazine(MAGAZINE);

        playerManager = spy(PlayerStateManager.class);
    }

    @AfterEach
    void afterEach() {
        playerManager.destroy();
    }


    @Test
    void shouldNotGetAnyPlayerState() {
        playerManager = mock(PlayerStateManager.class);
        assertNull(playerManager.currentPlayerState());
        assertNull(playerManager.trackPlayerState(playerState));
    }

    @Test
    void shouldCreateStartingPlayerState() {
        playerManager.createStartingPlayerState(RANGE_ATTACK, BASE_ATTACK, HEALTH, AMMO, BANDAGE, GOLD, WOUND_STATE,
                DEFENCE_LEVEL, MAGAZINE, ABILITY, MELEE_FILE_PATH, MELEE_WEAPON, ARMOR_TYPE, GAME_LEVEL);

        String EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + AMMO + ", magazine = " + MAGAZINE +
                ", gold = " + GOLD +
                ", bandage = " + BANDAGE + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + GAME_LEVEL;
        assertEquals(EXPECTED_STRING, playerManager.currentPlayerState().toString());
    }
}
