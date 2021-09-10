package com.deco2800.game.entities.factories;

import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.ItemComponent;
import com.deco2800.game.components.TouchAttackComponent;
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

    public static Entity createAmmoPickup(int itemQuantity) {
        Entity ammo = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setSensor(true).setLayer(PhysicsLayer.ITEM))
                .addComponent(new ItemComponent(Items.AMMO, itemQuantity))
                .addComponent(new TextureRenderComponent("images/playeritems/pickupammo.png"))
                .addComponent(new DisposingComponent());
        return ammo;
    }
}
