package com.deco2800.game.components.finalboss;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyBulletFactory;
import com.deco2800.game.entities.factories.FinalBossFactory;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * LaserTimer timer listener which destroys the laser after a set amount of time
 */
public class LaserTimer extends Component {
    private GameTime gameTime;
    private long endTime;

    /**
     * create
     * sets the duration of the laser
     */
    @Override
    public void create() {
        super.create();
        gameTime = ServiceLocator.getTimeSource();
        endTime = gameTime.getTime() + 3000;
    }

    /**
     * update
     * listens for the end of the update
     */
    @Override
    public void update() {
        if(gameTime.getTime() >= endTime) {
            ServiceLocator.getEntityService().removeEntityAfterUpdate(entity);

        }
    }




}
