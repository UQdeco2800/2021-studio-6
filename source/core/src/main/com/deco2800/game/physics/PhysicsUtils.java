package com.deco2800.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;

public class PhysicsUtils {

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledCollider(Entity entity, float scaleX, float scaleY) {
    Vector2 boundingBox = entity.getScale().cpy().scl(scaleX, scaleY);
    entity
      .getComponent(ColliderComponent.class)
      .setAsBoxAligned(boundingBox, PhysicsComponent.AlignX.Center, PhysicsComponent.AlignY.Bottom);
  }
}
