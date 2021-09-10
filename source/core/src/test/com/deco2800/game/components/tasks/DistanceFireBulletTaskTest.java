package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class DistanceFireBulletTaskTest {
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = new GameTime();
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldFireOnce() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        AITaskComponent ai = new AITaskComponent().addTask(new DistanceFireBulletTask(target, 0.1f, 5, 10));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);
        ArrayList<Integer> check = new ArrayList<>();
        entity.getEvents().addListener("fire", () ->  check.add(1));


        long start = ServiceLocator.getTimeSource().getTime();
        while (ServiceLocator.getTimeSource().getTimeSince(start) < 150) {

            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();

        }
        assertEquals(1, check.size());
        assertEquals(1, check.get(0));
    }
    @Test
    void shouldFireMany() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);
        int FIREAMOUNT = 5;
        AITaskComponent ai = new AITaskComponent().addTask(new DistanceFireBulletTask(target, 0.1f, 5, 10));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);
        ArrayList<Integer> check = new ArrayList<>();
        entity.getEvents().addListener("fire", () ->  check.add(1));


        long start = ServiceLocator.getTimeSource().getTime();
        while (ServiceLocator.getTimeSource().getTimeSince(start) < FIREAMOUNT * 100 + 50) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        assertEquals(FIREAMOUNT, check.size());
        for(int i = 0; i < FIREAMOUNT; i++){
            assertEquals(1, check.get(i));
        }
    }

    @Test
    void shouldOnlyFireWithinDistance() {
        Entity target = new Entity();
        target.setPosition(0f, 11f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        DistanceFireBulletTask fireTask = new DistanceFireBulletTask(target, 0.1f, 5, 10);
        fireTask.create(() -> entity);

        // Not currently active, target is too far, should have negative priority
        assertTrue(fireTask.getPriority() < 0);

        // When in view distance, should give higher priority
        target.setPosition(0f, 4f);
        assertEquals(5, fireTask.getPriority());

        // When active, should chase if within chase distance
        target.setPosition(0f, 8f);
        fireTask.start();
        assertEquals(5, fireTask.getPriority());

        // When active, should not chase outside chase distance
        target.setPosition(0f, 12f);
        assertTrue(fireTask.getPriority() < 0);
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }




}
