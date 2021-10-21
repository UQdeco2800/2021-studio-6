package com.deco2800.game.components.tasks.FinalBossFireBullets;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * Firing Laser state
 *  Boss is firing the laser
 */
public class FiringState extends LaserState {

    private long endTime;
    private int fireAmounts = 3;
    private int fireCount;
    private int volleyAmount = 4;
    private int volleyCount;
    private final GameTime timeSource;

    FiringState(FinalBossFireBullets finalBossFireBullets, Entity owner){
        super(finalBossFireBullets, owner);

        timeSource = ServiceLocator.getTimeSource();
        startTimer();
        volleyCount = 0;
        fireCount = 0;
    }

    public void startTimer() {
        endTime = timeSource.getTime() + (int) (100);
    }

    /**
     * update
     * Wait until elapsed time then start moving and move to post fire state
     */
    @Override
    public void update() {
        if(fireCount < fireAmounts) {
            if(volleyCount < volleyAmount) {
                if(timeSource.getTime() > endTime) {
                    owner.getEvents().trigger("fire");
                    volleyCount += 1;
                    endTime = timeSource.getTime() + 200;
                }

            } else {
                volleyCount = 0;
                fireCount += 1;
                endTime = timeSource.getTime() + 1000;
            }
        }
        else {
            System.out.println("entering cooldown");
            laser.changeState(new CooldownState(laser, owner));
        }
//        logger.debug("Final boss laser fired, moving to post fire state");




    }

}


