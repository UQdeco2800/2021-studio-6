package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.BulletCollisionComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
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
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldGetAndRestockBullets() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());
        Vector2 BULLET_HIDDING_COORD = new Vector2(-10,-10);
        Vector2 BULLET_SPEED = new Vector2(-5f,5f);
        int BULLET_COUNT = 5;
        int RESTOCK_EXTRA_BULLET = 6;

        Array<Entity> bullets = new Array<>();
        int NUM_BULLETS = 5;
        for (int i = 0; i < NUM_BULLETS; i++) {
            Entity newBullet = new Entity().addComponent(new PhysicsMovementComponent(BULLET_SPEED));

            // hide bullet out of game screen
            newBullet.setPosition(BULLET_HIDDING_COORD);
            bullets.add(newBullet);
        }
        player.getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
        assertEquals(BULLET_COUNT, PlayerRangeAttackComponent.getActiveBullets().size);

        Entity dudBullet = new Entity();
        PlayerRangeAttackComponent.restockBulletShot(dudBullet);
        assertEquals(RESTOCK_EXTRA_BULLET, PlayerRangeAttackComponent.getActiveBullets().size);
    }

    @Test
    void checkReloadingStatusAndGunMagazineCapacity() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());
        int BULLET_COUNT = 5;
        int RELOAD_AMMO = 1;
        int RESTOCK_EXTRA_BULLET = 6;

        assertFalse(player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus());
        player.getComponent(PlayerRangeAttackComponent.class).setReloadingStatus(true);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus());

        assertEquals(BULLET_COUNT, player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine());
        player.getComponent(PlayerRangeAttackComponent.class).reloadGunMagazine(RELOAD_AMMO);
        assertEquals(RESTOCK_EXTRA_BULLET, player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine());
        assertFalse(player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus());
    }

    @Test
    void checkAndSetPlayerDirection() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());
        Vector2 ORIGINAL_FACING_DIR = new Vector2(0,0);
        Vector2 FACING_RIGHT_DIR = new Vector2(1,0);

        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).getDirection().epsilonEquals(ORIGINAL_FACING_DIR));
        player.getComponent(PlayerRangeAttackComponent.class).setDirection(FACING_RIGHT_DIR);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).getDirection().epsilonEquals(FACING_RIGHT_DIR));
    }

    @Test
    void shouldScaleVectors() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());
        Vector2 PLAYER_POS = new Vector2(0,0);
        Vector2 FACING_LEFT_DIR = new Vector2(-1,0);
        Vector2 FACING_RIGHT_DIR = new Vector2(1,0);
        Vector2 FACING_DOWN_DIR = new Vector2(0,-1);
        Vector2 FACING_UP_DIR = new Vector2(0,1);
        Vector2 SCALED_LEFT = new Vector2(-1000,0);
        Vector2 SCALED_RIGHT = new Vector2(1000,0);
        Vector2 SCALED_UP = new Vector2(0,1000);
        Vector2 SCALED_DOWN = new Vector2(0,-1000);

        player.getComponent(PlayerRangeAttackComponent.class).setDirection(FACING_LEFT_DIR);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).scaleVector(PLAYER_POS)
                .epsilonEquals(SCALED_LEFT));

        player.getComponent(PlayerRangeAttackComponent.class).setDirection(FACING_RIGHT_DIR);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).scaleVector(PLAYER_POS)
                .epsilonEquals(SCALED_RIGHT));

        player.getComponent(PlayerRangeAttackComponent.class).setDirection(FACING_DOWN_DIR);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).scaleVector(PLAYER_POS)
                .epsilonEquals(SCALED_DOWN));

        player.getComponent(PlayerRangeAttackComponent.class).setDirection(FACING_UP_DIR);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).scaleVector(PLAYER_POS)
                .epsilonEquals(SCALED_UP));
    }

    @Test
    void shouldFireCorrectly() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());
        Vector2 BULLET_HIDDING_COORD = new Vector2(-10,-10);
        Vector2 BULLET_SPEED = new Vector2(5f,5f);
        Vector2 PLAYER_POS = new Vector2(0,0);
        Vector2 SHOOT = new Vector2(0,0);
        Vector2 FACING_RIGHT_DIR = new Vector2(1,0);
        int NO_BULLETS_SHOT = 0;
        int BULLET_COUNT = 4;
        Vector2 SCALED_RIGHT = new Vector2(1000,0);

        Array<Entity> bullets = new Array<>();
        int NUM_BULLETS = 5;
        for (int i = 0; i < NUM_BULLETS; i++) {
            Entity newBullet = new Entity()
                    .addComponent(new PhysicsMovementComponent(BULLET_SPEED))
                    .addComponent(new BulletCollisionComponent());
            // hide bullet out of game screen
            newBullet.setPosition(BULLET_HIDDING_COORD);
            bullets.add(newBullet);
        }
        player.getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
        player.setPosition(PLAYER_POS);

        player.getComponent(PlayerRangeAttackComponent.class).fire(FACING_RIGHT_DIR);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).getDirection().epsilonEquals(FACING_RIGHT_DIR));

        Entity bulletAlmostFired = PlayerRangeAttackComponent.getActiveBullets().get(NO_BULLETS_SHOT);
        player.getComponent(PlayerRangeAttackComponent.class).fire(SHOOT);

        assertEquals(BULLET_COUNT, player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine());
        assertEquals(BULLET_COUNT, PlayerRangeAttackComponent.getActiveBullets().size);

        assertTrue(bulletAlmostFired.getPosition().epsilonEquals(0,0));
        assertTrue(bulletAlmostFired.getComponent(BulletCollisionComponent.class).getBulletLaunchStatus());

        assertEquals(SCALED_RIGHT.cpy(), bulletAlmostFired.getComponent(PhysicsMovementComponent.class).getTarget()); //
    }
}
