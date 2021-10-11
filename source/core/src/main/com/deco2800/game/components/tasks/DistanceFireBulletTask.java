package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * DistanceFireBulletTask creates a task which triggers a fire event at fixed interval if
 * the target is within a certain radius of the entity
 */
public class DistanceFireBulletTask extends DefaultTask implements PriorityTask {
    private final GameTime timeSource;
    private final float duration;
    private long endTime;
    private int priority;
    private final Entity target;
    private final float viewDistance;
    private static final int DEFAULT_FIRE_INTERVAL = 2000;
    private int fireInterval;

    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();

    /**
     * @param duration How long to wait for, in seconds.
     * @param priority priority used to swtich to this task
     * @param viewDistance radius around entity to fire
     * @param target the target entity (most likely the player)
     */
    public DistanceFireBulletTask(Entity target, float duration, int priority, float viewDistance) {
        this.timeSource = ServiceLocator.getTimeSource();
        this.duration = duration;
        this.priority = priority;
        this.target = target;
        this.viewDistance = viewDistance;
        this.fireInterval = DEFAULT_FIRE_INTERVAL;

        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    /**
     * Start waiting from now until duration has passed.
     */
    @Override
    public void start() {
        super.start();
        endTime = timeSource.getTime() + (int)(duration * fireInterval);
    }

    /**
     * if the interval timer has been reached trigger fire event and reset timer
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            this.owner.getEntity().getEvents().trigger("fire");
            endTime = timeSource.getTime() + (int)(duration * fireInterval);
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    public void setFireInterval(int fireInterval) {
        this.fireInterval = fireInterval;
    }

    public int getFireInterval() {
        return fireInterval;
    }

    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    private int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst > viewDistance || !isTargetVisible()) {
            return -1; // Too far, stop chasing
        }
        return priority;
    }

    private int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();

        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }
}
