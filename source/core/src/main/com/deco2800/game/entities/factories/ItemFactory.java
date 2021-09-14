package com.deco2800.game.entities.factories;

import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.ItemComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.items.Items;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Used to create different type of items that will be spawned after an enemy
 * is killed at specific positions around the game world
 */
public class ItemFactory {

    /**
     * Ammo pickup will spawn when enemies die - but for now, it has been spawned randomly in game world
     *
     * @param itemQuantity the number of ammo quantity for ammo pick up
     * @return ammo pickup for player to use
     */
    public static Entity createAmmoPickup(int itemQuantity) {
        Entity ammo = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setSensor(true).setLayer(PhysicsLayer.ITEM))
                .addComponent(new ItemComponent(Items.AMMO, itemQuantity))
                .addComponent(new TextureRenderComponent("images/playeritems/pickupammo.png"))
                .addComponent(new DisposingComponent());
        ammo.setScale(0.7f, 0.7f);
        return ammo;
    }

    /**
     * Coin pickup will spawn when enemies die - but for now, it has been spawned randomly in game world
     *
     * @param itemQuantity the number of coin quantity for coin pick up
     * @return ammo pickup for player to use to purchase items
     */
    public static Entity createCoinPickup(int itemQuantity) {
        Entity coin = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setSensor(true).setLayer(PhysicsLayer.ITEM))
                .addComponent(new ItemComponent(Items.COINS, itemQuantity))
                .addComponent(new TextureRenderComponent("images/playeritems/coin.png"))
                .addComponent(new DisposingComponent());
        coin.setScale(0.7f, 0.7f);
        return coin;
    }

    /**
     * Armor is supposed to only be in safehouses but for now, it will spawn in forest game area for testing
     *
     * @param itemQuantity the number of armor quantity
     * @return ammo pickup for player to use to purchase items
     */
    public static Entity createArmour(int itemQuantity) {
        Entity coin = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setSensor(true).setLayer(PhysicsLayer.ITEM))
                .addComponent(new ItemComponent(Items.ARMOUR, itemQuantity))
                .addComponent(new TextureRenderComponent("images/playeritems/armour.png"))
                .addComponent(new DisposingComponent());
        coin.setScale(0.7f, 0.7f);
        return coin;
    }
}
