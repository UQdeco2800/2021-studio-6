package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.MultiAITaskComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LaserListener
 * Listens for "fireLaser" events then spawns a laser beam
 */
public class BossHealthListener extends Component {
    private static final Logger logger = LoggerFactory.getLogger(BossHealthListener.class);
    private Entity boss;

    public BossHealthListener(Entity boss) {
        this.boss = boss;
    }
    /**
     * Adds the fireLaser listener to the entity
     */
    @Override
    public void create() {
        super.create();
        boss.getEvents().addListener("updateHealth", this::state);
    }

    /**
     * 'fires' a beam from the boss
     */
    void state(int health) {

        logger.info("Boss health {}", health);

        boss.getComponent(MultiAITaskComponent.class).removeAllTasks();

    }

}
