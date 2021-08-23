

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
    private int baseRangedAttack;
    private int woundState;
    private int stateMax;
    private int defenceLevel;
    private final int woundMax = 3;
    private int[] stateGates = new int[] {0, 5, 4, 10};
    private final double[] attackModifiers = new double[] {0, 0.6, 0.9, 1};
    private final double[] defenceModifiers = new double[] {1, 0.9, 0.8, 0};

    public PlayerCombatStatsComponent(int health, int baseAttack, int woundState, int baseRangedAttack, int defenceLevel) {
        super(health, baseAttack); // Sets initial health/baseAttack in parent
        setWoundState(woundState);
        setHealth(health); // overrides the parents setting of health
        setBaseRangedAttack(baseRangedAttack);
        setDefenceLevel(defenceLevel);
    }

    /**
     * Returns true if the player's has 0 wound state, otherwise false.
     *
     * @return is player dead
     */
    @Override
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
     * Returns the entity's melee attack damage, accounting for wound state
     *
     * @return attack damage
     */
    public int getMeleeAttack() {
        return (int) Math.round(super.getBaseAttack() * attackModifiers[woundState]);
    }

    /**
     * Sets the entity's ranged attack damage. Attack damage has a minimum bound of 0.
     *
     * @param attack Attack damage
     */
    public void setBaseRangedAttack(int attack) {
        if (attack >= 0) {
            this.baseRangedAttack = attack;
        } else {
            logger.error("Can not set base attack to a negative ranged attack value");
        }
    }

    /**
     * Returns the entity's base attack damage for ranged attacks.
     *
     * @return base attack damage
     */
    public int getBaseRangedAttack() {
        return baseRangedAttack;
    }

    /**
     * Returns the entity's melee attack damage, accounting for wound state
     *
     * @return attack damage
     */
    public int getRangedAttack() {
        return (int) Math.round(baseRangedAttack * attackModifiers[woundState]);
    }

    /**
     * Sets the entity's defence level. Must be between 0 and 2 inclusive
     *
     * @param level the level to set it to
     */
    public void setDefenceLevel(int level) {
        if (level >= 0 && level <=2) {
            this.defenceLevel= level;
        } else {
            logger.error("Can not set defence level outside 0-2");
        }
    }

    /**
     * Returns the entity's base attack damage for ranged attacks.
     *
     * @return base attack damage
     */
    public int getDefenceLevel() {
        return defenceLevel;
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
     * Sets the player's health state. Caps new health state at current state max
     * and will reduce the player's wound state if their state health runs out.
     *
     * @param health state health
     */
    @Override
    public void setHealth(int health) {
        if (health > 0) {
            if (health > getStateMax()) {
                super.setHealth(getStateMax()); // event triggered in the supers set health
            } else {
                super.setHealth(health);
            }
        } else {
            super.setHealth(0);
            if (getWoundState() != 0) {
                setWoundState(getWoundState() - 1);
            }
        }
    }

    /**
     * Sets the player's state health max value.
     *
     * @param newMax state max value
     */
    public void setStateMax(int newMax) {
        stateMax = newMax;
        setHealth(getStateMax());
    }

    /**
     * Calculates taking damage from an enemy. Reduces health by
     * enemy attack value but will include player's defence state when implemented.
     *
     * @param attacker Enemy attacker
     */
    public void hit(CombatStatsComponent attacker) {
        int newHealth = getHealth() - ((int) Math.round(attacker.getBaseAttack() * defenceModifiers[this.defenceLevel]));
        setHealth(newHealth);
    }
}

