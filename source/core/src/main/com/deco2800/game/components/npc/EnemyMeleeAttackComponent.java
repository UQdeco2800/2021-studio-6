package com.deco2800.game.components.npc;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class EnemyMeleeAttackComponent extends Component {
    private static final int ONE_SECOND = 1000;
    private boolean playerInAttackRange;
    private boolean playerInitialised = false;
    short targetLayer = PhysicsLayer.PLAYER;
    private long endTime;
    private float attackInterval = 2f;
    PlayerCombatStatsComponent playerStats;
    CombatStatsComponent myStats;

    private final GameTime timeSource = ServiceLocator.getTimeSource();

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onPlayerClose);
        entity.getEvents().addListener("collisionEnd", this::onPlayerFar);
        myStats = entity.getComponent(CombatStatsComponent.class);
    }

    @Override
    public void update() {
        if (!playerInitialised) {
            if (ServiceLocator.getGameArea() != null && ServiceLocator.getGameArea().getPlayer() != null) {
                playerStats = ServiceLocator.getGameArea().getPlayer().getComponent(PlayerCombatStatsComponent.class);
            }
            playerInitialised = true;
        }

        if (playerInAttackRange && timeSource.getTime() >= endTime) {
            playerStats.hit(myStats);
            endTime = timeSource.getTime() + (int)(attackInterval * ONE_SECOND);
        }
    }

    private void onPlayerFar(Fixture me, Fixture other) {
        // If this person is not the player then return early
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            return;
        }
        playerInAttackRange = false;
    }

    private void onPlayerClose(Fixture me, Fixture other) {
        // If this person is not the player then return early
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            return;
        }
        playerInAttackRange = true;
    }
}