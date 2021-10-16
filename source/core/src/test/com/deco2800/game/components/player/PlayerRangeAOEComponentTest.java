package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.game.components.FireCrackerCollisionComponent;
import com.deco2800.game.components.FirecrackerAnimationController;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.lighting.FlickerLightComponent;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PlayerRangeAOEComponentTest {
    @Mock GameTime gameTime;
    int HEALTH = 3;
    int DAMAGE = 2;
    int WOUND = 3;
    int DEFENCE = 0;
    @Mock Entity fireCracker;
    @Mock Entity player;

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

        fireCracker = createFireCracter();
        player = createPlayer();
    }

    @Test
    void shouldAddAndFireFireCracker() {
        player.getComponent(PlayerRangeAOEComponent.class).addFireCracker(fireCracker);
        player.getComponent(PlayerRangeAOEComponent.class).fire(2000);

        // since fire cracker launched - need to check if it is moving
        assertTrue(fireCracker.getComponent(PhysicsMovementComponent.class).getMoving());
    }

    Entity createFireCracter() {
        return new Entity()
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new FirecrackerAnimationController())
                .addComponent(new FireCrackerCollisionComponent())
                .addComponent(new FlickerLightComponent(Color.RED, Color.SCARLET, Color.MAROON, Color.SALMON,
                        0, 0, 0))
                .addComponent(new PlayerCombatStatsComponent(HEALTH, DAMAGE, WOUND,
                        DAMAGE, DEFENCE));
    }

    Entity createPlayer() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PlayerRangeAOEComponent())
                .addComponent(new KeyboardPlayerInputComponent());
    }
}
