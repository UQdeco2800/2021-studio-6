package com.deco2800.game.components.tasks.FinalBossFireBullets;

import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LaserState {
    FinalBossFireBullets laser;
    Entity owner;
    static final Logger logger = LoggerFactory.getLogger(LaserState.class);

    LaserState(FinalBossFireBullets finalBossFireBullets, Entity owner){
        this.laser = finalBossFireBullets;
        this.owner = owner;
    }
    public abstract void update();
}
