package com.deco2800.game.entities.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.deco2800.game.components.ItemComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class ItemFactoryTest {

    @BeforeEach
    void beforeEach() {
        // Setup resource locator to load speech bubble texture and npc movement atlas
        String[] textures = {"images/playeritems/rock/pickupammo.png", "images/playeritems/coin/coin1.png",
                "images/playeritems/coin/coin2.png", "images/playeritems/coin/money-bag.png",
                "images/playeritems/bandage/bandage01.png"};
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadTextures(textures);
        ServiceLocator.getResourceService().loadAll();

        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldCreateAmmo() {
        Entity oneAmmo = ItemFactory.createAmmoPickup(1);
        assertEquals(1, oneAmmo.getComponent(ItemComponent.class).getItemQuantity());
    }

    @Test
    void shouldCreateAllCoins() {
        Entity oneCoin = ItemFactory.createAmmoPickup(1);
        assertEquals(1, oneCoin.getComponent(ItemComponent.class).getItemQuantity());

        Entity moreCoin = ItemFactory.createAmmoPickup(3);
        assertEquals(3, moreCoin.getComponent(ItemComponent.class).getItemQuantity());

        Entity manyCoins = ItemFactory.createAmmoPickup(6);
        assertEquals(6, manyCoins.getComponent(ItemComponent.class).getItemQuantity());
    }

    @Test
    void shouldCreateBandage() {
        Entity oneBandage = ItemFactory.createAmmoPickup(1);
        assertEquals(1, oneBandage.getComponent(ItemComponent.class).getItemQuantity());
    }
}
