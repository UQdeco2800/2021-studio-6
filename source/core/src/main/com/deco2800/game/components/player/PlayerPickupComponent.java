package com.deco2800.game.components.player;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.ItemComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.items.Items;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;

/**
 * Gives player entity the ability to pick up items that are spawned
 * during the game. When items are picked up, player entity will update
 * relevant data variables in the inventory component
 */
public class PlayerPickupComponent extends Component {
    private short targetLayer;

    public PlayerPickupComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collisionStart", this::tryPickUpItem);
    }

    private void tryPickUpItem(Fixture me, Fixture other) {
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore - could be obstacle, NPC or even safehouse
            return;
        }

        // Try to detect item which must have an item component to begin with
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        ItemComponent item = target.getComponent(ItemComponent.class);
        InventoryComponent inventory = entity.getComponent(InventoryComponent.class);
        if (item != null && inventory != null) {
            int ammoLeft = inventory.getAmmo();
            int coinLeft = inventory.getGold();
            int itemQuantity = item.getItemQuantity();

            if (item.getItemType() == Items.AMMO) {
                // dispose item when picked up, can be changed later on
                inventory.setAmmo(ammoLeft + itemQuantity);
            } else if (item.getItemType() == Items.COINS) {
                // dispose item when picked up, can be changed later on
                inventory.setGold(coinLeft + itemQuantity);
            }
            target.getComponent(DisposingComponent.class).toBeDisposed();
        }
    }
}
