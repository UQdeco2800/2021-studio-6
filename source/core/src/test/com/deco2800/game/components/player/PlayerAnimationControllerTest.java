package com.deco2800.game.components.player;

import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.items.Directions;
import com.deco2800.game.rendering.AnimationRenderComponent;
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
class PlayerAnimationControllerTest {
    @Mock AnimationRenderComponent anim;
    @Mock PlayerActions actions;
    @Mock PlayerCombatStatsComponent combat;
    @Mock KeyboardPlayerInputComponent keys;
    @Mock GameTime time;
    Entity player;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
        player = new Entity().addComponent(new PlayerAnimationController()).addComponent(anim).addComponent(actions).addComponent(combat).addComponent(keys);
        player.create();
    }

    @Test
    void shouldStartFacingFront() {
        verify(anim).startAnimation("front");
    }

    @Test
    void shouldAnimateWalkingLeft() {
        when(actions.isMoving()).thenReturn(true);
        when(combat.getDefenceLevel()).thenReturn(0);
        when(keys.getDirection()).thenReturn(Directions.MOVE_LEFT);
        player.update();
        verify(anim).startAnimation("left-run");
    }

    @Test
    void shouldAnimateIdleRight() {
        when(actions.isMoving()).thenReturn(false);
        when(combat.getDefenceLevel()).thenReturn(0);
        when(keys.getDirection()).thenReturn(Directions.MOVE_RIGHT);
        player.update();
        verify(anim).startAnimation("right");
    }

    @Test
    void shouldAnimateHurtDown() {
        player.getEvents().trigger("hurt");
        when(actions.isMoving()).thenReturn(true);
        when(combat.getDefenceLevel()).thenReturn(0);
        when(keys.getDirection()).thenReturn(Directions.MOVE_DOWN);
        player.update();
        verify(anim).startAnimation("front-run-hurt");

        when(time.getTimeSince(0L)).thenReturn(1000L); // after duration of hurt animation
        player.update();
        verify(anim).startAnimation("front-run");
    }

    @Test
    void shouldhaveDefenceOneUp() {
        when(actions.isMoving()).thenReturn(true);
        when(combat.getDefenceLevel()).thenReturn(2);
        when(keys.getDirection()).thenReturn(Directions.MOVE_UP);
        player.update();
        verify(anim).startAnimation("back-run-helmet");
    }

    @Test
    void shouldhaveDefenceTwoLeftIdle() {
        when(actions.isMoving()).thenReturn(false);
        when(combat.getDefenceLevel()).thenReturn(5);
        when(keys.getDirection()).thenReturn(Directions.MOVE_LEFT);
        player.update();
        verify(anim).startAnimation("left-armour");
    }

    @Test
    void shouldAnimateInvincibility() {
        // set up arbitrary direction, defence and movement
        when(actions.isMoving()).thenReturn(true);
        when(combat.getDefenceLevel()).thenReturn(0);
        when(keys.getDirection()).thenReturn(Directions.MOVE_UP);
        when(time.getTime()).thenReturn(0L);
        // Setting up required independent animator
        IndependentAnimator independent = mock(IndependentAnimator.class);
        player.getComponent(PlayerAnimationController.class).setAnimator(independent);
        // Test invincibility not active and time condition false
        player.update();
        verifyNoMoreInteractions(independent);
        // Start invincibility
        player.getComponent(PlayerAnimationController.class).invincibleStart(1L); // random number for testing
        verify(independent).startAnimation("active");
        // test invincibility active and time condition is false
        player.update();
        verifyNoMoreInteractions(independent);
        // test active and time condition is true
        when(time.getTime()).thenReturn(3L); // arbitrary number after 1L
        player.update();
        verify(independent).stopAnimation(); // sets active condition to false
        // test with false active condition and true time condition
        player.update();
        verifyNoMoreInteractions(independent);
    }

    @Test
    void shouldStartInvincibility() {
        // Setting up independent animator
        IndependentAnimator independent = mock(IndependentAnimator.class);
        player.getComponent(PlayerAnimationController.class).setAnimator(independent);
        // Starting invincibility
        player.getComponent(PlayerAnimationController.class).invincibleStart(100L); // random number for testing
        verify(independent).startAnimation("active");
    }

    @Test
    void shouldDisposeOnlyAnimator() {
        player.getComponent(PlayerAnimationController.class).dispose();
        verify(anim).dispose();
    }

    @Test
    void shouldDisposeBothAnimators() {
        // Setting up independent animator
        IndependentAnimator independent = mock(IndependentAnimator.class);
        player.getComponent(PlayerAnimationController.class).setAnimator(independent);
        // Dispose
        player.getComponent(PlayerAnimationController.class).dispose();
        verify(anim).dispose();
        verify(independent).dispose();
    }
}
