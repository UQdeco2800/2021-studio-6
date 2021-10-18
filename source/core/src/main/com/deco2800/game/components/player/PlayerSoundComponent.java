package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.deco2800.game.components.Component;

/**
 * This component controls the sound effects for the Player, their abilities and item usage.
 */
public class PlayerSoundComponent extends Component {
    private float volume;           // Global Volume for Player Sound Effects.

    // Player sounds.
    private Sound useBandage;
    private Sound wounded;
    private Sound genericItemPickup;

    /**
     * Initialize component with specified volume.
     * @param volume volume [0, 1] to set for all player sound effects.
     */
    public PlayerSoundComponent(float volume) {
        this.setVolume(volume);
    }

    /**
     * Initialize component with 50% volume.
     */
    public PlayerSoundComponent() {
        this.setVolume(0.5f);
    }

    /**
     * Set the volume of all player sound effects.
     * @param volume volume [0,1] to set for all player sound effects.
     */
    public void setVolume(float volume) { this.volume = volume < 0f ? 0f : volume > 1.0f ? 1.0f : volume; }


    public void setUseBandage(Sound useBandage) { this.useBandage = useBandage; }

    public void setWounded(Sound wounded) { this.wounded = wounded; }

    public void setGenericItemPickup(Sound genericItemPickup) { this.genericItemPickup = genericItemPickup; }


    public void playUseBandage() {
        if (this.useBandage != null) {
            long soundId = this.useBandage.play();
            this.useBandage.setVolume(soundId, this.volume);
        }
    }

    public void playWounded() {
        if (this.wounded != null) {
            long soundId = this.wounded.play();
            this.wounded.setVolume(soundId, this.volume);
        }
    }

    public void playGenericItemPickup() {
        if (this.genericItemPickup != null) {
            long soundId = this.genericItemPickup.play();
            this.genericItemPickup.setVolume(soundId, this.volume);
        }
    }

    //TODO: Add More Sound Effects.


}  // END: PlayerSoundComponent
