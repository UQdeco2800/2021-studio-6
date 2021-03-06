package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.items.Abilities;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * Ability component for specific player abilities, utilises triggers to trigger effects within other components.
 * Only the player should have an instance of this class.
 */
public class PlayerAbilitiesComponent extends Component {
    private final GameTime timeSource = ServiceLocator.getTimeSource();
    private static final int DELAY_LENGTH = 20000; // in milliseconds
    private long delayEndTime = 0;
    private Abilities ability;
    // Ability Specific Variables
    private static final long DASH_LENGTH = 200; // in milliseconds
    private static final long EXPLOSION_DELAY_LENGTH = 2000; // in milliseconds
    private static final long INVINCIBILITY_LENGTH = 3000;
    private static final long COOLDOWN_REDUCTION = 2000; // in milliseconds
    private IndependentAnimator invincibiltyAnimation;
    /**
     * Basic constructor for setting the players chosen ability
     * @param ability is the ability state to set the player to
     */
    public PlayerAbilitiesComponent (Abilities ability) {
        setAbility(ability);
    }

    /**
     * Adds the listener for triggering the ability alongside other creation of components
     */
    @Override
    public void create() {
        entity.getEvents().addListener("tryAbility", this::triggerAbility);
    }

    /**
     * Changes the players chosen ability through int code
     * @param ability is the chosen ability of the player
     */
    public void setAbility(Abilities ability) {
        this.ability = ability;
    }

    /**
     * Returns the players chosen ability
     * @return returns the ability
     */
    public Abilities getAbility() {
        return ability;
    }

    /**
     * Attempts to trigger the players ability
     *
     * @param direction Used for the implementation of abilities
     */
    void triggerAbility(Vector2 direction) {
        if (ability != Abilities.NONE && (ability != Abilities.LONG_DASH || !direction.isZero()) && (timeSource.getTime() >= delayEndTime)) { // ensuring that abilities which require movement have it
            delayEndTime = timeSource.getTime() + DELAY_LENGTH;
            entity.getEvents().trigger("abilityCooldown");
            switch (ability) {
                case LONG_DASH:
                    entity.getEvents().trigger("longDash", DASH_LENGTH+timeSource.getTime());
                    break;
                case INVINCIBILITY:
                    entity.getEvents().trigger("invincibility", INVINCIBILITY_LENGTH);
                    break;
                case FIRE_CRACKER:
                    entity.getEvents().trigger("fireCracker", EXPLOSION_DELAY_LENGTH);
                // default not required as all set enums should have function in switch
            }
        }
    }

    /**
     * Used to reduce ability cooldown when you kill an enemy
     */
    void reduceCooldown() {
        entity.getEvents().trigger("jumpAnimation");
        this.delayEndTime -= COOLDOWN_REDUCTION;
    }
}
