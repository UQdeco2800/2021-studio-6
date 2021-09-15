package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.player.PlayerRangeAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class Level2 extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(Level2.class);
  private static final int NUM_COBWEBS = 7;
  private static final int NUM_BUSH = 7;
  private static final int NUM_BULLETS = 5;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(1, 7);
  private static final float WALL_WIDTH = 0.1f;

  // this can be removed - this is purely for testing purposes
  private static final int NUM_AMMO_PICKUPS = 3;
  private static final int NUM_COIN_PICKUPS = 3;

  private static final String[] forestTextures = {
    "images/playeritems/shootingammo.png", "images/playeritems/pickupammo.png", "images/playeritems/coin.png",
    "images/Player_Sprite/front01.png", "images/playeritems/bandage/bandage01.png",
    "images/obstacle_sprite/cobweb.png",
    "images/obstacle_sprite/bush.png",
    "images/tree.png",
    "images/ghost_king.png",
    "images/level_2/level2_grass_1.png",
    "images/level_2/level2_grass_2.png",
    "images/level_2/level2_grass_3.png",
    "images/level_2/level2_grass_4.png",
    "images/level_2/level2_grass_5.png",
    "images/level_2/level2_grass_6.png",
    "images/level_2/level2_tree_1-1.png",
    "images/level_2/level2_tree_2-1.png",
    "images/gunman.png",
    "images/eye.png",
    "images/blood_ball.png",
    "images/player.png",
    "images/large_enemy_pix.png",
    "images/largeEnemy.png",
    "images/safehouse/exterior-day1-latest.png",
    "images/hud/dashbarFull.png",
    "images/hud/healthFull.png"
  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas",
    "images/largeEnemy.atlas",
    "images/ghost.atlas",
    "images/ghostKing.atlas",
    "images/small_enemy.atlas",
    "images/Player_Sprite/player_movement.atlas",
    "images/hud/dashbar.atlas",
    "images/hud/health.atlas",
    "images/weapon/sword.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  public Level2(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();
    displayUI();

    spawnTerrain();
    spawnPineTrees();
    spawnBigTrees();
    spawnSafehouse();
    spawnCobweb();
    spawnBush();

    player = spawnPlayer();
    spawnBullet();
    spawnPickupItems();

    spawnSmallEnemy();
    spawnLargeEnemy();
    spawnLongRangeEnemies();

    playMusic();
  }

  public Entity getPlayer() {
    return player;
  }

  private void displayUI() {
    Entity ui = new Entity();
    // Can change level name here
    ui.addComponent(new GameAreaDisplay("Level 2"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    // Left
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            GridPoint2Utils.ZERO, false, false);
    // Right
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(tileBounds.x, 0),false,false);
    // Top
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, tileBounds.y),false,false);
    // Bottom
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            GridPoint2Utils.ZERO, false, false);
    // Right
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(tileBounds.x, 0),
            false,
            false);
    // Top
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, tileBounds.y),
            false,
            false);
    // Bottom
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);
  }

  private void spawnPineTrees() {
    LinkedList<GridPoint2> spawnLocations = new LinkedList<>();

    // Add horizontal BOTTOM and TOP outer out of bound tree edge
    for (int i = 0; i < 30; ++i) {
      spawnLocations.add(new GridPoint2(i,0));  // bottom
      spawnLocations.add(new GridPoint2(i,14)); // top
    }

    // Add vertical LEFT outer out of bound tree edge
    int gapLeftBottom = 6;
    int gapLeftTop = 8;
    for (int y = 0; y < 14; ++y) {
      if (y < gapLeftBottom || y > gapLeftTop ) {
        spawnLocations.add(new GridPoint2(0,y));
      }
    }

    // Add vertical RIGHT outer out of bound tree edge
    int gapRightBottom = 4;
    int gapRightTop = 7;
    for (int y = 0; y < 14; ++y) {
      if (y < gapRightBottom || y > gapRightTop ) {
        spawnLocations.add(new GridPoint2(29, y));
      }
    }

    // Add bottom-left inner bound tree edge
    spawnLocations.add(new GridPoint2(5,1));
    spawnLocations.add(new GridPoint2(5,2));
    spawnLocations.add(new GridPoint2(5,3));
    spawnLocations.add(new GridPoint2(5,4));

    // Spawn the pine trees at designated positions
    for (int i = 0; i < spawnLocations.size(); i++) {
      Entity tree = ObstacleFactory.createPineTree();
      spawnEntityAt(tree, spawnLocations.get(i), true, false);
    }
  }

  private void spawnBigTrees() {
    GridPoint2[] spawnLocations = {
      // Left side
      new GridPoint2(1, 9),
      new GridPoint2(3, 9),
      new GridPoint2(5, 9),

      // "L" shape middle
      new GridPoint2(10, 4),
      new GridPoint2(12, 4),
      new GridPoint2(14, 4),
      new GridPoint2(16, 4),

      new GridPoint2(10, 5),
      new GridPoint2(10, 6),
      new GridPoint2(10, 7),
      new GridPoint2(10, 8),
      new GridPoint2(10, 9),
      new GridPoint2(10, 10),
      new GridPoint2(10, 11),
      new GridPoint2(10, 12),
      new GridPoint2(10, 13),

      // Right Side
      new GridPoint2(28, 4),
      new GridPoint2(26, 4),
      new GridPoint2(24, 4),
      new GridPoint2(22, 4),

      new GridPoint2(22, 5),
      new GridPoint2(22, 6),
      new GridPoint2(22, 7),
      new GridPoint2(22, 8),

      new GridPoint2(22, 8),
      new GridPoint2(20, 8),
      new GridPoint2(18, 8),
      new GridPoint2(16, 8),
      new GridPoint2(14, 8),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity tree = ObstacleFactory.createBigTree();
      spawnEntityAt(tree, spawnLocations[i], true, false);
    }
  }

  public void spawnSafehouse() {
    GridPoint2 center = new GridPoint2(28, 6);

    Entity safehouse = SafehouseFactory.createSafehouse();
    // Position is currently procedurally (kidding, just randomly) generated.
    spawnEntityAt(safehouse, center, true, false);
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

  private void spawnSmallEnemy() {//this da noo 1
    GridPoint2[] spawnLocations = {
      new GridPoint2(8, 5),
      new GridPoint2(7,12),
      new GridPoint2(20, 2),
      new GridPoint2(22,2),
      new GridPoint2(19, 4),
      new GridPoint2(20,6),
      new GridPoint2(16,7),
      new GridPoint2(12, 12),
      new GridPoint2(21,12),
      new GridPoint2(27,12),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity smallEnemy = NPCFactory.createSmallEnemy(player);
      spawnEntityAt(smallEnemy, spawnLocations[i], true, true);
    }
  }

  private void spawnLargeEnemy() {
    GridPoint2[] spawnLocations = {
      new GridPoint2(2, 2),
      new GridPoint2(4,12),
      new GridPoint2(10, 2),
      new GridPoint2(24,2),
      new GridPoint2(29, 12),
      new GridPoint2(18,13),
      new GridPoint2(25,7)
  };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity largeEnemy = NPCFactory.createLargeEnemy(player);
      spawnEntityAt(largeEnemy, spawnLocations[i], true, true);
    }
  }

  private void spawnLongRangeEnemies() {
    GridPoint2[] spawnLocations = {
      new GridPoint2(14, 2),
      new GridPoint2(16,2),
      new GridPoint2(12, 7),
      new GridPoint2(14,7),
      new GridPoint2(25, 10),
      new GridPoint2(25,12)
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity archer = NPCFactory.createLongRangeEnemy(player, this);
      spawnEntityAt(archer, spawnLocations[i], true, true);
    }
  }

  private void spawnCobweb() {
    GridPoint2[] spawnLocations = {
      new GridPoint2(7, 3),
      new GridPoint2(8, 10),
      new GridPoint2(12,3),
      new GridPoint2(21, 2),

      new GridPoint2(20, 3),
      new GridPoint2(19,7),
      new GridPoint2(18, 6),

      new GridPoint2(16, 11),
      new GridPoint2(20,13),
      new GridPoint2(27, 10),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity cobweb = ObstacleFactory.createCobweb();
      spawnEntityAt(cobweb, spawnLocations[i], true, false);
    }
  }

  private void spawnBush() {
    GridPoint2[] spawnLocations = {
      new GridPoint2(3, 4),
      new GridPoint2(8, 8),

      new GridPoint2(7, 12),
      new GridPoint2(26,3),
      new GridPoint2(14, 11),

      new GridPoint2(16, 13),
      new GridPoint2(23,11),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity bush = ObstacleFactory.createBush();
      spawnEntityAt(bush, spawnLocations[i], true, false);
    }
  }

  private void spawnPickupItems() {
    GridPoint2[] ammoSpawnLocations = {
      new GridPoint2(1, 13),
      new GridPoint2(27,2),
      new GridPoint2(13, 13),
    };

    GridPoint2[] bandageSpawnLocations = {
      new GridPoint2(28, 2),
      new GridPoint2(12,13),
    };

    GridPoint2[] coinSpawnLocations = {
      new GridPoint2(2, 12),
      new GridPoint2(4,2),
      new GridPoint2(8, 2),
      new GridPoint2(18, 2),
      new GridPoint2(15, 12),
      new GridPoint2(23,12),
      new GridPoint2(27, 9),
    };

    for (int i = 0; i < ammoSpawnLocations.length; i++) {
      int randomAmmoQuantity = RandomUtils.randomInt(5);
      Entity pickupAmmo = ItemFactory.createAmmoPickup(randomAmmoQuantity);
      spawnEntityAt(pickupAmmo, ammoSpawnLocations[i], true, false);
    }

//    for (int i = 0; i < bandageSpawnLocations.length; i++) {
//      int randomAmmoQuantity = RandomUtils.randomInt(5);
//      Entity pickupAmmo = ItemFactory.create;
//      spawnEntityAt(pickupAmmo, ammoSpawnLocations[i], true, false);
//    }

    for (int i = 0; i < coinSpawnLocations.length; i++) {
      int randomCoinQuantity = RandomUtils.randomInt(5);
      Entity pickupCoin = ItemFactory.createCoinPickup(randomCoinQuantity);
      spawnEntityAt(pickupCoin, coinSpawnLocations[i], true, false);
    }
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
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
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
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {

    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}