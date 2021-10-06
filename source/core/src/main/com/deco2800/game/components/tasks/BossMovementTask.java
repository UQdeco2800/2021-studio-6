package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class BossMovementTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);

    private final GameTime gameTime;
    private Vector2 target;

    private long lastTimeMoved;
    private Vector2 lastPos;
    private PhysicsMovementComponent movementComponent;

    public BossMovementTask() {
        this.target = new Vector2(0, 0);
        this.gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();

        this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
        this.setTarget(randomTarget());

        movementComponent.setMoving(true);
        logger.debug("Starting movement towards {}", target);
        lastTimeMoved = gameTime.getTime();
        lastPos = owner.getEntity().getPosition();

        this.owner.getEntity().getEvents().addListener("stopMoving", this::stop);
        this.owner.getEntity().getEvents().addListener("startMoving", this::startMoving);
    }

    @Override
    public void update() {
        if (isAtTarget()) {
            System.out.println("reached target");
            this.setTarget(randomTarget());
//            movementComponent.setMoving(false);
//            status = Status.FINISHED;
//            logger.debug("Finished moving to {}", target);
        }
    }

    public void setTarget(Vector2 target) {
        this.target = target;
        movementComponent.setTarget(target);
    }

    @Override
    public void stop() {
        super.stop();
        movementComponent.setMoving(false);
        logger.debug("Stopping movement");
    }

    public void startMoving() {
        movementComponent.setTarget(randomTarget());
        movementComponent.setMoving(true);
    }
    private boolean isAtTarget() {
//        System.out.println(owner.getEntity().getComponent(PhysicsComponent.class).getBody().getWorldCenter());
//        System.out.println(target);
////        System.out.println(owner.getEntity().getPosition().dst(target));
//        System.out.println("physics at = " + owner.getEntity().getComponent(PhysicsComponent.class).getBody().getWorldCenter().dst(target));
        return owner.getEntity().getComponent(PhysicsComponent.class).getBody().getWorldCenter().dst(this.target) <= 2f;
    }


    private Vector2 randomTarget() {
        Vector2 newPos = owner.getEntity().getPosition();
        int dice = MathUtils.random(10);

        if(dice > 5) {
            newPos.add(MathUtils.random(10.0f), 0);
        } else {
            newPos.add(MathUtils.random(-10.0f), 0);
        }
        return newPos;
    }
    private void checkIfStuck() {

        if (didMove()) {
            lastTimeMoved = gameTime.getTime();
            lastPos = owner.getEntity().getPosition();
        } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
//            movementComponent.setMoving(false);
//            status = Status.FAILED;
//            logger.debug("Got stuck! Failing movement task");
        }
    }

    private boolean didMove() {
        if( lastPos == null) {
            return false;
        }
        return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
