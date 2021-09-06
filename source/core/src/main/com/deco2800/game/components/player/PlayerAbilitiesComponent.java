package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ability component for specific player abilities, utilises triggers to trigger effects within other components.
 * Only the player should have an instance of this class.
 */
public class PlayerAbilitiesComponent extends Component {
    private final GameTime timeSource = ServiceLocator.getTimeSource();
    private static final Logger logger = LoggerFactory.getLogger(PlayerAbilitiesComponent.class);
    private static final int delayLength = 60000; // in milliseconds
    private static final long dashLength = 350; // in milliseconds
    private long delayEndTime = 0;
    private int ability;

    /**
     * Basic constructor for setting the players chosen ability
     * @param ability is the ability state to set the player to
     */
    public PlayerAbilitiesComponent (int ability) {
        setAbility(ability);
    }

    @Override
    public void create() {
        entity.getEvents().addListener("tryAbility", this::triggerAbility);
    }

    /**
     * Changes the players chosen ability through int code
     * @param ability is int indicator the chosen ability of the player (must be between 0 and 3)
     */
    public void setAbility(int ability) {
        if(ability < 0 || ability > 3) {
            logger.error("Invalid ability indicator");
        } else {
            this.ability = ability;
        }
    }

    /**
     * Returns an int code indicating the players chosen ability
     */
    public int getAbility() {
        return ability;
    }

    /**
     * Attempts to trigger the players ability
     */
    void triggerAbility() {
        if (timeSource.getTime() >= delayEndTime) {
            delayEndTime = timeSource.getTime() + delayLength;
            switch (this.ability) {
                case 0:
                    entity.getEvents().trigger("longDash", dashLength+timeSource.getTime());
                case 1:
                    entity.getEvents().trigger("shortInvincibility");
                case 2:
                    entity.getEvents().trigger("knockback");
                default:
                    entity.getEvents().trigger("rangedAOE");
            }
        }
    }
}
