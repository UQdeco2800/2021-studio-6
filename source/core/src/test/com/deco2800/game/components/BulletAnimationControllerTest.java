package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.player.PlayerAnimationController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.extensions.GameExtension;
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
public class BulletAnimationControllerTest {
    @Mock AnimationRenderComponent anim;
    @Mock GameTime time;
    Entity player;
    BulletAnimationController bullet;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
        player = new Entity().addComponent(new PlayerAnimationController()).addComponent(anim);
        player.create();
        bullet = new BulletAnimationController();
        bullet.setEntity(player);
        bullet.create();
    }

    @Test
    void shouldBreakRock() {
        player.getEvents().trigger("breakRock");
        verify(anim).startAnimation("rock");
    }

    @Test
    void shouldSetDirection() {
        bullet.setDirection(new Vector2(1,0));
        verify(anim).startAnimation("right");
        bullet.setDirection(new Vector2(-1,0));
        verify(anim).startAnimation("left");
        bullet.setDirection(new Vector2(0,1));
        verify(anim).startAnimation("up");
        bullet.setDirection(new Vector2(0,-1));
        verify(anim).startAnimation("down");
    }

    @Test
    void shouldUpdate() {
        EventListener0 listener = mock(EventListener0.class);
        player.getEvents().addListener("rockDone", listener);
        // not started
        bullet.update();
        verifyNoInteractions(listener);
        // Only is finished condition true
        when(anim.isFinished()).thenReturn(Boolean.TRUE);
        bullet.update();
        verifyNoInteractions(listener);
        // Only started condition true
        when(anim.isFinished()).thenReturn(Boolean.FALSE);
        bullet.breakRock();
        bullet.update();
        verifyNoInteractions(listener);
        // Both conditions true
        when(anim.isFinished()).thenReturn(Boolean.TRUE);
        bullet.update();
        verify(listener).handle();
    }
}
