package com.deco2800.game.components.tasks.FinalBossFireLaser;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * PostFiringState
 * The boss has fired the laser, wait for laser to 'cooldown'
 */
public class PostFiringState extends LaserState {

    private long endTime;
    private final GameTime timeSource;

    PostFiringState(FinalBossFireLaser finalBossFireLaser, Entity owner){
        super(finalBossFireLaser, owner);

        timeSource = ServiceLocator.getTimeSource();
        startTimer();
    }

    public void startTimer() {
        endTime = timeSource.getTime() + (int) (2000);
    }

    /**
     * update
     * boss waits for laser to 'cooldown' then moves back to free moving state
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            logger.debug("Final boss post fire complete, Boss laser cooling down");
            owner.getEvents().trigger("postFireLaser");
            owner.getEvents().trigger("startMoving");
            laser.changeState(new CooldownState(laser, owner));
        }
    }

}


