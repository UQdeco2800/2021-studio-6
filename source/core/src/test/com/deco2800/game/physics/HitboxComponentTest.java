package com.deco2800.game.physics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class HitboxComponentTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
  }

  @Test
  void shouldBeSensor() {
    Entity entity = new Entity();
    entity.addComponent(new PhysicsComponent());
    HitboxComponent component = new HitboxComponent();
    entity.addComponent(component);
    entity.create();

    assertTrue(component.getFixture().isSensor());
  }
}