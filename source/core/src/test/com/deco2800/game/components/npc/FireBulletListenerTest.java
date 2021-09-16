package com.deco2800.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.npc.FireBulletListener;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyBulletFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import javafx.application.Preloader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class FireBulletListenerTest {


    GameArea gameArea;

    FireBulletListener fireBulletListener;
    private MockedStatic<EnemyBulletFactory> mockEnemyBulletFactory;
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        mockEnemyBulletFactory = mockStatic(EnemyBulletFactory.class);
    }
    @AfterEach
    void AfterEach() {
        mockEnemyBulletFactory.close();
    }

    @Test
    void testFireListenOnce(){
        GameArea mockGameArea = mock(GameArea.class);
        TextureRenderComponent textureRenderComponent = mock(TextureRenderComponent.class);
        Entity player = new Entity();
        Entity enemy = new Entity().addComponent(new FireBulletListener(player, mockGameArea));

        player.create();
        enemy.create();
        enemy.getEvents().trigger("fire");
        mockEnemyBulletFactory.when(() -> EnemyBulletFactory.createBullet(enemy, player, mockGameArea)).then(InvocationOnMock -> null);

        mockEnemyBulletFactory.verify(times(1),
                () -> EnemyBulletFactory.createBullet(enemy, player, mockGameArea));




    }
    @Test
    void testFireListenMany() {
        int FIREAMOUNT = 100;
        GameArea mockGameArea = mock(GameArea.class);
        TextureRenderComponent textureRenderComponent = mock(TextureRenderComponent.class);
        Entity player = new Entity();
        Entity enemy = new Entity().addComponent(new FireBulletListener(player, mockGameArea));

        player.create();
        enemy.create();
        for (int i = 0; i < FIREAMOUNT; i++) {
            enemy.getEvents().trigger("fire");
        }
        mockEnemyBulletFactory.when(() -> EnemyBulletFactory.createBullet(enemy, player, mockGameArea)).then(InvocationOnMock -> null);

        mockEnemyBulletFactory.verify(times(FIREAMOUNT),
                () -> EnemyBulletFactory.createBullet(enemy, player, mockGameArea));
    }
}
