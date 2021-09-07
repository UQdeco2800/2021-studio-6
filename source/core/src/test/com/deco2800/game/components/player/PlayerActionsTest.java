package com.deco2800.game.components.player;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener1;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.badlogic.gdx.math.Vector2;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class PlayerActionsTest {
    @Mock GameTime time;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
    }

    @Test
    void shouldSetSpeed() {
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
    void shouldChangeSpeedWhenWoundChanges() {
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        Vector2 speed = new Vector2(3f, 3f);
        assertEquals(speed.x, player.getComponent(PlayerActions.class).getCurrentSpeed().x);
        assertEquals(speed.y, player.getComponent(PlayerActions.class).getCurrentSpeed().y);

        player.getEvents().trigger("updateWound", 2);
        speed = new Vector2(4f, 4f);
        assertEquals(speed.x, player.getComponent(PlayerActions.class).getCurrentSpeed().x);
        assertEquals(speed.y, player.getComponent(PlayerActions.class).getCurrentSpeed().y);
    }

    @Test
    void shouldTriggerDashOnlyWhenMoving() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        assertFalse(player.getComponent(PlayerActions.class).isDashing());
        player.getEvents().trigger("dash");
        assertFalse(player.getComponent(PlayerActions.class).isDashing());

        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        assertTrue(player.getComponent(PlayerActions.class).isDashing());
    }

    @Test
    void shouldTriggerDashOutsideDelay() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();

        // Regular trigger
        assertTrue(player.getComponent(PlayerActions.class).canDash());
        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        assertFalse(player.getComponent(PlayerActions.class).canDash());

        // Trigger before delay is up
        when(time.getTime()).thenReturn(10L);
        assertFalse(player.getComponent(PlayerActions.class).canDash());

        // Trigger after delay
        when(time.getTime()).thenReturn(2990L);
        assertTrue(player.getComponent(PlayerActions.class).canDash());

        // Checking delay reset
        player.getEvents().trigger("dash");
        assertFalse(player.getComponent(PlayerActions.class).canDash());
    }

    @Test
    void shouldGetRemainingDelayTime() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        assertEquals(0, player.getComponent(PlayerActions.class).getDelayTimeRemaining());

        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        assertEquals(2000, player.getComponent(PlayerActions.class).getDelayTimeRemaining());

        when(time.getTime()).thenReturn(1000L);
        assertEquals(1000, player.getComponent(PlayerActions.class).getDelayTimeRemaining());

        when(time.getTime()).thenReturn(3000L);
        assertEquals(0, player.getComponent(PlayerActions.class).getDelayTimeRemaining());
    }

    @Test
    void dashShouldTriggerInvincible() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        EventListener1 listener = mock(EventListener1.class);
        player.getEvents().addListener("invincible", listener);
        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        verify(listener).handle(100L);
    }
}