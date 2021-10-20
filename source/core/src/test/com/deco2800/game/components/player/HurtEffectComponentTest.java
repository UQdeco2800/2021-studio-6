package com.deco2800.game.components.player;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class HurtEffectComponentTest {

    /**
     * Simply alter parameters through constructor, and setters.
     */
    @Test
    void alterParameters() {
        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Create entity with HurtEffectComponent.
        Entity obstacle =
                new Entity()
                        .addComponent(new HurtEffectComponent(PhysicsLayer.PLAYER, 1, 2000))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent());
        obstacle.create();
        HurtEffectComponent component = obstacle.getComponent(HurtEffectComponent.class);

        // Check constructor / getters.
        assertEquals(1, component.getAttack());
        assertEquals(2000, component.getDelay());

        // Check setters.
        component.setAttack(2);
        assertEquals(2, component.getAttack());
        component.setDelay(1000);
        assertEquals(1000, component.getDelay());
    }

    /**
     * Test events to enable and disable component's effects.
     */
    @Test
    void testActive() {
        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Construct entities required to trigger events.
        Entity player = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));

        Entity obstacle = new Entity()
                .addComponent(new HurtEffectComponent(PhysicsLayer.PLAYER, 1, 2000L))
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent());

        player.create();
        obstacle.create();
        HurtEffectComponent component = obstacle.getComponent(HurtEffectComponent.class);


        assertFalse(component.isActive());          // By default / initially, should be disabled.

        obstacle.getEvents().trigger("collisionStart",
                obstacle.getComponent(HitboxComponent.class).getFixture(),
                player.getComponent(HitboxComponent.class).getFixture());
        assertTrue(component.isActive());           // Collision event, should be doing damage.

        obstacle.getEvents().trigger("collisionEnd",
                obstacle.getComponent(HitboxComponent.class).getFixture(),
                player.getComponent(HitboxComponent.class).getFixture());
        assertFalse(component.isActive());          // No collision event, should be disabled.
    }

    /**
     * Recognise if damage is being inflicted onth the player.
     * The most unstable unit test, requires A LOT of mocking.
     */
    @Test
    void testEvents() {
        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Mock ocnditions to trigger next damage event.
        GameTime time = mock(GameTime.class);
        when(time.isPaused()).thenReturn(false);
        when(time.getTimeSince(anyLong())).thenReturn(10000L);  // Should be greater than obstacle/HEC/delay.
        ServiceLocator.registerTimeSource(time);

        // Mock the CombatStatsComponent (which handles the player's stats)
        PlayerCombatStatsComponent pcsc = mock(PlayerCombatStatsComponent.class);
        doNothing().when(pcsc).hit(anyInt());       // Target function to verify.

        Entity player = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(pcsc);
        player.create();

        // Game Area should encapsulate a real entity for proper collision detection.
        GameArea gameArea = mock(GameArea.class);
        when(gameArea.getPlayer()).thenReturn(player);
        ServiceLocator.registerGameArea(gameArea);

        // Create obstacle.
        Entity obstacle = new Entity()
                .addComponent(new HurtEffectComponent(PhysicsLayer.PLAYER, 1, 2000L))
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent());
        obstacle.create();
        HurtEffectComponent component = obstacle.getComponent(HurtEffectComponent.class);

        assertFalse(component.isActive());      // Component is initially disabled.

        // trigger collision event
        obstacle.getEvents().trigger("collisionStart",
                obstacle.getComponent(HitboxComponent.class).getFixture(),
                player.getComponent(HitboxComponent.class).getFixture());

        assertTrue(component.isActive());       // Component is enabled (player colliding with object)


        // Perform update on component.
        component.update();
        verify(pcsc, times(1)).hit(any());
    }

}
