package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.World;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class DisposingComponentTest {
    @Mock GameTime gameTime;
    @Mock World world;

    @Test
    void queueShouldBeEmpty() {
        PhysicsEngine engine = new PhysicsEngine(world, gameTime);
        assertTrue(engine.getReuseQueue().isEmpty());
    }

    @Test
    void shouldBeAddedForDisposalOrRemoval() {
        PhysicsService physicsService = new PhysicsService();
        EntityService entityService = new EntityService();

        ServiceLocator.registerPhysicsService(physicsService);
        ServiceLocator.registerEntityService(entityService);

        PhysicsEngine physicsEngine = physicsService.getPhysics();

        assertTrue(physicsEngine.getReuseQueue().isEmpty());

        Entity entity = new Entity();
        entity.addComponent(new DisposingComponent());
        entity.create();
        entity.update();

        Entity anotherEntity = new Entity();
        anotherEntity.addComponent(new DisposingComponent());
        anotherEntity.create();
        anotherEntity.update();

        anotherEntity.getComponent(DisposingComponent.class).toBeReused();

        assertNotNull(physicsEngine.getReuseQueue());
        assertEquals(1, physicsEngine.getReuseQueue().size());
    }
}
