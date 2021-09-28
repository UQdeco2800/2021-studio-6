package com.deco2800.game.memento;

/**
 * Basic java memento object that is used as a storage to store player state. In general this object is immutable and
 * it holds the internal state of hte originator and allows the originator to restore itself to an older version of
 * itself
 */
public class PlayerMemento {
    protected int ammo, gold, bandage, defenceLevel, woundState, baseAttack, baseRangedAttack, health, id, bulletMagazine;
    protected double currentGameLevel;
    protected String ability, meleeWeaponType, armorType, meleeFilePath;

    /**
     * Creates a player memento object to be stored internally
     *
     * @param id that is unique and linked to originator created
     * @param baseRangedAttack amount of damage dealt by player via range
     * @param baseAttack amount of dadmage dealt by player via melee
     * @param health health of player in current state
     * @param ammo amount of ammo used for rnage attack
     * @param bandage used to heal player's wound state
     * @param gold amount of gold used to purchase items at player's current state
     * @param woundState wound state based on player's current state
     * @param defenceLevel of player in current state
     * @param currentGameLevel current game level of player's current state
     * @param ability being used in player's current state
     * @param meleeFilePath loads data relevant to melee weapon which will correspond to player's current state
     * @param meleeWeaponType weapon type of current player's state
     * @param armorType armor type of current player's state
     */
    public PlayerMemento(int id, int ammo, int gold, int bandage, int defenceLevel, int woundState,
                         int baseRangedAttack, int baseAttack, int health, int bulletMagazine,
                         String ability, String meleeFilePath, String meleeWeaponType, String armorType,
                         double currentGameLevel) {
        super();
        this.id = id;
        this.ammo = ammo;
        this.bulletMagazine = bulletMagazine;
        this.gold = gold;
        this.bandage = bandage;
        this.defenceLevel = defenceLevel;
        this.woundState = woundState;
        this.baseRangedAttack = baseRangedAttack;
        this.baseAttack = baseAttack;
        this.health = health;
        this.ability = ability;
        this.meleeFilePath = meleeFilePath;
        this.meleeWeaponType = meleeWeaponType;
        this.armorType = armorType;
        this.currentGameLevel = currentGameLevel;
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
     * Bullet magazine of current player state
     * @return bullet number in player's magazine count
     */
    public int getBulletMagazine() {
        return bulletMagazine;
    }

    /**
     * Gold of current player state
     * @return gold number in player's inventory
     */
    public int getGold() {
        return gold;
    }

    /**
     * Bandage of current player state
     * @return Bandage number in player's inventory
     */
    public int getBandage() {
        return bandage;
    }

    /**
     * Defence level of current player state
     * @return defence level of player - corresponds to armor being worn by player
     */
    public int getDefenceLevel() {
        return defenceLevel;
    }

    /**
     * Wound state of current player state
     * @return wound state of player which has different health number for each
     */
    public int getWoundState() {
        return woundState;
    }

    /**
     * Base attack of player state
     * @return base melee attack of player
     */
    public int getBaseAttack() {
        return baseAttack;
    }

    /**
     * Get base range attack of player state
     * @return base range attack of player
     */
    public int getBaseRangedAttack() {
        return baseRangedAttack;
    }

    /**
     * Get health of current player state
     * @return health of current player state
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Get current game level of current player state
     * @return game level that current player state is currently at
     */
    public double getCurrentGameLevel() {
        return this.currentGameLevel;
    }

    /**
     * Get ability of current player state
     * @return ability of player state currently
     */
    public String getAbility() {
        return this.ability;
    }

    /**
     * Get melee weapon type of current player state
     * @return type of melee weapon type player is holding for this state
     */
    public String getMeleeWeaponType() {
        return this.meleeWeaponType;
    }

    /**
     * Get armor type of current player state
     * @return armor type player is holding for this state
     */
    public String getArmorType() {
        return this.armorType;
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
        return "Player [ID = " + id + "], ammo = " + ammo + ", magazine = " + bulletMagazine + ", gold = " + gold +
                ", bandage = " + bandage + ", defenceLevel = " + defenceLevel + ", woundState = " + woundState +
                ", baseAttack = " + baseAttack + ", baseRangedAttack = " + baseRangedAttack + ", health = " + health +
                "\n abiliy: " + ability + ", meleeWeaponType: " + meleeWeaponType + ", armorType: " + armorType +
                ", meleeFilePath: " + meleeFilePath + "\n Player is currently at level " + currentGameLevel;
    }
}
