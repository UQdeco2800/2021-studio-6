package com.deco2800.game.components.tasks.FinalBossFireLaser;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * PlayerInRangeState
 * The player is in range set timer if the player is still within range start to fire laser
 * if the player goes out of range move back to free moving state
 */
public class PlayerInRangeState extends LaserState {

    private boolean inRange;
    private long endTime;
    private final GameTime timeSource;

    PlayerInRangeState(FinalBossFireLaser finalBossFireLaser, Entity owner){
        super(finalBossFireLaser, owner);
        inRange = false;
        timeSource = ServiceLocator.getTimeSource();
        startTimer();
    }

    public void startTimer() {
        endTime = timeSource.getTime() + (int) (1000);
    }

    /**
     * update
     * if the player stays within range for the a specified amount of time move to prefire laser state
     * if the player moves out of range move state back to free moving
     */
    @Override
    public void update() {
        if (Math.abs(laser.target.getCenterPosition().x - owner.getCenterPosition().x) >= 2) {
            logger.debug("Final boss Player out of range stopping timer");

            laser.changeState(new FreeMovingState(laser, owner));
        }
        else if (timeSource.getTime() >= endTime) {
            logger.debug("Final boss laser time elapsed laser prefire starting");
            owner.getEvents().trigger("preFireLaser");
            owner.getEvents().trigger("stopMoving");
            laser.changeState(new PreFiringState(laser, owner));
        }
    }

}


