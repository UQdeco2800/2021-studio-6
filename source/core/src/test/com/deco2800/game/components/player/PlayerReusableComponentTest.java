package com.deco2800.game.components.player;

import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PlayerReusableComponentTest {

    @Test
    void shouldApplyAndUseBandage() {
        Entity player = createPlayer();
        player.getComponent(PlayerCombatStatsComponent.class).setWoundState(2);
        assertEquals(3, player.getComponent(InventoryComponent.class).getBandages());
        player.getEvents().trigger("useBandage");
        assertEquals(3, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        assertEquals(2, player.getComponent(InventoryComponent.class).getBandages());
    }

    @Test
    void shouldNotApplyBandageWithoutAnyInInventory() {
        Entity player = createPlayer();
        player.getComponent(PlayerCombatStatsComponent.class).setWoundState(2);
        player.getComponent(InventoryComponent.class).setBandages(0);
        player.getEvents().trigger("useBandage");
        assertEquals(2, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
    }

    @Test
    void shouldNotApplyBandageWhenHealthIsMax() {
        Entity player = createPlayer();
        player.getEvents().trigger("useBandage");
        assertEquals(3, player.getComponent(InventoryComponent.class).getBandages());
    }

    Entity createPlayer() {
        Entity player = new Entity()
                .addComponent(new PlayerCombatStatsComponent(3, 10, 3, 2, 1))
                .addComponent(new InventoryComponent(5, 5, 3))
                .addComponent(new PlayerReusableComponent());
        player.create();
        return player;
    }
}
