package com.deco2800.game.components.finalboss;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyBulletFactory;
import com.deco2800.game.entities.factories.FinalBossFactory;
import com.deco2800.game.services.ServiceLocator;

/**
 * LaserListener
 * Listens for "fireLaser" events then spawns a laser beam
 */
public class LaserListener extends Component {

    /**
     * Adds the fireLaser listener to the entity
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("fireLaser", this::fire);
    }

    /**
     * 'fires' a beam from the boss
     */
    void fire() {
        Entity beam = FinalBossFactory.createBeam();
        beam.setPosition(
                this.entity.getCenterPosition().x - (beam.getScale().x/2),
                this.entity.getPosition().y - (beam.getScale().y)
        );

        ServiceLocator.getGameArea().spawnEntity(beam);
    }

}
