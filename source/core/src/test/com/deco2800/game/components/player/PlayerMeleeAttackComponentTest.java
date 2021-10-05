package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PlayerMeleeAttackComponentTest {
    @Mock GameTime gameTime;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void checkStatesAfterBeforeAttack() {
        long START_TIME = 0L;
        long PAST_TIME = 1000L;

        when(gameTime.getTime()).thenReturn(START_TIME);
        Entity player = createPlayer();

        // there should not be any fixtures yet
        assertNull(player.getComponent(PlayerMeleeAttackComponent.class).getFixture());
        assertEquals(PhysicsLayer.DEFAULT, player.getComponent(PlayerMeleeAttackComponent.class).getLayer());

        player.getEvents().trigger("attackStart");
        // fixture created melee attack executed
        assertNotNull(player.getComponent(PlayerMeleeAttackComponent.class).getFixture());
        assertEquals(PhysicsLayer.WEAPON, player.getComponent(PlayerMeleeAttackComponent.class).getLayer());

        // fixture should be null after attack and time steps
        when(gameTime.getTime()).thenReturn(PAST_TIME);
        player.getComponent(PlayerMeleeAttackComponent.class).update();

        assertNull(player.getComponent(PlayerMeleeAttackComponent.class).getFixture());
    }

    @Test
    void shouldAttack() {
        // by default a fixture will be created in the north direction of player
        Entity player = createPlayer();
        Entity enemy = createEnemy();
        int ENEMY_DAMAGED_HEALTH = 18;

        player.getEvents().trigger("attackStart");
        Fixture playerFixture = player.getComponent(PlayerMeleeAttackComponent.class).getFixture();
        Fixture enemyFixture = enemy.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("collisionStart", playerFixture, enemyFixture);
        player.getEvents().trigger("attackStart");
        assertEquals(ENEMY_DAMAGED_HEALTH, enemy.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldNotAttackAfterMoving() {
        Entity player = createPlayer();
        Entity enemy = createEnemy();
        int FULL_ENEMY_HEALTH = 20;

        player.getEvents().trigger("attackStart");
        Fixture playerFixture = player.getComponent(PlayerMeleeAttackComponent.class).getFixture();
        Fixture enemyFixture = enemy.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("walk", Vector2Utils.RIGHT);
        player.getEvents().trigger("collisionStart", playerFixture, enemyFixture);
        assertEquals(FULL_ENEMY_HEALTH, enemy.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldAttackAfterMoving() {
        Entity player = createPlayer();
        Entity enemy = createEnemy();
        int ENEMY_DAMAGED_HEALTH = 18;
        player.getEvents().trigger("walk", Vector2Utils.RIGHT);

        player.getEvents().trigger("attackStart");
        Fixture playerFixture = player.getComponent(PlayerMeleeAttackComponent.class).getFixture();
        Fixture enemyFixture = enemy.getComponent(HitboxComponent.class).getFixture();
        player.getEvents().trigger("collisionStart", playerFixture, enemyFixture);
        assertEquals(ENEMY_DAMAGED_HEALTH, enemy.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldNotAttackIfNoCollision() {
        Entity player = createPlayer();
        Entity enemy = createEnemy();
        int FULL_ENEMY_HEALTH = 20;

        player.getEvents().trigger("attackStart");
        assertEquals(FULL_ENEMY_HEALTH, enemy.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldNotAttackIfNotEnemy() {
        Entity player = createPlayer();
        Entity nonEnemy = createNonEnemy();
        int FULL_ENEMY_HEALTH = 20;

        player.getEvents().trigger("attackStart");
        Fixture playerFixture = player.getComponent(PlayerMeleeAttackComponent.class).getFixture();
        Fixture nonEnemyFixture = nonEnemy.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("attackStart");
        player.getEvents().trigger("collisionStart", playerFixture, nonEnemyFixture);
        assertEquals(FULL_ENEMY_HEALTH, nonEnemy.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldAttackMultipleEnemy() {
        // by default a fixture will be created in the north direction of player
        // currently not able to test this - but can be shown in game
        Entity player = createPlayer();
        Entity enemy1 = createEnemy();
        Entity enemy2 = createEnemy();
        int ENEMY_DAMAGED_HEALTH = 18;

        player.getEvents().trigger("attackStart");
        Fixture playerFixture = player.getComponent(PlayerMeleeAttackComponent.class).getFixture();
        Fixture enemy1Fixture = enemy1.getComponent(HitboxComponent.class).getFixture();
//        Fixture enemy2Fixture = enemy2.getComponent(HitboxComponent.class).getFixture();
        player.getComponent(PlayerMeleeAttackComponent.class).update();

        player.getEvents().trigger("attackStart");

        player.getEvents().trigger("collisionStart", playerFixture, enemy1Fixture);
        assertEquals(ENEMY_DAMAGED_HEALTH, enemy1.getComponent(CombatStatsComponent.class).getHealth());
//        player.getEvents().trigger("collisionStart", playerFixture, enemy2Fixture);
//        assertEquals(15, enemy2.getComponent(CombatStatsComponent.class).getHealth());
    }

    Entity createPlayer() {
        // these are arbitrary values used purely for testing and it is based real values extracted from the config
        // file, currently not in used because may change depending on game balancing - this may change - beware of test
        String sword = "configs/Sword.json";
        Entity player = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PlayerMeleeAttackComponent(sword));
        player.create();
        return player;
    }

    Entity createEnemy() {
        // arbitrary values are used here as game balancing may occur
        Entity entity =
                new Entity()
                        .addComponent(new CombatStatsComponent(20, 2))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));
        entity.create();
        return entity;
    }

    Entity createNonEnemy() {
        // arbitrary values are used here as game balancing may occur
        Entity entity =
                new Entity()
                        .addComponent(new CombatStatsComponent(20, 2))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.DEFAULT));
        entity.create();
        return entity;
    }
}
