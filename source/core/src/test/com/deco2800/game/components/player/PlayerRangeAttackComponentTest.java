package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PlayerRangeAttackComponentTest {
    @Mock GameTime gameTime;
    @Mock World world;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }


    @Test
    void shouldGetAndRestockBullets() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());

        Array<Entity> bullets = new Array<>();
        int NUM_BULLETS = 5;
        for (int i = 0; i < NUM_BULLETS; i++) {
            Entity newBullet = new Entity().addComponent(new PhysicsMovementComponent(new Vector2(5f, 5f)));

            // hide bullet out of game screen
            newBullet.setPosition(-10,-10);
            bullets.add(newBullet);
        }
        player.getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
        assertEquals(5, PlayerRangeAttackComponent.getActiveBullets().size);

        Entity dudBullet = new Entity();
        PlayerRangeAttackComponent.restockBulletShot(dudBullet);
        assertEquals(6, PlayerRangeAttackComponent.getActiveBullets().size);
    }

    @Test
    void checkReloadingStatusAndGunMagazineCapacity() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());

        assertFalse(player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus());
        player.getComponent(PlayerRangeAttackComponent.class).setReloadingStatus(true);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus());

        assertEquals(5, player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine());
        player.getComponent(PlayerRangeAttackComponent.class).reloadGunMagazine(1);
        assertEquals(6, player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine());
        assertFalse(player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus());
    }
}
