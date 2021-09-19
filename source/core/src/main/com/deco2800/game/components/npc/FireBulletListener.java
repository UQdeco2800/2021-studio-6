package com.deco2800.game.components.npc;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyBulletFactory;

/**
 * This class listens for the fire event then spawns and fires a bullet from
 * the entity to the target entity
 */
public class FireBulletListener extends Component {
    private Entity target;
    private GameArea gameArea;

    /**
     * Constructor sets a reference to the target entity and the gameArea
     * @param target the target to fire at
     * @param gameArea the gamearea to spawn the bullet in
     */
    public FireBulletListener(Entity target, GameArea gameArea) {
        this.target = target;
        this.gameArea = gameArea;
    }

    /**
     * Adds the fire listener to the entity
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("fire", this::fire);
    }

    /**
     * 'fires' a bullet from the entity to the target
     */
    void fire() {
        EnemyBulletFactory.createBullet(this.entity, target, gameArea);
    }

}
