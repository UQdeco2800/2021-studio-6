package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener1;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.items.Abilities;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class PlayerAbilitiesComponentTest {
    @Mock GameTime time;
    @Mock EventListener1 listener1;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
    }

    @Test
    public void shouldSetGetAbility() {
        PlayerAbilitiesComponent abilities = new PlayerAbilitiesComponent(Abilities.NONE);
        assertEquals(Abilities.NONE, abilities.getAbility());
        abilities.setAbility(Abilities.INVINCIBILITY);
        assertEquals(Abilities.INVINCIBILITY, abilities.getAbility());
    }

    @Test
    public void shouldBeOnDelay() {
        Vector2 sample = new Vector2(1,1); // random direction for testing
        Entity player = new Entity().addComponent(new PlayerAbilitiesComponent(Abilities.INVINCIBILITY));
        player.create();
        player.getEvents().addListener("invincibility", listener1);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1).handle(3000L); // 3000L is set length of long dash
        player.getEvents().trigger("tryAbility", sample);
        verifyNoMoreInteractions(listener1);

        when(time.getTime()).thenReturn(60000L); // length of delay (60000L long)
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1, times(2)).handle(3000L);

        when(time.getTime()).thenReturn(140000L); // length of next delay (60000L long)
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1, times(3)).handle(3000L);
    }

    @Test
    public void shouldTriggerCorrectAbility() {
        Vector2 sample = new Vector2(1,1); // random direction for testing
        when(time.getTime()).thenReturn(5L); // random start time
        Entity player = new Entity().addComponent(new PlayerAbilitiesComponent(Abilities.LONG_DASH));
        player.create();
        player.getEvents().addListener("longDash", listener1);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1).handle(205L); // 200L is length of longDash

        when(time.getTime()).thenReturn(60005L); // after ability delay (60000L long)
        player.getComponent(PlayerAbilitiesComponent.class).setAbility(Abilities.INVINCIBILITY);
        player.getEvents().addListener("invincibility", listener1);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1).handle(3000L); // 3000L is length of invincibility

        when(time.getTime()).thenReturn(120005L); // after the next ability delay (60000L long)
        player.getComponent(PlayerAbilitiesComponent.class).setAbility(Abilities.NONE);
        player.getEvents().trigger("tryAbility", sample);
        verifyNoMoreInteractions(listener1);
        player.getComponent(PlayerAbilitiesComponent.class).setAbility(Abilities.INVINCIBILITY);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1, times(2)).handle(3000L);
    }

    @Test
    void shouldntTriggerIfMovementDependentAndNotMoving() {
        Vector2 sample = new Vector2(1,1); // random direction for testing
        when(time.getTime()).thenReturn(5L); // random start time
        Entity player = new Entity().addComponent(new PlayerAbilitiesComponent(Abilities.LONG_DASH));
        player.create();
        player.getEvents().addListener("longDash", listener1);
        player.getEvents().trigger("tryAbility", Vector2.Zero);
        verifyNoMoreInteractions(listener1);

        player.getEvents().trigger("tryAbility", sample);
        verify(listener1).handle(205L); // 200L is length of longDash

        when(time.getTime()).thenReturn(60005L); // after ability delay (60000L long)
        player.getEvents().addListener("invincibility", listener1);
        player.getComponent(PlayerAbilitiesComponent.class).setAbility(Abilities.INVINCIBILITY);
        player.getEvents().trigger("tryAbility", Vector2.Zero);
        verify(listener1).handle(3000L); // 3000L is length of invincibility

        when(time.getTime()).thenReturn(120005L); // after next ability delay (60000L long)
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1, times(2)).handle(3000L); // 3000L is length of invincibility

    }
}