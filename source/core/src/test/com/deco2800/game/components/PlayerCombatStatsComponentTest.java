package com.deco2800.game.components;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class PlayerCombatStatsComponentTest {
    @Mock
    ResourceService resourceService;
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerResourceService(resourceService);
    }

    @Test
    void shouldSetGetWoundState() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20, 3, 25, 0); // settings other than wound state are random
        assertEquals(3, combat.getWoundState()); // w0und state can be between 0-3 inclusive
        combat.setWoundState(2);
        assertEquals(2, combat.getWoundState());
        combat.setWoundState(20);
        assertEquals(3, combat.getWoundState());
        combat.setWoundState(-50);
        assertEquals(0, combat.getWoundState());
    }

    @Test
    void shouldCheckIsDead() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20, 3, 25, 0); // settings other than wound state are random
        assertFalse(combat.isDead());
        combat.setWoundState(0);
        assertTrue(combat.isDead());
        combat.setWoundState(-50);
        assertTrue(combat.isDead());
    }

    @Test
    void shouldCheckWoundAtMax() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 20, 3, 25, 0); // settings other than wound state are random
        assertTrue(combat.atMax());
        combat.setWoundState(0);
        assertFalse(combat.atMax());
        combat.setWoundState(2);
        assertFalse(combat.atMax());
    }

    @Test
    void shouldCheckStateMax() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        Entity player = new Entity().addComponent(new PlayerCombatStatsComponent(10, 20, 3, 25, 0)); // settings other than wound state are random
        player.create();
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getHealth(), player.getComponent(PlayerCombatStatsComponent.class).getStateMax());

        player.getComponent(PlayerCombatStatsComponent.class).setWoundState(2);
        player.getComponent(PlayerCombatStatsComponent.class).setHealth(2);
        assertNotEquals(player.getComponent(PlayerCombatStatsComponent.class).getHealth(), player.getComponent(PlayerCombatStatsComponent.class).getStateMax());
        player.getComponent(PlayerCombatStatsComponent.class).setStateMax(2);
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getStateMax(), player.getComponent(PlayerCombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldSetWoundAndHealth() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        Entity player = new Entity().addComponent(new PlayerCombatStatsComponent(10, 20, 3, 25, 0)); // settings other than wound state & health are random
        player.create();
        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getStateMax(), player.getComponent(PlayerCombatStatsComponent.class).getHealth());

        player.getComponent(PlayerCombatStatsComponent.class).setHealth(100);
        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getWoundState()); // max for the wound state is 3
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getStateMax(), player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        player.getComponent(PlayerCombatStatsComponent.class).setHealth(0);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState()); // if player loses all health they move down wound state
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getStateMax(), player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        player.getComponent(PlayerCombatStatsComponent.class).setHealth(-20);
        assertEquals(1, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getStateMax(), player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        player.getComponent(PlayerCombatStatsComponent.class).setHealth(2);
        assertEquals(1, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldSetGetBaseAttack() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15, 3, 25, 0); // settings other than baseAttack are random
        assertEquals(15, combat.getBaseAttack());
        combat.setBaseAttack(150);
        assertEquals(150, combat.getBaseAttack());
        combat.setBaseAttack(-50);
        assertEquals(150, combat.getBaseAttack()); // cannot have negative so changes nothing
    }

    @Test
    void shouldGetMeleeAttack() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15, 3, 25, 0); // settings other than baseAttack are random
        assertEquals(15, combat.getMeleeAttack()); // wound 3: meleeAttack = baseAttack
        combat.setWoundState(2);
        assertEquals(14, combat.getMeleeAttack()); // wound 2: meleeAttack = baseAttack*0.9
        combat.setWoundState(1);
        assertEquals(9, combat.getMeleeAttack()); // wound 1: meleeAttack = baseAttack*0.8
    }

    @Test
    void shouldSetGetBaseRangedAttack() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15, 3, 25, 0); // settings other than baseRangedAttack are random
        assertEquals(25, combat.getBaseRangedAttack());
        combat.setBaseRangedAttack(150);
        assertEquals(150, combat.getBaseRangedAttack());
        combat.setBaseRangedAttack(-50); // cannot be negative
        assertEquals(150, combat.getBaseRangedAttack());
    }

    @Test
    void shouldGetRangedAttack() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15, 3, 25, 0); // settings other than baseRangedAttack are random
        assertEquals(25, combat.getRangedAttack()); // wound 3: rangedAttack = baseRangedAttack
        combat.setWoundState(2);
        assertEquals(23, combat.getRangedAttack()); // wound 2: rangedAttack = baseRangedAttack*0.9
        combat.setWoundState(1);
        assertEquals(15, combat.getRangedAttack()); // wound 1: rangedAttack = baseRangedAttack*0.8
    }

    @Test
    void shouldSetGetDefenceLevel() {
        PlayerCombatStatsComponent combat = new PlayerCombatStatsComponent(3, 15, 3, 25, 0); // settings other than defenceLevel are random
        assertEquals(0, combat.getDefenceLevel());
        combat.setDefenceLevel(2);
        assertEquals(2, combat.getDefenceLevel());
        combat.setDefenceLevel(0);
        assertEquals(0, combat.getDefenceLevel());
        combat.setDefenceLevel(-50); // defence level cannot be negative
        assertEquals(0, combat.getDefenceLevel());
        combat.setDefenceLevel(5);
        assertEquals(5, combat.getDefenceLevel());
        combat.setDefenceLevel(7); // defence level cannot be over 5
        assertEquals(5, combat.getDefenceLevel());
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

        when(time.getTime()).thenReturn(400L); // invincibility frame is 400ms long (adjusting for it)
        player.getComponent(PlayerCombatStatsComponent.class).update();
        when(enemy.getBaseAttack()).thenReturn(1);
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        assertEquals(player.getComponent(PlayerCombatStatsComponent.class).getStateMax() - 1, player.getComponent(PlayerCombatStatsComponent.class).getHealth());

        when(enemy.getBaseAttack()).thenReturn(8);
        when(time.getTime()).thenReturn(800L); // invincibility frame is 400ms long (adjusting for it)
        player.getComponent(PlayerCombatStatsComponent.class).update();
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        when(time.getTime()).thenReturn(1600L); // invincibility frame is 400ms long (adjusting for it)
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

        player.getComponent(PlayerCombatStatsComponent.class).setDefenceLevel(2);
        when(time.getTime()).thenReturn(400L); // invincibility frame is 400ms long (adjusting for it)
        player.getComponent(PlayerCombatStatsComponent.class).update();
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());


        player.getComponent(PlayerCombatStatsComponent.class).setDefenceLevel(3);
        when(time.getTime()).thenReturn(800L); // invincibility frame is 400ms long (adjusting for it)
        player.getComponent(PlayerCombatStatsComponent.class).update();
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(5, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        assertEquals(1, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());

        when(time.getTime()).thenReturn(1200L); // invincibility frame is 400ms long (adjusting for it)
        player.getComponent(PlayerCombatStatsComponent.class).update();
        player.getComponent(PlayerCombatStatsComponent.class).setDefenceLevel(4);
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(4, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
        assertEquals(1, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
    }

    @Test
    void shouldSetUnsetInvincible() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        CombatStatsComponent enemy = mock(CombatStatsComponent.class);
        when(enemy.getBaseAttack()).thenReturn(1);
        when(time.getTime()).thenReturn(0L);

        Entity player = new Entity().addComponent(new PlayerCombatStatsComponent(3, 20, 3, 25, 0));
        player.create();

        player.getEvents().trigger("invincibility", 100L);
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getHealth());

        when(time.getTime()).thenReturn(60L);
        player.update();
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getHealth());

        when(time.getTime()).thenReturn(100L);
        player.update();
        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getHealth());

        player.getEvents().trigger("invincibility", 100L);
        when(time.getTime()).thenReturn(10000L);
        player.update();
