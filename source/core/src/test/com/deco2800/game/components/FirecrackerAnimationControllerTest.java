package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.player.PlayerAnimationController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class FirecrackerAnimationControllerTest {
    @Mock AnimationRenderComponent anim;
    @Mock GameTime time;
    Entity player;
    FirecrackerAnimationController fire;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
        player = new Entity().addComponent(new PlayerAnimationController()).addComponent(anim);
        player.create();
        fire = new FirecrackerAnimationController();
        fire.setEntity(player);
        fire.create();
    }

    @Test
    void shouldSetDirection() {
        player.getEvents().trigger("firecrackerStart", Directions.MOVE_LEFT);
        verify(anim).startAnimation("firecrackerRev");
        player.getEvents().trigger("firecrackerStart", Directions.MOVE_DOWN);
        verify(anim).startAnimation("firecracker");
        player.getEvents().trigger("firecrackerStart", Directions.MOVE_UP);
        verify(anim, times(2)).startAnimation("firecrackerRev");
        player.getEvents().trigger("firecrackerStart", Directions.MOVE_RIGHT);
        verify(anim, times(2)).startAnimation("firecracker");
    }

    @Test
    void shouldExplode() {
        // Set independent animator
        IndependentAnimator independent = mock(IndependentAnimator.class);
        fire.setAnimator(independent);
        // trigger explosion
        player.getEvents().trigger("explosionStart");
        // check effect of explosion
        verify(anim).stopAnimation();
        verify(anim).setPaused(FALSE);
        verify(independent).setPositions(-0.5f,-0.5f); // the correct position based on how the player is set up
        verify(independent).startAnimation("explosion");
    }

    @Test
    void shouldLoop() {
        // Set independent animator
        IndependentAnimator independent = mock(IndependentAnimator.class);
        fire.setAnimator(independent);
        /// trigger the animations
        player.getEvents().trigger("explosionLoop");
        verify(independent).setPositions(-0.5f,-0.5f); // the correct position based on how the player is set up
        verify(independent).startAnimation("explosionLoop");
    }

    @Test
    void shouldPauseThrow() {
        player.getEvents().trigger("pauseFirecracker");
        verify(anim).setPaused(TRUE);
    }

    @Test
    void shouldStopAnimations() {
        // Set independent animator
        IndependentAnimator independent = mock(IndependentAnimator.class);
        fire.setAnimator(independent);
        /// trigger the animation stop
        player.getEvents().trigger("endFirecracker");
        verify(anim).stopAnimation();
        verify(independent).stopAnimation();
    }

    @Test
    void shouldDisposeOnlyMainAnimator() {
        player.getEvents().trigger("dispose");
        verify(anim).dispose();
    }

    @Test
    void shouldDisposeBoth() {
        // Set independent animator
        IndependentAnimator independent = mock(IndependentAnimator.class);
        fire.setAnimator(independent);
        // Trigger disposal
        player.getEvents().trigger("dispose");
        verify(anim).dispose();
        verify(independent).dispose();
    }

    @Test
    void shouldUpdate() {
        // Set independent animator
        IndependentAnimator independent = mock(IndependentAnimator.class);
        fire.setAnimator(independent);
        // explosion not active and not finished
        when(independent.isFinished()).thenReturn(FALSE);
        fire.update();
        verifyNoInteractions(independent);
        // explosion active but not finished
        player.getEvents().trigger("explosionStart"); // Start the explosion
        fire.update();
        verify(independent, times(2)).setPositions(-0.5f, -0.5f); // as it is also called by explosion start
        // explosion active and finished
        when(independent.isFinished()).thenReturn(TRUE);
        fire.update();
        verify(independent, times(3)).setPositions(-0.5f, -0.5f); // loop also calls this
        verify(independent).startAnimation("explosionLoop"); // calls loop which triggers this animation
    }
}
