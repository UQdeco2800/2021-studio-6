package com.deco2800.game.memento;

import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerAbilitiesComponent;
import com.deco2800.game.components.player.PlayerMeleeAttackComponent;
import com.deco2800.game.components.player.PlayerRangeAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.items.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton manager class used to manage player state. Currently, there is only 1 player state and it is updated
 * when a new level is generated and when game is restarted/started. This class is responsible for updating, restoring
 * and carrying that state from one level to another.
 */
public class PlayerStateManager {
    private static final Logger logger = LoggerFactory.getLogger(PlayerStateManager.class);

    private static final String MEMENTO_MESSAGE_INITIAL_STATE = "Player state at level 1";
    private static final String MEMENTO_MESSAGE_CHECKPOINT = "Player state at level ";
    private static final int FULL_MAGAZINE = 5;
    private static int LEVEL_ONE = 1;
    private static double LEVEL_INCREMENT = 0.5;
    private static PlayerStateManager manager = null;
    private static final PlayerCaretaker caretaker = new PlayerCaretaker();
    private static Player playerState = null;
    private static PlayerMemento playerMemento = null;

    // this id is used to track player - can be extended to track different objects in game
    private static final int playerID = 0;

    /**
     * Returns single instance of singletone
     * @return player state manager instance
     */
    public static PlayerStateManager getInstance() {
        if (manager == null) {
            manager = new PlayerStateManager();
        }
        return manager;
    }

    /**
     * Acquire current player state
     * @return most recent created player state
     */
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
     * @param bullet left in magazine of player
     * @param currentGameLevel current game level of player's current state
     * @param ability being used in player's current state
     * @param meleeFilePath loads data relevant to melee weapon which will correspond to player's current state
     * @param meleeWeaponType weapon type of current player's state
     * @param armorType armor type of current player's state
     */
    public void createStartingPlayerState(int baseRangedAttack, int baseAttack, int health, int ammo, int bandages, int gold,
                                  int woundState, int defenceLevel, int bullet, String ability, String meleeFilePath,
                                  String meleeWeaponType, String armorType, double currentGameLevel) {
        playerState = new Player(playerID).setBaseRangedAttack(baseRangedAttack).setBaseAttack(baseAttack)
                .setHealth(health).setAmmo(ammo).setBandage(bandages).setGold(gold).setWoundState(woundState)
                .setDefenceLevel(defenceLevel).setAbility(ability).setMeleeFilePath(meleeFilePath)
                .setMeleeWeaponType(meleeWeaponType).setArmorType(armorType).setCurrentGameLevel(currentGameLevel)
                .setBulletMagazine(bullet);
        trackPlayerState(playerState);
        playerMemento = playerState.createMemento();
        caretaker.addMemento(playerState.getId(), MEMENTO_MESSAGE_INITIAL_STATE, playerMemento);
    }

    /**
     * Adds a new player state to internal memory and at the same time updates current player state that
     * will be used in current game level and for checkpointing
     * @param player entity that is used to extract relevant information to be stored in player state
     * @param gameLevel track which level player is currently at for storage and restoring purposes
     */
    public void addAndUpdatePlayerState(Entity player, double gameLevel) {
        // these are localized variables to that will be used to update and store player's state
        int ammo = player.getComponent(InventoryComponent.class).getAmmo();

        // player is currently reloading when player enters safehouse, assume magazine will be filled
        int bulletMagazine;
        if (player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus()) {
            bulletMagazine = FULL_MAGAZINE;
        } else {
            bulletMagazine = player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine();
        }

        int bandage = player.getComponent(InventoryComponent.class).getBandages();
        int gold = player.getComponent(InventoryComponent.class).getGold();
        int health = player.getComponent(PlayerCombatStatsComponent.class).getHealth();
        if (health != player.getComponent(PlayerCombatStatsComponent.class).getStateMax()) {
            health = player.getComponent(PlayerCombatStatsComponent.class).getStateMax();
        }
        int woundState = player.getComponent(PlayerCombatStatsComponent.class).getWoundState();
        int defenceLevel = player.getComponent(PlayerCombatStatsComponent.class).getDefenceLevel();
        String ability = player.getComponent(PlayerAbilitiesComponent.class).getAbility().toString();
        String meleeFilePath = player.getComponent(PlayerMeleeAttackComponent.class).getWeapon();
        String meleeWeaponType = player.getComponent(PlayerMeleeAttackComponent.class).getMeleeWeaponType().toString();
        String armorType = Items.getArmorType(defenceLevel);
        playerState.setAmmo(ammo).setBandage(bandage).setGold(gold).setHealth(health).setWoundState(woundState)
                .setDefenceLevel(defenceLevel).setAbility(ability).setMeleeFilePath(meleeFilePath)
                .setMeleeWeaponType(meleeWeaponType).setArmorType(armorType).setCurrentGameLevel(gameLevel)
                .setBulletMagazine(bulletMagazine);

        trackPlayerState(playerState);
        playerMemento = playerState.createMemento();

        // message will change when player progresses in game - must be unique
        String mementoMessageOtherLevels = "Player state at level " + gameLevel;
        caretaker.addMemento(playerState.getId(), mementoMessageOtherLevels, playerMemento);
    }

    /**
     * Used specifically for logging purposes and debugging. Will be useful to track player's current state
     * @param playerState print most recent player state to logging
     */
    public String trackPlayerState(Player playerState) {
        logger.info(playerState.toString());
        return playerState.toString();
    }

    /**
     * Reverts player state to the first player state used when game was started and removes all other player
     * state stored previously
     */
    public void restorePlayerState() {
        PlayerMemento initialPlayerMemento = caretaker.getMemento(playerState.getId(), MEMENTO_MESSAGE_INITIAL_STATE);
        playerState.restore(initialPlayerMemento);
    }

    /**
     * Reverts player state to the most recent safehouse visited before current level where player died. This
     * method will only be called when player dies in levels that are not in safehouse. This is assuming player
     * cannot die in safehouse and should not be able to anyway. This function is not meant to be used when or if
     * player dies in safehouse - and this should not happen either way which is not the intention of the game
     */
    public void restorePlayerStateToClosestSafehouse() {
        if (playerState.getCurrentGameLevel() > LEVEL_ONE) {
            double safehouseLevel = playerState.getCurrentGameLevel() - LEVEL_INCREMENT;
            String mementoMessageSafehouse = MEMENTO_MESSAGE_CHECKPOINT + safehouseLevel;
            PlayerMemento safehousePlayerMemento = caretaker.getMemento(playerState.getId(), mementoMessageSafehouse);
            playerState.restore(safehousePlayerMemento);
        }
    }
}
