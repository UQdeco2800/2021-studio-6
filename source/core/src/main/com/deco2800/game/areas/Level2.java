package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.player.PlayerRangeAttackComponent;
import com.deco2800.game.components.player.PlayerRangeAOEComponent;
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
public class Level2 extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(Level2.class);
  private static final int NUM_BULLETS = 5;
  private static final int NUM_SPAWNER_ENEMY = 2;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(2, 23);
  private static final float WALL_WIDTH = 0.1f;
  private static final String NPC_DEAD_ATLAS_FILENAME = "images/npc_movement/dead_npc.atlas";

  /**
   * Images and assets file path for Level 2 map generation.
   */
  private static final String[] forestTextures = {
    "images/obstacle_sprite/cobweb.png",
    "images/obstacle_sprite/bush.png",
    "images/level_2/level2_grass_1.png",
    "images/level_2/level2_grass_2.png",
    "images/level_2/level2_grass_3.png",
    "images/level_2/level2_grass_4.png",
    "images/level_2/level2_grass_5.png",
    "images/level_2/level2_grass_6.png",
    "images/level_2/level2_grass_7.png",
    "images/level_2/level2_grass_8.png",
    "images/level_2/level2_background_tile.png",
    "images/level_2/level2_tree_1-1.png",
    "images/level_2/level2_tree_2-1.png",
    "images/safehouse/exterior-day1-latest.png",
    "images/level_2/level2_torch_frame1_ver1.png",
    "images/level_2/level2_tree_2_group_ver1.png",
    "images/dialogue/raw/npc_indicator.png",
    "images/level_2/fire-fly-bug-NPC.png",
    "images/playeritems/bandage/bandage01.png"
  };

  /**
   * Texture atlases file path for Level 2.
   */
  private static final String[] forestTextureAtlases = {
    NPC_DEAD_ATLAS_FILENAME
  };

  // Music and sound variables
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};

  private static final String[] playerSounds = {
          "sounds/bandage-use.ogg",
          "sounds/hurt.ogg",
          "sounds/item-pickup.ogg"
  };

  private static final String backgroundMusic = "sounds/fireflies-theme-woods2.mp3";
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

    // Spawn terrain/assets entities
    spawnTerrain();
    spawnTerrainPineTrees();
    spawnTerrainBigTrees();
    spawnSafehouse();
    spawnCobweb();
    spawnBush();
    spawnTorch();
    spawnFireFlyBugNPC();

    // Spawn player related entities
    player = spawnPlayer();
    spawnBullet();
    spawnFireCracker();
    spawnPickupItems();
    spawnLevelTwoIntro();

    spawnDeadNPC();

    // Spawn enemy entities
    //spawnSmallEnemy();
    //spawnLargeEnemy();
    //spawnLongRangeEnemies();
    //spawnSpawnerEnemy();
    //spawnToughLongRangeEnemies();

    // Listener for level 2 intro to finish and then play music
    StoryManager.getInstance().getEntity().getEvents().addListener("story-finished:" + StoryNames.LEVEL2_INTRO,
        this::playMusic);
  }

  /**
   * Displays the name of the level on the top left screen.
   */
  private void displayUI() {
    Entity ui = new Entity();
    // Can change level name here
    ui.addComponent(new GameAreaDisplay("Level 2"));
    spawnEntity(ui);
  }

  /**
   * Spawns the Level 2 forrest tile layout and hard terrain boundaries to stop the player
   * and enemies from going outside the map.
   */
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

  /**
   * Spawns the group pine trees that act as a visual terrain boundary.
   */
  private void spawnTerrainPineTrees() {
    // This looks pretty ugly right now I know, this was generated with excel
    GridPoint2[] spawnLocations = {
      new GridPoint2(0, 36), new GridPoint2(1, 36), new GridPoint2(2, 36),
      new GridPoint2(3, 36), new GridPoint2(4, 36), new GridPoint2(5, 36),
      new GridPoint2(6, 36), new GridPoint2(7, 36), new GridPoint2(8, 36),
      new GridPoint2(9, 36), new GridPoint2(10, 36), new GridPoint2(11, 36),
      new GridPoint2(12, 36), new GridPoint2(13, 36), new GridPoint2(14, 36),
      new GridPoint2(15, 36), new GridPoint2(16, 36), new GridPoint2(17, 36),
      new GridPoint2(18, 36), new GridPoint2(19, 36), new GridPoint2(20, 36),
      new GridPoint2(21, 36), new GridPoint2(22, 36), new GridPoint2(23, 36),
      new GridPoint2(24, 36), new GridPoint2(25, 36), new GridPoint2(26, 36),
      new GridPoint2(27, 36), new GridPoint2(28, 36), new GridPoint2(29, 36),
      new GridPoint2(30, 36), new GridPoint2(31, 36), new GridPoint2(32, 36),
      new GridPoint2(33, 36), new GridPoint2(34, 36), new GridPoint2(35, 36),
      new GridPoint2(36, 36), new GridPoint2(37, 36), new GridPoint2(38, 36),
      new GridPoint2(39, 36), new GridPoint2(40, 36), new GridPoint2(41, 36),
      new GridPoint2(42, 36), new GridPoint2(43, 36), new GridPoint2(44, 36),
      new GridPoint2(45, 36), new GridPoint2(46, 36), new GridPoint2(47, 36),
      new GridPoint2(48, 36), new GridPoint2(49, 36), new GridPoint2(50, 36),
      new GridPoint2(51, 36), new GridPoint2(52, 36), new GridPoint2(53, 36),
      new GridPoint2(54, 36), new GridPoint2(55, 36), new GridPoint2(56, 36),
      new GridPoint2(57, 36), new GridPoint2(58, 36), new GridPoint2(59, 36),
      new GridPoint2(60, 36), new GridPoint2(61, 36), new GridPoint2(62, 36),
      new GridPoint2(0, 35), new GridPoint2(62, 35), new GridPoint2(0, 34),
      new GridPoint2(62, 34), new GridPoint2(0, 33), new GridPoint2(62, 33),
      new GridPoint2(0, 32), new GridPoint2(62, 32), new GridPoint2(0, 31),
      new GridPoint2(62, 31), new GridPoint2(0, 30), new GridPoint2(62, 30),
      new GridPoint2(0, 29), new GridPoint2(62, 29), new GridPoint2(0, 28),
      new GridPoint2(62, 28), new GridPoint2(0, 27), new GridPoint2(1, 27),
      new GridPoint2(2, 27), new GridPoint2(3, 27), new GridPoint2(4, 27),
      new GridPoint2(5, 27), new GridPoint2(6, 27), new GridPoint2(7, 27),
      new GridPoint2(8, 27), new GridPoint2(9, 27), new GridPoint2(54, 27),
      new GridPoint2(55, 27), new GridPoint2(56, 27), new GridPoint2(57, 27),
      new GridPoint2(58, 27), new GridPoint2(59, 27), new GridPoint2(60, 27),
      new GridPoint2(61, 27), new GridPoint2(62, 27), new GridPoint2(0, 26),
      new GridPoint2(62, 26), new GridPoint2(0, 25), new GridPoint2(62, 25),
      new GridPoint2(0, 20), new GridPoint2(62, 20), new GridPoint2(0, 19),
      new GridPoint2(62, 19), new GridPoint2(0, 18), new GridPoint2(9, 18),
      new GridPoint2(54, 18), new GridPoint2(62, 18), new GridPoint2(0, 17),
      new GridPoint2(9, 17), new GridPoint2(54, 17), new GridPoint2(62, 17),
      new GridPoint2(0, 16), new GridPoint2(9, 16), new GridPoint2(54, 16),
      new GridPoint2(62, 16), new GridPoint2(0, 15), new GridPoint2(9, 15),
      new GridPoint2(54, 15), new GridPoint2(62, 15), new GridPoint2(0, 14),
      new GridPoint2(9, 14), new GridPoint2(54, 14), new GridPoint2(62, 14),
      new GridPoint2(0, 13), new GridPoint2(9, 13), new GridPoint2(54, 13),
      new GridPoint2(62, 13), new GridPoint2(0, 12), new GridPoint2(9, 12),
      new GridPoint2(54, 12), new GridPoint2(62, 12), new GridPoint2(0, 11),
      new GridPoint2(9, 11), new GridPoint2(54, 11), new GridPoint2(62, 11),
      new GridPoint2(0, 10), new GridPoint2(9, 10), new GridPoint2(54, 10),
      new GridPoint2(62, 10), new GridPoint2(0, 9), new GridPoint2(1, 9),
      new GridPoint2(2, 9), new GridPoint2(3, 9), new GridPoint2(4, 9),
      new GridPoint2(5, 9), new GridPoint2(6, 9), new GridPoint2(7, 9),
      new GridPoint2(8, 9), new GridPoint2(9, 9), new GridPoint2(10, 9),
      new GridPoint2(11, 9), new GridPoint2(12, 9), new GridPoint2(13, 9),
      new GridPoint2(14, 9), new GridPoint2(15, 9), new GridPoint2(16, 9),
      new GridPoint2(17, 9), new GridPoint2(18, 9), new GridPoint2(19, 9),
      new GridPoint2(20, 9), new GridPoint2(21, 9), new GridPoint2(22, 9),
      new GridPoint2(23, 9), new GridPoint2(24, 9), new GridPoint2(25, 9),
      new GridPoint2(26, 9), new GridPoint2(27, 9), new GridPoint2(28, 9),
      new GridPoint2(29, 9), new GridPoint2(30, 9), new GridPoint2(31, 9),
      new GridPoint2(32, 9), new GridPoint2(33, 9), new GridPoint2(34, 9),
      new GridPoint2(35, 9), new GridPoint2(36, 9), new GridPoint2(45, 9),
      new GridPoint2(46, 9), new GridPoint2(47, 9), new GridPoint2(48, 9),
      new GridPoint2(49, 9), new GridPoint2(50, 9), new GridPoint2(51, 9),
      new GridPoint2(52, 9), new GridPoint2(53, 9), new GridPoint2(54, 9),
      new GridPoint2(55, 9), new GridPoint2(56, 9), new GridPoint2(57, 9),
      new GridPoint2(58, 9), new GridPoint2(59, 9), new GridPoint2(60, 9),
      new GridPoint2(61, 9), new GridPoint2(62, 9), new GridPoint2(18, 8),
      new GridPoint2(45, 8), new GridPoint2(18, 7), new GridPoint2(45, 7),
      new GridPoint2(18, 6), new GridPoint2(45, 6), new GridPoint2(18, 5),
      new GridPoint2(45, 5), new GridPoint2(18, 4), new GridPoint2(45, 4),
      new GridPoint2(18, 3), new GridPoint2(45, 3), new GridPoint2(18, 2),
      new GridPoint2(45, 2), new GridPoint2(18, 1), new GridPoint2(45, 1),
      new GridPoint2(18, 0), new GridPoint2(19, 0), new GridPoint2(20, 0),
      new GridPoint2(21, 0), new GridPoint2(22, 0), new GridPoint2(23, 0),
      new GridPoint2(24, 0), new GridPoint2(25, 0), new GridPoint2(26, 0),
      new GridPoint2(27, 0), new GridPoint2(28, 0), new GridPoint2(29, 0),
      new GridPoint2(30, 0), new GridPoint2(31, 0), new GridPoint2(32, 0),
      new GridPoint2(33, 0), new GridPoint2(34, 0), new GridPoint2(35, 0),
      new GridPoint2(36, 0), new GridPoint2(37, 0), new GridPoint2(38, 0),
      new GridPoint2(39, 0), new GridPoint2(40, 0), new GridPoint2(41, 0),
      new GridPoint2(42, 0), new GridPoint2(43, 0), new GridPoint2(44, 0),
      new GridPoint2(45, 0),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity pineTriTree = ObstacleFactory.createTriGreenPineTree();
      spawnEntityAt(pineTriTree, spawnLocations[i], false, false);
    }
  }

  /**
   * Spawns the big trees that act as a visual terrain boundary.
   */
  private void spawnTerrainBigTrees() {
    // This looks pretty ugly right now I know, this was generated with excel
    GridPoint2[] spawnLocations = {
      new GridPoint2(27, 35), new GridPoint2(27, 34), new GridPoint2(27, 33),
      new GridPoint2(27, 32), new GridPoint2(27, 31), new GridPoint2(27, 30),
      new GridPoint2(27, 29), new GridPoint2(27, 28), new GridPoint2(19, 27),
      new GridPoint2(21, 27), new GridPoint2(23, 27), new GridPoint2(25, 27),
      new GridPoint2(27, 27), new GridPoint2(37, 27), new GridPoint2(39, 27),
      new GridPoint2(41, 27), new GridPoint2(43, 27), new GridPoint2(45, 27),
      new GridPoint2(19, 26), new GridPoint2(27, 26), new GridPoint2(45, 26),
      new GridPoint2(19, 25), new GridPoint2(27, 25), new GridPoint2(45, 25),
      new GridPoint2(19, 24), new GridPoint2(27, 24), new GridPoint2(45, 24),
      new GridPoint2(19, 23), new GridPoint2(27, 23), new GridPoint2(45, 23),
      new GridPoint2(19, 22), new GridPoint2(27, 22), new GridPoint2(45, 22),
      new GridPoint2(19, 21), new GridPoint2(27, 21), new GridPoint2(45, 21),
      new GridPoint2(19, 20), new GridPoint2(27, 20), new GridPoint2(45, 20),
      new GridPoint2(19, 19), new GridPoint2(27, 19), new GridPoint2(45, 19),
      new GridPoint2(19, 18), new GridPoint2(27, 18), new GridPoint2(29, 18),
      new GridPoint2(31, 18), new GridPoint2(33, 18), new GridPoint2(35, 18),
      new GridPoint2(45, 18), new GridPoint2(45, 17), new GridPoint2(45, 16),
      new GridPoint2(45, 15), new GridPoint2(45, 14), new GridPoint2(45, 13),
      new GridPoint2(45, 12), new GridPoint2(45, 11), new GridPoint2(45, 10),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity bigTree = ObstacleFactory.createBigTree();
      spawnEntityAt(bigTree, spawnLocations[i], false, false);
    }
  }

  /**
   * Spawns the safehouse.
   */
  public void spawnSafehouse() {
    GridPoint2 center = new GridPoint2(61, 22);

    Entity safehouse = SafehouseFactory.createSafehouse();
    // Position is currently procedurally (kidding, just randomly) generated.
    spawnEntityAt(safehouse, center, false, false);
  }

  /**
   * Spawns the player entity onto the map..
   * @return Player entity.
   */
  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  /**
   * Spawns the bullets projectile when player shoots them out using the ENTER key.
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
   * Spawns the spawner enemy onto the map.
   */
  private void spawnSpawnerEnemy() {
    GridPoint2[] spawnLocations = {
            new GridPoint2(8, 13),
            new GridPoint2(26,2),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity spawnerEnemy = NPCFactory.createSpawnerEnemy();
      spawnerEnemy.getComponent(AITaskComponent.class).addTask(new SpawnerEnemyTask(player, 10, 5f, 6f, this,
              spawnerEnemy));
      spawnEntityAt(spawnerEnemy, spawnLocations[i], true, true);
    }
  }

  /**
   * Spawns a small enemy from the appropriate spawner's position.
   */
  public void spawnFromSpawner(Vector2 position, int maxSpawnDistance) {
    super.spawnFromSpawner(position, maxSpawnDistance);
  }

  /**
   * Spawns the small enemies onto the map.
   */
  private void spawnSmallEnemy() {//this da noo 1
    GridPoint2[] spawnLocations = {
      new GridPoint2(35, 33),
      new GridPoint2(7, 31),
      new GridPoint2(53, 31),
      new GridPoint2(31, 29),
      new GridPoint2(51, 25),
      new GridPoint2(23, 23),
      new GridPoint2(48, 20),
      new GridPoint2(41, 16),
      new GridPoint2(22, 13),
      new GridPoint2(39, 9),
      new GridPoint2(33, 2),
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
      new GridPoint2(31, 23),
      new GridPoint2(54, 23),
      new GridPoint2(13, 22),
      new GridPoint2(31, 14),
      new GridPoint2(24, 7),
      new GridPoint2(41, 3),
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
      new GridPoint2(3, 33),
      new GridPoint2(58, 32),
      new GridPoint2(41, 24),
      new GridPoint2(14, 14),
      new GridPoint2(57, 12),
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
      new GridPoint2(45, 32),
      new GridPoint2(22, 22),
      new GridPoint2(49, 15),
      new GridPoint2(29, 5),
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
      new GridPoint2(10, 32),
      new GridPoint2(19, 32),
      new GridPoint2(18, 31),
      new GridPoint2(22, 30),
      new GridPoint2(38, 30),
      new GridPoint2(54, 30),
      new GridPoint2(32, 25),
      new GridPoint2(15, 23),
      new GridPoint2(50, 19),
      new GridPoint2(3, 18),
      new GridPoint2(19, 17),
      new GridPoint2(36, 14),
      new GridPoint2(34, 7),
      new GridPoint2(25, 5),
      new GridPoint2(38, 4),
      new GridPoint2(28, 2),
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
      new GridPoint2(8, 34),
      new GridPoint2(38, 34),
      new GridPoint2(20, 30),
      new GridPoint2(52, 29),
      new GridPoint2(5, 25),
      new GridPoint2(34, 25),
      new GridPoint2(25, 20),
      new GridPoint2(60, 19),
      new GridPoint2(12, 15),
      new GridPoint2(42, 15),
      new GridPoint2(6, 12),
      new GridPoint2(38, 7),
      new GridPoint2(21, 4),
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
      new GridPoint2(30, 34),
      new GridPoint2(26, 30),
      new GridPoint2(25, 26),
      new GridPoint2(35, 11),
      new GridPoint2(57, 11),
    };

    GridPoint2[] bandageSpawnLocations = {
      new GridPoint2(60, 33),
      new GridPoint2(21, 8),
      new GridPoint2(26, 26),
    };

    GridPoint2[] coinSpawnLocations = {
      new GridPoint2(26, 35),
      new GridPoint2(46, 34),
      new GridPoint2(13, 32),
      new GridPoint2(2, 29),
      new GridPoint2(61, 29),
      new GridPoint2(22, 26),
      new GridPoint2(43, 25),
      new GridPoint2(61, 25),
      new GridPoint2(30, 21),
      new GridPoint2(2, 11),
      new GridPoint2(27, 11),
      new GridPoint2(48, 11),
      new GridPoint2(52, 11),
      new GridPoint2(60, 11),
      new GridPoint2(20, 8),
      new GridPoint2(20, 7),
      new GridPoint2(20, 6),
      new GridPoint2(20, 5),
      new GridPoint2(20, 4),
      new GridPoint2(20, 3),
      new GridPoint2(37, 2),
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

  private void spawnLevelTwoIntro() {
    StoryManager.getInstance().loadCutScene(StoryNames.LEVEL2_INTRO);
    StoryManager.getInstance().displayStory();
  }

  private void spawnDeadNPC() {
    GridPoint2 pos = new GridPoint2(6,18);
    Entity npcTut = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.NPC_DEAD, NPC_DEAD_ATLAS_FILENAME, false);
    spawnEntityAt(npcTut, pos, true, true);
  }

  /**
   * Spawns the torch entity that can emit out light onto the map.
   */
  private void spawnTorch() {
    GridPoint2[] spawnLocations = {
        new GridPoint2(48, 28),
        new GridPoint2(36, 27),
        new GridPoint2(54, 26),
        new GridPoint2(59, 22),
        new GridPoint2(11, 19),
        new GridPoint2(38, 19),
        new GridPoint2(20, 17),
        new GridPoint2(28, 17),
    };
    /*new GridPoint2(54, 25),
        new GridPoint2(9, 20),
        new GridPoint2(54, 20),
        new GridPoint2(12, 18),
        new GridPoint2(17, 18),
        new GridPoint2(38, 18),
        new GridPoint2(43, 18),
        new GridPoint2(27, 16),
        new GridPoint2(27, 12)*/

    for (GridPoint2 position : spawnLocations) {
      Entity torch = ObstacleFactory.createTorch();
      spawnEntityAt(torch, position, false, false);
    }
  }

  /**
   * Spawns the fire fly bug NPC.
   */
  private void spawnFireFlyBugNPC() {
    GridPoint2[] spawnLocations = {
            new GridPoint2(26,21),
            new GridPoint2(27,7),
            new GridPoint2(49,31),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity fireFlyBugNPC = NPCFactory.createFireFlyBugNPC(1f,1f,10f,10f,0.5f);
      spawnEntityAt(fireFlyBugNPC, spawnLocations[i], true, false);
    }
  }

  /**
   * Plays the music for Level 2.
   */
  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setVolume(ServiceLocator.getResourceService().getMusicVolume());
    music.setLooping(true);
    music.play();
  }

  /**
   * Load the assets needed to render Level 2.
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
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  /**
   * Unload the assets when Level 2 GameArea is no longer active.
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
    resourceService.unloadAssets(forestMusic);
  }

  /**
   * Disposes the assets and music.
   */
  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}