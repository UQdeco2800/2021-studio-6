package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.MultiAITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



enum BossState {
    PHASE1,
    PHASE2,
    PHASEFINAL,
}


/**
 * LaserListener
 * Listens for "fireLaser" events then spawns a laser beam
 */
public class BossHealthListener extends Component {
    private static final Logger logger = LoggerFactory.getLogger(BossHealthListener.class);
    private Entity boss;
    private BossState bossState = BossState.PHASE1;

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
        logger.debug("Boss health {}", health);
        if(bossState == BossState.PHASE1) {
            if(health < 80) {
                bossState = BossState.PHASE2;
                logger.debug("Boss State change from {} to {}", BossState.PHASE1, bossState);
                boss.getComponent(MultiAITaskComponent.class).removeAllTasks();
                //change tasks

            }
        } else if(bossState == BossState.PHASE2) {
            if(health < 70) {
                bossState = BossState.PHASEFINAL;
                logger.debug("Boss State change from {} to {}", BossState.PHASE2, bossState);
            }
        }



    }

}
