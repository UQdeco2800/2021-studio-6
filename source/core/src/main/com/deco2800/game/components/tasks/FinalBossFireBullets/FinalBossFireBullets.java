package com.deco2800.game.components.tasks.FinalBossFireBullets;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FinalBossFireLaser
 * AI task which fires a laser, also triggers events for stopping an starting the boss movement
 */
public class FinalBossFireBullets extends DefaultTask implements Task {
    private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);

    private final GameTime timeSource;
    protected Entity target;

    private LaserState state;


    /**
     * FinalBossFireLaser
     * initialises the FireLaser task
     * @param target functionality depends on position of the player
     */
    public FinalBossFireBullets(Entity target) {
        timeSource = ServiceLocator.getTimeSource();
        this.target = target;
//        changeState(new CooldownState(this, this.owner.getEntity()));
    }

    /**
     * start
     * starts the task
     */
    @Override
    public void start() {
        super.start();
        changeState(new FiringState(this, this.owner.getEntity()));
    }

    public void changeState(LaserState state) {
        this.state = state;
    }

    /**
     * update
     * handles the logic for firing the laser
     * fire listeners for the boss to stop moving
     */
    @Override
    public void update() {
        this.state.update();

    }
}
