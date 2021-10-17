package com.deco2800.game.components.npc;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * This class controls the melee attack mechanics for enemies
 */
public class EnemyMeleeAttackComponent extends Component {
    private static final int ONE_SECOND = 1000;
    private boolean playerInAttackRange;
    private boolean playerInitialised = false;
    short targetLayer = PhysicsLayer.PLAYER;
    private long endTime;
    private float attackInterval = 2f;
    PlayerCombatStatsComponent playerStats;
    CombatStatsComponent myStats;
    NPCSoundComponent soundComponent;

    private final GameTime timeSource = ServiceLocator.getTimeSource();

    /**
     * Sets up listeners for collision start and end
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onPlayerClose);
        entity.getEvents().addListener("collisionEnd", this::onPlayerFar);
        myStats = entity.getComponent(CombatStatsComponent.class);
        soundComponent = entity.getComponent(NPCSoundComponent.class);
    }

    /**
     * On update, initialises the player if it hasnt already, otherwise hit the player if within range
     */
    @Override
    public void update() {
        // If the player is not initialised yet then store their combat stats
        if (!playerInitialised) {
            if (ServiceLocator.getGameArea() != null && ServiceLocator.getGameArea().getPlayer() != null) {
                playerStats = ServiceLocator.getGameArea().getPlayer().getComponent(PlayerCombatStatsComponent.class);
            }
            playerInitialised = true;
        }

        // If the player is in attack range and the attack is not on cooldown then hit the player and start cooldown
        if (!timeSource.isPaused() && playerInAttackRange && timeSource.getTime() >= endTime) {
            soundComponent.playMeleeAttack();
            playerStats.hit(myStats);
            endTime = timeSource.getTime() + (int)(attackInterval * ONE_SECOND);
        }
    }

    /**
     * When the player is too far away for the entity to melee, switch off the in range flag
     * @param me this entity
     * @param other the entity that has left melee range
     */
    private void onPlayerFar(Fixture me, Fixture other) {
        // If this person is not the player then return early
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            return;
        }
        playerInAttackRange = false;
    }

    /**
     * When the player is close enough for the entity to melee, switch on the in range flag
     * @param me this entity
     * @param other the entity that is now in melee range
     */
    private void onPlayerClose(Fixture me, Fixture other) {
        // If this person is not the player then return early
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            return;
        }
        playerInAttackRange = true;
    }
}