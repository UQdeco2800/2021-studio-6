package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.components.EntityEffectsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.utils.math.RandomUtils;

import java.awt.*;

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
   * Creates a dead tree entity for level 1.
   * @return entity
   */
  public static Entity createBarrier() {
    Entity tree =
        new Entity()
            .addComponent(new TextureRenderComponent("images/level_1/road_barrier.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2f);
    PhysicsUtils.setScaledCollider(tree, 0.4f, 1f);
    return tree;
  }

  /**
   * Creates a dead tree entity for level 1.
   * @return entity
   */
  public static Entity createDeadTree() {
    Entity tree =
        new Entity()
            .addComponent(new TextureRenderComponent("images/level_1/dead_tree1-day1-latest.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();

    tree.scaleHeight(3.5f);
    PhysicsUtils.setScaledCollider(tree, 0.8f, 0.8f);
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

  public static Entity createLamp() {
    String lampPath;
    if (RandomUtils.randomInt(2) == 1) {
      lampPath = "images/level_1/street_lamp.png";
    } else {
      lampPath = "images/level_1/street_lamped_vined.png";
    }
    Entity lamp =
            new Entity()
                    .addComponent(new TextureRenderComponent(lampPath))
                    .addComponent(new PointLightComponent(Color.ORANGE, 4f, 0, 0))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new HitboxComponent());

    lamp.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    lamp.getComponent(TextureRenderComponent.class).scaleEntity();
    lamp.scaleHeight(2.0f);
    lamp.scaleWidth(1f);
    PhysicsUtils.setScaledCollider(lamp, 0.5f, 0.5f);
    return lamp;
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
   * @return entity
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

  public static Entity createBuilding(int buildingNumber) {
    String buildingPath;
    if (buildingNumber == 1) {
      buildingPath = "images/level_1/building2-day1-latest.png";
    } else {
      buildingPath = "images/level_1/building3-day1-latest.png";
    }
    Entity building =
        new Entity()
            .addComponent(new TextureRenderComponent(buildingPath))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    building.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    building.getComponent(TextureRenderComponent.class).scaleEntity();
    building.scaleHeight(10f);
    PhysicsUtils.setScaledCollider(building, 0.7f, 0.6f);
    return building;
  }

  /**
   * Creates a torch entity, lightens the background of the map.
   * @return entity
   */
  public static Entity createTorch() {
    Entity torch =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_2/level2_torch_frame1_ver1.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new HitboxComponent())
        .addComponent(new PointLightComponent(Colors.get("BLACK"), 4f, 0f, 0.25f));

    torch.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    torch.getComponent(TextureRenderComponent.class).scaleEntity();
    torch.scaleHeight(1.0f);
    return torch;
  }

  /**
   * Creates a triangle group of green pine tree entity.
   * @return entity
   */
  public static Entity createTriGreenPineTree() {
    Entity tree =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_2/level2_tree_2_group_ver1.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.25f);
    PhysicsUtils.setScaledCollider(tree, 1f, 0.65f);
    return tree;
  }

  /**
   * Creates a triangle group of dead pine tree entity.
   * @return entity
   */
  public static Entity createTriDeadPineTree() {
    Entity tree =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/level3_dead_group_pine_tree.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.25f);
    PhysicsUtils.setScaledCollider(tree, 1f, 0.65f);
    return tree;
  }

  /**
   * Creates a triangle group of brown pine tree entity.
   * @return entity
   */
  public static Entity createTriBrownPineTree() {
    Entity tree =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/level3_brown_group_pine_tree.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.25f);
    PhysicsUtils.setScaledCollider(tree, 1f, 0.65f);
    return tree;
  }

  /**
   * Creates a top-left grass edge water tile (1).
   * |1|X|X|
   * |X|X|X|
   * |X|X|X|
   * @return entity
   */
  public static Entity createWaterTile1() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-top-left.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a top grass edge water tile (2).
   * |X|2|X|
   * |X|X|X|
   * |X|X|X|
   * @return entity
   */
  public static Entity createWaterTile2() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-top.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a top-right grass edge water tile (3).
   * |X|X|3|
   * |X|X|X|
   * |X|X|X|
   * @return entity
   */
  public static Entity createWaterTile3() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-top-right.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a left grass edge water tile (4).
   * |X|X|X|
   * |4|X|X|
   * |X|X|X|
   * @return entity
   */
  public static Entity createWaterTile4() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-left.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a full water tile (5).
   * |X|X|X|
   * |X|5|X|
   * |X|X|X|
   * @return entity
   */
  public static Entity createWaterTile5() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-full.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a right grass edge water tile (5).
   * |X|X|X|
   * |X|X|6|
   * |X|X|X|
   * @return entity
   */
  public static Entity createWaterTile6() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-right.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a bottom-left grass edge water tile (7).
   * |X|X|X|
   * |X|X|X|
   * |7|X|X|
   * @return entity
   */
  public static Entity createWaterTile7() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-bottom-left.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a bottom-left grass edge water tile (8).
   * |X|X|X|
   * |X|X|X|
   * |X|8|X|
   * @return entity
   */
  public static Entity createWaterTile8() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-bottom.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a bottom-left grass edge water tile (8).
   * |X|X|X|
   * |X|X|X|
   * |X|X|9|
   * @return entity
   */
  public static Entity createWaterTile9() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-bottom-right.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a walkable bridge asset (vertical-left) for Level 3.
   * @return entity
   */
  public static Entity createBridgeVerticalLeftTile() {
    Entity bridge =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/bridge_tile_left-vertical.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new HitboxComponent());

    bridge.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    bridge.getComponent(TextureRenderComponent.class).scaleEntity();
    return bridge;
  }

  /**
   * Creates a walkable bridge asset (vertical-left) for Level 3.
   * @return entity
   */
  public static Entity createBridgeVerticalRightTile() {
    Entity bridge =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/level_3/bridge_tile_right-vertical.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new HitboxComponent());

    bridge.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    bridge.getComponent(TextureRenderComponent.class).scaleEntity();
    return bridge;
  }

  /**
   * Creates a top sand edge water tile (2).
   * |X|2|X|
   * |X|X|X|
   * |X|X|X|
   * @return entity
   */
  public static Entity createWaterSandTile2() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-top-sand.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a top-right sand edge water tile (3).
   * |X|X|3|
   * |X|X|X|
   * |X|X|X|
   * @return entity
   */
  public static Entity createWaterSandTile3() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-top-right-sand.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  /**
   * Creates a right sand edge water tile (6).
   * |X|X|X|
   * |X|X|6|
   * |X|X|X|
   * @return entity
   */
  public static Entity createWaterSandTile6() {
    Entity waterTile =
      new Entity()
        .addComponent(new TextureRenderComponent("images/level_3/new_darker_water_tiles/water-right-sand.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    waterTile.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    waterTile.getComponent(TextureRenderComponent.class).scaleEntity();
    waterTile.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(waterTile, 1f, 1f);
    return waterTile;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
