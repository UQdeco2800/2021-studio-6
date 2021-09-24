package com.deco2800.game.memento;

import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlayerStateManager {
    private static final Logger logger = LoggerFactory.getLogger(PlayerStateManager.class);

    private static final int MOST_RECENT_PLAYER_STATE_INDEX = 0;
    private static PlayerStateManager manager = null;
    private static final PlayerCaretaker caretaker = new PlayerCaretaker();
    private static Player playerState = null;
    private static PlayerMemento playerMemento = null;

    // this id is used to track player - can be extended to track different objects in game
    private static final int playerID = 0;

    // used to track player's different states during game
    private static List<String> mementoMessages = new ArrayList();

    public static PlayerStateManager getInstance() {
        if (manager == null) {
            manager = new PlayerStateManager();
        }
        return manager;
    }

    public Player currentPlayerState() {
        return playerState;
    }

    /**
     * This is used to create the first player state in game. Player state will be restored to this when game is
     * restarted by user in game
     * @param baseRangedAttack amount of damage dealt by player via range
     * @param baseAttack amount of dadmage dealt by player via melee
     * @param health health of player in current state
     * @param ammo amount of ammo used for rnage attack
     * @param bandages used to heal player's wound state
     * @param gold amount of gold used to purchase items at player's current state
     * @param woundState wound state based on player's current state
     * @param defenceLevel of player in current state
     * @param currentGameLevel current game level of player's current state
     * @param ability being used in player's current state
     * @param meleeFilePath loads data relevant to melee weapon which will correspond to player's current state
     * @param meleeWeaponType weapon type of current player's state
     * @param armorType armor type of current player's state
     */
    public void createStartingPlayerState(int baseRangedAttack, int baseAttack, int health, int ammo, int bandages, int gold,
                                  int woundState, int defenceLevel, int currentGameLevel,String ability, String meleeFilePath,
                                  String meleeWeaponType, String armorType) {
        playerState = new Player(playerID).setBaseRangedAttack(baseRangedAttack).setBaseAttack(baseAttack)
                .setHealth(health).setAmmo(ammo).setBandage(bandages).setGold(gold).setWoundState(woundState)
                .setDefenceLevel(defenceLevel).setAbility(ability).setMeleeFilePath(meleeFilePath)
                .setMeleeWeaponType(meleeWeaponType).setArmorType(armorType).setCurrentGameLevel(currentGameLevel);
        trackPlayerState(playerState);
        playerMemento = playerState.createMemento();
        caretaker.addMemento(playerState.getId(), "Starting player state", playerMemento);
        mementoMessages.add("Starting player state");
    }

    public void addAndUpdatePlayerState(Entity player) {
        // these are localized variables to that will be used to update player state
        int ammo = player.getComponent(InventoryComponent.class).getAmmo();
        int bandage = player.getComponent(InventoryComponent.class).getBandages();
        int gold = player.getComponent(InventoryComponent.class).getGold();
        int health = player.getComponent(PlayerCombatStatsComponent.class).getHealth();
        int woundState = player.getComponent(PlayerCombatStatsComponent.class).getWoundState();
        int defenceLevel = player.getComponent(PlayerCombatStatsComponent.class).getDefenceLevel();
//        int
    }

    public void trackPlayerState(Player playerState) {
        logger.info(playerState.toString());
    }
}
