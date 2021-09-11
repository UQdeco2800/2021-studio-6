package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.events.listeners.EventListener1;
import com.deco2800.game.extensions.GameExtension;
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
    @Mock EventListener0 listener0;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
    }

    @Test
    public void shouldSetGetAbility() {
        PlayerAbilitiesComponent abilities = new PlayerAbilitiesComponent(1);
        assertEquals(1, abilities.getAbility());
        abilities.setAbility(3);
        assertEquals(3, abilities.getAbility());
        abilities.setAbility(0);
        assertEquals(0, abilities.getAbility());
        abilities.setAbility(-900);
        assertEquals(0, abilities.getAbility());
        abilities.setAbility(900);
        assertEquals(0, abilities.getAbility());
    }

    @Test
    public void shouldBeOnDelay() {
        Vector2 sample = new Vector2(1,1);
        Entity player = new Entity().addComponent(new PlayerAbilitiesComponent(1));
        player.create();
        player.getEvents().addListener("invincibility", listener1);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1).handle(3000L);
        player.getEvents().trigger("tryAbility", sample);
        verifyNoMoreInteractions(listener1);

        when(time.getTime()).thenReturn(60000L); // length of delay
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1, times(2)).handle(3000L);

        when(time.getTime()).thenReturn(140000L);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1, times(3)).handle(3000L);
    }

    @Test
    public void shouldTriggerCorrectAbility() {
        Vector2 sample = new Vector2(1,1);
        when(time.getTime()).thenReturn(5L);
        Entity player = new Entity().addComponent(new PlayerAbilitiesComponent(0));
        player.create();
        player.getEvents().addListener("longDash", listener1);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1).handle(205L); // 200L is length of longDash

        when(time.getTime()).thenReturn(60005L);
        player.getComponent(PlayerAbilitiesComponent.class).setAbility(1); // ability code corresponding to invincibility
        player.getEvents().addListener("invincibility", listener1);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1).handle(3000L); // 3000L is length of invincibility

        when(time.getTime()).thenReturn(120005L);
        player.getComponent(PlayerAbilitiesComponent.class).setAbility(2);
        player.getEvents().addListener("knockback", listener0);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener0).handle();

        when(time.getTime()).thenReturn(180005L);
        player.getComponent(PlayerAbilitiesComponent.class).setAbility(3);
        player.getEvents().addListener("rangedAOE", listener0);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener0, times(2)).handle();
    }

    @Test
    void shouldntTriggerIfMovementDependentAndNotMoving() {
        Vector2 sample = new Vector2(1,1);
        when(time.getTime()).thenReturn(5L);
        Entity player = new Entity().addComponent(new PlayerAbilitiesComponent(0));
        player.create();
        player.getEvents().addListener("longDash", listener1);
        player.getEvents().trigger("tryAbility", Vector2.Zero);
        verifyNoMoreInteractions(listener1);

        player.getEvents().trigger("tryAbility", sample);
        verify(listener1).handle(205L); // 200L is length of longDash

        when(time.getTime()).thenReturn(60005L);
        player.getEvents().addListener("invincibility", listener1);
        player.getComponent(PlayerAbilitiesComponent.class).setAbility(1);
        player.getEvents().trigger("tryAbility", Vector2.Zero);
        verify(listener1).handle(3000L); // 3000L is length of invincibility

        when(time.getTime()).thenReturn(120005L);
        player.getEvents().trigger("tryAbility", sample);
        verify(listener1, times(2)).handle(3000L); // 3000L is length of invincibility

    }
}