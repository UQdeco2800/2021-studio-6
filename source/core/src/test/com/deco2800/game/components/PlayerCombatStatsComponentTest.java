package com.deco2800.game.components;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.logging.Handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class PlayerCombatStatsComponentTest {

    @Test
    void shouldSetGetWoundState() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20, 3, 25, 0);
        assertEquals(3, combat.getWoundState());

        combat.setWoundState(2);
        assertEquals(2, combat.getWoundState());

        combat.setWoundState(20);
        assertEquals(3, combat.getWoundState());

        combat.setWoundState(-50);
        assertEquals(0, combat.getWoundState());
    }

    @Test
    void shouldCheckIsDead() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20, 3, 25, 0);
        assertFalse(combat.isDead());

        combat.setWoundState(0);
        assertTrue(combat.isDead());

        combat.setWoundState(-50);
        assertTrue(combat.isDead());
    }

    @Test
    void shouldCheckWoundAtMax() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20, 3, 25, 0);
        assertTrue(combat.atMax());

        combat.setWoundState(0);
        assertFalse(combat.atMax());

        combat.setWoundState(2);
        assertFalse(combat.atMax());
    }

    @Test
    void shouldCheckStateMax() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(10, 20, 3, 25, 0);
        assertEquals(combat.getHealth(), combat.getStateMax());

        combat.setWoundState(2);
        combat.setHealth(2);
        assertNotEquals(combat.getHealth(), combat.getStateMax());

        combat.setStateMax(2);
        assertEquals(combat.getStateMax(), combat.getHealth());
    }

    @Test
    void shouldSetWoundAndHealth() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(10, 20, 3, 25, 0);
        assertEquals(3, combat.getWoundState());
        assertEquals(combat.getStateMax(), combat.getHealth());

        combat.setHealth(100);
        assertEquals(3, combat.getWoundState());
        assertEquals(combat.getStateMax(), combat.getHealth());

        combat.setHealth(0);
        assertEquals(2, combat.getWoundState());
        assertEquals(combat.getStateMax(), combat.getHealth());

        combat.setHealth(-20);
        assertEquals(1, combat.getWoundState());
        assertEquals(combat.getStateMax(), combat.getHealth());

        combat.setHealth(2);
        assertEquals(1, combat.getWoundState());
        assertEquals(2, combat.getHealth());
    }

    @Test
    void shouldSetGetBaseAttack() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15, 3, 25, 0);
        assertEquals(15, combat.getBaseAttack());

        combat.setBaseAttack(150);
        assertEquals(150, combat.getBaseAttack());

        combat.setBaseAttack(-50);
        assertEquals(150, combat.getBaseAttack());
    }

    @Test
    void shouldGetMeleeAttack() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15, 3, 25, 0);
        assertEquals(15, combat.getMeleeAttack());

        combat.setWoundState(2);
        assertEquals(14, combat.getMeleeAttack());

        combat.setWoundState(1);
        assertEquals(9, combat.getMeleeAttack());
    }

    @Test
    void shouldSetGetBaseRangedAttack() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15, 3, 25, 0);
        assertEquals(25, combat.getBaseRangedAttack());

        combat.setBaseRangedAttack(150);
        assertEquals(150, combat.getBaseRangedAttack());

        combat.setBaseRangedAttack(-50);
        assertEquals(150, combat.getBaseRangedAttack());
    }

    @Test
    void shouldGetRangedAttack() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15, 3, 25, 0);
        assertEquals(25, combat.getRangedAttack());

        combat.setWoundState(2);
        assertEquals(23, combat.getRangedAttack());

        combat.setWoundState(1);
        assertEquals(15, combat.getRangedAttack());
    }

    @Test
    void shouldSetGetDefenceLevel() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15, 3, 25, 0);
        assertEquals(0, combat.getDefenceLevel());

        combat.setDefenceLevel(2);
        assertEquals(2, combat.getDefenceLevel());

        combat.setDefenceLevel(0);
        assertEquals(0, combat.getDefenceLevel());

        combat.setDefenceLevel(-50);
        assertEquals(0, combat.getDefenceLevel());

        combat.setDefenceLevel(5);
        assertEquals(0, combat.getDefenceLevel());
    }

    @Test
    void shouldSetGetHit() {
        GameTime time = mock(GameTime.class);
        DisposingComponent dispose = mock(DisposingComponent.class);
        ServiceLocator.registerTimeSource(time);
        Entity player = new Entity().addComponent(new PlayerCombatStatsComponent(3, 20, 3, 25, 0)).addComponent(dispose);
        CombatStatsComponent enemy = mock(CombatStatsComponent.class);
        when(enemy.getBaseAttack()).thenReturn(3);

        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getStateMax(), player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());

        when(time.getTime()).thenReturn(400L);
        player.getComponent(PlayerCombatStatsComponent.class).update();
        when(enemy.getBaseAttack()).thenReturn(1);
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getStateMax() - 1, player.getComponent(PlayerCombatStatsComponent.class).getHealth());

        when(enemy.getBaseAttack()).thenReturn(8);
        when(time.getTime()).thenReturn(800L);
        player.getComponent(PlayerCombatStatsComponent.class).update();
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        when(time.getTime()).thenReturn(1600L);
        player.getComponent(PlayerCombatStatsComponent.class).update();
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(0, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        assertTrue(player.getComponent(PlayerCombatStatsComponent.class).isDead());
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getStateMax(), player.getComponent(PlayerCombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldDefendHit() {
        GameTime time = mock(GameTime.class);
        when(time.getTime()).thenReturn(0L);
        ServiceLocator.registerTimeSource(time);
        CombatStatsComponent enemy = mock(CombatStatsComponent.class);
        when(enemy.getBaseAttack()).thenReturn(3);
        Entity player = new Entity().addComponent(new PlayerCombatStatsComponent(3, 20, 3, 25, 0));

        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getStateMax(), player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());

        player.getComponent(PlayerCombatStatsComponent.class).setDefenceLevel(1);
        when(time.getTime()).thenReturn(400L);
        player.getComponent(PlayerCombatStatsComponent.class).update();
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());

        when(time.getTime()).thenReturn(800L);
        player.getComponent(PlayerCombatStatsComponent.class).update();
        player.getComponent(PlayerCombatStatsComponent.class).setDefenceLevel(2);
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(1, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
    }

    @Test
    void shouldSetInvincible() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        when(time.getTime()).thenReturn(0L);
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20, 3, 25, 0);
        CombatStatsComponent enemy = mock(CombatStatsComponent.class);
        when(enemy.getBaseAttack()).thenReturn(1);

        assertEquals(3, combat.getHealth());
        combat.invincibleStart(5);
        combat.hit(enemy);
        assertEquals(3, combat.getHealth());
    }

    @Test
    void shouldBeInvincible() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        when(time.getTime()).thenReturn(0L);
        CombatStatsComponent enemy = mock(CombatStatsComponent.class);
        when(enemy.getBaseAttack()).thenReturn(1);
        Entity player = new Entity().addComponent(new PlayerCombatStatsComponent(3, 20, 3, 25, 0));

        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getHealth());

        when(time.getTime()).thenReturn(300L);
        player.getComponent(PlayerCombatStatsComponent.class).update();
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldDealDamageNegative() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerCombatStatsComponent(3, 20, 3, 25, 0));
        CombatStatsComponent enemy = mock(CombatStatsComponent.class);
        when(enemy.getBaseAttack()).thenReturn(-10);

        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
    }
}
