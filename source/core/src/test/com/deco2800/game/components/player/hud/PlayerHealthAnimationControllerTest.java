package com.deco2800.game.components.player.hud;

import com.deco2800.game.components.player.PlayerInterfaceDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class PlayerHealthAnimationControllerTest {
    @Mock IndependentAnimator anim;
    @Mock PlayerInterfaceDisplay display;
    @Mock GameTime time;
    @Mock EventListener0 listener;
    Entity player;
    PlayerHealthAnimationController health;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
        when(display.getHealthAnimator()).thenReturn(anim);
        player = new Entity().addComponent(display);
        player.create();
        health = new PlayerHealthAnimationController();
        health.setEntity(player);
        health.setter();
    }

    @Test
    void shouldStartAnim() {
        verify(display).getHealthAnimator();
        verify(anim).startAnimation("health1"); // the standard starting animation
    }

    @Test
    void shouldChange() {
        player.getEvents().addListener("firstHit", listener);
        player.getEvents().trigger("health", 1); // set to health 1, should play animation health1
        verify(listener).handle();
        verify(anim, times(2)).startAnimation("health1"); // 2 times as setter function also calls it

        // now player should be hit
        player.getEvents().trigger("health", 2); // set to health 2, should play animation health2
        verifyNoMoreInteractions(listener);
        verify(anim).startAnimation("health2");
    }

    @Test
    void shouldDispose() {
        player.getEvents().trigger("dispose");
        verify(anim).dispose();
    }
}
