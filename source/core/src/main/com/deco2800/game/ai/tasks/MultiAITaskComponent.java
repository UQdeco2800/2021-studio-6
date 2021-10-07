package com.deco2800.game.ai.tasks;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * AI task runner which allows multiple tasks to be run at once
 */
public class MultiAITaskComponent extends Component implements TaskRunner {
    private static final Logger logger = LoggerFactory.getLogger(AITaskComponent.class);

    private final List<Task> parallelTasks = new ArrayList<>(2);

    /**
     * addTask
     * adds a task to be run in parallel with other tasks
     * @param task the task to be added
     * @return MultiAITaskComponent
     */
    public MultiAITaskComponent addTask(Task task) {
        logger.debug("{} Adding task {}", this, task);
        parallelTasks.add(task);
        task.create(this);
        return this;
    }

    /**
     * create
     * starts all attached tasks
     */
    @Override
    public void create() {
        for(Task p : parallelTasks) {
            p.start();
        }
    }

    /**
     * update
     * runs all attached tasks
     */
    @Override
    public void update() {
        for(Task p : parallelTasks) {
            p.update();
        }
    }

    /**
     * dispose
     * stops all tasks
     */
    @Override
    public void dispose() {
        for(Task p : parallelTasks) {
            if (p != null) {
                p.stop();
            }
        }

    }


}
