package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MoveInCircle extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);

    private final GameTime gameTime;
    private Vector2 circleSize;
    private float stopDistance = 0.01f;
    private long lastTimeMoved;

    private ArrayList<Vector2> thetaList;
    private int index;
    private float stepSize = 0.2f;

    private Vector2 origin;
    private float radius;

    private Vector2 target;

    private int startIndex;

    private Vector2 lastPos;
    private PhysicsMovementComponent movementComponent;

    public MoveInCircle(Vector2 origin, float radius, int startIndex) {
        this.origin = origin;
        this.radius = radius;
        this.startIndex = startIndex;
        this.index = this.startIndex + 1;
        this.gameTime = ServiceLocator.getTimeSource();
        thetaList = new ArrayList<>();
        for (float theta=0;  theta < 2*Math.PI;  theta+=stepSize) {
            thetaList.add(new Vector2(
                    (float) (origin.x + radius*Math.cos(theta)),
                    (float) (origin.y - radius*Math.sin(theta))));
        }
        for(Vector2 point : thetaList){
            System.out.println(point);
        }
    }

    @Override
    public void start() {
        super.start();
        owner.getEntity().setPosition(thetaList.get(this.startIndex));
        this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
        setTarget(thetaList.get(this.startIndex + 1));
        movementComponent.setMoving(true);
        logger.debug("Starting movement towards {}", target);
        lastTimeMoved = gameTime.getTime();
        lastPos = owner.getEntity().getPosition();
    }

    @Override
    public void update() {

        if (isAtTarget()) {
            index += 1;
            if (this.index > this.thetaList.size() - 1) {
                this.index = 0;
            }
            setTarget(thetaList.get(index));
        }
    }

    public void setTarget(Vector2 target) {
        System.out.println(target);
        this.target = target;
        this.movementComponent.setTarget(target);
    }

    @Override
    public void stop() {
        super.stop();
        movementComponent.setMoving(false);
        logger.debug("Stopping movement");
    }

    private boolean isAtTarget() {
        return owner.getEntity().getPosition().dst(target) <= stopDistance;
    }

    private void checkIfStuck() {
        if (didMove()) {
            lastTimeMoved = gameTime.getTime();
            lastPos = owner.getEntity().getPosition();
        } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
            movementComponent.setMoving(false);
            status = Task.Status.FAILED;
            logger.debug("Got stuck! Failing movement task");
        }
    }

    private boolean didMove() {
        return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
    }

    @Override
    public int getPriority() {
        return 100;
    }
}
