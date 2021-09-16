package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.components.EntityEffectsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {

  /**
   * Creates a wide tree entity.
   * @return entity
   */
  public static Entity createBigTree() {
    Entity tree =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_2/level2_tree_1-1.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(tree, 0.75f, 0.75f);
    return tree;
  }

  /**
   * Creates a pine tree entity.
   * @return entity
   */
  public static Entity createPineTree() {
    Entity tree =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_2/level2_tree_2-1.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(tree, 0.75f, 0.55f);
    return tree;
  }

  /**
   * Create a cobweb entity, slows the player's movement.
   * @return entity
   */
  public static Entity createCobweb() {
    Entity cobweb =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/obstacle_sprite/cobweb.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new HitboxComponent())
                    .addComponent(new EntityEffectsComponent(PhysicsLayer.PLAYER, EntityEffectsComponent.Effect.MOVEMENT));

    cobweb.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    cobweb.getComponent(TextureRenderComponent.class).scaleEntity();
    cobweb.scaleHeight(1.0f);

    //TODO: Investigate ColliderComponent instead of HitboxComponent and different PhysicsLayers.

    return cobweb;
  }

  /**
   * Creates a bush entity, does damage to the player when they walk over it.
   * @return entity
   */
  public static Entity createBush() {
    Entity bush =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/obstacle_sprite/bush.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new HitboxComponent())
                    .addComponent(new EntityEffectsComponent(PhysicsLayer.PLAYER, EntityEffectsComponent.Effect.HEALTH));

    bush.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    bush.getComponent(TextureRenderComponent.class).scaleEntity();
    bush.scaleHeight(1.0f);
    return bush;
  }
  /**
   * Creates an invisible physics wall.
   * @param width Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    wall.setScale(width, height);
    return wall;
  }

  /**
   * Creates an object given the path to its image and its size
   * @param image Path to the image to be used for object
   * @param height How much to scale the height of the image
   */
  public static Entity createObject(String image, float height) {
    Entity object =
        new Entity()
            .addComponent(new TextureRenderComponent(image))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    object.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    object.getComponent(TextureRenderComponent.class).scaleEntity();
    object.scaleHeight(height);
    PhysicsUtils.setScaledCollider(object, 1f, 1f);
    return object;
  }

  public static Entity createBuilding() {
    Entity building =
        new Entity()
            .addComponent(new TextureRenderComponent("images/level_1/building2-day1-latest.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    building.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    building.getComponent(TextureRenderComponent.class).scaleEntity();
    building.scaleHeight(10f);
    return building;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
