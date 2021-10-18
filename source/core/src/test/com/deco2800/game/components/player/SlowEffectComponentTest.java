package com.deco2800.game.components.player;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class SlowEffectComponentTest {

    /**
     * Simply alter parameters through constructor, and setters.
     */
    @Test
    void alterParameters() {
        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Create entity with HurtEffectComponent.
        Entity obstacle =
                new Entity()
                        .addComponent(new SlowEffectComponent(PhysicsLayer.PLAYER, 100))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent());
        obstacle.create();
        SlowEffectComponent component = obstacle.getComponent(SlowEffectComponent.class);

        assertEquals(100, component.getSpeed());

        component.setSpeed(50);
        assertEquals(50, component.getSpeed());

        component.setSpeed(0);  // minimum limit of 10%
        assertEquals(10, component.getSpeed());
    }


    /**
     * Test events to enable and disable component's effects.
     */
    @Test
    void testActive() {
        int speedModifier = 50;  // % speed modifier (>10%)

        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Mock target function.
        PlayerActions pa = mock(PlayerActions.class);
        doNothing().when(pa).setScaleSpeed(any(), any());
        doNothing().when(pa).clearScaleSpeed(any());

        // Construct entities required to trigger events.
        Entity player = new Entity()
                .addComponent(pa)
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));

        Entity obstacle = new Entity()
                .addComponent(new SlowEffectComponent(PhysicsLayer.PLAYER, speedModifier))
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent());

        player.create();
        obstacle.create();
        SlowEffectComponent component = obstacle.getComponent(SlowEffectComponent.class);

        assertFalse(component.isActive());          // By default / initially, should be disabled.

        // Start slowness effect.
        obstacle.getEvents().trigger("collisionStart",
                obstacle.getComponent(HitboxComponent.class).getFixture(),
                player.getComponent(HitboxComponent.class).getFixture());
        assertTrue(component.isActive());           // Collision event, should be doing damage.
        verify(pa, times(1)).setScaleSpeed(any(), speedModifier);

        // Clear slowness effect.
        obstacle.getEvents().trigger("collisionEnd",
                obstacle.getComponent(HitboxComponent.class).getFixture(),
                player.getComponent(HitboxComponent.class).getFixture());
        assertFalse(component.isActive());          // No collision event, should be disabled.
        verify(pa, times(1)).clearScaleSpeed(any());

    }


}
