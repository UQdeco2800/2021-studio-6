package com.deco2800.game.components.finalboss;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyBulletFactory;
import com.deco2800.game.entities.factories.FinalBossFactory;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * This class listens for the fire event then spawns and fires a bullet from
 * the entity to the target entity
 */
public class LaserTimer extends Component {
    private GameTime gameTime;
    private long endTime;

    /**
     * Adds the fire listener to the entity
     */
    @Override
    public void create() {
        super.create();
        gameTime = ServiceLocator.getTimeSource();
        endTime = gameTime.getTime() + 3000;
    }

    @Override
    public void update() {
        if(gameTime.getTime() >= endTime) {
            ServiceLocator.getEntityService().removeEntityAfterUpdate(entity);

        }
    }




}
