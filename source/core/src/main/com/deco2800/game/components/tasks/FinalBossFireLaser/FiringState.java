package com.deco2800.game.components.tasks.FinalBossFireLaser;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * Firing Laser state
 *  Boss is firing the laser
 */
public class FiringState extends LaserState {

    private long endTime;
    private final GameTime timeSource;

    FiringState(FinalBossFireLaser finalBossFireLaser, Entity owner){
        super(finalBossFireLaser, owner);

        timeSource = ServiceLocator.getTimeSource();
        startTimer();
    }

    public void startTimer() {
        endTime = timeSource.getTime() + (int) (1500);
    }

    /**
     * update
     * Wait until elapsed time then start moving and move to post fire state
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            logger.debug("Final boss laser fired, moving to post fire state");
            owner.getEvents().trigger("postFireLaser");
            laser.changeState(new PostFiringState(laser, owner));

        }
    }

}


