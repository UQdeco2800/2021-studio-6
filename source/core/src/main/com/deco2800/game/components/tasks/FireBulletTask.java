package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * FireBulletTask creates a task which triggers a fire event at fixed interval
 */
public class FireBulletTask extends DefaultTask implements PriorityTask {
    private final GameTime timeSource;
    private final float duration;
    private long endTime;
    private int priority;

    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * @param duration How long to wait for, in seconds.
     */
    public FireBulletTask(float duration, int priority) {
        timeSource = ServiceLocator.getTimeSource();
        this.duration = duration;
    }

    /**
     * Start waiting from now until duration has passed.
     */
    @Override
    public void start() {
        super.start();
        endTime = timeSource.getTime() + (int)(duration * 1000);
    }

    /**
     * if the interval timer has been reached trigger fire event and reset timer
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            this.owner.getEntity().getEvents().trigger("fire");
            endTime = timeSource.getTime() + (int)(duration * 1000);
        }
    }
}
