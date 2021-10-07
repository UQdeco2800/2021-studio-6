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
public class PlayerWeaponAnimationControllerTest {
    @Mock IndependentAnimator anim;
    @Mock PlayerMeleeAttackComponent melee;
    @Mock KeyboardPlayerInputComponent keys;
    @Mock GameTime time;
    PlayerWeaponAnimationController weapon;
    Entity player;
    Float[][] coords;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
        Float[] arrayOne = {0f, 1f}; // setting unique values to each array position to test correct is grabbed later
        Float[] arrayTwo = {2f, 3f};
        Float[] arrayThree = {4f, 5f};
        Float[] arrayFour = {6f, 7f};
        coords = new Float[][] {arrayOne, arrayTwo, arrayThree, arrayFour};
        when(melee.getAnimator()).thenReturn(anim);
        player = new Entity().addComponent(melee).addComponent(keys);
        player.create();
        weapon = new PlayerWeaponAnimationController();
        weapon.setEntity(player);
        weapon.setter(coords);
    }

    @Test
    void shouldGetIndependentAnimator() {
        verify(melee).getAnimator();
    }

    @Test
    void shouldCheckIfAnimationIsFinished() {
        when(anim.isFinished()).thenReturn(false);
        weapon.update();
        verify(anim, never()).stopAnimation();

        when(anim.isFinished()).thenReturn(true);
        weapon.update();
        verify(anim).stopAnimation();
    }

    @Test
    void ShouldAttack() {
        when(keys.getDirection()).thenReturn(Directions.MOVE_DOWN);
        weapon.attack();
//        verify(anim).setPositions(2f, 3f); // all floats correspond to floats in this test represent the correct position for animation
        verify(anim).startAnimation("attackDown");

        when(keys.getDirection()).thenReturn(Directions.MOVE_UP);
        weapon.attack();
//        verify(anim).setPositions(0f, 1f);
        verify(anim).startAnimation("attackUp");

        when(keys.getDirection()).thenReturn(Directions.MOVE_LEFT);
        weapon.attack();
//        verify(anim).setPositions(4f, 5f);
        verify(anim).startAnimation("attackLeft");

        when(keys.getDirection()).thenReturn(Directions.MOVE_RIGHT);
        weapon.attack();
//        verify(anim).setPositions(6f, 7f);
        verify(anim).startAnimation("attackRight");

    }

    @Test
    void shouldStop() {
        weapon.stop();
        verify(anim).stopAnimation();
    }

    @Test
    void shouldDispose() {
        weapon.disposeAnimation();
        verify(anim).dispose();
    }
}
