package com.deco2800.game.components.finalboss;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyBulletFactory;
import com.deco2800.game.entities.factories.FinalBossFactory;
import com.deco2800.game.services.ServiceLocator;

/**
 * This class listens for the fire event then spawns and fires a bullet from
 * the entity to the target entity
 */
public class LaserListener extends Component {



    /**
     * Adds the fire listener to the entity
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("fireLaser", this::fire);
    }

    /**
     * 'fires' a bullet from the entity to the target
     */
    void fire() {

        Entity beam = FinalBossFactory.createBeam();
        beam.setPosition(
                this.entity.getCenterPosition().x - (beam.getScale().x / 2),
                this.entity.getCenterPosition().y - (beam.getScale().y / 2) - 8
        );

        ServiceLocator.getGameArea().spawnEntity(beam);
    }

}
