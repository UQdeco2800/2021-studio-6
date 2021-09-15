package com.deco2800.game.components.npc;

import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.tasks.EnemySoundTask;
import com.deco2800.game.entities.factories.EnemyBulletFactory;

public class EnemySoundListener extends Component {
    private String assetPath;
    public EnemySoundListener(String assetPath) {
        this.assetPath = assetPath;
    }

    /**
     * Adds the sound listener to the entity
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("playSound", this::playSound);
    }

    /**
     * Plays a sound
     */
    void playSound() {
        MusicPlayer music = new MusicPlayer();
        music.playSound(assetPath);
    }
}
