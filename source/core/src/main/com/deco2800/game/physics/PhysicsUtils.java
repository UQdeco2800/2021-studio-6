package com.deco2800.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;

public class PhysicsUtils {

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledCollider(Entity entity, float scaleX, float scaleY) {
      Vector2 boundingBox = entity.getScale().cpy().scl(scaleX, scaleY);
      entity.getComponent(ColliderComponent.class).setAsBoxAligned(
                boundingBox, PhysicsComponent.AlignX.CENTER, PhysicsComponent.AlignY.BOTTOM);
  }

  public static void setEntityPhysics(Entity entity, float density, float scaleX, float scaleY, float offsetX, float offsetY) {
    ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);
    HitboxComponent hitboxComponent = entity.getComponent(HitboxComponent.class);
    colliderComponent.setDensity(density);
    Vector2 hitboxScale = new Vector2(scaleX,scaleY);
    Vector2 hitboxPosition = new Vector2(entity.getCenterPosition().x + offsetX, entity.getCenterPosition().y + offsetY);
    colliderComponent.setAsBox(hitboxScale, hitboxPosition);
    hitboxComponent.setAsBox(hitboxScale, hitboxPosition);
  }

  private PhysicsUtils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
