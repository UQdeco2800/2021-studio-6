package com.deco2800.game.components.tasks.FinalBossFireLaser;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * PreFiringState
 * The boss stops in this state and an animation plays before the boss actually fires the laser
 */
public class PreFiringState extends LaserState {

    private long endTime;
    private final GameTime timeSource;

    PreFiringState(FinalBossFireLaser finalBossFireLaser, Entity owner){
        super(finalBossFireLaser, owner);

        timeSource = ServiceLocator.getTimeSource();
        startTimer();
    }

    public void startTimer() {
        endTime = timeSource.getTime() + (int) (800);
    }

    /**
     * update
     * wait for an amount of time then fire laser
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            logger.debug("Final boss laser prefire complete, firing laser");
            owner.getEvents().trigger("fireLaser");
            laser.changeState(new FiringState(laser, owner));

        }
    }
}


