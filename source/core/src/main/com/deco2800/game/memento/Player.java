package com.deco2800.game.memento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Originator which is used to store player state internally. Originator has a method which will create a memento
 * object to store its internal state and this memento object is then added to a history list of all memento objects
 * created in the past. It is able to restore itself to a past originator if needed to restore a player state
 */
public class Player {
    private static final Logger logger = LoggerFactory.getLogger(Player.class);
    protected int id;
    protected int ammo, gold, bandage, defenceLevel, woundState, baseAttack, baseRangedAttack, health;
    protected double currentGameLevel;
    protected String ability, meleeWeaponType, armorType, meleeFilePath;

    /**
     * Creates an originator with a unique ID.
     * @param id unique to a specific originator
     */
    public Player(int id) {
        super();
        this.id = id;
    }

    /**
     * Returns id of originator that is unique
     * @return id linked to originator created
     */
    public int getId() {
        return id;
    }

    /**
     * Ammo of current player state
     * @return ammo number in player's inventory
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * Modify ammo count of current player state
     * @param ammo number to set
     */
    public Player setAmmo(int ammo) {
        this.ammo = ammo;
        return this;
    }

    /**
     * Gold of current player state
     * @return gold number in player's inventory
     */
    public int getGold() {
        return gold;
    }

    /**
     * Modify gold count of current player state
     * @param gold number to set
     */
    public Player setGold(int gold) {
        this.gold = gold;
        return this;
    }

    /**
     * Bandage of current player state
     * @return Bandage number in player's inventory
     */
    public int getBandage() {
        return bandage;
    }

    /**
     * Modify bandage count of current player state
     * @param bandage number to set
     */
    public Player setBandage(int bandage) {
        this.bandage = bandage;
        return this;
    }

    /**
     * Defence level of current player state
     * @return defence level of player - corresponds to armor being worn by player
     */
    public int getDefenceLevel() {
        return defenceLevel;
    }

    /**
     * Modify defence level of current player state
     * @param defenceLevel number to set
     */
    public Player setDefenceLevel(int defenceLevel) {
        this.defenceLevel = defenceLevel;
        return this;
    }

    /**
     * Wound state of current player state
     * @return wound state of player which has different health number for each
     */
    public int getWoundState() {
        return woundState;
    }

    /**
     * Modify wound state of current player state
     * @param woundState number to set
     */
    public Player setWoundState (int woundState) {
        this.woundState = woundState;
        return this;
    }

    /**
     * Base attack of player state
     * @return base melee attack of player
     */
    public int getBaseAttack() {
        return baseAttack;
    }

    /**
     * Modify melee base attack of current player state
     * @param baseAttack number to set
     */
    public Player setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
        return this;
    }

    /**
     * Get base range attack of player state
     * @return base range attack of player
     */
    public int getBaseRangedAttack() {
        return baseRangedAttack;
    }

    /**
     * Modify ranged base attack of current player state
     * @param baseRangedAttack number to set
     */
    public Player setBaseRangedAttack(int baseRangedAttack) {
        this.baseRangedAttack = baseRangedAttack;
        return this;
    }

    /**
     * Modify health of current player state
     * @param health number to set
     */
    public Player setHealth(int health) {
        this.health = health;
        return this;
    }

    /**
     * Get health of current player state
     * @return health of current player state
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Modify current game level of current player state
     * @param level number to set
     */
    public Player setCurrentGameLevel(double level) {
        this.currentGameLevel = level;
        return this;
    }

    /**
     * Get current game level of current player state
     * @return game level that current player state is currently at
     */
    public double getCurrentGameLevel() {
        return this.currentGameLevel;
    }

    /**
     * Modify abilities of current player state
     * @param ability type to set for player state
     */
    public Player setAbility(String ability) {
        this.ability = ability;
        return this;
    }

    /**
     * Get ability of current player state
     * @return ability of player state currently
     */
    public String getAbility() {
        return this.ability;
    }

    /**
     * Modify melee weapon type of current player state
     * @param meleeWeaponType type to set for player state
     */
    public Player setMeleeWeaponType(String meleeWeaponType) {
        this.meleeWeaponType = meleeWeaponType;
        return this;
    }

    /**
     * Get melee weapon type of current player state
     * @return type of melee weapon type player is holding for this state
     */
    public String getMeleeWeaponType() {
        return this.meleeWeaponType;
    }

    /**
     * Modify armor type of current player state
     * @param armorType type to set for player state
     */
    public Player setArmorType(String armorType) {
        this.armorType = armorType;
        return this;
    }

    /**
     * Get armor type of current player state
     * @return armor type player is holding for this state
     */
    public String getArmorType() {
        return this.armorType;
    }

    /**
     * Modify melee config file path used for melee weapons
     * @param meleeFilePath to config files for melee weapon
     */
    public Player setMeleeFilePath(String meleeFilePath) {
        this.meleeFilePath = meleeFilePath;
        return this;
    }

    /**
     * Get file path to melee weapons used in player state
     * @return file path to melee weapon config files
     */
    public String getMeleeFilePath() {
        return this.meleeFilePath;
    }

    @Override
    public String toString() {
        return "Player [ID = " + id + "], ammo = " + ammo + ", gold = " + gold + ", bandage = " + bandage + ", defenceLevel = "
                + defenceLevel + ", woundState = " + woundState + ", baseAttack = " + baseAttack +
                ", baseRangedAttack = " + baseRangedAttack + ", health = " + health + "\n abiliy: " +
                ability + ", meleeWeaponType: " + meleeWeaponType + ", armorType: " + armorType + ", meleeFilePath: " +
                meleeFilePath + "\n Player is currently at level " + currentGameLevel;
    }

    /**
     * Creates memento that stores originator internally
     * @return player memento object that has information on originator
     */
    public PlayerMemento createMemento() {
        return new PlayerMemento(id, ammo, gold, bandage, defenceLevel, woundState, baseRangedAttack, baseAttack,
                health, ability, meleeFilePath, meleeWeaponType, armorType, currentGameLevel);
    }

    /**
     * Restores originator to past originator created previously
     * @param memento object that will be used to restore originator
     */
    public void restore(PlayerMemento memento) {
        if (memento != null) {
            this.id = memento.id;
            this.ammo = memento.ammo;
            this.gold = memento.gold;
            this.bandage = memento.bandage;
            this.defenceLevel = memento.defenceLevel;
            this.woundState = memento.woundState;
            this.baseRangedAttack = memento.baseRangedAttack;
            this.baseAttack = memento.baseAttack;
            this.health = memento.health;
            this.ability = memento.ability;
            this.meleeFilePath = memento.meleeFilePath;
            this.meleeWeaponType = memento.meleeWeaponType;
            this.armorType = memento.armorType;
            this.currentGameLevel = memento.currentGameLevel;
        } else {
            logger.info("Can't restore without memento object");
        }
    }
}
