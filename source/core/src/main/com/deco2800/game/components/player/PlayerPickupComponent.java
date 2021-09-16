package com.deco2800.game.components.player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.ItemComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.items.Items;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.services.ServiceLocator;

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

    /**
     * Colliding with any specific objects in game world will trigger method below. This
     * method aims to find specific objects with item variables behavior to be put into
     * player's inventory when player collides with it
     * @param me player's fixture that is moving in game world
     * @param other any other fixture in game world
     */
    private void tryPickUpItem(Fixture me, Fixture other) {
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore - could be obstacle, NPC or even safehouse
            return;
        }
        if (!PhysicsLayer.contains(PhysicsLayer.PLAYER, me.getFilterData().categoryBits)) {
            return;
        }

        // Try to detect item which must have an item component to begin with
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        ItemComponent item = target.getComponent(ItemComponent.class);
        InventoryComponent inventory = entity.getComponent(InventoryComponent.class);
        PlayerCombatStatsComponent stats = entity.getComponent(PlayerCombatStatsComponent.class);
        PlayerMeleeAttackComponent weapon = entity.getComponent(PlayerMeleeAttackComponent.class);
        if (item != null && inventory != null) {
            int ammoLeft = inventory.getAmmo();
            int coinLeft = inventory.getGold();
            int itemQuantity = item.getItemQuantity();

            if (item.getItemType() == Items.AMMO) {
                inventory.setAmmo(ammoLeft + itemQuantity);
            } else if (item.getItemType() == Items.COINS) {
                inventory.setGold(coinLeft + itemQuantity);
            } else if (item.getItemType() == Items.ARMOUR) {
                stats.setDefenceLevel(2);
            } else if (item.getItemType() == Items.HELMET) {
                stats.setDefenceLevel(1);
            } else if (item.getItemType() == Items.SWORD) {
                 weapon.setWeapon("configs/Sword.json");
            } else if (item.getItemType() == Items.AXE) {
                weapon.setWeapon("configs/Axe.json");
            } else if (item.getItemType() == Items.DAGGER) {
                weapon.setWeapon("configs/Dagger.json");
            }


            if (ServiceLocator.getGameArea() != null) {
                ServiceLocator.getGameArea().despawnEntity(target);
            }
            //target.getComponent(DisposingComponent.class).toBeDisposed();
        }
    }
}
