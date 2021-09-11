package com.deco2800.game.components.player;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {
  @Mock GameTime time;
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(time);
  }

  @Test
  void shouldSetGetGold() {
    Entity player = new Entity()
            .addComponent(new InventoryComponent(150, 5, 10))
            .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    assertEquals(150, inventory.getGold());

    inventory.setGold(150);
    assertEquals(150, inventory.getGold());

    inventory.setGold(-50);
    assertEquals(0, inventory.getGold());
  }

  @Test
  void shouldSetGetAmmo() {
    Entity player = new Entity()
            .addComponent(new InventoryComponent(150, 5, 10))
            .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    assertEquals(5, inventory.getAmmo());

    inventory.setAmmo(150);
    assertEquals(150, inventory.getAmmo());

    inventory.setAmmo(-50);
    assertEquals(0, inventory.getAmmo());
  }

  @Test
  void shouldCheckHasGold() {
    InventoryComponent inventory = new InventoryComponent(150, 5, 10);
    assertTrue(inventory.hasGold(100));
    assertFalse(inventory.hasGold(200));
  }

  @Test
  void shouldCheckHasAmmo() {
    InventoryComponent inventory = new InventoryComponent(150, 5, 10);
    assertTrue(inventory.hasAmmo(5));
    assertFalse(inventory.hasAmmo(200));
  }

  @Test
  void shouldAddGold() {
    Entity player = new Entity()
            .addComponent(new InventoryComponent(150, 5, 10))
            .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    inventory.addGold(-500);
    assertEquals(0, inventory.getGold());

    inventory.addGold(100);
    inventory.addGold(-20);
    assertEquals(80, inventory.getGold());
  }

  @Test
  void shouldAddAmmo() {
    Entity player = new Entity()
            .addComponent(new InventoryComponent(150, 5, 10))
            .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    inventory.addAmmo(-500);
    assertEquals(0, inventory.getAmmo());

    inventory.addAmmo(100);
    inventory.addAmmo(-20);
    assertEquals(80, inventory.getAmmo());
  }

  @Test
  void shouldSetGetBandages() {
    Entity player = new Entity()
            .addComponent(new InventoryComponent(150, 5, 10))
            .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    assertEquals(10, inventory.getBandages());

    inventory.setBandages(150);
    assertEquals(150, inventory.getBandages());

    inventory.setBandages(-50);
    assertEquals(0, inventory.getBandages());
  }

  @Test
  void shouldCheckHasBandages() {
    Entity player = new Entity()
            .addComponent(new InventoryComponent(150, 5, 10))
            .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    assertTrue(inventory.hasBandages(10));
    assertFalse(inventory.hasBandages(200));
  }

  @Test
  void shouldAddBandages() {
    Entity player = new Entity()
            .addComponent(new InventoryComponent(150, 5, 10))
            .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    inventory.addBandages(-500);
    assertEquals(0, inventory.getBandages());

    inventory.addBandages(100);
    inventory.addBandages(-20);
    assertEquals(80, inventory.getBandages());
  }
}
