package com.deco2800.game.components.npc;

import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.factories.EnemyBulletFactory;

public class EnemySoundListener extends Component {
    /**
     * Adds the fire listener to the entity
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("playSound", this::playSound);
    }

    /**
     * 'fires' a bullet from the entity to the target
     */
    void playSound() {

    }
}
