package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class PlayerActionsTest {
    @Test
    void shouldSetInitialSpeed() {
        PlayerActions action = new PlayerActions(3);
        Vector2 speed = new Vector2(3f, 3f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(0);
        speed = new Vector2(0f, 0f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(20);
        speed = new Vector2(3f, 3f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(-20);
        speed = new Vector2(0f, 0f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);
    }

    @Test
    void shouldIncreaseSpeedWhenWounded() {
        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(new PlayerCombatStatsComponent(5, 5, 3, 5, 0));
        player.create();
        Vector2 speed = new Vector2(3f, 3f);
        assertEquals(speed.x, player.getComponent(PlayerActions.class).getCurrentSpeed().x);
        assertEquals(speed.y, player.getComponent(PlayerActions.class).getCurrentSpeed().y);

        player.getComponent(PlayerCombatStatsComponent.class).setWoundState(2);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        speed = new Vector2(4f, 4f);
        assertEquals(speed.x, player.getComponent(PlayerActions.class).getCurrentSpeed().x);
        assertEquals(speed.y, player.getComponent(PlayerActions.class).getCurrentSpeed().y);

        player.getComponent(PlayerCombatStatsComponent.class).setWoundState(0);
        assertEquals(0, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        speed = new Vector2(0f, 0f);
        assertEquals(speed.x, player.getComponent(PlayerActions.class).getCurrentSpeed().x);
        assertEquals(speed.y, player.getComponent(PlayerActions.class).getCurrentSpeed().y);
    }

    @Test
    void shouldSetDashOnEvent() {
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        assertFalse(player.getComponent(PlayerActions.class).getDashState());

        player.getEvents().trigger("dash", 5);
        assertTrue(player.getComponent(PlayerActions.class).getDashState());
    }
}