package com.deco2800.game.components.player;

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
        Entity player = new Entity().addComponent(new PlayerAbilitiesComponent(1));
        player.create();
        player.getEvents().addListener("shortInvincibility", listener0);
        player.getEvents().trigger("tryAbility");
        verify(listener0).handle();
        player.getEvents().trigger("tryAbility");
        verifyNoMoreInteractions(listener0);

        when(time.getTime()).thenReturn(5000L);
        player.getEvents().trigger("tryAbility");
        verifyNoMoreInteractions(listener0);

        when(time.getTime()).thenReturn(60000L);
        player.getEvents().trigger("tryAbility");
        verify(listener0, times(2)).handle();
    }

    @Test
    public void shouldTriggerCorrectAbility() {
        when(time.getTime()).thenReturn(5L);
        Entity player = new Entity().addComponent(new PlayerAbilitiesComponent(0));
        player.create();
        player.getEvents().addListener("longDash", listener1);
        player.getEvents().trigger("tryAbility");
        verify(listener1).handle(355L);

        when(time.getTime()).thenReturn(70000L);
        player.getComponent(PlayerAbilitiesComponent.class).setAbility(1);
        player.getEvents().addListener("shortInvincibility", listener0);
        player.getEvents().trigger("tryAbility");
        verify(listener0).handle();
    }
}