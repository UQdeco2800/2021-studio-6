package com.deco2800.game.components.tasks.FinalBossFireLaser;

import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LaserState {
    FinalBossFireLaser laser;
    Entity owner;
    static final Logger logger = LoggerFactory.getLogger(LaserState.class);

    LaserState(FinalBossFireLaser finalBossFireLaser, Entity owner){
        this.laser = finalBossFireLaser;
        this.owner = owner;
    }
    public abstract void update();
}