//        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getHealth()); // from regen
//        player.getComponent(PlayerCombatStatsComponent.class).hit(enemy);
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getHealth());

    }

    @Test
    void shouldBeInvincibleAfterHit() {
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

    @Test
    void shouldRegenerate() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerCombatStatsComponent(3, 20, 3, 25, 0));
        player.create();
        player.getComponent(PlayerCombatStatsComponent.class).setHealth(1); // set to 1 as it is below regen line

        when(time.getTime()).thenReturn(100L);
        player.update();
        assertEquals(1, player.getComponent(PlayerCombatStatsComponent.class).getHealth());

//        when(time.getTime()).thenReturn(8000L); // 5000L(regenCooldown) + 3000L(initial offset)  (total regen time)
//        player.update();
//        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getHealth());

//        when(time.getTime()).thenReturn(13000L); // previous + 5000L(regenCooldown)
//        player.update();
//        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldBeAbleToAttackDuringInvinciblity() {
        GameTime time = mock(GameTime.class);
        ServiceLocator.registerTimeSource(time);
        when(time.getTime()).thenReturn(0L);
        EventListener0 listener = mock(EventListener0.class);
        EventListener0 listener_2 = mock(EventListener0.class);

        Entity player = new Entity().addComponent(new PlayerCombatStatsComponent(3, 20, 3, 25, 0));
        player.create();
        player.getEvents().addListener("enableAttack", listener_2);

        player.getComponent(PlayerCombatStatsComponent.class).invincibleStart(10L); // Arbitrary number to start invincibility
        verifyNoInteractions(listener_2);
        when(time.getTime()).thenReturn(10L); // End invincibility
        player.update();
        verify(listener_2).handle();
        verifyNoMoreInteractions(listener);
    }
}
