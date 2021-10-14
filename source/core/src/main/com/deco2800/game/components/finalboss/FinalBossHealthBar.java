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

public class FinalBossHealthBar extends DefaultTask {
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private final GameArea gameArea;
    private final GameTime timeSource;
    private final Entity boss;
    private long endTime;
    private static final float INTERVAL = 0.1f;
    private Entity healthBackground;
    private Entity healthForeground;


    public FinalBossHealthBar(GameArea gameArea, Entity boss) {
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        timeSource = ServiceLocator.getTimeSource();

        this.gameArea = gameArea;
        this.boss = boss;

        healthBackground = FinalBossFactory.createHealthBarBackground();
        healthForeground = FinalBossFactory.createHealthBarForeground();

    }

    public void createHealthBar() {
        healthBackground.setPosition(15,22);
        healthForeground.setPosition(15,22);

        this.gameArea.spawnEntity(healthBackground);
        this.gameArea.spawnEntity(healthForeground);
    }

    public void updateHealthBar() {
        float healthPercentage = (float) boss.getComponent(CombatStatsComponent.class).getHealth() / 50;

        healthForeground.setScale(10f * healthPercentage, 0.5f);

    }


    @Override
    public void start() {
        super.start();

        createHealthBar();

    }


    /**
     * Each update, the timer is checked to see if enough time has passed for an enemy to be spawned
     */
    @Override
    public void update() {

        if (timeSource.getTime() >= endTime) {
            updateHealthBar();

            endTime = timeSource.getTime() + (int)(INTERVAL * 1000);
        }

    }

    @Override
    public void stop() {
        super.stop();
    }
}
