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

    @Test
    void checkAndSetPlayerDirection() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());

        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).getDirection().epsilonEquals(0,0));
        Vector2 direction = new Vector2(1,0);
        player.getComponent(PlayerRangeAttackComponent.class).setDirection(direction);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).getDirection().epsilonEquals(1,0));
    }

    @Test
    void shouldScaleVectors() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());
        player.setPosition(0,0);

        Vector2 directionL = new Vector2(-1,0);
        player.getComponent(PlayerRangeAttackComponent.class).setDirection(directionL);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).scaleVector(player.getPosition())
                .epsilonEquals(-15,0));

        Vector2 directionD = new Vector2(0,-1);
        player.getComponent(PlayerRangeAttackComponent.class).setDirection(directionD);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).scaleVector(player.getPosition())
                .epsilonEquals(0,-15));

        Vector2 directionU = new Vector2(0,1);
        player.getComponent(PlayerRangeAttackComponent.class).setDirection(directionU);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).scaleVector(player.getPosition())
                .epsilonEquals(0,15));
    }

    @Test
    void shouldScaleRight() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());
        player.setPosition(0,0);

        Vector2 directionR = new Vector2(1,0);
        player.getComponent(PlayerRangeAttackComponent.class).setDirection(directionR.cpy());
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).scaleVector(player.getPosition()).cpy()
                .epsilonEquals(15,0));
    }

    @Test
    void shouldFireCorrectly() {
        Entity player = new Entity();
        player.addComponent(new PlayerRangeAttackComponent());

        Array<Entity> bullets = new Array<>();
        int NUM_BULLETS = 5;
        for (int i = 0; i < NUM_BULLETS; i++) {
            Entity newBullet = new Entity()
                    .addComponent(new PhysicsMovementComponent(new Vector2(5f, 5f)))
                    .addComponent(new BulletCollisionComponent());

            // hide bullet out of game screen
            newBullet.setPosition(-10,-10);
            bullets.add(newBullet);
        }
        player.getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
        player.setPosition(0,0);

        Vector2 movingDirection = new Vector2(1,0);
        player.getComponent(PlayerRangeAttackComponent.class).fire(movingDirection);
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).getDirection().epsilonEquals(1,0));

        Vector2 shot = new Vector2(0,0);
        Entity bulletAlmostFired = PlayerRangeAttackComponent.getActiveBullets().get(0);
        player.getComponent(PlayerRangeAttackComponent.class).fire(shot);

        assertEquals(4, player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine());
        assertEquals(4, PlayerRangeAttackComponent.getActiveBullets().size);

        assertTrue(bulletAlmostFired.getPosition().epsilonEquals(0,0));
        assertTrue(bulletAlmostFired.getComponent(BulletCollisionComponent.class).getBulletLaunchStatus());
        assertTrue(bulletAlmostFired.getComponent(PhysicsMovementComponent.class).getTarget().epsilonEquals(15,0));
    }
}
