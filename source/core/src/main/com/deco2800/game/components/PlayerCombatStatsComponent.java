package com.deco2800.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Component used to store information related to combat for the player such as
 * health, attack, etc. Only the player character should have an instance of
 * this class registered. This class can be extended for more specific combat needs.
 */
public class PlayerCombatStatsComponent extends CombatStatsComponent {

    private static final Logger logger = LoggerFactory.getLogger(PlayerCombatStatsComponent.class);
    private int baseAttack;
    private int woundState;
    private int stateHealth;
    private final int woundMax = 3;
    private int stateMax;
    private int[] stateGates = new int[] {0, 5, 4, 3};

    public PlayerCombatStatsComponent(int health, int baseAttack) {
        setWoundState(health);
        setBaseAttack(baseAttack);
    }

    /**
     * Returns true if the player's has 0 wound state, otherwise false.
     *
     * @return is player dead
     */
    public Boolean isDead() {
        return woundState == 0;
    }

    /**
     * Returns true if the player's wound state is at max value.
     *
     * @return is player full wound state
     */
    public Boolean atMax() {
        return woundState == woundMax;
    }

    /**
     * Returns the player's wound state.
     *
     * @return player's wound state
     */
    public int getWoundState() {
        return woundState;
    }

    /**
     * Sets the player's wound state. Wound state has a min bound of 0 and max of 3.
     *
     * @param wound wound state
     */
    public void setWoundState(int wound) {
        if (wound > 0) {
            this.woundState = wound;
            if (getWoundState() > woundMax) {
                this.woundState = woundMax;
            }
            setStateMax(stateGates[getWoundState()]);
        } else {
            this.woundState = 0;
            setStateMax(0);
        }
        if (entity != null) {
            entity.getEvents().trigger("updateWound", this.woundState);
        }
    }


    /**
     * Returns the player's state health max value.
     *
     * @return player's state health max value
     */
    public int getStateMax() {
        return stateMax;
    }


    /**
     * Sets the player's state health max value.
     *
     * @param newMax state max value
     */
    public void setStateMax(int newMax) {
        stateMax = newMax;
        setStateHealth(getStateMax());
    }

    /**
     * Returns the player's state health.
     *
     * @return player's state health
     */
    public int getStateHealth() {
        return stateHealth;
    }

    /**
     * Sets the player's health state. Caps new health state at current state max
     * and will reduce the player's wound state if their state health runs out.
     *
     * @param health state health
     */
    public void setStateHealth(int health) {
        if (health > 0) {
            this.stateHealth = health;
            if (getStateHealth() > getStateMax()) {
                this.stateHealth = getStateMax();
            }
        } else {
            this.stateHealth = 0;
            if (getWoundState() != 0) {
                setWoundState(getWoundState() - 1);
            }
        }
        if (entity != null) {
            entity.getEvents().trigger("updateHealth", this.stateHealth);
        }
    }

    /**
     * Returns the player's base attack damage.
     *
     * @return base attack damage
     */
    public int getBaseAttack() {
        return baseAttack;
    }

    /**
     * Sets the player's attack damage. Attack damage has a minimum bound of 0.
     *
     * @param attack Attack damage
     */
    public void setBaseAttack(int attack) {
        if (attack >= 0) {
            this.baseAttack = attack;
        } else {
            logger.error("Can not set base attack to a negative attack value");
        }
    }

    /**
     * Calculates taking damage from an enemy. Currently reduces health by
     * enemy attack value but will include player's defence state when implemented.
     *
     * @param attacker Enemy attacker
     */
    public void hit(CombatStatsComponent attacker) {
        int newHealth = getStateHealth() - attacker.getBaseAttack();
        setStateHealth(newHealth);
    }
}

