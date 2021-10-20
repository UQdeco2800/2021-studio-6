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
import com.deco2800.game.components.tasks.SpawnerEnemyTask;
import com.deco2800.game.components.story.StoryManager;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Game area for the first level: Edge of a cityscape */
public class Level1 extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(Level1.class);
  private static final int NUM_BULLETS = 5;
  private static final float WALL_WIDTH = 0.1f;
  private static final String NPC_SAMPLE_ATLAS_FILENAME = "images/npc_movement/npc_movement.atlas";
  private static final String NPC_TUT_1_ATLAS_FILENAME = "images/npc_movement/tut_npc1.atlas";
  private static final String NPC_INJURED_ATLAS_FILENAME = "images/npc_movement/injured_npc.atlas";
  private static final String NPC_PILOT_ATLAS_FILENAME = "images/npc_movement/pilot_npc.atlas";

  // All textures that make up the city map
  private static final String[] cityTextures = {
    "images/obstacle_sprite/cobweb.png",
    "images/obstacle_sprite/bush.png",
    "images/level_1/road_tile_black.png",
    "images/level_1/sidewalk.png",
    "images/level_1/cracked_sidewalk.png",
    "images/level_1/curbUpper.png",
    "images/level_1/pathHorizontal.png",
    "images/level_1/pathHorizontalOld.png",
    "images/level_1/pathHorizontalDecay.png",
    "images/level_1/pathVertical.png",
    "images/level_1/pathVerticalOld.png",
    "images/level_1/pathVerticalDecay.png",
    "images/level_1/curbLower.png",
    "images/level_1/road_tile_cracked.png",
    "images/level_1/placeholder_road.png",
    "images/level_1/rubble.png",
    "images/level_1/placeholder_curb.png",
    "images/level_1/road_tile_white.png",
    "images/level_1/road_barrier.png",
    "images/level_1/horizontal_road_barrier.png",
    "images/level_1/building2-day1-latest.png",
    "images/level_1/building3-day1-latest.png",
    "images/safehouse/exterior-day1-latest.png",
    "images/level_1/dead_tree1-day1-latest.png",
    "images/level_1/street_lamp.png",
    "images/level_1/street_lamped_vined.png",
    "images/level_1/leaving_city_sign.png",
    "images/level_1/forest_sign.png",
    "images/dialogue/raw/npc_indicator.png",
    "images/grass_1.png",
    "images/firefly/firefly.atlas",
    "images/level_2/level2_grass_1.png", // adding grass
    "images/level_2/level2_grass_2.png",
    "images/level_2/level2_grass_4.png",
    "images/level_2/dirtSmall.png",
    "images/level_2/treeBlue.png"
  };
  private static final String[] cityTextureAtlases = {
      "images/level_2/tree.atlas",
      "images/level_1/lamp.atlas",
      NPC_SAMPLE_ATLAS_FILENAME,
      NPC_TUT_1_ATLAS_FILENAME,
      NPC_INJURED_ATLAS_FILENAME,
      NPC_PILOT_ATLAS_FILENAME
  };

  private static final String[] citySounds = {};

  private static final String BACKGROUND_MUSIC = "sounds/fireflies-theme-sneak.mp3";
  private static final String[] cityMusic = {BACKGROUND_MUSIC};

  private final TerrainFactory terrainFactory;

  public Level1(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    // Loading in and setting up UI
    loadAssets();
    displayUI();

    // Spawning Obstacles/Environment objects
    spawnTerrain();
    spawnBarriers();
    spawnBuildings();
    spawnSafehouse();
    spawnDeadTrees();
    spawnNiceTrees();
    spawnLamps();
    spawnSigns();
    spawnPickupItems();

    // Spawn in player and their projectiles
    player = spawnPlayer();
    spawnBullet();
    spawnFireCracker();

    // Spawn in enemies
    spawnLargeEnemy();
    spawnSmallEnemy();
    spawnSpawnerEnemy();
    spawnLongRangeEnemies();
    spawnToughLongRangeEnemies();

    // Spawn in NPCs
    spawnPilotNpc();
    spawnInjuredNPC();

    // Start the prologue
    spawnPrologue();

    //Listener for prologue finished to play music
    StoryManager.getInstance().getEntity().getEvents().addListener("story-finished:" + StoryNames.PROLOGUE,
        this::startTutorialAndMusic);
  }

  private void displayUI() {
    Entity ui = new Entity();
    // Can change level name here
    ui.addComponent(new GameAreaDisplay("Level 1"));
    spawnEntity(ui);
  }

  /**
  * Spawn in the background/map of the game area
   */
  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.CITY);
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
   * Add pickup-able items to the level
   */
  private void spawnPickupItems() {
    // Spawning in Bandage
    spawnEntityAt(ItemFactory.createBandagePickup(1), new GridPoint2(12, 48), true, true);
    spawnEntityAt(ItemFactory.createBandagePickup(1), new GridPoint2(13, 8), true, true);

    // Setting ammo positions
    GridPoint2[] ammoPositions = new GridPoint2[]{
            new GridPoint2(28, 56),
            new GridPoint2(29, 43),  new GridPoint2(17, 39), new GridPoint2(54, 44),
            new GridPoint2(54, 24),
            new GridPoint2(49, 15),
            new GridPoint2(31, 10), new GridPoint2(19, 12)
    };
    for (GridPoint2 point: ammoPositions) {
      int randomAmmoQuantity = RandomUtils.randomInt(5); // random amount of ammo
      Entity pickupAmmo = ItemFactory.createAmmoPickup(randomAmmoQuantity);
      spawnEntityAt(pickupAmmo, point, true, false);
    }

    // Setting coin positions
    GridPoint2[] coinPositions = new GridPoint2[]{
            new GridPoint2(52, 59), new GridPoint2(34, 58), new GridPoint2(34, 54),
            new GridPoint2(46, 46), new GridPoint2(39, 44), new GridPoint2(37, 44), new GridPoint2(29, 44), new GridPoint2(12, 39),
            new GridPoint2(56, 37), new GridPoint2(22, 36), new GridPoint2(35, 33),
            new GridPoint2(44, 24), new GridPoint2(30, 24), new GridPoint2(13, 22),
            new GridPoint2(34, 17), new GridPoint2(56, 15), new GridPoint2(11, 13),
            new GridPoint2(46, 8), new GridPoint2(57, 7), new GridPoint2(23, 7)
    };
    // Spawn in coins
    for (GridPoint2 point: coinPositions) {
      int randomCoinQuantity = RandomUtils.randomInt(5); // Random amount of coins
      Entity pickupCoin = ItemFactory.createCoinPickup(randomCoinQuantity);
      spawnEntityAt(pickupCoin, point, true, false);
    }
    /*
    // CREATED 3 ARMOURS FOR TESTING
    for (int i = 0; i < 3; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int armourQuantity = 1;
      Entity armour = ItemFactory.createArmour(armourQuantity);
      spawnEntityAt(armour, randomPos, true, false);
    }

    // CREATED 3 HELMETS FOR TESTING
    for (int i = 0; i < 3; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int helmetQuantity = 1;
      Entity helmet = ItemFactory.createHelmet(helmetQuantity);
      spawnEntityAt(helmet, randomPos, true, false);
    }

    // CREATED 3 DAGGERS FOR TESTING
    for (int i = 0; i < 3; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int daggerQuantity = 1;
      Entity dagger = ItemFactory.createDagger(daggerQuantity);
      spawnEntityAt(dagger, randomPos, true, false);
    }

    // CREATED 3 AXES FOR TESTING
    for (int i = 0; i < 3; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int axeQuantity = 1;
      Entity axe = ItemFactory.createAxe(axeQuantity);
      spawnEntityAt(axe, randomPos, true, false);
    }

    // CREATED 3 SWORDS FOR TESTING
    for (int i = 0; i < 3; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int swordQuantity = 1;
      Entity sword = ItemFactory.createSword(swordQuantity);
      spawnEntityAt(sword, randomPos, true, false);
    }

    // CREATED 3 AXES FOR TESTING
    for (int i = 0; i < 3; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int sledgeQuantity = 1;
      Entity axe = ItemFactory.createSledge(sledgeQuantity);
      spawnEntityAt(axe, randomPos, true, false);
    }

    // CREATED 3 SWORDS FOR TESTING
    for (int i = 0; i < 3; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int macheteQuantity = 1;
      Entity sword = ItemFactory.createMachete(macheteQuantity);
      spawnEntityAt(sword, randomPos, true, false);
    }

    // CREATED 3 SWORDS FOR TESTING
    for (int i = 0; i < 3; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int macheteQuantity = 1;
      Entity sword1 = ItemFactory.createBat(macheteQuantity);
      spawnEntityAt(sword1, randomPos, true, false);
    }
  }

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 6);

    for (int i = 0; i < NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createBigTree();
      spawnEntityAt(tree, randomPos, true, false);
    }
    */
  }

  /**
   * Used to add buildings to the game area
   */
  private void spawnBuildings() {
    // Set positions of houses
   GridPoint2[] houseOnePositions = new GridPoint2[]{
            new GridPoint2(8, 61), new GridPoint2(18, 61), new GridPoint2(28, 61), new GridPoint2(38, 61),
            new GridPoint2(3, 54),
            new GridPoint2(8, 47), new GridPoint2(21, 46), new GridPoint2(33, 46), new GridPoint2(43, 46),
            new GridPoint2(3, 40),
            new GridPoint2(3, 29), new GridPoint2(13, 29), new GridPoint2(23, 29),
            new GridPoint2(8, 14), new GridPoint2(18, 14), new GridPoint2(28, 14),
            new GridPoint2(3, 7),
            new GridPoint2(8, 0), new GridPoint2(43, 0), new GridPoint2(53, 0)
    };
    GridPoint2[] houseTwoPositions = new GridPoint2[]{
            new GridPoint2(3, 61), new GridPoint2(13, 61), new GridPoint2(23, 61), new GridPoint2(33, 61),
            new GridPoint2(8, 54),
            new GridPoint2(3, 47), new GridPoint2(16, 46), new GridPoint2(26, 46), new GridPoint2(38, 46),
            new GridPoint2(33, 39), new GridPoint2(43, 39),
            new GridPoint2(8, 29), new GridPoint2(18, 29),
            new GridPoint2(3, 14), new GridPoint2(13, 14), new GridPoint2(23, 14),
            new GridPoint2(8, 7),
            new GridPoint2(3, 0), new GridPoint2(13, 0), new GridPoint2(48, 0)
    };

    // Spawn in type one houses
    for (GridPoint2 point: houseOnePositions) {
        Entity house = ObstacleFactory.createBuilding(1);
        spawnEntityAt(house, point, true, false);
    }
    // Spawn in type two houses
    for (GridPoint2 point: houseTwoPositions) {
      Entity house = ObstacleFactory.createBuilding(2);
      spawnEntityAt(house, point, true, false);
    }
  }

  /**
   * Used to add signs to the game area
   */
  private void spawnSigns() {
    // Spawn in the leaving sign
    GridPoint2 position  = new GridPoint2(50, 30);
    Entity sign = ObstacleFactory.createObject("images/level_1/leaving_city_sign.png", 4f);
    spawnEntityAt(sign, position, true, false);

    // Spawn in the forest sign
    position  = new GridPoint2(59, 55);
    sign = ObstacleFactory.createObject("images/level_1/forest_sign.png", 3f);
    spawnEntityAt(sign, position, true, false);
  }

  /**
   * Used to add barriers to the game area
   */
  private void spawnBarriers() {
    // Vertical Barriers
    for (int y = 36; y <= 46; y += 2) { // top barrier on left
      GridPoint2 position = new GridPoint2(10, y);
      Entity rubble = ObstacleFactory.createVerticalRubble();
      spawnEntityAt(rubble, position, true, false);
    }

    for (int y = 53; y <= 60; y += 2) { // top barrier on right
      GridPoint2 position = new GridPoint2(32, y);
      Entity rubble = ObstacleFactory.createVerticalRubble();
      spawnEntityAt(rubble, position, true, false);
    }
    for (int y = 21; y <= 28; y += 2) { // bottom barrier on left
      GridPoint2 position = new GridPoint2(12, y);
      Entity rubble = ObstacleFactory.createVerticalRubble();
      spawnEntityAt(rubble, position, true, false);
    }
    for (int y = 6; y <= 62; y += 2) { // Main right wall
      GridPoint2 position = new GridPoint2(61, y);
      Entity barrier = ObstacleFactory.createVerticalBarrier();
      spawnEntityAt(barrier, position, true, false);
    }

    // Horizontal Barriers
    for (int x = 41; x <= 62; x += 2) { // Top Barrier
      GridPoint2 position = new GridPoint2(x, 62);
      Entity barrier = ObstacleFactory.createHorizontalBarrier();
      spawnEntityAt(barrier, position, true, false);
    }
    for (int x = 11; x <= 13; x += 2) { // Small Barrier
      GridPoint2 position = new GridPoint2(x, 50);
      Entity barrier = ObstacleFactory.createHorizontalBarrier();
      spawnEntityAt(barrier, position, true, false);
    }
    for (int x = 16; x <= 40; x += 2) { // Left bottom Barrier
      GridPoint2 position = new GridPoint2(x, 6);
      Entity barrier = ObstacleFactory.createHorizontalBarrier();
      spawnEntityAt(barrier, position, true, false);
    }
    for (int x = 56; x <= 61; x += 2) { // Right  bottom Barrier
      GridPoint2 position = new GridPoint2(x, 6);
      Entity barrier = ObstacleFactory.createHorizontalBarrier();
      spawnEntityAt(barrier, position, true, false);
    }
  }

  /**
   * Used to add dead tree entities to the game area
   */
  private void spawnDeadTrees() {
    GridPoint2[] treePositions = new GridPoint2[]{
            new GridPoint2(72, 40), new GridPoint2(12, 36),
            new GridPoint2(70, 23), new GridPoint2(67, 19),
            new GridPoint2(59, 18), new GridPoint2(43, 18),
            new GridPoint2(72, 8), new GridPoint2(60, 7),
            new GridPoint2(21, 7), new GridPoint2(66, 5),
            new GridPoint2(21, 42), new GridPoint2(53, 13),
            new GridPoint2(18, 2)
    };
    // Spawn the trees at the given positions
    for (GridPoint2 point: treePositions) {
      Entity tree = ObstacleFactory.createDeadTree();
      spawnEntityAt(tree, point, true, false);
    }
  }

  /**
   * Used to add living tree entities to the game area
   */
  private void spawnNiceTrees() {
    Entity tree;

    GridPoint2[] treePositions = new GridPoint2[]{
        new GridPoint2(49, 56), new GridPoint2(64, 56),
        new GridPoint2(42, 65), new GridPoint2(70, 65),
        new GridPoint2(64, 42), new GridPoint2(44, 9),
        new GridPoint2(62, 2), new GridPoint2(47, 43)
    };

    for (GridPoint2 point : treePositions) {
      if (point.y % 2 == 0) {
        tree = ObstacleFactory.createAnimatedTree();
      } else {
        tree = ObstacleFactory.createBlueTree();
      }
      spawnEntityAt(tree, point, true, false);
    }
  }

  /**
   * Used to add lamp entities to the map
   */
  private void spawnLamps() {
    GridPoint2[] lampPositions = new GridPoint2[]{
            new GridPoint2(12, 59), new GridPoint2(24, 53),
            new GridPoint2(28, 45), new GridPoint2(31, 38),
            new GridPoint2(30, 27), new GridPoint2(40, 21),
            new GridPoint2(50, 27), new GridPoint2(59, 31)
    };
    GridPoint2[] vinedLampPositions = new GridPoint2[]{
            new GridPoint2(58, 44),
            new GridPoint2(49, 54), new GridPoint2(59, 60)
    };
    // Spawning in regular lamps
    for (GridPoint2 point: lampPositions) {
      Entity lamp = ObstacleFactory.createLamp(0);
      spawnEntityAt(lamp, point, true, false);
    }
    // Spawning in vined lamps
    for (GridPoint2 point: vinedLampPositions) {
      Entity lamp = ObstacleFactory.createLamp(1);
      spawnEntityAt(lamp, point, true, false);
    }
  }

  /**
   * For spawning in the safehouse (level end)
   */
  private void spawnSafehouse() {
    GridPoint2 position  = new GridPoint2(56, 62);
    Entity safehouse = SafehouseFactory.createSafehouse();
    spawnEntityAt(safehouse, position, true, false);
  }

  /**
   * For adding the player to the level
   * @return Entity player
   */
  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, new GridPoint2(12, 56), true, true);
    return newPlayer;
  }

  /**
   * For adding the bullets to the game for reuse
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
    // Setting enemy positions
    GridPoint2[] enemyPositions = new GridPoint2[]{
            new GridPoint2(36, 24), new GridPoint2(56, 44)
    };
    // Spawning enemies
    for (GridPoint2 enemyPos : enemyPositions) {
      Entity spawnerEnemy = NPCFactory.createSpawnerEnemy();
      spawnerEnemy.getComponent(AITaskComponent.class).addTask(new SpawnerEnemyTask(player, 10, 9f, 10f, this, spawnerEnemy));
      spawnEntityAt(spawnerEnemy, enemyPos, true, true);
    }
  }
  /**
   * Spawns a small enemy from the appropriate spawner's position
   */
  @Override
  public void spawnFromSpawner(Vector2 position, int maxSpawnDistance) {
    super.spawnFromSpawner(position, maxSpawnDistance);
  }

  /**
   * Spawns the small enemies
   */
  private void spawnSmallEnemy() {
    // Setting enemy positions
    GridPoint2[] enemyPositions = new GridPoint2[]{
            new GridPoint2(26, 40), new GridPoint2(38, 41),
            new GridPoint2(21, 11), new GridPoint2(36, 10),
            new GridPoint2(57, 19), new GridPoint2(49, 13)
    };
    // Spawning enemies
    for (GridPoint2 enemyPos : enemyPositions) {
      Entity smallEnemy = NPCFactory.createSmallEnemy(player);
      spawnEntityAt(smallEnemy, enemyPos, true, true);
    }
  }

  /**
   * Spawns the large enemy
   */
  private void spawnLargeEnemy() {
      // Only one so spawn it directly in
      Entity largeEnemy = NPCFactory.createLargeEnemy(player);
      spawnEntityAt(largeEnemy, new GridPoint2(52, 58), true, true);
    }

  /**
   * Spawns the long range enemies that shoot one projectile
   */
  private void spawnLongRangeEnemies() {
    // Setting enemy positions
    GridPoint2[] enemyPositions = new GridPoint2[]{
            new GridPoint2(16, 44), new GridPoint2(27, 33),
            new GridPoint2(38, 43), new GridPoint2(17, 27),
            new GridPoint2(17, 21), new GridPoint2(41, 14),
            new GridPoint2(58, 11), new GridPoint2(60, 50)
    };
    // Spawning enemies
    for (GridPoint2 enemyPos : enemyPositions) {
      Entity archer = NPCFactory.createLongRangeEnemy(player, this);
      spawnEntityAt(archer, enemyPos, true, true);
    }
  }

  /**
   * Spawning in the 3-shot long ranged enemies
   */
  private void spawnToughLongRangeEnemies() {
    // Setting enemy positions
    GridPoint2[] enemyPositions = new GridPoint2[]{
            new GridPoint2(14, 12),
            new GridPoint2(60, 29)
    };
    // Spawning in the enemies
    for (GridPoint2 enemyPos : enemyPositions) {
      Entity touchArcher = NPCFactory.createToughLongRangeEnemy(player, this);
      spawnEntityAt(touchArcher, enemyPos, true, true);
    }
  }

  /**
   * Used to start the games prologue
   */
  private void spawnPrologue(){
    StoryManager.getInstance().loadCutScene(StoryNames.PROLOGUE);
    StoryManager.getInstance().displayStory();
  }

  /**
   * Used to start the tutorial dialog
   */
  private void spawnTutorial(){
    StoryManager.getInstance().loadCutScene(StoryNames.TUTORIAL_GUIDE);
    StoryManager.getInstance().displayStory();
  }

  /**
   * Used to spawn in the tutorial NPC
   */
  private void spawnTutorialNpc() {
    GridPoint2 pos = new GridPoint2(11,56);
    Entity npcTut = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.TUTORIAL_GUIDE, NPC_TUT_1_ATLAS_FILENAME, true);
    spawnEntityAt(npcTut, pos, true, true);
  }

  /**
   * Used to spawn in the firefly pilot NPC
   */
  private void spawnPilotNpc() {
    GridPoint2 pos = new GridPoint2(12,51);
    Entity npcTut = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.NPC_PILOT_FIRST, NPC_PILOT_ATLAS_FILENAME, true);
    spawnEntityAt(npcTut, pos, true, true);
  }

  /**
   * Used to spawn in the injured NPC
   */
  private void spawnInjuredNPC() {
    GridPoint2 pos = new GridPoint2(48,20);
    Entity npcTut = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.NPC_INJURED, NPC_INJURED_ATLAS_FILENAME, false);
    spawnEntityAt(npcTut, pos, true, true);
  }

  /**
   * Starting the tutorial sequence
   */
  private void startTutorialAndMusic() {
    spawnTutorialNpc();
    spawnTutorial();
    playMusic();
  }

  /**
   * Used for playing the background music for the game
   */
  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
    music.setVolume(ServiceLocator.getResourceService().getMusicVolume());
    music.setLooping(true);
    music.play();
  }

  /**
   * Used to load in all assets required for the level
   */
  private void loadAssets() {
    logger.debug("Loading assets");
    loadSharedAssets();
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(cityTextures);
    resourceService.loadTextureAtlases(cityTextureAtlases);
    resourceService.loadSounds(citySounds);
    resourceService.loadMusic(cityMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  /**
   * Used for unloading all assets
   */
  private void unloadAssets() {
    logger.debug("Unloading assets");
    unloadSharedAssets();
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(cityTextures);
    resourceService.unloadAssets(cityTextureAtlases);
    resourceService.unloadAssets(citySounds);
    resourceService.unloadAssets(cityMusic);
  }

  /**
   * Used for disposing the level after completion
   */
  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
    this.unloadAssets();
  }
}