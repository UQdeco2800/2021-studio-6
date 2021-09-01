package com.deco2800.game.components.player;

import com.badlogic.gdx.Input;
import com.deco2800.game.components.CombatStatsComponent;
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
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class PlayerActionsTest {

    @Test
    void shouldSetInitialSpeed() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        PlayerActions action = new PlayerActions(3);
        Vector2 speed = new Vector2(3f, 3f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(0);
        speed = new Vector2(0f, 0f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(20);
        speed = new Vector2(3f, 3f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(-20);
        speed = new Vector2(0f, 0f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);
    }

    @Test
    void shouldChangeSpeedWhenWounded() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(new PlayerCombatStatsComponent(3, 5, 3, 5, 0));
        player.create();
        Vector2 speed = new Vector2(3f, 3f);
        assertEquals(speed.x, player.getComponent(PlayerActions.class).getCurrentSpeed().x);
        assertEquals(speed.y, player.getComponent(PlayerActions.class).getCurrentSpeed().y);

        player.getComponent(PlayerCombatStatsComponent.class).setWoundState(2);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        speed = new Vector2(4f, 4f);
        assertEquals(speed.x, player.getComponent(PlayerActions.class).getCurrentSpeed().x);
        assertEquals(speed.y, player.getComponent(PlayerActions.class).getCurrentSpeed().y);
    }

    @Test
    void shouldTriggerDashOnlyWhenMoving() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForPlayer();
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(inputComponent).addComponent(new PlayerCombatStatsComponent(3, 5, 3, 5, 0));
        player.create();

        player.getEvents().trigger("dash");
        assertFalse(player.getComponent(PlayerActions.class).getDashState());

        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.W);
        player.getEvents().trigger("dash");
        assertTrue(player.getComponent(PlayerActions.class).getDashState());
    }

    @Test
    void shouldTriggerDashOutsideDelay() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForPlayer();
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(inputComponent).addComponent(new PlayerCombatStatsComponent(3, 5, 3, 5, 0));
        player.create();

        // Regular trigger
        assertTrue(player.getComponent(PlayerActions.class).canDash());
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.W);
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.SHIFT_LEFT);
        assertFalse(player.getComponent(PlayerActions.class).canDash());

        // Trigger before delay is up
        when(time.getTime()).thenReturn(10L);
        assertFalse(player.getComponent(PlayerActions.class).canDash());

        // Trigger after delay
        when(time.getTime()).thenReturn(2990L);
        assertTrue(player.getComponent(PlayerActions.class).canDash());

        // Checking delay reset
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.SHIFT_LEFT);
        assertFalse(player.getComponent(PlayerActions.class).canDash());
    }

    @Test
    void dashShouldSetInvincible() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForPlayer();
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(inputComponent).addComponent(new PlayerCombatStatsComponent(3, 5, 3, 5, 0));
        player.create();

        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.W);
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.SHIFT_LEFT);

        CombatStatsComponent enemy = new CombatStatsComponent(100, 2);

        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
    }
}