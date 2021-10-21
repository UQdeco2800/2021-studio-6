package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BossMovementTask
 * This class handles the AI for moving the boss around
 */
public class BossMovementTask extends DefaultTask implements Task {
    private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);

    private final GameTime gameTime;
    private Vector2 target;

    private long lastTimeMoved;
    private Vector2 lastPos;
    private PhysicsMovementComponent movementComponent;

    private float bounds;

    /**
     * BossMovementTask
     * Initalises the boss
     */
    public BossMovementTask(float bounds) {
        this.target = new Vector2(0, 0);
        this.bounds = bounds;
        this.gameTime = ServiceLocator.getTimeSource();
    }

    /**
     * start
     * Starts the AI for moving the boss
     * sets up listeners for stopping and starting the boss
     */
    @Override
    public void start() {
        super.start();

        this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
        this.setTarget(randomXAxisTarget());

        movementComponent.setMoving(true);
        logger.info("Boss starting movement towards {}", target);
        lastTimeMoved = gameTime.getTime();
        lastPos = owner.getEntity().getPosition();

        this.owner.getEntity().getEvents().addListener("stopMoving", this::stop);
        this.owner.getEntity().getEvents().addListener("startMoving", this::startMoving);
    }

    /**
     * update
     * if the boss has reached his target, choose another random target to go towards
     */
    @Override
    public void update() {
        if (isAtTarget()) {
//            logger.info("Boss reached target {}", target);
            this.setTarget(randomXAxisTarget());
//            logger.info("Boss new target {}", target);

        }
    }

    /**
     * setTarget
     * sets the target for the boss to move towards
     * @param target the new target to move toward
     */
    public void setTarget(Vector2 target) {
        this.target = target;
        movementComponent.setTarget(target);
    }

    /**
     * stop
     * stops the boss's movement
     */
    @Override
    public void stop() {
        super.stop();
        movementComponent.setMoving(false);
        logger.info("Final boss Stopping movement");
    }

    /**
     * startMoving
     * starts the boss's movement
     */
    public void startMoving() {

        movementComponent.setMoving(true);
        logger.info("Final boss Starting movement");
    }

    /**
     * isAtTarget
     * check for whether the boss has reached the target or not
     * @return true if the boss has reached the target false if not
     */
    private boolean isAtTarget() {
        return owner.getEntity().getComponent(PhysicsComponent.class).getBody().getWorldCenter().dst(this.target) <= 3.5f;
    }

    /**
     * randomXAxisTarget
     * generates a random target for the boss to move toward
     * this only generates a random target on the x axis 
     * @return vector for the randomly generated target
     */
    private Vector2 randomXAxisTarget() {

        int dice = MathUtils.random(10);
        float newXpos = MathUtils.random(10, 30);


        if(dice > 5) {
            return new Vector2(newXpos, owner.getEntity().getPosition().y);
        } else {
            return new Vector2(newXpos, owner.getEntity().getPosition().y);
        }

    }

}
