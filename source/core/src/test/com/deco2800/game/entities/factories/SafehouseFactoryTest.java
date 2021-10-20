package com.deco2800.game.entities.factories;

import com.deco2800.game.components.ItemComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class SafehouseFactoryTest {

    @BeforeEach
    void beforeEach() {
        // Setup resource locator to load speech bubble texture and npc movement atlas
        String[] textures = {"images/safehouse/exterior-day1-latest.png"};
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadTextures(textures);
        ServiceLocator.getResourceService().loadAll();

        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldCreateSafehouse() {
        Entity safehouse = SafehouseFactory.createSafehouse();
        assertEquals(PhysicsLayer.SAFEHOUSE,safehouse.getComponent(HitboxComponent.class).getLayer());
    }
}
