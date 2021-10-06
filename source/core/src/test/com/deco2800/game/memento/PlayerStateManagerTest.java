package com.deco2800.game.memento;

import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.player.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.items.Abilities;
import com.deco2800.game.items.Items;
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
    Entity player, nextPlayerState, finalPlayerState;
    Player playerState = null;
    int PLAYER_ID, AMMO, MAGAZINE, GOLD, BANDAGE, DEFENCE_LEVEL, WOUND_STATE,
            RANGE_ATTACK, BASE_ATTACK, HEALTH, NEW_AMMO, NEW_GOLD, NEW_BANDAGE, LESSER_MAGAZINE, TORCH_TIMER;
    double GAME_LEVEL, NEW_GAME_LEVEL, FINAL_LEVEL;
    String ABILITY, MELEE_WEAPON, ARMOR_TYPE, MELEE_FILE_PATH;

    @BeforeEach
    void beforeEach() {
        PLAYER_ID = 0;
        NEW_GAME_LEVEL = 1.5;
        FINAL_LEVEL = 2.0;
        AMMO = 5;
        NEW_AMMO = 7;
        MAGAZINE = 5;
        LESSER_MAGAZINE = 4;
        GOLD = 10;
        NEW_GOLD = 12;
        BANDAGE = 3;
        NEW_BANDAGE = 4;
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

        player = new Entity()
                .addComponent(new PlayerMeleeAttackComponent(MELEE_FILE_PATH))
                .addComponent(new PlayerActions(WOUND_STATE))
                .addComponent(new PlayerCombatStatsComponent(HEALTH, BASE_ATTACK, WOUND_STATE,
                        RANGE_ATTACK, DEFENCE_LEVEL))
                .addComponent(new InventoryComponent(NEW_GOLD, NEW_AMMO, NEW_BANDAGE, TORCH_TIMER))
                .addComponent(new PlayerAbilitiesComponent(Abilities.getAbility(ABILITY)))
                .addComponent(new PlayerRangeAttackComponent());
        player.getComponent(PlayerMeleeAttackComponent.class).setMeleeWeaponType(Items.getMeleeWeapon(MELEE_WEAPON));
        player.getComponent(PlayerRangeAttackComponent.class).setBulletMagazine(MAGAZINE);

        nextPlayerState = new Entity()
                .addComponent(new PlayerMeleeAttackComponent(MELEE_FILE_PATH))
                .addComponent(new PlayerActions(WOUND_STATE))
                .addComponent(new PlayerCombatStatsComponent(HEALTH, BASE_ATTACK, WOUND_STATE,
                        RANGE_ATTACK, DEFENCE_LEVEL))
                .addComponent(new InventoryComponent(NEW_GOLD, NEW_AMMO, NEW_BANDAGE, TORCH_TIMER))
                .addComponent(new PlayerAbilitiesComponent(Abilities.getAbility(ABILITY)))
                .addComponent(new PlayerRangeAttackComponent());
        nextPlayerState.getComponent(PlayerMeleeAttackComponent.class).setMeleeWeaponType(Items.getMeleeWeapon(MELEE_WEAPON));
        nextPlayerState.getComponent(PlayerRangeAttackComponent.class).setBulletMagazine(LESSER_MAGAZINE);

        finalPlayerState = new Entity()
                .addComponent(new PlayerMeleeAttackComponent(MELEE_FILE_PATH))
                .addComponent(new PlayerActions(WOUND_STATE))
                .addComponent(new PlayerCombatStatsComponent(HEALTH, BASE_ATTACK, WOUND_STATE,
                        RANGE_ATTACK, DEFENCE_LEVEL))
                .addComponent(new InventoryComponent(NEW_GOLD + 1, NEW_AMMO + 1, NEW_BANDAGE + 1, TORCH_TIMER))
                .addComponent(new PlayerAbilitiesComponent(Abilities.getAbility(ABILITY)))
                .addComponent(new PlayerRangeAttackComponent());
        finalPlayerState.getComponent(PlayerMeleeAttackComponent.class).setMeleeWeaponType(Items.getMeleeWeapon(MELEE_WEAPON));
        finalPlayerState.getComponent(PlayerRangeAttackComponent.class).setBulletMagazine(LESSER_MAGAZINE);

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
                DEFENCE_LEVEL, MAGAZINE, ABILITY, MELEE_FILE_PATH, MELEE_WEAPON, ARMOR_TYPE, GAME_LEVEL, TORCH_TIMER);

        String EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + AMMO + ", magazine = " + MAGAZINE +
                ", gold = " + GOLD +
                ", bandage = " + BANDAGE + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + GAME_LEVEL;
        assertEquals(EXPECTED_STRING, playerManager.currentPlayerState().toString());
    }

    @Test
    void shouldAddAndUpdatePlayerState() {
        assertNull(playerManager.currentPlayerState());

        playerManager.createStartingPlayerState(RANGE_ATTACK, BASE_ATTACK, HEALTH, AMMO, BANDAGE, GOLD, WOUND_STATE,
                DEFENCE_LEVEL, MAGAZINE, ABILITY, MELEE_FILE_PATH, MELEE_WEAPON, ARMOR_TYPE, GAME_LEVEL, TORCH_TIMER);
        String EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + AMMO + ", magazine = " + MAGAZINE +
                ", gold = " + GOLD +
                ", bandage = " + BANDAGE + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + GAME_LEVEL;
        assertEquals(EXPECTED_STRING, playerManager.currentPlayerState().toString());

        playerManager.addAndUpdatePlayerState(player, NEW_GAME_LEVEL);
        String NEW_EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + NEW_AMMO + ", magazine = " + MAGAZINE +
                ", gold = " + NEW_GOLD +
                ", bandage = " + NEW_BANDAGE + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + NEW_GAME_LEVEL;
        assertEquals(NEW_EXPECTED_STRING, playerManager.currentPlayerState().toString());
    }

    @Test
    void shouldAddAndUpdatePlayerStateWhenReloading() {
        assertNull(playerManager.currentPlayerState());

        playerManager.createStartingPlayerState(RANGE_ATTACK, BASE_ATTACK, HEALTH, AMMO, BANDAGE, GOLD, WOUND_STATE,
                DEFENCE_LEVEL, MAGAZINE, ABILITY, MELEE_FILE_PATH, MELEE_WEAPON, ARMOR_TYPE, GAME_LEVEL, TORCH_TIMER);
        String EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + AMMO + ", magazine = " + MAGAZINE +
                ", gold = " + GOLD +
                ", bandage = " + BANDAGE + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + GAME_LEVEL;
        assertEquals(EXPECTED_STRING, playerManager.currentPlayerState().toString());

        // player move to next level while reloading
        nextPlayerState.getComponent(PlayerRangeAttackComponent.class).setReloadingStatus(true);

        playerManager.addAndUpdatePlayerState(nextPlayerState, NEW_GAME_LEVEL);
        String NEW_EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + NEW_AMMO + ", magazine = " + MAGAZINE +
                ", gold = " + NEW_GOLD +
                ", bandage = " + NEW_BANDAGE + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + NEW_GAME_LEVEL;
        assertEquals(NEW_EXPECTED_STRING, playerManager.currentPlayerState().toString());
    }

    @Test
    void shouldRestorePlayerState() {
        playerManager.createStartingPlayerState(RANGE_ATTACK, BASE_ATTACK, HEALTH, AMMO, BANDAGE, GOLD, WOUND_STATE,
                DEFENCE_LEVEL, MAGAZINE, ABILITY, MELEE_FILE_PATH, MELEE_WEAPON, ARMOR_TYPE, GAME_LEVEL, TORCH_TIMER);
        playerManager.addAndUpdatePlayerState(nextPlayerState, NEW_GAME_LEVEL);
        playerManager.addAndUpdatePlayerState(finalPlayerState, FINAL_LEVEL);

        playerManager.restorePlayerState();
        String EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + AMMO + ", magazine = " + MAGAZINE + ", gold = " + GOLD +
                ", bandage = " + BANDAGE + ", torch timer = " + TORCH_TIMER + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + GAME_LEVEL;

        assertEquals(EXPECTED_STRING, playerManager.currentPlayerState().toString());
    }

    @Test
    void shouldRestorePlayerStateToClosestSafehouse() {
        playerManager.createStartingPlayerState(RANGE_ATTACK, BASE_ATTACK, HEALTH, AMMO, BANDAGE, GOLD, WOUND_STATE,
                DEFENCE_LEVEL, MAGAZINE, ABILITY, MELEE_FILE_PATH, MELEE_WEAPON, ARMOR_TYPE, GAME_LEVEL, TORCH_TIMER);
        playerManager.addAndUpdatePlayerState(nextPlayerState, NEW_GAME_LEVEL);
        playerManager.addAndUpdatePlayerState(finalPlayerState, FINAL_LEVEL);

        playerManager.restorePlayerStateToClosestSafehouse();
        String EXPECTED_STRING = "Player [ID = " + PLAYER_ID + "], ammo = " + NEW_AMMO + ", magazine = " + LESSER_MAGAZINE + ", gold = " + NEW_GOLD +
                ", bandage = " + NEW_BANDAGE + ", torch timer = " + TORCH_TIMER + ", defenceLevel = " + DEFENCE_LEVEL + ", woundState = " + WOUND_STATE +
                ", baseAttack = " + BASE_ATTACK + ", baseRangedAttack = " + RANGE_ATTACK + ", health = " + HEALTH +
                "\n abiliy: " + ABILITY + ", meleeWeaponType: " + MELEE_WEAPON + ", armorType: " + ARMOR_TYPE +
                ", meleeFilePath: " + MELEE_FILE_PATH + "\n Player is currently at level " + NEW_GAME_LEVEL;

        assertEquals(EXPECTED_STRING, playerManager.currentPlayerState().toString());
    }
}
