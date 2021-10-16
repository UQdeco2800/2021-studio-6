package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class ProjectileFactoryTest {
    Vector2 hiddenCoord = new Vector2(-10,-10);

    @BeforeEach
    void beforeEach() {
        String[] atlas = {"images/playeritems/rock/rock.atlas", "images/playeritems/firecracker/firecracker.atlas"};
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadTextureAtlases(atlas);
        ServiceLocator.getResourceService().loadAll();
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldCreateBullet() {
        Entity bullet = ProjectileFactory.createBullet();
        assertEquals(hiddenCoord, bullet.getPosition());
    }

    @Test
    void shouldCreateFireCracker() {
        Entity fireCracker = ProjectileFactory.createFireCracker();
        assertEquals(hiddenCoord, fireCracker.getPosition());
    }
}
