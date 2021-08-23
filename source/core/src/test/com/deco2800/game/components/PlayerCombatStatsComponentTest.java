package com.deco2800.game.components;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class PlayerCombatStatsComponentTest {

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
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20, 3, 25, 0);
        CombatStatsComponent enemy = new CombatStatsComponent(100, 20);
        assertEquals(20, enemy.getBaseAttack());

        combat.hit(enemy);
        assertEquals(combat.getStateMax(), combat.getHealth());
        assertEquals(2, combat.getWoundState());

        CombatStatsComponent enemy2 = new CombatStatsComponent(100, 1);

        combat.hit(enemy2);
        assertEquals(2, combat.getWoundState());
        assertEquals(combat.getStateMax() - 1, combat.getHealth());

        combat.hit(enemy);
        combat.hit(enemy);
        assertEquals(0, combat.getWoundState());
        assertTrue(combat.isDead());
        assertEquals(combat.getStateMax(), combat.getHealth());
    }

    @Test
    void shouldDefendHit() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(10, 20, 3, 25, 0);
        CombatStatsComponent enemy = new CombatStatsComponent(100, 10);

        combat.hit(enemy);
        assertEquals(combat.getStateMax(), combat.getHealth());
        assertEquals(2, combat.getWoundState());

        combat.setWoundState(3);
        combat.setHealth(10);
        combat.setDefenceLevel(1);
        combat.hit(enemy);
        assertEquals(1, combat.getHealth());
        assertEquals(3, combat.getWoundState());

        combat.setHealth(10);
        combat.setDefenceLevel(2);
        combat.hit(enemy);
        assertEquals(2, combat.getHealth());
        assertEquals(3, combat.getWoundState());
    }
}
