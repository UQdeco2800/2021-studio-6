package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PlayerMeleeAttackComponentTest {
    @Mock GameTime gameTime;
    @Mock World world;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldTrackDirection() {
        String sword = "configs/Sword.json";
        Entity player = new Entity().addComponent(new PlayerMeleeAttackComponent(sword));
        player.create();

        assertNull(player.getComponent(PlayerMeleeAttackComponent.class).getDirection());
        Vector2 directionR = new Vector2(1,0);
        player.getEvents().trigger("walk", directionR);
        assertTrue(player.getComponent(PlayerMeleeAttackComponent.class).getDirection()
                .epsilonEquals(directionR));
    }

    @Test
    void checkStatesAfterBeforeAttack() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        String sword = "configs/Sword.json";
        Entity player = createPlayer();
        Vector2 directionR = new Vector2(1,0);

        // there should not be any fixtures yet
        assertNull(player.getComponent(PlayerMeleeAttackComponent.class).getFixture());
        assertEquals(PhysicsLayer.DEFAULT, player.getComponent(PlayerMeleeAttackComponent.class).getLayer());

//        when(time.getTime()).thenReturn(3000L);
        player.getEvents().trigger("attack");
        // fixture created melee attack executed
        assertNotNull(player.getComponent(PlayerMeleeAttackComponent.class).getFixture());
        assertEquals(PhysicsLayer.WEAPON, player.getComponent(PlayerMeleeAttackComponent.class).getLayer());
        player.getEvents().trigger("walk", directionR);

        player.getComponent(PlayerMeleeAttackComponent.class).update();
//        player.getComponent(PlayerMeleeAttackComponent.class).update();
//        player.getComponent(PlayerMeleeAttackComponent.class).update();

        assertNull(player.getComponent(PlayerMeleeAttackComponent.class).getFixture());
    }


    @Test
    void shouldAttack() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        // by default a fixture will be created in the north direction of player
        Entity player = createPlayer();
        Entity enemy = createEnemy();

        player.getEvents().trigger("attack");
        Fixture playerFixture = player.getComponent(PlayerMeleeAttackComponent.class).getFixture();
        Fixture enemyFixture = enemy.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("collisionStart", playerFixture, enemyFixture);
        player.getEvents().trigger("attack");
        assertEquals(15, enemy.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldAttackAfterMoving() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        Entity player = createPlayer();
        Entity enemy = createEnemy();

        player.getEvents().trigger("attack");
        Fixture playerFixture = player.getComponent(PlayerMeleeAttackComponent.class).getFixture();
        Fixture enemyFixture = enemy.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("walk", Vector2Utils.RIGHT);
        player.getEvents().trigger("collisionStart", playerFixture, enemyFixture);
    }

    @Test
    void shouldNotAttack() {
    }

    Entity createPlayer() {
        String sword = "configs/Sword.json";
        Entity player = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PlayerMeleeAttackComponent(sword));
        player.create();
        return player;
    }

    Entity createEnemy() {
        Entity entity =
                new Entity()
                        .addComponent(new CombatStatsComponent(20, 2))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));
        entity.create();
        return entity;
    }

    Entity createNonEnemy() {
        Entity entity =
                new Entity()
                        .addComponent(new CombatStatsComponent(20, 2))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.DEFAULT));
        entity.create();
        return entity;
    }


}
