package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class EnemySoundTask extends DefaultTask {
    private final float duration;
    private long endTime;
    private final GameTime timeSource;

    public EnemySoundTask(float duration) {
        this.duration = duration;
        this.timeSource = ServiceLocator.getTimeSource();
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

    @Override
    public void stop() {
        super.stop();
    }
}
