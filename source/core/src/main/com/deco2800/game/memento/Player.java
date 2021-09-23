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

    public int setAmmo(int ammo) {
        return this.ammo = ammo;
    }

    public int getGold() {
        return gold;
    }

    public int setGold(int gold) {
        return this.gold = gold;
    }

    public int getBandage() {
        return bandage;
    }

    public int setBandage(int bandage) {
        return this.bandage = bandage;
    }

    public int getDefenceLevel() {
        return defenceLevel;
    }

    public int setDefenceLevel(int defenceLevel) {
        return this.defenceLevel = defenceLevel;
    }

    public int getWoundState() {
        return woundState;
    }

    public int setWoundState(int woundState) {
        return this.woundState = woundState;
    }

    @Override
    public String toString() {
        return "Player [ID = " + id + ", ammo = " + ammo + ", gold = " + gold + " bandage = " + bandage + " defenceLevel = "
                + defenceLevel + " woundState = " + woundState;
    }

    public PlayerMemento createMemento() {
        return new PlayerMemento(id, ammo, gold, bandage, defenceLevel, woundState);
    }

    public void restore(PlayerMemento memento) {
        if (memento != null) {
            this.id = memento.id;
            this.ammo = memento.ammo;
            this.gold = memento.gold;
            this.bandage = memento.bandage;
            this.defenceLevel = memento.defenceLevel;
            this.woundState = memento.woundState;
        } else {
            logger.info("Can't restore without memento object");
        }
    }
}
