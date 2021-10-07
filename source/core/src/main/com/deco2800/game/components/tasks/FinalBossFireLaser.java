package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * FinalBossFireLaser
 * AI task which fires a laser, also triggers events for stopping an starting the boss movement
 */
public class FinalBossFireLaser extends DefaultTask implements Task {
    private final GameTime timeSource;
    private Entity target;

    private boolean inRange;
    private boolean firing;

    private long firingPause;
    private long movePause;
    private long endTime;


    /**
     * FinalBossFireLaser
     * initialises the FireLaser task
     * @param target functionality depends on position of the player
     */
    public FinalBossFireLaser(Entity target) {
        timeSource = ServiceLocator.getTimeSource();
        this.target = target;
    }

    /**
     * start
     * starts the task
     */
    @Override
    public void start() {
        super.start();
        endTime = timeSource.getTime() + (int)(1000);
        this.inRange = false;
    }

    /**
     * update
     * handles the logic for firing the laser
     * fire listeners for the boss to stop moving
     */
    @Override
    public void update() {
        //null guard
        if(target == null) {
            return;
        }

        //if the boss is firing stop other logic from triggering
        if(firing) {
            if(timeSource.getTime() >= movePause) {
                this.owner.getEntity().getEvents().trigger("startMoving");
                this.firing = false;
                this.inRange = false;
            }  else {
                return;
            }
        }

        if (inRange && timeSource.getTime() >= endTime && timeSource.getTime() >= firingPause) {
            this.owner.getEntity().getEvents().trigger("fireLaser");
            this.owner.getEntity().getEvents().trigger("stopMoving");
            this.inRange = false;
            this.firing = true;
            this.movePause = timeSource.getTime() + 3000;
            this.firingPause = timeSource.getTime() + 10000;
        }

        if (Math.abs(target.getCenterPosition().x - this.owner.getEntity().getCenterPosition().x) < 3) {
            if(this.inRange == false){
                endTime = timeSource.getTime() + (int)(1000);
            }
            this.inRange = true;

        } else {
            this.inRange = false;
        }

    }
}
