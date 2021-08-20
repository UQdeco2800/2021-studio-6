package com.deco2800.game.components.npc;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BulletFactory;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class BulletTimer extends Component {
    private Entity target;
    private GameArea gameArea;

    public BulletTimer(Entity target, GameArea gameArea) {
        this.target = target;
        this.gameArea = gameArea;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("fire", this::fire);
    }

    void fire() {
        gameArea.spawnEntity(BulletFactory.createBullet(this.entity, target, gameArea));
    }

}
