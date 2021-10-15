package com.deco2800.game.components.finalboss;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.FinalBossFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.util.Calendar;

/**
 * A class representing the final boss health bar
 */
public class FinalBossHealthBar extends DefaultTask {

    private final GameArea gameArea;
    private final GameTime timeSource;
    private final Entity boss;
    private long endTime;
    private static final float INTERVAL = 0.1f;
    private Entity healthBackground;
    private Entity healthForeground;

    /**
     * Constructs the final boss health bar
     * @param gameArea the game area to create the health bar in
     * @param boss the final boss entity
     */
    public FinalBossHealthBar(GameArea gameArea, Entity boss) {

        timeSource = ServiceLocator.getTimeSource();

        this.gameArea = gameArea;
        this.boss = boss;

        healthBackground = FinalBossFactory.createHealthBarBackground();
        healthForeground = FinalBossFactory.createHealthBarForeground();

    }

    /**
     * Spawns the health bar into the game area
     */
    public void createHealthBar() {
        healthBackground.setPosition(15,22);
        healthForeground.setPosition(15,22);

        this.gameArea.spawnEntity(healthBackground);
        this.gameArea.spawnEntity(healthForeground);
    }


    @Override
    public void start() {
        super.start();

        createHealthBar();

    }


    /**
     * Each update, the timer is checked to see if enough time has passed for the health bar to be updated
     */
    @Override
    public void update() {

        if (timeSource.getTime() >= endTime) {
            float healthPercentage = (float) boss.getComponent(CombatStatsComponent.class).getHealth() / 100;

            healthForeground.setScale(10f * healthPercentage, 0.5f);

            endTime = timeSource.getTime() + (int)(INTERVAL * 1000);
        }

    }

    @Override
    public void stop() {
        super.stop();
    }
}
