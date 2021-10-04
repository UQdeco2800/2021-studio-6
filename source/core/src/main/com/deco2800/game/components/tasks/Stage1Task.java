package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.*;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;

/** Spawns small enemies while the player is in range */
public class Stage1Task extends DefaultTask implements PriorityTask {
    private final int priority;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private final GameArea gameArea;
    private final GameTime timeSource;
    private final Entity darkness;
    private final Entity beam;
    private final Entity bossHead;
    private long endTime;
    private static final float INTERVAL = 5;

    public Stage1Task(int priority, Level4 gameArea, Entity darkness, Entity beam, Entity bossHead) {
        this.priority = priority;
        this.gameArea = gameArea;
        this.darkness = darkness;
        this.beam = beam;
        this.bossHead = bossHead;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        timeSource = ServiceLocator.getTimeSource();
    }

    public void attack() {
        this.beam.setPosition(this.bossHead.getPosition());
        this.beam.getEvents().trigger("attackStart");
    }

    @Override
    public void start() {
        super.start();

        //this.beam.setPosition(30f, 10f);
        //this.beam.setPosition(new Vector2(10, 10));
        this.gameArea.spawnEntity(beam);
        //this.beam.setPosition(30f, 10f);
        //this.beam.setPosition(new Vector2(10, 10));
        //this.beam.setPosition(this.bossHead.getPosition());

        //this.owner.getEntity().getEvents().trigger("chaseStart");
    }


    /**
     * Each update, the timer is checked to see if enough time has passed for an enemy to be spawned
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            this.attack();
            //this.gameArea.spawnFromSpawner(this.beam.getPosition(), MAX_SPAWN_DISTANCE);
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
