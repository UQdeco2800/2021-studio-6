package com.deco2800.game.components.player;

import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.items.Directions;
import com.deco2800.game.rendering.AnimationRenderComponent;
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
        when(combat.getDefenceLevel()).thenReturn(1);
        when(keys.getDirection()).thenReturn(Directions.MOVE_UP);
        player.update();
        verify(anim).startAnimation("back-run-helmet");
    }

    @Test
    void shouldhaveDefenceTwoLeftIdle() {
        when(actions.isMoving()).thenReturn(false);
        when(combat.getDefenceLevel()).thenReturn(2);
        when(keys.getDirection()).thenReturn(Directions.MOVE_LEFT);
        player.update();
        verify(anim).startAnimation("left-armour");
    }
}
