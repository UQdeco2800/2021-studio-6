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
import com.deco2800.game.utils.math.RandomUtils;

public class Stage2Task extends DefaultTask implements PriorityTask {
    private final int priority;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private final GameArea gameArea;
    private final GameTime timeSource;
    private final Entity target;
    private long endTime;
    private long beamEndTime;
    private static final float INTERVAL = 15;
    private Entity spawner1;
    private Entity spawner2;
    private Entity spawner3;
    private Entity spawner4;

    /**
     *
     * @param priority the priority of the task
     * @param gameArea the game area for this task to occur
     * @param target the target for the enemies to attack
     */
    public Stage2Task(int priority, GameArea gameArea, Entity target) {
        this.priority = priority;
        this.gameArea = gameArea;

        this.target = target;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        timeSource = ServiceLocator.getTimeSource();
        spawner1 = FinalBossFactory.createLightSpawner(target, gameArea);
        spawner2 = FinalBossFactory.createLightSpawner(target, gameArea);
        spawner3 = FinalBossFactory.createLightSpawner(target, gameArea);
        spawner4 = FinalBossFactory.createLightSpawner(target, gameArea);
    }

    /**
     * Creates the spawners for stage 2
     */
    public void createSpawners() {

        Vector2 pos1 = new Vector2(6, 2);
        Vector2 pos2 = new Vector2(6,15);
        Vector2 pos3 = new Vector2(33, 2);
        Vector2 pos4 = new Vector2(33,15);

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
            int offset = RandomUtils.randomInt(5);

            Entity largeEnemy = NPCFactory.createLargeEnemy(target);
            largeEnemy.setPosition(spawner1.getPosition().x + offset, spawner1.getPosition().y + offset);

            Entity smallEnemy = NPCFactory.createSmallEnemy(target);
            smallEnemy.setPosition(spawner2.getPosition().x + offset, spawner2.getPosition().y - offset);

            Entity longRangeEnemy = NPCFactory.createLongRangeEnemy(target, gameArea);
            longRangeEnemy.setPosition(spawner3.getPosition().x - offset, spawner3.getPosition().y + offset);

            Entity toughLongRangeEnemy = NPCFactory.createToughLongRangeEnemy(target, gameArea);
            toughLongRangeEnemy.setPosition(spawner4.getPosition().x - offset, spawner4.getPosition().y - offset);

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
