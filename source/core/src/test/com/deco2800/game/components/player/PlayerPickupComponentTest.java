package com.deco2800.game.components.player;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.ItemComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.items.Items;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PlayerPickupComponentTest {
    @Mock GameTime gameTime;

    @BeforeEach
    void beforeEach() {
        EntityService entityService = new EntityService();
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(entityService);
    }

    @Test
    void shouldPickUpAmmo() {
        PhysicsService physicsService = new PhysicsService();
        EntityService entityService = new EntityService();
        int GOLD_BEFORE_PICKUP = 5;
        int BANDAGE_BEFORE_PICKUP = 3;
        int AMMO_BEFORE_PICKUP = 5;
        int AMMO_AFTER_PICKUP = 6;

        ServiceLocator.registerPhysicsService(physicsService);
        ServiceLocator.registerEntityService(entityService);

        Entity player = createPlayer();
        Entity ammo = createAmmoItem();

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture ammoFixture = ammo.getComponent(ColliderComponent.class).getFixture();

        assertEquals(GOLD_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getGold());
        assertEquals(BANDAGE_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getBandages());
        assertEquals(AMMO_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getAmmo());
        player.getEvents().trigger("collisionStart", playerFixture, ammoFixture);

        // target fixture should be added for disposal in engine
        assertEquals(AMMO_AFTER_PICKUP,player.getComponent(InventoryComponent.class).getAmmo());
        assertEquals(GOLD_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getGold());
        assertEquals(BANDAGE_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getBandages());
    }

    @Test
    void shouldPickUpCoins() {
        PhysicsService physicsService = new PhysicsService();
        EntityService entityService = new EntityService();
        int GOLD_BEFORE_PICKUP = 5;
        int BANDAGE_BEFORE_PICKUP = 3;
        int AMMO_BEFORE_PICKUP = 5;
        int GOLD_AFTER_PICKUP = 6;

        ServiceLocator.registerPhysicsService(physicsService);
        ServiceLocator.registerEntityService(entityService);

        Entity player = createPlayer();
        Entity coin = createCoinItem();

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture coinFixture = coin.getComponent(ColliderComponent.class).getFixture();
        System.out.println(coinFixture);

        assertEquals(GOLD_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getGold());
        assertEquals(BANDAGE_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getBandages());
        assertEquals(AMMO_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getAmmo());
        player.getEvents().trigger("collisionStart", playerFixture, coinFixture);

        // target fixture should be added for disposal in engine
        assertEquals(AMMO_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getAmmo());
        assertEquals(GOLD_AFTER_PICKUP,player.getComponent(InventoryComponent.class).getGold());
        assertEquals(BANDAGE_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getBandages());
    }

    @Test
    void shouldNotPickUpNonItem() {
        PhysicsService physicsService = new PhysicsService();
        EntityService entityService = new EntityService();
        int GOLD_BEFORE_PICKUP = 5;
        int BANDAGE_BEFORE_PICKUP = 3;
        int AMMO_BEFORE_PICKUP = 5;
        int NOTHING_TO_DIPOSE = 0;

        ServiceLocator.registerPhysicsService(physicsService);
        ServiceLocator.registerEntityService(entityService);

        PhysicsEngine physicsEngine = physicsService.getPhysics();

        Entity player = createPlayer();
        Entity nonItem = createNonItem();

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture nonItemFixture = nonItem.getComponent(ColliderComponent.class).getFixture();

        assertEquals(GOLD_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getGold());
        assertEquals(BANDAGE_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getBandages());
        assertEquals(AMMO_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getAmmo());
        player.getEvents().trigger("collisionStart", playerFixture, nonItemFixture);

        // target fixture should be added for disposal in engine
        assertEquals(AMMO_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getAmmo());
        assertEquals(GOLD_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getGold());
        assertEquals(BANDAGE_BEFORE_PICKUP,player.getComponent(InventoryComponent.class).getBandages());
    }

    Entity createPlayer() {
        Entity player = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new InventoryComponent(5, 5, 3))
                .addComponent(new PlayerPickupComponent(PhysicsLayer.ITEM));
        player.create();
        return player;
    }

    Entity createAmmoItem() {
        Entity ammo = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setSensor(true).setLayer(PhysicsLayer.ITEM))
                .addComponent(new ItemComponent(Items.AMMO, 1))
                .addComponent(new DisposingComponent());
        ammo.create();
        return ammo;
    }

    Entity createNonItem() {
        Entity nonItem = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new DisposingComponent());
        nonItem.create();
        return nonItem;
    }

    Entity createCoinItem() {
        Entity coin = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setSensor(true).setLayer(PhysicsLayer.ITEM))
                .addComponent(new ItemComponent(Items.COINS, 1))
                .addComponent(new DisposingComponent());
        coin.create();
        return coin;
    }
}
