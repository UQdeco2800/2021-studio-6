package com.deco2800.game.components;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class PlayerCombatStatsComponentTest {

    @Test
    void shouldSetGetWoundState() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20);
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
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20);
        assertFalse(combat.isDead());

        combat.setWoundState(0);
        assertTrue(combat.isDead());

        combat.setWoundState(-50);
        assertTrue(combat.isDead());
    }

    @Test
    void shouldCheckWoundAtMax() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20);
        assertTrue(combat.atMax());

        combat.setWoundState(0);
        assertFalse(combat.atMax());

        combat.setWoundState(2);
        assertFalse(combat.atMax());
    }

    @Test
    void shouldCheckStateMax() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20);
        assertEquals(combat.getStateHealth(), combat.getStateMax());

        combat.setWoundState(2);
        combat.setStateHealth(2);
        assertNotEquals(combat.getStateHealth(), combat.getStateMax());

        combat.setStateMax(2);
        assertEquals(combat.getStateMax(), combat.getStateHealth());
    }

    @Test
    void shouldSetWoundAndStateHealth() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20);
        assertEquals(3, combat.getWoundState());
        assertEquals(combat.getStateMax(), combat.getStateHealth());

        combat.setStateHealth(100);
        assertEquals(3, combat.getWoundState());
        assertEquals(combat.getStateMax(), combat.getStateHealth());

        combat.setStateHealth(0);
        assertEquals(2, combat.getWoundState());
        assertEquals(combat.getStateMax(), combat.getStateHealth());

        combat.setStateHealth(-20);
        assertEquals(1, combat.getWoundState());
        assertEquals(combat.getStateMax(), combat.getStateHealth());

        combat.setStateHealth(2);
        assertEquals(1, combat.getWoundState());
        assertEquals(2, combat.getStateHealth());
    }

    @Test
    void shouldSetGetBaseAttack() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15);
        assertEquals(15, combat.getBaseAttack());

        combat.setBaseAttack(150);
        assertEquals(150, combat.getBaseAttack());

        combat.setBaseAttack(-50);
        assertEquals(150, combat.getBaseAttack());
    }

    @Test
    void shouldSetGetHit() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20);
        CombatStatsComponent enemy = new CombatStatsComponent(100, 20);
        assertEquals(20, enemy.getBaseAttack());

        combat.hit(enemy);
        assertEquals(combat.getStateMax(), combat.getStateHealth());
        assertEquals(2, combat.getWoundState());


        CombatStatsComponent enemy2 = new CombatStatsComponent(100, 1);

        combat.hit(enemy2);
        assertEquals(2, combat.getWoundState());
        assertEquals(combat.getStateMax() - 1, combat.getStateHealth());

        combat.hit(enemy);
        combat.hit(enemy);
        assertEquals(0, combat.getWoundState());
        assertTrue(combat.isDead());
        assertEquals(combat.getStateMax(), combat.getStateHealth());
    }
}
