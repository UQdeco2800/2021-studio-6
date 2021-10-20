package com.deco2800.game.components.npc;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyBulletFactory;

public class ToughFireBulletListener extends Component {
    private Entity target;
    private GameArea gameArea;
    private String textureFileName;

    /**
     * Constructor sets a reference to the target entity and the gameArea
     * @param target the target to fire at
     * @param gameArea the gamearea to spawn the bullet in
     */
    public ToughFireBulletListener(Entity target, GameArea gameArea, String textureFileName) {
        this.target = target;
        this.gameArea = gameArea;
        this.textureFileName = textureFileName;
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
        EnemyBulletFactory.createToughBullet(this.entity, target, textureFileName);
    }

}


