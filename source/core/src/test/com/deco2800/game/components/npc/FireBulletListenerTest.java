package com.deco2800.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.npc.FireBulletListener;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyBulletFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class FireBulletListenerTest {


    GameArea gameArea;

    FireBulletListener fireBulletListener;

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }


    @Test
    void testFireListenOnce(){
        GameArea mockGameArea = mock(GameArea.class);
        Entity player = new Entity();
        Entity enemy = new Entity().addComponent(new FireBulletListener(player, mockGameArea));
        try (MockedStatic<EnemyBulletFactory> theMock = Mockito.mockStatic(EnemyBulletFactory.class)) {
            theMock.when(() -> EnemyBulletFactory.createBullet(enemy, player, mockGameArea)).thenReturn(new Entity());
            player.create();
            enemy.create();
            enemy.getEvents().trigger("fire");
            verify(mockGameArea).spawnEntity(any(Entity.class));
        }
    }


}
