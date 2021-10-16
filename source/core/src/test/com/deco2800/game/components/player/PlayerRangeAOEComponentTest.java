package com.deco2800.game.components.player;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PlayerRangeAOEComponentTest {
    @Mock GameTime gameTime;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Setup resource locator to load speech bubble texture and npc movement atlas
        String[] speechTextures = {"images/dialogue/raw/npc_indicator.png"};
        String[] atlas = {"images/playeritems/firecracker/firecracker.atlas"};
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadTextures(speechTextures);
        ServiceLocator.getResourceService().loadTextureAtlases(atlas);
        ServiceLocator.getResourceService().loadAll();

        //Setup lighting service
        ServiceLocator.registerUnlitRenderService(new RenderService());
        ServiceLocator.registerRenderService(new RenderService());
    }

//    Entity createFireCracter() {
//        Entity fireCracker = new Entity()
//                .addComponent(new ColliderComponent().setSensor(true))
//                .addComponent(new PhysicsMovementComponent())
//                .addComponent()
//    }

//    Entity createPlayer() {
//        Entity player = new Entity()
//                .addComponent(new PhysicsComponent())
//                .addComponent(new PlayerRangeAOEComponent())
//                .addComponent()
//    }
}
