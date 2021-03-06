package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.ItemComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.items.Items;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gives player entity the ability to pick up items that are spawned
 * during the game. When items are picked up, player entity will update
 * relevant data variables in the inventory component
 */
public class PlayerPickupComponent extends Component {
    private short targetLayer;
    private static final Logger logger = LoggerFactory.getLogger(PlayerPickupComponent.class);

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
            int bandageLeft = inventory.getBandages();
            int itemQuantity = item.getItemQuantity();

            if (item.getItemType() == Items.AMMO) {
                PlayerSoundComponent pcs = entity.getComponent(PlayerSoundComponent.class);
                if(pcs != null) { pcs.playGenericItemPickup(); }

                inventory.setAmmo(ammoLeft + itemQuantity);

            } else if (item.getItemType() == Items.COINS) {
                PlayerSoundComponent pcs = entity.getComponent(PlayerSoundComponent.class);
                if(pcs != null) { pcs.playGenericItemPickup(); }

                inventory.setGold(coinLeft + itemQuantity);

            } else if (item.getItemType() == Items.BANDAGE) {
                PlayerSoundComponent pcs = entity.getComponent(PlayerSoundComponent.class);
                if(pcs != null) { pcs.playGenericItemPickup(); }

                inventory.setBandages(bandageLeft + itemQuantity);

            }


            if (ServiceLocator.getGameArea() != null && item.getItemType() != Items.SHOP) {
                ServiceLocator.getGameArea().despawnEntity(target);
            } else if (item.getItemType() == Items.SHOP) {
                entity.getEvents().trigger("toggleShopBox");
            }
            //target.getComponent(DisposingComponent.class).toBeDisposed();
        }
    }
}
