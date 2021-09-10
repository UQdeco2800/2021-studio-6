package com.deco2800.game.components.player;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {
  @Test
  void shouldSetGetGold() {
    InventoryComponent inventory = new InventoryComponent(100, 5, 10);
    assertEquals(100, inventory.getGold());

    inventory.setGold(150);
    assertEquals(150, inventory.getGold());

    inventory.setGold(-50);
    assertEquals(0, inventory.getGold());
  }

  @Test
  void shouldSetGetAmmo() {
    InventoryComponent inventory = new InventoryComponent(100, 5, 10);
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
    InventoryComponent inventory = new InventoryComponent(100, 5, 10);
    inventory.addGold(-500);
    assertEquals(0, inventory.getGold());

    inventory.addGold(100);
    inventory.addGold(-20);
    assertEquals(80, inventory.getGold());
  }

  @Test
  void shouldAddAmmo() {
    InventoryComponent inventory = new InventoryComponent(100, 5, 10);
    inventory.addAmmo(-500);
    assertEquals(0, inventory.getAmmo());

    inventory.addAmmo(100);
    inventory.addAmmo(-20);
    assertEquals(80, inventory.getAmmo());
  }

  @Test
  void shouldSetGetBandages() {
    InventoryComponent inventory = new InventoryComponent(100, 5, 10);
    assertEquals(10, inventory.getBandages());

    inventory.setBandages(150);
    assertEquals(150, inventory.getBandages());

    inventory.setBandages(-50);
    assertEquals(0, inventory.getBandages());
  }

  @Test
  void shouldCheckHasBandages() {
    InventoryComponent inventory = new InventoryComponent(150, 5, 10);
    assertTrue(inventory.hasBandages(10));
    assertFalse(inventory.hasBandages(200));
  }

  @Test
  void shouldAddBandages() {
    InventoryComponent inventory = new InventoryComponent(100, 5, 10);
    inventory.addBandages(-500);
    assertEquals(0, inventory.getBandages());

    inventory.addBandages(100);
    inventory.addBandages(-20);
    assertEquals(80, inventory.getBandages());
  }
}
