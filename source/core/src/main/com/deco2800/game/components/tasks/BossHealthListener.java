package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.MultiAITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.finalboss.FinalBossHealthBar;
import com.deco2800.game.components.tasks.FinalBossFireBullets.FinalBossFireBullets;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



enum BossState {
    PHASE1,
    PHASE2,
    PHASE3,
    PHASEFINAL,
}


/**
 * LaserListener
 * Listens for "fireLaser" events then spawns a laser beam
 */
public class BossHealthListener extends Component {
    private static final Logger logger = LoggerFactory.getLogger(BossHealthListener.class);
    private Entity boss;
    private Entity darkness;
    private Entity healthBackground;
    private Entity healthForeground;
    private BossState bossState = BossState.PHASE1;
    private Entity target;

    public BossHealthListener(Entity boss, Entity darkness,
                              Entity healthBackground, Entity healthForeground, Entity target) {
        this.boss = boss;
        this.darkness = darkness;
        this.healthBackground = healthBackground;
        this.healthForeground = healthForeground;
        this.target = target;
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
        if(bossState == BossState.PHASE1) {
            if(health < 80) {
                bossState = BossState.PHASE2; //currently skips phase 2
                logger.info("Boss State change from {} to {}", BossState.PHASE1, bossState);
                boss.getComponent(MultiAITaskComponent.class).addMidStep(new FinalBossFireBullets(target));
//                boss.getComponent(MultiAITaskComponent.class).removeAllTasks();
                //change tasks

            }
        } else if(bossState == BossState.PHASE2) {
            if(health < 70) {
                bossState = BossState.PHASE3;
                logger.info("Boss State change from {} to {}", BossState.PHASE2, bossState);

                //add bullet hell task
            }
        } else if(bossState == BossState.PHASE3) {
            if(health <= 0) {
                bossState = BossState.PHASEFINAL;
                ServiceLocator.getGameArea().stopMusic();
                healthBackground.dispose();
                healthForeground.dispose();
                boss.dispose();
                darkness.dispose();
                MainGameScreen.changeLevel();
                logger.info("Boss State change from {} to {}", BossState.PHASE3, bossState);
            }
        }



    }

}
