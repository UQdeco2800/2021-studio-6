package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.*;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.utils.math.RandomUtils;

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
    private long beamEndTime;
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
        Vector2 bossHeadPos = new Vector2((RandomUtils.randomInt(
                (int) (this.darkness.getCenterPosition().x * 2 - this.beam.getScale().x))),
                (this.darkness.getPosition().y));
        Vector2 beamPos = new Vector2(bossHeadPos.x + (this.bossHead.getScale().x - this.beam.getScale().x)/2, bossHeadPos.y - this.beam.getScale().y);
        this.beam.setPosition(beamPos);
        this.bossHead.setPosition(bossHeadPos);
        this.beam.getEvents().trigger("attackStart");
        this.bossHead.getEvents().trigger("attackStart");
    }

    @Override
    public void start() {
        super.start();

        this.attack();
        //this.beam.setPosition(30f, 10f);
        this.gameArea.spawnEntity(beam);
        this.gameArea.spawnEntity(bossHead);

        //this.owner.getEntity().getEvents().trigger("chaseStart");
    }


    /**
     * Each update, the timer is checked to see if enough time has passed for an enemy to be spawned
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            this.attack();
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
