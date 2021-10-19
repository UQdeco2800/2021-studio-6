package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.player.PlayerRangeAOEComponent;
import com.deco2800.game.components.player.PlayerRangeAttackComponent;
import com.deco2800.game.components.story.StoryManager;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.components.tasks.SpawnerEnemyTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class Level3 extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(Level3.class);
  private static final int NUM_BULLETS = 5; // Must be 5, to allow range-attack.
  private static final int NUM_SPAWNER_ENEMY = 0;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(1, 41);
  private static final float WALL_WIDTH = 0.1f;
  private static final String NPC_DEAD_PILOT_ATLAS_FILENAME = "images/npc_movement/dead_pilot_npc.atlas";
  private static final String[] forestTextures = {
    "images/obstacle_sprite/cobweb.png",
    "images/obstacle_sprite/bush.png",
    "images/level_2/level2_grass_1.png",
    "images/level_2/level2_grass_2.png",
    "images/level_2/level2_grass_3.png",
    "images/level_2/level2_grass_4.png",
    "images/level_2/level2_grass_5.png",
    "images/level_2/level2_grass_6.png",
    "images/level_2/level2_tree_1-1.png",
    "images/level_2/level2_tree_2-1.png",
    "images/level_2/level2_background_tile.png",
    "images/safehouse/exterior-day1-latest.png",
    "images/grass_1.png",
    "images/grass_2.png",
    "images/grass_3.png",
    "images/level_3/level3_dead_group_pine_tree.png",
    "images/level_3/level3_brown_group_pine_tree.png",
    "images/level_2/level2_tree_2_group_ver1.png",
    "images/level_3/new_darker_water_tiles/water-bottom-left.png",
    "images/level_3/new_darker_water_tiles/water-bottom.png",
    "images/level_3/new_darker_water_tiles/water-bottom-right.png",
    "images/level_3/new_darker_water_tiles/water-full.png",
    "images/level_3/new_darker_water_tiles/water-left.png",
    "images/level_3/new_darker_water_tiles/water-right.png",
    "images/level_3/new_darker_water_tiles/water-top.png",
    "images/level_3/new_darker_water_tiles/water-top-left.png",
    "images/level_3/new_darker_water_tiles/water-top-right.png",
    "images/level_3/level3_grass_tiles/grass-1.png",
    "images/level_3/level3_grass_tiles/grass-2.png",
    "images/level_3/level3_grass_tiles/grass-3.png",
    "images/level_3/level3_grass_tiles/grass-4.png",
    "images/level_3/level3_grass_tiles/grass-base.png",
    "images/level_3/bridge_horizontal.png",
    "images/level_3/bridge_vertical.png",
    "images/level_3/bridge_tile.png",
    "images/level_3/bridge_tile_left-vertical.png",
    "images/level_3/bridge_tile_right-vertical.png",
    "images/level_3/sand.png",
    "images/level_3/new_darker_water_tiles/water-right-sand.png",
    "images/level_3/new_darker_water_tiles/water-top-right-sand.png",
    "images/level_3/new_darker_water_tiles/water-top-sand.png",
    "images/dialogue/raw/npc_indicator.png",
    "images/level_2/fire-fly-bug-NPC.png",
    "images/playeritems/bandage/bandage01.png"
  };

  /**
   * Texture atlases file path for Level 3.
   */
  private static final String[] forestTextureAtlases = {
    NPC_DEAD_PILOT_ATLAS_FILENAME
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};

  private static final String BACKGROUND_MUSIC = "sounds/fireflies-theme-sneak.mp3";
  private static final String[] LEVEL3_MUSIC = {BACKGROUND_MUSIC};

  private final TerrainFactory terrainFactory;

  public Level3(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();
    displayUI();

    // Spawn terrain/assets entities
    spawnTerrain();
    spawnTerrainTriGreenPineTrees();
    spawnTerrainTriBrownPineTrees();
    spawnSafehouse();
    spawnCobweb();
    spawnBush();
    spawnWaterGreenTiles();
    spawnBridge();
    spawnWaterSandTiles();
    spawnFireFlyBugNPC();

    // Spawn player related entities
    player = spawnPlayer();
    spawnBullet();
    spawnFireCracker();
    spawnPickupItems();
    spawnPilotNpc();

    spawnLevelThreeIntro();

    // Spawn enemy entities
    spawnSmallEnemy();
    spawnLongRangeEnemies();
    spawnLargeEnemy();
    spawnSpawnerEnemy();
    spawnToughLongRangeEnemies();

    // Listener for level 3 intro to finish and then play music
    StoryManager.getInstance().getEntity().getEvents().addListener("story-finished:" + StoryNames.LEVEL3_INTRO,
        this::playMusic);
  }

  /**
   * Displays the name of the level on the top left screen.
   */
  private void displayUI() {
    Entity ui = new Entity();
    // Can change level name here
    ui.addComponent(new GameAreaDisplay("Level 3"));
    spawnEntity(ui);
  }

  /**
   * Spawns the Level 3 forrest tile layout and hard terrain boundaries to stop the player
   * and enemies from going outside the map.
   */
  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST2);
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

  /**
   * Spawns the group green pine trees that act as a visual terrain boundary.
   */
  private void spawnTerrainTriGreenPineTrees() {
    // This looks pretty ugly right now I know, this was generated with excel
    GridPoint2[] spawnLocations = {
      new GridPoint2(0, 45), new GridPoint2(1, 45), new GridPoint2(2, 45),
      new GridPoint2(3, 45), new GridPoint2(4, 45), new GridPoint2(5, 45),
      new GridPoint2(6, 45), new GridPoint2(7, 45), new GridPoint2(8, 45),
      new GridPoint2(9, 45), new GridPoint2(10, 45), new GridPoint2(11, 45),
      new GridPoint2(12, 45), new GridPoint2(13, 45), new GridPoint2(14, 45),
      new GridPoint2(15, 45), new GridPoint2(16, 45), new GridPoint2(17, 45),
      new GridPoint2(18, 45), new GridPoint2(0, 44), new GridPoint2(18, 44),
      new GridPoint2(0, 43), new GridPoint2(18, 43), new GridPoint2(18, 42),
      new GridPoint2(18, 41), new GridPoint2(18, 40), new GridPoint2(18, 39),
      new GridPoint2(0, 38), new GridPoint2(18, 38), new GridPoint2(0, 37),
      new GridPoint2(18, 37), new GridPoint2(0, 36), new GridPoint2(1, 36),
      new GridPoint2(2, 36), new GridPoint2(3, 36), new GridPoint2(4, 36),
      new GridPoint2(5, 36), new GridPoint2(6, 36), new GridPoint2(7, 36),
      new GridPoint2(8, 36), new GridPoint2(9, 36), new GridPoint2(18, 36),
      new GridPoint2(0, 35), new GridPoint2(18, 35), new GridPoint2(0, 34),
      new GridPoint2(18, 34), new GridPoint2(0, 33), new GridPoint2(18, 33),
      new GridPoint2(0, 32), new GridPoint2(18, 32), new GridPoint2(0, 31),
      new GridPoint2(18, 31), new GridPoint2(0, 30), new GridPoint2(18, 30),
      new GridPoint2(0, 29), new GridPoint2(18, 29), new GridPoint2(0, 28),
      new GridPoint2(18, 28), new GridPoint2(0, 27), new GridPoint2(9, 27),
      new GridPoint2(10, 27), new GridPoint2(11, 27), new GridPoint2(12, 27),
      new GridPoint2(13, 27), new GridPoint2(14, 27), new GridPoint2(15, 27),
      new GridPoint2(16, 27), new GridPoint2(17, 27), new GridPoint2(18, 27),
      new GridPoint2(0, 26), new GridPoint2(18, 26), new GridPoint2(0, 25),
      new GridPoint2(18, 25), new GridPoint2(0, 24), new GridPoint2(18, 24),
      new GridPoint2(0, 23), new GridPoint2(18, 23), new GridPoint2(0, 22),
      new GridPoint2(18, 22), new GridPoint2(0, 21), new GridPoint2(18, 21),
      new GridPoint2(0, 20), new GridPoint2(18, 20), new GridPoint2(0, 19),
      new GridPoint2(18, 19), new GridPoint2(0, 18), new GridPoint2(9, 18),
      new GridPoint2(10, 18), new GridPoint2(11, 18), new GridPoint2(12, 18),
      new GridPoint2(13, 18), new GridPoint2(14, 18), new GridPoint2(15, 18),
      new GridPoint2(16, 18), new GridPoint2(17, 18), new GridPoint2(18, 18),
      new GridPoint2(0, 17), new GridPoint2(0, 16), new GridPoint2(0, 15),
      new GridPoint2(0, 14), new GridPoint2(0, 13), new GridPoint2(0, 12),
      new GridPoint2(0, 11), new GridPoint2(0, 10), new GridPoint2(0, 9),
      new GridPoint2(9, 9), new GridPoint2(0, 8), new GridPoint2(9, 8),
      new GridPoint2(18, 8), new GridPoint2(0, 7), new GridPoint2(9, 7),
      new GridPoint2(18, 7), new GridPoint2(0, 6), new GridPoint2(9, 6),
      new GridPoint2(18, 6), new GridPoint2(0, 5), new GridPoint2(9, 5),
      new GridPoint2(18, 5), new GridPoint2(0, 4), new GridPoint2(9, 4),
      new GridPoint2(18, 4), new GridPoint2(0, 3), new GridPoint2(9, 3),
      new GridPoint2(18, 3), new GridPoint2(0, 2), new GridPoint2(9, 2),
      new GridPoint2(18, 2), new GridPoint2(0, 1), new GridPoint2(9, 1),
      new GridPoint2(18, 1), new GridPoint2(0, 0), new GridPoint2(1, 0),
      new GridPoint2(2, 0), new GridPoint2(3, 0), new GridPoint2(4, 0),
      new GridPoint2(5, 0), new GridPoint2(6, 0), new GridPoint2(7, 0),
      new GridPoint2(8, 0), new GridPoint2(9, 0), new GridPoint2(10, 0),
      new GridPoint2(11, 0), new GridPoint2(12, 0), new GridPoint2(13, 0),
      new GridPoint2(14, 0), new GridPoint2(15, 0), new GridPoint2(16, 0),
      new GridPoint2(17, 0), new GridPoint2(18, 0),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity pineTriTree = ObstacleFactory.createTriGreenPineTree();
      spawnEntityAt(pineTriTree, spawnLocations[i], false, false);
    }
  }

  /**
   * Spawns the group brown pine trees that act as a visual terrain boundary.
   */
  private void spawnTerrainTriBrownPineTrees() {
    // This looks pretty ugly right now I know, this was generated with excel
    GridPoint2[] spawnLocations = {
      new GridPoint2(36, 45), new GridPoint2(37, 45), new GridPoint2(43, 45),
      new GridPoint2(44, 45), new GridPoint2(45, 45), new GridPoint2(46, 45),
      new GridPoint2(47, 45), new GridPoint2(48, 45), new GridPoint2(49, 45),
      new GridPoint2(50, 45), new GridPoint2(51, 45), new GridPoint2(52, 45),
      new GridPoint2(53, 45), new GridPoint2(54, 45), new GridPoint2(36, 44),
      new GridPoint2(54, 44), new GridPoint2(36, 43), new GridPoint2(54, 43),
      new GridPoint2(36, 42), new GridPoint2(54, 42), new GridPoint2(54, 41),
      new GridPoint2(54, 40), new GridPoint2(54, 39), new GridPoint2(54, 38),
      new GridPoint2(54, 37), new GridPoint2(19, 36), new GridPoint2(20, 36),
      new GridPoint2(21, 36), new GridPoint2(22, 36), new GridPoint2(23, 36),
      new GridPoint2(24, 36), new GridPoint2(25, 36), new GridPoint2(26, 36),
      new GridPoint2(27, 36), new GridPoint2(28, 36), new GridPoint2(29, 36),
      new GridPoint2(30, 36), new GridPoint2(31, 36), new GridPoint2(32, 36),
      new GridPoint2(33, 36), new GridPoint2(34, 36), new GridPoint2(35, 36),
      new GridPoint2(36, 36), new GridPoint2(54, 36), new GridPoint2(54, 35),
      new GridPoint2(24, 30), new GridPoint2(25, 30), new GridPoint2(26, 30),
      new GridPoint2(27, 30), new GridPoint2(28, 30), new GridPoint2(29, 30),
      new GridPoint2(30, 30), new GridPoint2(31, 30), new GridPoint2(32, 30),
      new GridPoint2(33, 30), new GridPoint2(34, 30), new GridPoint2(35, 30),
      new GridPoint2(36, 30), new GridPoint2(36, 29), new GridPoint2(36, 28),
      new GridPoint2(54, 28), new GridPoint2(36, 27), new GridPoint2(54, 27),
      new GridPoint2(36, 26), new GridPoint2(54, 26), new GridPoint2(36, 25),
      new GridPoint2(54, 25), new GridPoint2(19, 24), new GridPoint2(20, 24),
      new GridPoint2(21, 24), new GridPoint2(22, 24), new GridPoint2(23, 24),
      new GridPoint2(24, 24), new GridPoint2(25, 24), new GridPoint2(26, 24),
      new GridPoint2(27, 24), new GridPoint2(28, 24), new GridPoint2(29, 24),
      new GridPoint2(30, 24), new GridPoint2(36, 24), new GridPoint2(54, 24),
      new GridPoint2(36, 23), new GridPoint2(54, 23), new GridPoint2(36, 22),
      new GridPoint2(54, 22), new GridPoint2(36, 21), new GridPoint2(54, 21),
      new GridPoint2(36, 20), new GridPoint2(36, 19), new GridPoint2(24, 18),
      new GridPoint2(25, 18), new GridPoint2(26, 18), new GridPoint2(27, 18),
      new GridPoint2(28, 18), new GridPoint2(29, 18), new GridPoint2(30, 18),
      new GridPoint2(31, 18), new GridPoint2(32, 18), new GridPoint2(33, 18),
      new GridPoint2(34, 18), new GridPoint2(35, 18), new GridPoint2(36, 18),
      new GridPoint2(37, 18), new GridPoint2(38, 18), new GridPoint2(39, 18),
      new GridPoint2(40, 18), new GridPoint2(41, 18), new GridPoint2(54, 18),
      new GridPoint2(54, 17), new GridPoint2(54, 16), new GridPoint2(54, 15),
      new GridPoint2(54, 14), new GridPoint2(54, 13), new GridPoint2(54, 12),
      new GridPoint2(54, 11), new GridPoint2(54, 10), new GridPoint2(18, 9),
      new GridPoint2(19, 9), new GridPoint2(20, 9), new GridPoint2(21, 9),
      new GridPoint2(22, 9), new GridPoint2(23, 9), new GridPoint2(24, 9),
      new GridPoint2(25, 9), new GridPoint2(26, 9), new GridPoint2(27, 9),
      new GridPoint2(28, 9), new GridPoint2(29, 9), new GridPoint2(30, 9),
      new GridPoint2(31, 9), new GridPoint2(32, 9), new GridPoint2(33, 9),
      new GridPoint2(34, 9), new GridPoint2(35, 9), new GridPoint2(36, 9),
      new GridPoint2(54, 9), new GridPoint2(36, 8), new GridPoint2(54, 8),
      new GridPoint2(36, 7), new GridPoint2(54, 7), new GridPoint2(36, 6),
      new GridPoint2(54, 6), new GridPoint2(36, 5), new GridPoint2(54, 5),
      new GridPoint2(36, 4), new GridPoint2(54, 4), new GridPoint2(36, 3),
      new GridPoint2(54, 3), new GridPoint2(36, 2), new GridPoint2(54, 2),
      new GridPoint2(36, 1), new GridPoint2(54, 1), new GridPoint2(36, 0),
      new GridPoint2(37, 0), new GridPoint2(38, 0), new GridPoint2(39, 0),
      new GridPoint2(40, 0), new GridPoint2(41, 0), new GridPoint2(42, 0),
      new GridPoint2(43, 0), new GridPoint2(44, 0), new GridPoint2(45, 0),
      new GridPoint2(46, 0), new GridPoint2(47, 0), new GridPoint2(48, 0),
      new GridPoint2(49, 0), new GridPoint2(50, 0), new GridPoint2(51, 0),
      new GridPoint2(52, 0), new GridPoint2(53, 0), new GridPoint2(54, 0),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity pineTriTree = ObstacleFactory.createTriBrownPineTree();
      spawnEntityAt(pineTriTree, spawnLocations[i], false, false);
    }
  }

  /**
   * Spawns the safehouse.
   */
  public void spawnSafehouse() {
    GridPoint2 center = new GridPoint2(40, 43);

    Entity safehouse = SafehouseFactory.createSafehouse();
    // Position is currently procedurally (kidding, just randomly) generated.
    spawnEntityAt(safehouse, center, true, false);
  }

  /**
   * Gets the player entity for Level 3.
   * @return Player entity.
   */
  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  /**
   * Spawns the player entity onto the map..
   * @return Player entity.
   */
  private void spawnBullet() {
    Array<Entity> bullets = new Array<>();

    for (int i = 0; i < NUM_BULLETS; i++) {
      Entity newBullet = ProjectileFactory.createBullet();
      bullets.add(newBullet);
      spawnEntity(newBullet);
    }

    player.getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
  }

  /**
   * Spawns the fire cracker entity for player to use
   */
  private void spawnFireCracker() {
      Entity fireCracker = ProjectileFactory.createFireCracker();
      spawnEntity(fireCracker);

      player.getComponent(PlayerRangeAOEComponent.class).addFireCracker(fireCracker);
  }

  /**
   * Spawns the spawner enemy
   */
  private void spawnSpawnerEnemy() {
    GridPoint2[] spawnLocations = {
      new GridPoint2(33, 33),
      new GridPoint2(45, 22),
      new GridPoint2(13, 13),
      new GridPoint2(44, 6),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity spawnerEnemy = NPCFactory.createSpawnerEnemy();
      spawnerEnemy.getComponent(AITaskComponent.class).addTask(new SpawnerEnemyTask(player, 10, 5f, 6f, this,
              spawnerEnemy));
      spawnEntityAt(spawnerEnemy, spawnLocations[i], true, true);
    }
  }

  /**
   * Spawns a small enemy from the appropriate spawner's position
   */
  public void spawnFromSpawner(Vector2 position, int maxSpawnDistance) {
    super.spawnFromSpawner(position, maxSpawnDistance);
  }

  /**
   * Spawns the small enemies onto the map.
   */
  private void spawnSmallEnemy() {//this da noo 1
    GridPoint2[] spawnLocations = {
      new GridPoint2(31, 28),
      new GridPoint2(6, 26),
      new GridPoint2(27, 26),
      new GridPoint2(25, 22),
      new GridPoint2(3, 20),
      new GridPoint2(28, 15),
      new GridPoint2(51, 14),
      new GridPoint2(21, 12),
      new GridPoint2(32, 12),
      new GridPoint2(6, 9),
      new GridPoint2(50, 8),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity smallEnemy = NPCFactory.createSmallEnemy(player);
      spawnEntityAt(smallEnemy, spawnLocations[i], true, true);
    }
  }

  /**
   * Spawns the large enemies onto the map.
   */
  private void spawnLargeEnemy() {
    GridPoint2[] spawnLocations = {
      new GridPoint2(48, 42),
      new GridPoint2(13, 31),
      new GridPoint2(3, 27),
      new GridPoint2(21, 27),
      new GridPoint2(39, 25),
      new GridPoint2(49, 22),
      new GridPoint2(33, 21),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity largeEnemy = NPCFactory.createLargeEnemy(player);
      spawnEntityAt(largeEnemy, spawnLocations[i], true, true);
    }
  }

  /**
   * Spawns the long range enemies onto the map.
   */
  private void spawnLongRangeEnemies() {
    GridPoint2[] spawnLocations = {
      new GridPoint2(42, 38),
      new GridPoint2(46, 40),
      new GridPoint2(48, 40),
      new GridPoint2(25, 34),
      new GridPoint2(28, 32),
      new GridPoint2(3, 31),
      new GridPoint2(25, 28),
      new GridPoint2(33, 27),
      new GridPoint2(44, 25),
      new GridPoint2(41, 21),
      new GridPoint2(29, 20),
      new GridPoint2(51, 17),
      new GridPoint2(5, 14),
      new GridPoint2(15, 6),
      new GridPoint2(46, 5),
      new GridPoint2(12, 3),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity archer = NPCFactory.createLongRangeEnemy(player, this);
      spawnEntityAt(archer, spawnLocations[i], true, true);
    }
  }

  /**
   * Spawns the tough long range enemies onto the map.
   */
  private void spawnToughLongRangeEnemies() {
    GridPoint2[] spawnLocations = {
      new GridPoint2(42, 41),
      new GridPoint2(21, 33),
      new GridPoint2(13, 22),
      new GridPoint2(35, 15),
      new GridPoint2(40, 13),
      new GridPoint2(4, 4),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity toughArcher = NPCFactory.createToughLongRangeEnemy(player, this);
      spawnEntityAt(toughArcher, spawnLocations[i], true, true);
    }
  }

  /**
   * Spawns the cobwebs onto the map.
   */
  private void spawnCobweb() {
    GridPoint2[] spawnLocations = {
      new GridPoint2(10, 43),
      new GridPoint2(4, 38),
      new GridPoint2(14, 33),
      new GridPoint2(9, 29),
      new GridPoint2(24, 28),
      new GridPoint2(32, 24),
      new GridPoint2(4, 23),
      new GridPoint2(10, 23),
      new GridPoint2(30, 14),
      new GridPoint2(11, 12),
      new GridPoint2(5, 7),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity cobweb = ObstacleFactory.createCobweb();
      spawnEntityAt(cobweb, spawnLocations[i], true, false);
    }
  }

  /**
   * Spawns the bushes onto the map.
   */
  private void spawnBush() {
    GridPoint2[] spawnLocations = {
        new GridPoint2(8, 38),
        new GridPoint2(9, 34),
        new GridPoint2(38, 27),
        new GridPoint2(9, 25),
        new GridPoint2(44, 19),
        new GridPoint2(17, 16),
        new GridPoint2(2, 14),
        new GridPoint2(28, 11),
        new GridPoint2(38, 9),
        new GridPoint2(7, 5),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity bush = ObstacleFactory.createBush();
      spawnEntityAt(bush, spawnLocations[i], true, false);
    }
  }

  /**
   * Spawns the items that can be picked up by the player onto the map.
   */
  private void spawnPickupItems() {
    GridPoint2[] ammoSpawnLocations = {
      new GridPoint2(16, 41),
      new GridPoint2(36, 34),
      new GridPoint2(41, 30),
      new GridPoint2(49, 26),
      new GridPoint2(2, 25),
      new GridPoint2(20, 22),
      new GridPoint2(53, 18),
      new GridPoint2(46, 14),
      new GridPoint2(13, 5),
      new GridPoint2(43, 2),
    };

    GridPoint2[] bandageSpawnLocations = {
      new GridPoint2(17, 2),
      new GridPoint2(28, 28),
      new GridPoint2(47, 14),
      new GridPoint2(50, 26),
    };

    GridPoint2[] coinSpawnLocations = {
      new GridPoint2(52, 43),
      new GridPoint2(16, 42),
      new GridPoint2(52, 42),
      new GridPoint2(52, 41),
      new GridPoint2(52, 40),
      new GridPoint2(20, 34),
      new GridPoint2(20, 32),
      new GridPoint2(20, 30),
      new GridPoint2(15, 29),
      new GridPoint2(16, 29),
      new GridPoint2(20, 28),
      new GridPoint2(34, 28),
      new GridPoint2(20, 26),
      new GridPoint2(34, 26),
      new GridPoint2(52, 26),
      new GridPoint2(52, 25),
      new GridPoint2(16, 24),
      new GridPoint2(34, 24),
      new GridPoint2(52, 24),
      new GridPoint2(16, 23),
      new GridPoint2(52, 23),
      new GridPoint2(16, 22),
      new GridPoint2(34, 22),
      new GridPoint2(38, 22),
      new GridPoint2(16, 21),
      new GridPoint2(38, 21),
      new GridPoint2(34, 20),
      new GridPoint2(38, 20),
      new GridPoint2(38, 16),
      new GridPoint2(39, 16),
      new GridPoint2(40, 16),
      new GridPoint2(2, 2),
      new GridPoint2(3, 2),
      new GridPoint2(15, 2),
      new GridPoint2(16, 2),
    };

    for (int i = 0; i < ammoSpawnLocations.length; i++) {
      int randomAmmoQuantity = RandomUtils.randomInt(5);
      Entity pickupAmmo = ItemFactory.createAmmoPickup(randomAmmoQuantity);
      spawnEntityAt(pickupAmmo, ammoSpawnLocations[i], true, false);
    }

    for (int i = 0; i < bandageSpawnLocations.length; i++) {
      Entity pickupBandage = ItemFactory.createBandagePickup(1);
      spawnEntityAt(pickupBandage, bandageSpawnLocations[i], true, false);
    }

    for (int i = 0; i < coinSpawnLocations.length; i++) {
      int randomCoinQuantity = RandomUtils.randomInt(5);
      Entity pickupCoin = ItemFactory.createCoinPickup(randomCoinQuantity);
      spawnEntityAt(pickupCoin, coinSpawnLocations[i], true, false);
    }
  }

  private void spawnPilotNpc() {
    GridPoint2 pos = new GridPoint2(10,32);
    Entity npcTut = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.NPC_PILOT_DEAD, NPC_DEAD_PILOT_ATLAS_FILENAME, false);
    spawnEntityAt(npcTut, pos, true, true);
  }

  private void spawnLevelThreeIntro() {
    StoryManager.getInstance().loadCutScene(StoryNames.LEVEL3_INTRO);
    StoryManager.getInstance().displayStory();
  }

  /**
   * Spawns all the water green edge tiles variation (1,2,...,9) onto the map.
   */
  private void spawnWaterGreenTiles() {
    GridPoint2[] water1SpawnLocations = {
      new GridPoint2(38, 41), new GridPoint2(52, 20), new GridPoint2(41, 17),
      new GridPoint2(48, 17), new GridPoint2(43, 13), new GridPoint2(42, 12),
      new GridPoint2(38, 7), new GridPoint2(52, 6), new GridPoint2(50, 4),
      new GridPoint2(47, 3),
    };

    GridPoint2[] water2SpawnLocations = {
      new GridPoint2(36, 41), new GridPoint2(37, 41), new GridPoint2(38, 41),
      new GridPoint2(43, 36), new GridPoint2(46, 34), new GridPoint2(47, 34),
      new GridPoint2(48, 34), new GridPoint2(51, 34), new GridPoint2(52, 34),
      new GridPoint2(53, 34), new GridPoint2(54, 34), new GridPoint2(55, 34),
      new GridPoint2(53, 20), new GridPoint2(54, 20), new GridPoint2(55, 20),
      new GridPoint2(42, 17), new GridPoint2(43, 17), new GridPoint2(44, 17),
      new GridPoint2(46, 12), new GridPoint2(47, 12), new GridPoint2(51, 4),
      new GridPoint2(48, 3), new GridPoint2(49, 3),
    };

    GridPoint2[] water3SpawnLocations = {
      new GridPoint2(39, 41), new GridPoint2(40, 40), new GridPoint2(41, 38),
      new GridPoint2(42, 37), new GridPoint2(44, 36), new GridPoint2(45, 35),
      new GridPoint2(45, 17), new GridPoint2(49, 17), new GridPoint2(39, 7),
      new GridPoint2(53, 6), new GridPoint2(40, 5),
    };

    GridPoint2[] water4SpawnLocations = {
      new GridPoint2(38, 40), new GridPoint2(38, 39), new GridPoint2(38, 38),
      new GridPoint2(38, 37), new GridPoint2(41, 33), new GridPoint2(48, 16),
      new GridPoint2(44, 15), new GridPoint2(48, 15), new GridPoint2(44, 14),
      new GridPoint2(48, 14), new GridPoint2(48, 13), new GridPoint2(38, 6),
      new GridPoint2(38, 5), new GridPoint2(52, 5), new GridPoint2(38, 4),
      new GridPoint2(38, 3),
    };

    GridPoint2[] water5SpawnLocations = {
      new GridPoint2(36, 40), new GridPoint2(37, 40), new GridPoint2(38, 40),
      new GridPoint2(39, 40), new GridPoint2(36, 39), new GridPoint2(37, 39),
      new GridPoint2(38, 39), new GridPoint2(39, 39), new GridPoint2(36, 38),
      new GridPoint2(37, 38), new GridPoint2(38, 38), new GridPoint2(39, 38),
      new GridPoint2(40, 38), new GridPoint2(38, 37), new GridPoint2(39, 37),
      new GridPoint2(40, 37), new GridPoint2(41, 37), new GridPoint2(39, 36),
      new GridPoint2(40, 36), new GridPoint2(41, 36), new GridPoint2(42, 36),
      new GridPoint2(40, 35), new GridPoint2(41, 35), new GridPoint2(42, 35),
      new GridPoint2(43, 35), new GridPoint2(44, 35), new GridPoint2(41, 34),
      new GridPoint2(42, 34), new GridPoint2(43, 34), new GridPoint2(44, 34),
      new GridPoint2(45, 34), new GridPoint2(42, 33), new GridPoint2(43, 33),
      new GridPoint2(44, 33), new GridPoint2(45, 33), new GridPoint2(46, 33),
      new GridPoint2(47, 33), new GridPoint2(48, 33), new GridPoint2(51, 33),
      new GridPoint2(52, 33), new GridPoint2(53, 33), new GridPoint2(54, 33),
      new GridPoint2(55, 33), new GridPoint2(43, 32), new GridPoint2(44, 32),
      new GridPoint2(45, 32), new GridPoint2(46, 32), new GridPoint2(47, 32),
      new GridPoint2(48, 32), new GridPoint2(51, 32), new GridPoint2(52, 32),
      new GridPoint2(53, 32), new GridPoint2(54, 32), new GridPoint2(55, 32),
      new GridPoint2(44, 31), new GridPoint2(45, 31), new GridPoint2(46, 31),
      new GridPoint2(47, 31), new GridPoint2(48, 31), new GridPoint2(51, 31),
      new GridPoint2(52, 31), new GridPoint2(53, 31), new GridPoint2(54, 31),
      new GridPoint2(55, 31), new GridPoint2(46, 30), new GridPoint2(47, 30),
      new GridPoint2(48, 30), new GridPoint2(51, 30), new GridPoint2(52, 30),
      new GridPoint2(53, 30), new GridPoint2(54, 30), new GridPoint2(55, 30),
      new GridPoint2(44, 16), new GridPoint2(44, 13), new GridPoint2(43, 12),
      new GridPoint2(44, 12), new GridPoint2(45, 12), new GridPoint2(48, 12),
      new GridPoint2(45, 11), new GridPoint2(46, 11), new GridPoint2(47, 11),
      new GridPoint2(48, 11), new GridPoint2(39, 5), new GridPoint2(39, 4),
      new GridPoint2(52, 4), new GridPoint2(39, 3), new GridPoint2(50, 3),
      new GridPoint2(51, 3), new GridPoint2(52, 3),
    };

    GridPoint2[] water6SpawnLocations = {
      new GridPoint2(40, 39), new GridPoint2(45, 16), new GridPoint2(49, 16),
      new GridPoint2(45, 15), new GridPoint2(49, 15), new GridPoint2(45, 14),
      new GridPoint2(49, 14), new GridPoint2(45, 13), new GridPoint2(49, 13),
      new GridPoint2(49, 12), new GridPoint2(39, 6), new GridPoint2(53, 5),
      new GridPoint2(40, 4), new GridPoint2(53, 4), new GridPoint2(40, 3),
      new GridPoint2(53, 3),
    };

    GridPoint2[] water7SpawnLocations = {
      new GridPoint2(38, 36), new GridPoint2(39, 35), new GridPoint2(40, 34),
      new GridPoint2(41, 32), new GridPoint2(43, 31), new GridPoint2(44, 30),
      new GridPoint2(46, 29), new GridPoint2(52, 19), new GridPoint2(41, 16),
      new GridPoint2(45, 10), new GridPoint2(38, 2), new GridPoint2(47, 2),
    };

    GridPoint2[] water8SpawnLocations = {
      new GridPoint2(36, 37), new GridPoint2(37, 37), new GridPoint2(42, 32),
      new GridPoint2(45, 30), new GridPoint2(47, 29), new GridPoint2(48, 29),
      new GridPoint2(51, 29), new GridPoint2(52, 29), new GridPoint2(53, 29),
      new GridPoint2(54, 29), new GridPoint2(55, 29), new GridPoint2(53, 19),
      new GridPoint2(54, 19), new GridPoint2(55, 19), new GridPoint2(42, 16),
      new GridPoint2(43, 16), new GridPoint2(43, 11), new GridPoint2(44, 11),
      new GridPoint2(46, 10), new GridPoint2(47, 10), new GridPoint2(39, 2),
      new GridPoint2(48, 2), new GridPoint2(49, 2), new GridPoint2(50, 2),
      new GridPoint2(51, 2), new GridPoint2(52, 2),
    };

    GridPoint2[] water9SpawnLocations = {
      new GridPoint2(49, 11), new GridPoint2(48, 10), new GridPoint2(40, 2),
      new GridPoint2(53, 2),
    };

    for (int i = 0; i < water1SpawnLocations.length; i++) {
      Entity water1 = ObstacleFactory.createWaterTile1();
      spawnEntityAt(water1, water1SpawnLocations[i], true, true);
    }
    for (int i = 0; i < water2SpawnLocations.length; i++) {
      Entity water2 = ObstacleFactory.createWaterTile2();
      spawnEntityAt(water2, water2SpawnLocations[i], true, true);
    }
    for (int i = 0; i < water3SpawnLocations.length; i++) {
      Entity water3 = ObstacleFactory.createWaterTile3();
      spawnEntityAt(water3, water3SpawnLocations[i], true, true);
    }
    for (int i = 0; i < water4SpawnLocations.length; i++) {
      Entity water4 = ObstacleFactory.createWaterTile4();
      spawnEntityAt(water4, water4SpawnLocations[i], true, true);
    }
    for (int i = 0; i < water5SpawnLocations.length; i++) {
      Entity water5 = ObstacleFactory.createWaterTile5();
      spawnEntityAt(water5, water5SpawnLocations[i], true, true);
    }
    for (int i = 0; i < water6SpawnLocations.length; i++) {
      Entity water6 = ObstacleFactory.createWaterTile6();
      spawnEntityAt(water6, water6SpawnLocations[i], true, true);
    }
    for (int i = 0; i < water7SpawnLocations.length; i++) {
      Entity water7 = ObstacleFactory.createWaterTile7();
      spawnEntityAt(water7, water7SpawnLocations[i], true, true);
    }
    for (int i = 0; i < water8SpawnLocations.length; i++) {
      Entity water8 = ObstacleFactory.createWaterTile8();
      spawnEntityAt(water8, water8SpawnLocations[i], true, true);
    }
    for (int i = 0; i < water9SpawnLocations.length; i++) {
      Entity water9 = ObstacleFactory.createWaterTile9();
      spawnEntityAt(water9, water9SpawnLocations[i], true, true);
    }
  }

  /**
   * Spawns all the water sand tiles variation (2,3,6) onto the map.
   */
  private void spawnWaterSandTiles() {
    GridPoint2[] water2SpawnLocations = {
      new GridPoint2(36,41), new GridPoint2(37,41), new GridPoint2(38,41),
      new GridPoint2(43,36), new GridPoint2(46,34), new GridPoint2(47,34),
      new GridPoint2(48,34), new GridPoint2(51,34), new GridPoint2(52,34),
      new GridPoint2(53,34), new GridPoint2(54,34), new GridPoint2(55,34),
    };

    GridPoint2[] water3SpawnLocations = {
      new GridPoint2(39, 41), new GridPoint2(40, 40), new GridPoint2(41, 38),
      new GridPoint2(42, 37), new GridPoint2(44, 36), new GridPoint2(45, 35),
    };

    GridPoint2[] water6SpawnLocations = {
      new GridPoint2(40, 39)
    };

    for (int i = 0; i < water2SpawnLocations.length; i++) {
      Entity water2 = ObstacleFactory.createWaterSandTile2();
      spawnEntityAt(water2, water2SpawnLocations[i], true, true);
    }
    for (int i = 0; i < water3SpawnLocations.length; i++) {
      Entity water3 = ObstacleFactory.createWaterSandTile3();
      spawnEntityAt(water3, water3SpawnLocations[i], true, true);
    }
    for (int i = 0; i < water6SpawnLocations.length; i++) {
      Entity water6 = ObstacleFactory.createWaterSandTile6();
      spawnEntityAt(water6, water6SpawnLocations[i], true, true);
    }
  }

  /**
   * Spawns the vertical bridge asset on the map.
   */
  private void spawnBridge() {
    GridPoint2[] spawnLocationsLeft = {
      new GridPoint2(49,28),
      new GridPoint2(49,29),
      new GridPoint2(49,30),
      new GridPoint2(49,31),
      new GridPoint2(49,32),
      new GridPoint2(49,33),
      new GridPoint2(49,34),
      new GridPoint2(49,35),
    };

    GridPoint2[] spawnLocationsRight= {
      new GridPoint2(50,28),
      new GridPoint2(50,29),
      new GridPoint2(50,30),
      new GridPoint2(50,31),
      new GridPoint2(50,32),
      new GridPoint2(50,33),
      new GridPoint2(50,34),
      new GridPoint2(50,35),
    };

    for (int i = 0; i < spawnLocationsLeft.length; i++) {
      Entity bridgeLeft = ObstacleFactory.createBridgeVerticalLeftTile();
      spawnEntityAt(bridgeLeft, spawnLocationsLeft[i], true, true);
    }

    for (int i = 0; i < spawnLocationsRight.length; i++) {
      Entity bridgeRight = ObstacleFactory.createBridgeVerticalRightTile();
      spawnEntityAt(bridgeRight, spawnLocationsRight[i], true, true);
    }
  }

  /**
   * Spawns the fire fly bug NPC.
   */
  private void spawnFireFlyBugNPC() {
    GridPoint2[] spawnLocations = {
        new GridPoint2(22,29),
        new GridPoint2(41,23),
        new GridPoint2(45,9),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity fireFlyBugNPC = NPCFactory.createFireFlyBugNPC(1f,1f,10f,10f,0.5f);
      spawnEntityAt(fireFlyBugNPC, spawnLocations[i], true, false);
    }
  }

  /**
   * Plays the music for Level 3.
   */
  private void playMusic() {
    Music gameOverSong = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
    gameOverSong.setLooping(true);
    gameOverSong.setVolume(0.3f);
    gameOverSong.play();
  }

  /**
   * Load the assets needed to render Level 3.
   */
  private void loadAssets() {
    logger.debug("Loading assets");
    loadSharedAssets();
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadSounds(playerSounds);
    resourceService.loadSounds(enemySounds);
    resourceService.loadMusic(LEVEL3_MUSIC);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  /**
   * Unload the assets when Level 3 GameArea is no longer active.
   */
  private void unloadAssets() {
    logger.debug("Unloading assets");
    unloadSharedAssets();
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(playerSounds);
    resourceService.unloadAssets(enemySounds);
    resourceService.unloadAssets(LEVEL3_MUSIC);
  }

  /**
   * Disposes the assets and music.
   */
  @Override
  public void dispose() {

    super.dispose();
    ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
    this.unloadAssets();
  }
}