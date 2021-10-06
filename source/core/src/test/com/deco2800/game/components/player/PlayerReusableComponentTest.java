package com.deco2800.game.components.player;

import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PlayerReusableComponentTest {
    @Mock
    ResourceService resourceService;
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerResourceService(resourceService);
    }

    @Test
    void shouldApplyAndUseBandage() {
        Entity player = createPlayer();
        int BANDAGE_COUNT = 3;
        int BANDAGE_USED = 2;
        int WOUNDED_STATE = 2;
        int FULL_STATE = 3;

        player.getComponent(PlayerCombatStatsComponent.class).setWoundState(WOUNDED_STATE);
        assertEquals(BANDAGE_COUNT, player.getComponent(InventoryComponent.class).getBandages());
        player.getEvents().trigger("useBandage");
        assertEquals(FULL_STATE, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
        assertEquals(BANDAGE_USED, player.getComponent(InventoryComponent.class).getBandages());
    }

    @Test
    void shouldNotApplyBandageWithoutAnyInInventory() {
        Entity player = createPlayer();
        int WOUNDED_STATE = 2;
        int NO_BANDAGE = 0;

        player.getComponent(PlayerCombatStatsComponent.class).setWoundState(WOUNDED_STATE);
        player.getComponent(InventoryComponent.class).setBandages(NO_BANDAGE);
        player.getEvents().trigger("useBandage");
        assertEquals(WOUNDED_STATE, player.getComponent(PlayerCombatStatsComponent.class).getWoundState());
    }

    @Test
    void shouldNotApplyBandageWhenHealthIsMax() {
        Entity player = createPlayer();
        int BANDAGE_COUNT = 3;

        player.getEvents().trigger("useBandage");
        assertEquals(BANDAGE_COUNT, player.getComponent(InventoryComponent.class).getBandages());
    }

    Entity createPlayer() {
        // these are arbitrary values used purely for testing and it is based real values extracted from the config
        // file, currently not in used because may change depending on game balancing
        Entity player = new Entity()
                .addComponent(new PlayerCombatStatsComponent(3, 10, 3, 2, 1))
                .addComponent(new InventoryComponent(5, 5, 3, 1))
                .addComponent(new PlayerReusableComponent());
        player.create();
        return player;
    }
}
