package com.deco2800.game.components.player;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * Component that allows on collision with the attached entity (ie. obstacle), the player gets damaged.
 */
public class HurtEffectComponent extends Component {

    // Component Parameters.
    private short targetLayer;                              // Only detect collisions on specific PhysicsLayer
    private int attackDamage;                               // Amount of damage entity does.
    private long delayTime;                                 // Delay between repetitive damage.

    // Component variables.
    private final GameTime timeSource = ServiceLocator.getTimeSource();     // Time source, used to schedule events.
    private HitboxComponent hitboxComponent;                // Collision sensor instance.
    private boolean active;                                 // Component active (ie. when player is colliding with me)
    private long lastEventTime;                             // Last time the event was triggered.


    /**
     * Initialize the internal variables for this HurtEffect component.
     *
     * @param targetLayer the player's physics layer.
     * @param attack amount of damage that should be inflicted on each repeat.
     * @param delay time (ms) between repeat damage.
     */
    public HurtEffectComponent(short targetLayer, int attack, long delay) {
        this.targetLayer = targetLayer;
        this.attackDamage = attack;
        this.delayTime = delay;

        this.active = false;
        this.lastEventTime = 0;
    }


    /**
     * Set the damage to be inflicted on the player.
     * @param attack amount of damage that should be inflicted on each repeat.
     */
    public void setAttack(int attack) {
        this.attackDamage = attack;
    }


    /**
     * Get the damage to be inflicted on the player.
     * @return amount of damage that should be inflicted on each repeat.
     */
    public int getAttack() {
        return this.attackDamage;
    }


    /**
     * Set the time between each repeat damage.
     * @param delay time (ms) between repeat damage.
     */
    public void setDelay(int delay) {
        this.delayTime = delay;
    }


    /**
     * Get the time between each repeat damage.
     * @return time (ms) between repeat damage.
     */
    public long getDelay() {
        return this.delayTime;
    }

    /**
     * Returns true if effect is active.
     */
    public boolean isActive() { return this.active; }

    /**
     * On component creation.
     * Create the necessary event listeners for this HurtEffect component.
     */
    @Override
    public void create() {
        super.create();

        // Add collision event listeners. These control whether damage is enabled.
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);

        // Save my entity's hitbox.
        this.hitboxComponent = entity.getComponent(HitboxComponent.class);
    }


    /**
     * On component update.
     * Checks to see whether component can perform damage on targeted player.
     */
    @Override
    public void update() {
        super.update();

        if(this.active && !timeSource.isPaused() && timeSource.getTimeSince(lastEventTime) > this.delayTime) {
            // Trigger event.
            perform();
        }
    }


    /**
     * Ran when player collides into me.
     * Tell component that it can damage the player.
     *
     * @param me the entity doing damage on collision
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

        // Enable damage to occur to player.
        this.active = true;
    }


    /**
     * Ran when player leaves and doesn't collide anymore.
     * Tell component to stop doing collision damage on player.
     *
     * @param me the entity doing damage on collision
     * @param other the targeted entity (usually the player)
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        // Disable damage to player.
        this.active = false;
    }


    /**
     * Perform the effect on the player.
     * This is triggered by the update() method to perform repetitive damage if still colliding.
     */
    private void perform() {
        // Check for PlayerCombatStatsComponent.
        if(
                ServiceLocator.getGameArea() == null
                || ServiceLocator.getGameArea().getPlayer() == null
                || ServiceLocator.getGameArea().getPlayer().getComponent(PlayerCombatStatsComponent.class) == null) {
            // Unable to get playerStatus.
            return;
        }

        // Retrieve playerStats.
        PlayerCombatStatsComponent playerStats = ServiceLocator.getGameArea().getPlayer().getComponent(PlayerCombatStatsComponent.class);

        // Use CombatStatsComponent privately to cast damage.
        CombatStatsComponent damageStats = new CombatStatsComponent(0, this.attackDamage);

        // Damage player using hit().
        playerStats.hit(damageStats);
        lastEventTime = timeSource.getTime();
    }

} // END: HurtEffectComponent
