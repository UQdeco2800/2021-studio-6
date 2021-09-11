package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.TouchTeleportComponent;
import com.deco2800.game.components.player.PlayerRangeAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class SafehouseGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(SafehouseGameArea.class);
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(2, 2);
  private static final int NUM_BULLETS = 5;
  private static Entity door;
  private final float WALL_WIDTH = 0.1f;
  private static final String[] safehouseTextures = {
    "images/playeritems/shootingammo.png", "images/playeritems/coin.png",
    "images/Player_Sprite/front01.png", "images/playeritems/pickupammo.png",
    "images/safehouse/interior-day1-tile-ground1-latest.png",
    "images/safehouse/interior-day1-tile-door1-latest.png",
      "images/hud/dashbarFull.png",
      "images/hud/healthFull.png"
  };

  private static final String[] safeHouseTextureAtlases = {
      "images/Player_Sprite/player_movement.atlas",
      "images/hud/dashbar.atlas",
      "images/hud/health.atlas"
  };

  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  public SafehouseGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();
    displayUI();
    spawnTerrain();
    spawnDoor();
    player = spawnPlayer(); // Always spawn player after spawning terrain, else NullPointerException
    player.getEvents().trigger("disableAttack");
    spawnBullet();
    //playMusic();
  }

  public Entity getPlayer() {
    return player;
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Safehouse"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.SAFEHOUSE);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    // Left
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            GridPoint2Utils.ZERO, false,false);
    // Right
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(tileBounds.x, 0), false,false);
    // Top
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, tileBounds.y),false,false);
    // Bottom
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            GridPoint2Utils.ZERO, false,false);
  }

  public static void spawnDoor() {
    GridPoint2 center = new GridPoint2(15, 15);

    // Create entity
    door = new Entity()
            .addComponent(new TextureRenderComponent("images/safehouse/interior-day1-tile-door1-latest.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.PARAPHERNALIA))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PARAPHERNALIA))
            .addComponent(new TouchTeleportComponent(PhysicsLayer.PLAYER))
            .addComponent(new DisposingComponent());
    door.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
    door.getComponent(TextureRenderComponent.class).scaleEntity();
    door.scaleHeight(2.5f);
    door.setPosition(10, 10);
    //PhysicsUtils.setScaledCollider(door, 0.3f, 0.5f);

    // Create in the world
    ServiceLocator.getEntityService().register(door);

  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  private void spawnBullet() {
    Array<Entity> bullets = new Array<>();

    for (int i = 0; i < NUM_BULLETS; i++) {
      Entity newBullet = BulletFactory.createBullet();
      bullets.add(newBullet);
      spawnEntity(newBullet);
    }

    player.getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(safehouseTextures);
    resourceService.loadTextureAtlases(safeHouseTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(safehouseTextures);
    resourceService.unloadAssets(safeHouseTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    door.getComponent(DisposingComponent.class).toBeDisposed();

    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}