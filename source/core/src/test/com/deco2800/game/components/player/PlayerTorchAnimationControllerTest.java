package com.deco2800.game.components.player;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.items.Directions;
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
public class PlayerTorchAnimationControllerTest {
    @Mock IndependentAnimator anim;
    @Mock PlayerLightingComponent lighting;
    @Mock KeyboardPlayerInputComponent keys;
    @Mock GameTime time;
    PlayerTorchAnimationController torch;
    Entity player;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
        when(lighting.getTorchAnimator()).thenReturn(anim);
        player = new Entity().addComponent(lighting).addComponent(keys);
        player.create();
        torch = new PlayerTorchAnimationController();
        torch.setEntity(player);
        torch.setter();
    }

    @Test
    void shouldGetIndependentAnimator() {
        verify(lighting).getTorchAnimator();
        verify(anim).startAnimation("front");
    }
    @Test
    void shouldCallInTorchOnUpdate() {
        when(keys.getDirection()).thenReturn(Directions.MOVE_DOWN);
        torch.update();
        verify(anim).setPositions(0,0);
        verify(keys).getDirection();
    }

    @Test
    void shouldCallInTorchOffUpdate() {
        torch.stop(); // sets it to off
        torch.update();
        verify(anim, times(2)).stopAnimation(); // as stop calls it too, should be called twice
    }

    @Test
    void shouldChangeNewDirections() {
        when(keys.getDirection()).thenReturn(Directions.MOVE_DOWN);
        torch.update();
        verify(anim, times(2)).startAnimation("front"); // Each start animation corresponds to the correct animation for that direction
        when(keys.getDirection()).thenReturn(Directions.MOVE_UP);
        torch.update();
        verify(anim).startAnimation("back");
        when(keys.getDirection()).thenReturn(Directions.MOVE_LEFT);
        torch.update();
        verify(anim).startAnimation("left");
        when(keys.getDirection()).thenReturn(Directions.MOVE_RIGHT);
        torch.update();
        verify(anim).startAnimation("right");
    }

    @Test
    void shouldNotChangeOldDirection() {
        when(keys.getDirection()).thenReturn(Directions.MOVE_DOWN);
        torch.update();
        verify(anim, times(2)).startAnimation("front"); // front is the animation associated with moving down
        when(keys.getDirection()).thenReturn(Directions.MOVE_DOWN); // Call same direction twice
        torch.update();
        verify(anim, times(2)).setPositions(0,0); // Calls every update
        verifyNoMoreInteractions(anim); // Shouldn't call a start animation
    }

    @Test
    void shouldStop() {
        torch.stop();
        verify(anim).stopAnimation();
    }

    @Test
    void shouldDispose() {
        torch.disposeAnimation();
        verify(anim).stopAnimation();
        verify(anim).dispose();
    }
}
