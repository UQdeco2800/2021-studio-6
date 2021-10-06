package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.MathUtils;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * FireBulletTask creates a task which triggers a fire event at fixed interval
 */
public class FinalBossFireLaser extends DefaultTask implements PriorityTask {
    private final GameTime timeSource;
    private Entity target;
    private long endTime;
    private boolean inRange;
    private int priority;

    private boolean firing;
    private long firingPause;
    private long movePause;

    @Override
    public int getPriority() {
        return 100;
    }


    public FinalBossFireLaser( Entity target) {
        timeSource = ServiceLocator.getTimeSource();
        this.target = target;


    }

    /**
     * Start waiting from now until duration has passed.
     */
    @Override
    public void start() {
        super.start();
        endTime = timeSource.getTime() + (int)(1000);
        this.inRange = false;
    }

    /**
     * if the interval timer has been reached trigger fire event and reset timer
     */
    @Override
    public void update() {
        //null guard
        if(target == null){
            return;
        }

        if(firing) {
            if(timeSource.getTime() >= movePause) {
                System.out.println("start moving");
                this.owner.getEntity().getEvents().trigger("startMoving");

                this.firing = false;
                this.inRange = false;
            }  else {
                return;
            }
        }

        if (inRange && timeSource.getTime() >= endTime && timeSource.getTime() >= firingPause) {
            System.out.println("stop moving");

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

//        if (timeSource.getTime() >= endTime) {
//            this.owner.getEntity().getEvents().trigger("fire");
//            endTime = timeSource.getTime() + (int)(duration * 1000);
//        }
    }
}
