package com.deco2800.game.components.tasks.FinalBossFireLaser;

import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FreeMovingState
 * The boss moves freely and checks whether the player is in range
 */
public class FreeMovingState extends LaserState {

    FreeMovingState(FinalBossFireLaser finalBossFireLaser, Entity owner){
        super(finalBossFireLaser, owner);
    }

    /**
     * update
     * The boss checks if the player is beneath him, if he is go to Player in range State
     */
    @Override
    public void update() {
        if (Math.abs(laser.target.getCenterPosition().x - owner.getCenterPosition().x) < 2) {
            logger.debug("Final boss Player withing range starting laser  timer");
            laser.changeState(new PlayerInRangeState(laser, owner));
        }
    }

}
