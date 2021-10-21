package com.deco2800.game.components.tasks.FinalBossFireBullets;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * PostFiringState
 * The boss has fired the laser, wait for laser to 'cooldown'
 */
public class CooldownState extends LaserState {

    private long endTime;
    private final GameTime timeSource;

    CooldownState(FinalBossFireBullets finalBossFireBullets, Entity owner){
        super(finalBossFireBullets, owner);

        timeSource = ServiceLocator.getTimeSource();
        startTimer();
    }

    public void startTimer() {
        endTime = timeSource.getTime() + (int) (3000);
    }

    /**
     * update
     * boss waits for laser to 'cooldown' then moves back to free moving state
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            logger.debug("Final laser cooldown complete laser ready to fire");
            System.out.println("Firing again");
            laser.changeState(new FiringState(laser, owner));
        }
    }

}


