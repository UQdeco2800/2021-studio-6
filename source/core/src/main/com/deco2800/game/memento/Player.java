package com.deco2800.game.memento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player {
    private static final Logger logger = LoggerFactory.getLogger(Player.class);
    protected int id;
    protected int ammo, gold, bandage, defenceLevel, woundState, baseAttack, baseRangedAttack, health;
    protected String ability, meleeWeaponType, armorType, meleeFilePath;

    public Player(int id) {
        super();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getAmmo() {
        return ammo;
    }

    public Player setAmmo(int ammo) {
        this.ammo = ammo;
        return this;
    }

    public int getGold() {
        return gold;
    }

    public Player setGold(int gold) {
        this.gold = gold;
        return this;
    }

    public int getBandage() {
        return bandage;
    }

    public Player setBandage(int bandage) {
        this.bandage = bandage;
        return this;
    }

    public int getDefenceLevel() {
        return defenceLevel;
    }

    public Player setDefenceLevel(int defenceLevel) {
        this.defenceLevel = defenceLevel;
        return this;
    }

    public int getWoundState() {
        return woundState;
    }

    public Player setWoundState (int woundState) {
        this.woundState = woundState;
        return this;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public Player setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
        return this;
    }

    public int getBaseRangedAttack() {
        return baseRangedAttack;
    }

    public Player setBaseRangedAttack(int baseRangedAttack) {
        this.baseRangedAttack = baseRangedAttack;
        return this;
    }

    public Player setHealth(int health) {
        this.health = health;
        return this;
    }

    public int getHealth() {
        return this.health;
    }

    public Player setAbility(String ability) {
        this.ability = ability;
        return this;
    }

    public String getAbility() {
        return this.ability;
    }

    public Player setMeleeWeaponType(String meleeWeaponType) {
        this.meleeWeaponType = meleeWeaponType;
        return this;
    }

    public String getMeleeWeaponType() {
        return this.meleeWeaponType;
    }

    public Player setArmorType(String armorType) {
        this.armorType = armorType;
        return this;
    }

    public String getArmorType() {
        return this.armorType;
    }

    public Player setMeleeFilePath(String meleeFilePath) {
        this.meleeFilePath = meleeFilePath;
        return this;
    }

    public String getMeleeFilePath() {
        return this.meleeFilePath;
    }

    @Override
    public String toString() {
        return "Player [ID = " + id + ", ammo = " + ammo + ", gold = " + gold + " bandage = " + bandage + " defenceLevel = "
                + defenceLevel + " woundState = " + woundState;
    }

    public PlayerMemento createMemento() {
        return new PlayerMemento(id, ammo, gold, bandage, defenceLevel, woundState, baseRangedAttack, baseAttack,
                health, ability, meleeFilePath, meleeWeaponType, armorType);
    }

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
        } else {
            logger.info("Can't restore without memento object");
        }
    }
}
