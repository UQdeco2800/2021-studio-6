package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.tasks.DeadTask;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * This class listens to events relevant to a bosss state and plays the animation when one
 * of the events is triggered.
 */
public class BossAnimationController extends Component {
    // arrays are used to easily index into the stationary and running states of the animation
    private static final String[] ANIMATIONS_LEFT = {"left", "angry_left", "float_left", "left_angry_float"};
    private static final String[] ANIMATIONS_RIGHT = {"right", "angry_right", "float_right", "right_angry_float"};
    private static final int STATIONARY = 0;
    private static final int WALKING = 2;
    private int indexOffset = 0;
    private String[] currentDirection;
    private Vector2 currentWalkingTarget;
    AnimationRenderComponent animator;
    private String lastAnimation;
    private GameTime gameTime;
    private int angry = 0;

    /**
     * Creates the animator and sets default animation.
     */
    @Override
    public void create() {
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        animator.startAnimation(ANIMATIONS_LEFT[STATIONARY]);
        currentDirection = ANIMATIONS_LEFT;
        gameTime = ServiceLocator.getTimeSource();
    }

    /**
     * Extracts the current target coordinates and changes the animation to face that direction. For example if the
     * target coordinates are to the north than we will see the boss back so update the animation to the back animation.
     */
    @Override
    public void update() {
        // If game paused then don't update anything
        if (gameTime.isPaused()) {
            return;
        }

        // Get the current walking target
        PhysicsMovementComponent bossMovement = entity.getComponent(PhysicsMovementComponent.class);
        Vector2 walkTarget = bossMovement.getTarget();
        Vector2 myPosition = entity.getPosition();

        // Account for rounding errors, if the entity is close enough to the walk position then stop
        if (walkTarget != null && Math.abs(walkTarget.x - myPosition.x) < 0.1 && Math.abs(walkTarget.y - myPosition.y) < 0.1) {
            bossMovement.setMoving(false);
        }

        // if the boss is not moving then set the animation as the stationary animation facing the same direction
        if (!bossMovement.getMoving()) {
            setDirection(currentDirection[STATIONARY + angry]);
            // if there is target coordinates then update the animation direction to face it
        } else if (walkTarget != null && walkTarget != currentWalkingTarget) {
            currentWalkingTarget = walkTarget;
            updateAnimationDirection(walkTarget, WALKING + angry);
        }
    }

    /**
     * Updates the direction the animation is facing based on the coordinates the animation should be facing towards.
     * Chooses the correct animation state that should be set as well.
     * @param lookPosition the coordinates that the animation should face
     * @param state whether the boss is currently walking (value of 1) or stationary (value of 0)
     */
    public void updateAnimationDirection(Vector2 lookPosition, int state) {
        Vector2 myPos = entity.getPosition();
        float xOffset = lookPosition.x - myPos.x;
        if (xOffset > 0) {
            setDirection(ANIMATIONS_RIGHT[state]);
            currentDirection = ANIMATIONS_RIGHT;
        }
        else if (xOffset < 0) {
            setDirection(ANIMATIONS_LEFT[state]);
            currentDirection = ANIMATIONS_LEFT;
        }
    }

    /**
     * Starts a directional animation and stores as the last animation played
     * @param direction the animation state to set the direction to
     */
    public void setDirection(String direction) {
        if (!direction.equals(lastAnimation)) {
            animator.startAnimation(direction);
            lastAnimation = direction;
        }
    }
}
