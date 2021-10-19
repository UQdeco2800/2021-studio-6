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
    private Sound swingAxe;
    private Sound swingSword;
    private Sound swingDagger;
    private Sound shoot;
    private Sound dash;

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

    public void setSwingAxe(Sound swingAxe) { this.swingAxe = swingAxe; }

    public void setSwingSword(Sound swingSword) { this.swingSword = swingSword; }

    public void setSwingDagger(Sound swingDagger) { this.swingDagger = swingDagger; }

    public void setShoot(Sound shoot) { this.shoot = shoot; }

    public void setDash(Sound dash) { this.dash = dash; }


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

    public void playSwingAxe() {
        if (this.swingAxe != null) {
            long soundId = this.swingAxe.play();
            this.swingAxe.setVolume(soundId, this.volume);
        }
    }

    public void playSwingSword() {
        if (this.swingSword != null) {
            long soundId = this.swingSword.play();
            this.swingSword.setVolume(soundId, this.volume);
        }
    }

    public void playSwingDagger() {
        if (this.swingDagger != null) {
            long soundId = this.swingDagger.play();
            this.swingDagger.setVolume(soundId, this.volume);
        }
    }

    public void playShoot() {
        if (this.shoot != null) {
            long soundId = this.shoot.play();
            this.shoot.setVolume(soundId, this.volume);
        }
    }

    public void playDash() {
        if (this.dash != null) {
            long soundId = this.dash.play();
            this.dash.setVolume(soundId, this.volume);
        }
    }

}  // END: PlayerSoundComponent
