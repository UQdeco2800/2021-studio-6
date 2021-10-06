package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.Level4;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.FinalBossFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class Stage2Task extends DefaultTask implements PriorityTask {
    private final int priority;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private final GameArea gameArea;
    private final GameTime timeSource;
    private final Entity darkness;
    private final Entity target;
    private long endTime;
    private long beamEndTime;
    private static final float INTERVAL = 30;
    private Entity spawner1;
    private Entity spawner2;
    private Entity spawner3;
    private Entity spawner4;


    public Stage2Task(int priority, Level4 gameArea, Entity darkness, Entity target) {
        this.priority = priority;
        this.gameArea = gameArea;
        this.darkness = darkness;
        this.target = target;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        timeSource = ServiceLocator.getTimeSource();
        spawner1 = FinalBossFactory.createLightSpawner(target, gameArea);
        spawner2 = FinalBossFactory.createLightSpawner(target, gameArea);
        spawner3 = FinalBossFactory.createLightSpawner(target, gameArea);
        spawner4 = FinalBossFactory.createLightSpawner(target, gameArea);
    }

    public void createSpawners() {

        Vector2 pos1 = new Vector2(0, 0);
        Vector2 pos2 = new Vector2(0,19);
        Vector2 pos3 = new Vector2(39, 0);
        Vector2 pos4 = new Vector2(39,19);

        spawner1.setPosition(pos1);
        spawner2.setPosition(pos2);
        spawner3.setPosition(pos3);
        spawner4.setPosition(pos4);

        this.gameArea.spawnEntity(spawner1);
        this.gameArea.spawnEntity(spawner2);
        this.gameArea.spawnEntity(spawner3);
        this.gameArea.spawnEntity(spawner4);


    }

    @Override
    public void start() {
        super.start();

        this.createSpawners();

    }


    /**
     * Each update, the timer is checked to see if enough time has passed for an enemy to be spawned
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            Entity largeEnemy = NPCFactory.createLargeEnemy(target);
            largeEnemy.setPosition(spawner1.getPosition().x + 1, spawner1.getPosition().y + 1);

            Entity smallEnemy = NPCFactory.createSmallEnemy(target);
            smallEnemy.setPosition(spawner2.getPosition().x + 1, spawner2.getPosition().y - 1);

            Entity longRangeEnemy = NPCFactory.createLongRangeEnemy(target, gameArea);
            longRangeEnemy.setPosition(spawner3.getPosition().x - 1, spawner3.getPosition().y + 1);

            Entity toughLongRangeEnemy = NPCFactory.createToughLongRangeEnemy(target, gameArea);
            toughLongRangeEnemy.setPosition(spawner4.getPosition().x - 1, spawner4.getPosition().y - 1);

            if (spawner1.getComponent(CombatStatsComponent.class).getHealth() > 0) {
                this.gameArea.spawnEntity(largeEnemy);
            }
            if (spawner2.getComponent(CombatStatsComponent.class).getHealth() > 0) {
                this.gameArea.spawnEntity(smallEnemy);
            }
            if (spawner3.getComponent(CombatStatsComponent.class).getHealth() > 0) {
                this.gameArea.spawnEntity(longRangeEnemy);
            }
            if (spawner4.getComponent(CombatStatsComponent.class).getHealth() > 0) {
                this.gameArea.spawnEntity(toughLongRangeEnemy);
            }
            endTime = timeSource.getTime() + (int)(INTERVAL * 1000);
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
