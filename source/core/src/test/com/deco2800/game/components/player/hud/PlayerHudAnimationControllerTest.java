package com.deco2800.game.components.player.hud;

import com.deco2800.game.components.player.PlayerInterfaceDisplay;
import com.deco2800.game.entities.Entity;
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
public class PlayerHudAnimationControllerTest {
    @Mock IndependentAnimator anim;
    @Mock PlayerInterfaceDisplay display;
    @Mock GameTime time;
    Entity player;
    PlayerHudAnimationController hud;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
        when(display.getDashAnimator()).thenReturn(anim);
        player = new Entity().addComponent(display);
        player.create();
        hud = new PlayerHudAnimationController();
        hud.setEntity(player);
        hud.setter();
    }

    @Test
    void shouldSet() {
        verify(display).getDashAnimator();
        verify(anim).startAnimation("dashbarFull");
    }

    @Test
    void shouldStart() {
        player.getEvents().trigger("start");
        verify(anim, times(2)).startAnimation("dashbarFull"); // already called once by setter
    }

    @Test
    void shouldDashStart() {
        player.getEvents().trigger("dashBar");
        verify(anim).startAnimation("dashbar"); // already called once by setter so calls twice
    }

    @Test
    void shouldDispose() {
        player.getEvents().trigger("dispose");
        verify(anim).dispose();
    }
}
