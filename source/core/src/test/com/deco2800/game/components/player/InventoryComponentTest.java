package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class InventoryComponentTest {
  @Mock GameTime time;
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(time);
  }

  @Test
  void shouldSetGetGold() {
    Entity player = new Entity()
            .addComponent(new InventoryComponent(150, 5, 10, 1))
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
            .addComponent(new InventoryComponent(150, 5, 10, 1))
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
    InventoryComponent inventory = new InventoryComponent(150, 5, 10, 1);
    assertTrue(inventory.hasGold(100));
    assertFalse(inventory.hasGold(200));
  }

  @Test
  void shouldCheckHasAmmo() {
    InventoryComponent inventory = new InventoryComponent(150, 5, 10, 1);
    assertTrue(inventory.hasAmmo(5));
    assertFalse(inventory.hasAmmo(200));
  }

  @Test
  void shouldAddGold() {
    Entity player = new Entity()
            .addComponent(new InventoryComponent(150, 5, 10, 1))
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
            .addComponent(new InventoryComponent(150, 5, 10, 1))
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
            .addComponent(new InventoryComponent(150, 5, 10, 1))
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
            .addComponent(new InventoryComponent(150, 5, 10, 1))
            .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    assertTrue(inventory.hasBandages(10));
    assertFalse(inventory.hasBandages(200));
  }

  @Test
  void shouldAddBandages() {
    Entity player = new Entity()
            .addComponent(new InventoryComponent(150, 5, 10, 1))
            .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    inventory.addBandages(-500);
    assertEquals(0, inventory.getBandages());

    inventory.addBandages(100);
    inventory.addBandages(-20);
    assertEquals(80, inventory.getBandages());
  }


  @Test
  void shouldSetGetTorch() {
    Entity player = new Entity()
        .addComponent(new InventoryComponent(150, 5, 10, 1))
        .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    assertEquals(1, inventory.getTorch());

    inventory.addTorch(150);
    assertEquals(151, inventory.getTorch());
    inventory.addTorch(-50);
    assertEquals(101, inventory.getTorch());
  }

  @Test
  void shouldAddTorch() {
    Entity player = new Entity()
        .addComponent(new InventoryComponent(150, 5, 10, 100))
        .addComponent(new PlayerInterfaceDisplay());
    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    inventory.addTorch(500);
    assertEquals(600, inventory.getTorch());
    inventory.addTorch(100);
    inventory.addTorch(-20);
    assertEquals(680, inventory.getTorch());
  }

  @Test
  void shouldTorchDecrease() {
    when(time.getTime()).thenReturn(0L);
    Entity player = new Entity()
        .addComponent(new InventoryComponent(150, 5, 10, 100))
        .addComponent(new PlayerInterfaceDisplay());

    InventoryComponent inventory = player.getComponent(InventoryComponent.class);
    inventory.addTorch(500);
    assertEquals(600, inventory.getTorch());
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    assertEquals(599, inventory.getTorch());
  }

  @Test
  void shouldTorchDecreaseMultiple() {
    when(time.getTime()).thenReturn(0L);
    Entity player = new Entity()
        .addComponent(new InventoryComponent(150, 5, 10, 100))
        .addComponent(new PlayerInterfaceDisplay());

    InventoryComponent inventory = player.getComponent(InventoryComponent.class);

    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    assertEquals(97, inventory.getTorch());
  }

  @Test
  void shouldTorchNotDecrease() {
    Entity player = new Entity()
        .addComponent(new InventoryComponent(150, 5, 10, 100))
        .addComponent(new PlayerInterfaceDisplay());

    InventoryComponent inventory = player.getComponent(InventoryComponent.class);

    when(time.getTimeSince(0)).thenReturn(100L); // on delay time
    inventory.update();
    assertEquals(100, inventory.getTorch());
  }

  @Test
  void shouldTorchRunOut() {
    Entity player = new Entity()
        .addComponent(new InventoryComponent(150, 5, 10, 3))
        .addComponent(new PlayerInterfaceDisplay());

    InventoryComponent inventory = player.getComponent(InventoryComponent.class);

    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();

    assertEquals(0, inventory.getTorch());
  }

  @Test
  void shouldTorchRunOutThenAdd() {
    Entity player = new Entity()
        .addComponent(new InventoryComponent(150, 5, 10, 3))
        .addComponent(new PlayerInterfaceDisplay());

    InventoryComponent inventory = player.getComponent(InventoryComponent.class);

    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();

    assertEquals(0, inventory.getTorch());
    inventory.addTorch(100);
    assertEquals(100, inventory.getTorch());

    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    assertEquals(97, inventory.getTorch());
  }

  @Test
  void shouldTorchNotDecreaseToggled() {
    Entity player = new Entity()
        .addComponent(new InventoryComponent(150, 5, 10, 100))
        .addComponent(new PlayerInterfaceDisplay());

    InventoryComponent inventory = player.getComponent(InventoryComponent.class);

    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();

    inventory.torchToggle();
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    assertEquals(99, inventory.getTorch());

    inventory.torchToggle();
    when(time.getTimeSince(0)).thenReturn(1000L); // on delay time
    inventory.update();
    assertEquals(98, inventory.getTorch());
  }
}
