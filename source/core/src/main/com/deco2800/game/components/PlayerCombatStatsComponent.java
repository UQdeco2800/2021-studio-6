package com.deco2800.game.components;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
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
    private static final int woundMax = 3;
    // Modifiers
    private final int[] stateGates = new int[] {0, 5, 4, 3};
    private final double[] attackModifiers = new double[] {0, 0.6, 0.9, 1};
    // Regeneration/Invincibility timer
    private final GameTime timeSource = ServiceLocator.getTimeSource();
    private boolean regenActive = false;
    private boolean invincibleActive = false;
    private static final long regenCooldown = 5000;
    private static final long initialRegenOffset = 3000;
    private long nextRegen;
    private static final long invincibilityLength = 400; // in ms
    private long invincibilityEndTime;
    private final int[] woundex = new int[] {7, 3, 0};
    private final int[] statex = new int[] {1, 2, 3, 4, 5};

    public PlayerCombatStatsComponent(int health, int baseAttack, int woundState, int baseRangedAttack, int defenceLevel) {
        super(health, baseAttack); // Sets initial health/baseAttack in parent
        setWoundState(woundState);
        setHealth(health); // overrides the parents setting of health
        setBaseRangedAttack(baseRangedAttack);
        setDefenceLevel(defenceLevel);
    }

    /**
     * Linking into update for player regeneration and disabling invincibility frames
     */
    @Override
    public void update() {
        if (regenActive && (timeSource.getTime() >= nextRegen)) {
            regenerate();
        }
        if (invincibleActive && (timeSource.getTime() >= invincibilityEndTime)) {
            invincibleActive = false;
        }
    }

    /**
     * Allowing invincibility to be called without knowing about this classF
     */
    @Override
    public void create() {
        entity.getEvents().addListener("invincibility", this::invincibleStart);
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
     * This unwieldy function is used to communicate with the health interface
     * to give it the necessary health index for the current state. Will
     * hopefully be improved later on.
     * @return the int relating to the health animation index
     */
    public int getindex() {
        if (woundState == 0) {
            return 13;
        }
        int woundIndex = woundex[getWoundState() - 1];
        int healthIndex = statex[getStateMax() - getHealth()];
        return woundIndex + healthIndex;
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
            if (this.getEntity() != null && ServiceLocator.getGameArea() != null) {
                entity.getEvents().trigger("dispose");
                ServiceLocator.getGameArea().despawnEntity(entity);
            }
        }
        if (entity != null) {
            // update player HUD interface when bandage is used to improve wound state
            entity.getEvents().trigger("updateWound", this.woundState);
            entity.getEvents().trigger("health", getindex());
            if (woundState == 0) {
                entity.getEvents().trigger("dead");
            }
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
            super.setHealth(Math.min(health, getStateMax())); // event triggered in the supers set health
        } else {
            super.setHealth(0);
            if (getWoundState() != 0) {
                setWoundState(getWoundState() - 1);
            }
        }
        if (getHealth() != getStateMax()) {
            regenStart();
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
     * (enemy attack value - player defence value).
     *
     * Gives the player momentary invincibility after getting hit for 0.5
     * seconds. Also starts player health regeneration if the player does not
     * currently have full state health. If the player gets hit again before
     * regenerating health, the previous regen is cancelled and reset.
     *
     * @param attacker Enemy attacker
     */
    public void hit(CombatStatsComponent attacker) {
        if (!invincibleActive) {
            if (regenActive) {
                regenActive = false;
            }
            int damage = attacker.getBaseAttack() - this.defenceLevel;
            if (damage <= 0) {
                damage = 1;
            }
            int newHealth = getHealth() - damage;
            setHealth(newHealth);
            if (getHealth() != getStateMax()) {
                regenStart();
            }
            entity.getEvents().trigger("hurt");
            entity.getEvents().trigger("health", getindex());
            invincibleStart(invincibilityLength);
        }
    }

    /**
     * Starts health regeneration that increases player's state health
     * periodically until it reaches max, at which point it stops itself.
     */
    private void regenStart() {
        regenActive = true;
        entity.getEvents().trigger("heal");
        nextRegen = timeSource.getTime() + regenCooldown + initialRegenOffset; // Extra 3 second so it takes longer to start regen
    }

    /**
     * Sets the invincibility check to momentarily prevent player damage
     *
     * @param length parameter for how long to set inivisibility for (in milliseconds)
     */
    public void invincibleStart(long length) {
        invincibilityEndTime = timeSource.getTime() + length;
        invincibleActive = true;
    }

    /**
     * Increases the players current health, unless health is full for their wound state
     */
    private void regenerate() {
        if (!timeSource.isPaused()) {
            setHealth(getHealth() + 1);
            entity.getEvents().trigger("health", getindex());
            if (getHealth() >= getStateMax()) {
                regenActive = false;
            }
            nextRegen = timeSource.getTime() + regenCooldown;
        }
    }

}

