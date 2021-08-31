package com.deco2800.game.components.player;

import com.badlogic.gdx.Input;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class KeyboardPlayerInputComponentTest {

    @Test
    void shouldTriggerDashOnButtonPress() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerTimeSource(time);
        ServiceLocator.registerInputService(new InputService());
        InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForPlayer();
        Entity player = new Entity().addComponent(inputComponent);
        player.create();

        // Regular trigger
        assertTrue(player.getComponent(KeyboardPlayerInputComponent.class).canDash());
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.SHIFT_LEFT);
        assertFalse(player.getComponent(KeyboardPlayerInputComponent.class).canDash());

        // Trigger before delay is up
        when(time.getTime()).thenReturn(10L);
        assertFalse(player.getComponent(KeyboardPlayerInputComponent.class).canDash());

        // Trigger after delay
        when(time.getTime()).thenReturn(2990L);
        assertTrue(player.getComponent(KeyboardPlayerInputComponent.class).canDash());

        // Checking delay reset
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.SHIFT_LEFT);
        assertFalse(player.getComponent(KeyboardPlayerInputComponent.class).canDash());
    }

}