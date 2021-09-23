package com.deco2800.game.memento;

import java.util.ArrayList;
import java.util.List;

public class PlayerStateManager {
    private static PlayerStateManager manager = null;
    private static final PlayerCaretaker caretaker = new PlayerCaretaker();
    private static Player playerState = null;
    private static PlayerMemento playerMemento = null;

    // this id is used to track player - can be extended to track different objects in game
    private static final int playerID = 0;

    // used to track which level player is currently at
    private static int playerCurrentLevel = 0;

    // used to track player's different states during game
    private static List mementoMessages = new ArrayList();

    public static PlayerStateManager getInstance() {
        if (manager == null) {
            manager = new PlayerStateManager();
        }
        return manager;
    }

    public Player currentPlayerState() {
        return playerState;
    }

    public void createPlayerState(int baseRangedAttack, int baseAttack, int health, int ammo, int bandages, int gold,
                                  int woundState, int defenceLevel, int currentGameLevel,String ability, String meleeFilePath,
                                  String meleeWeaponType, String armorType) {
        playerState = new Player(playerID).setBaseRangedAttack(baseRangedAttack).setBaseAttack(baseAttack)
                .setHealth(health).setAmmo(ammo).setBandage(bandages).setGold(gold).setWoundState(woundState)
                .setDefenceLevel(defenceLevel).setAbility(ability).setMeleeFilePath(meleeFilePath)
                .setMeleeWeaponType(meleeWeaponType).setArmorType(armorType).setCurrentGameLevel(currentGameLevel);
        playerMemento = playerState.createMemento();
    }


}
