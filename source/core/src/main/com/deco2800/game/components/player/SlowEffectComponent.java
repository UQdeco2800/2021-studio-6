package com.deco2800.game.components.player;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Component that allows on collision with the attached entity (ie. obstacle), the player receives a slowness effect.
 */
public class SlowEffectComponent extends Component {

    // Component Parameters.
    private short targetLayer;                              // Only detect collisions on specific PhysicsLayer
    private int speedMultiplier;                            // % multiplier to scale the speed.

    // Component variables.
    private HitboxComponent hitboxComponent;                // Collision sensor instance.
    private boolean active;                                 // Component active (ie. when player is colliding with me)


    /**
     * Initialize the internal variables for this SlowEffect component.
     *
     * @param targetLayer the player's physics layer.
     * @param speed percentage multiplier to apply to the player.
     */
    public SlowEffectComponent(short targetLayer, int speed) {
        this.targetLayer = targetLayer;
        this.setSpeed(speed);

        this.active = false;
    }


    /**
     * Set the speed multiplier to apply to player's speed on collision.
     * Limit speed to anything over 10% as to not prevent halting the player's movements.
     *
     * @param speed percentage multiplier to apply to the player.
     */
    public void setSpeed(int speed) {
        this.speedMultiplier = speed < 10 ? 10 : speed;
    }


    /**
     * Get the speed multiplier to apply to player's speed on collision.
     * @return percentage multiplier to apply to the player.
     */
    public int getSpeed() {
        return this.speedMultiplier;
    }

    /**
     * Returns true if effect is active.
     */
    public boolean isActive() { return this.active; }

    /**
     * On component creation.
     * Create the necessary event listeners for this SlowEffect component.
     */
    @Override
    public void create() {
        super.create();

        // Add collision event listeners. These control whether slowness is enabled.
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);

        // Save my entity's hitbox.
        this.hitboxComponent = entity.getComponent(HitboxComponent.class);
    }


    /**
     * Ran when player collides into me.
     * Tell component to slow the player.
     *
     * @param me the entity slowing on collision
     * @param other the targeted entity (usually the player)
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (this.hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Retrieve PlayerActions component from player.
        if(ServiceLocator.getGameArea() != null && ServiceLocator.getGameArea().getPlayer() != null
                && ServiceLocator.getGameArea().getPlayer().getComponent(PlayerActions.class) != null) {
            PlayerActions playerActions = ServiceLocator.getGameArea().getPlayer().getComponent(PlayerActions.class);

            // Enable slowness on player.
            playerActions.setScaleSpeed(entity.getId(), this.speedMultiplier);
        }

        this.active = true;
    }


    /**
     * Ran when player leaves and doesn't collide anymore.
     * Tell component to stop slowness on player.
     *
     * @param me the entity slowing on collision
     * @param other the targeted entity (usually the player)
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        // Retrieve PlayerActions component from player.
        if(ServiceLocator.getGameArea() != null && ServiceLocator.getGameArea().getPlayer() != null
                && ServiceLocator.getGameArea().getPlayer().getComponent(PlayerActions.class) != null) {
            PlayerActions playerActions = ServiceLocator.getGameArea().getPlayer().getComponent(PlayerActions.class);

            // Enable slowness on player.
            playerActions.clearScaleSpeed(entity.getId());
        }

        this.active = false;
    }

} // END: SlowEffectComponent
