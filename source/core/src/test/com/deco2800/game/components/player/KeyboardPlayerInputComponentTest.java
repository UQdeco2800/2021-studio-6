package com.deco2800.game.components.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.events.listeners.EventListener1;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class KeyboardPlayerInputComponentTest {
    @Mock GameTime time;
    @Mock InputService inputService;
    @Mock EventListener1 listener1;
    @Mock EventListener0 listener0;
    @Mock EventListener0 listener0_2;
    Entity player;

    @BeforeEach
    public void beforeEach() {
        ServiceLocator.registerTimeSource(time);
        ServiceLocator.registerInputService(inputService);
        player = new Entity().addComponent(new KeyboardPlayerInputComponent());
        player.create();
    }
/*
    @Test
    public void shouldTriggerWalkingAnimationVertical() {
        player.getEvents().addListener("moveUp", listener0);
        player.getEvents().addListener("moveDown", listener0_2);
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.W);
        verify(listener0).handle();
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.S);
        verify(listener0_2).handle();
    }

 */

    @Test
    public void shouldTriggerWalkingVertical() {
        player.getEvents().addListener("walk", listener1);
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.W);
        verify(listener1).handle(new Vector2(0f, 1f));
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.S);
        verify(listener1).handle(new Vector2(0f, 0f));
    }
/*
    @Test
    public void shouldTriggerWalkingAnimationHorizontal() {
        player.getEvents().addListener("moveLeft", listener0);
        player.getEvents().addListener("moveRight", listener0_2);
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.A);
        verify(listener0).handle();
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.D);
        verify(listener0_2).handle();
    }

 */

    @Test
    public void shouldSetWalkingHorizontal() {
        player.getEvents().addListener("walk", listener1);
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.A);
        verify(listener1).handle(new Vector2(-1f,0f));
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.D);
        verify(listener1).handle(new Vector2(0f,0f));
    }

    @Test
    public void shouldStopWalking() {
        player.getEvents().addListener("walkStop", listener0);
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.A);
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.D);
        verify(listener0).handle();
    }

    @Test
    public void shouldntTriggerDashOnPause() {
        player.getEvents().addListener("dash", listener0);
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.SHIFT_LEFT);
        verify(listener0).handle();

        when(time.isPaused()).thenReturn(true);
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.SHIFT_LEFT);
        verifyNoMoreInteractions(listener0);
    }
}