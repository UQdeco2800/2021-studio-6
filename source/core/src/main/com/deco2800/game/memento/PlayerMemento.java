package com.deco2800.game.memento;

public class PlayerMemento {
    protected int ammo, gold, bandage, defenceLevel, woundState, baseAttack, baseRangedAttack, health, id,
            currentGameLevel;
    protected String ability, meleeWeaponType, armorType, meleeFilePath;

    public PlayerMemento(int id, int ammo, int gold, int bandage, int defenceLevel, int woundState,
                         int baseRangedAttack, int baseAttack, int health, String ability, String meleeFilePath,
                         String meleeWeaponType, String armorType) {
        super();
        this.id = id;
        this.ammo = ammo;
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
    }

    public int getId() {
        return id;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getGold() {
        return gold;
    }

    public int getBandage() {
        return bandage;
    }

    public int getDefenceLevel() {
        return defenceLevel;
    }

    public int getWoundState() {
        return woundState;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseRangedAttack() {
        return baseRangedAttack;
    }

    public int getHealth() {
        return this.health;
    }

    public int getCurrentGameLevel() {
        return this.currentGameLevel;
    }

    public String getAbility() {
        return this.ability;
    }

    public String getMeleeWeaponType() {
        return this.meleeWeaponType;
    }

    public String getArmorType() {
        return this.armorType;
    }

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
}
