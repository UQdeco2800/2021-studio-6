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

/** Forest area for the demo game with trees, a player, and some enemies. */
public class Level1 extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(Level1.class);
  private static final int NUM_BULLETS = 5;
  private static final int NUM_TREES = 3;
  // this can be removed - this is purely for testing purposes
  private static final int NUM_AMMO_PICKUPS = 10;
  private static final int NUM_COIN_PICKUPS = 5;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(12, 57);

  private static final float WALL_WIDTH = 0.1f;
  private static final String NPC_SAMPLE_ATLAS_FILENAME = "images/npc_movement/npc_movement.atlas";
  private static final String NPC_TUT_1_ATLAS_FILENAME = "images/npc_movement/tut_npc1.atlas";
  private static final String NPC_INJURED_ATLAS_FILENAME = "images/npc_movement/injured_npc.atlas";
  private static final String NPC_PILOT_ATLAS_FILENAME = "images/npc_movement/pilot_npc.atlas";

  private static final String[] forestTextures = {
    "images/obstacle_sprite/cobweb.png",
    "images/obstacle_sprite/bush.png",
    "images/level_1/road_tile_black.png",
    "images/level_1/sidewalk.png",
    "images/level_1/cracked_sidewalk.png",
    "images/level_1/curbUpper.png",
    "images/level_1/curbLower.png",
    "images/level_1/road_tile_cracked.png",
    "images/level_1/placeholder_road.png",
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
    "images/grass_1.png"
  };

  private static final String[] cityTextureAtlases = {NPC_SAMPLE_ATLAS_FILENAME,
      NPC_TUT_1_ATLAS_FILENAME,
      NPC_INJURED_ATLAS_FILENAME,
      NPC_PILOT_ATLAS_FILENAME
  };

  private static final String[] citySounds = {"sounds/Impact4.ogg"};

  private static final String[] playerSounds = {
          "sounds/bandage-use.ogg",
          "sounds/hurt.ogg",
          "sounds/item-pickup.ogg"
  };

  private static final String BACKGROUND_MUSIC = "sounds/fireflies-theme-sneak.mp3";
  private static final String[] forestMusic = {BACKGROUND_MUSIC};

  private final TerrainFactory terrainFactory;

  public Level1(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();
    displayUI();

    spawnTerrain();
    spawnBarriers();
    player = spawnPlayer();
    spawnSafehouse();
    spawnBuildings();
    spawnSigns();
    spawnPrologue();

    spawnBullet();
    spawnFireCracker();
    spawnLargeEnemy();
    spawnSmallEnemy();
    spawnSpawnerEnemy();

    spawnPilotNpc();
    spawnInjuredNPC();

    spawnLongRangeEnemies();
    spawnToughLongRangeEnemies();
    spawnDeadTrees();
    spawnLamps();

    //Listener for prologue finished to play music
    StoryManager.getInstance().getEntity().getEvents().addListener("story-finished:" + StoryNames.PROLOGUE,
        this::startTutorialAndMusic);

    // this is used for testing purposes for player pick up
    spawnPickupItems();
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
    GridPoint2 minPos = new GridPoint2(25, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(15, 6);

    // Spawning in Bandage
    spawnEntityAt(ItemFactory.createBandagePickup(1), new GridPoint2(12, 49), true, true);
    spawnEntityAt(ItemFactory.createBandagePickup(1), new GridPoint2(13, 8), true, true);

    // Spawning in ammo randomly
    for (int i = 0; i < NUM_AMMO_PICKUPS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int randomAmmoQuantity = RandomUtils.randomInt(5);
      Entity pickupAmmo = ItemFactory.createAmmoPickup(randomAmmoQuantity);
      spawnEntityAt(pickupAmmo, randomPos, true, false);
    }

    // Spawning in Coins randomly
    for (int i = 0; i < NUM_COIN_PICKUPS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int randomCoinQuantity = RandomUtils.randomInt(5);
      Entity pickupCoin = ItemFactory.createCoinPickup(randomCoinQuantity);
      spawnEntityAt(pickupCoin, randomPos, true, false);
    }
  }

  /**
   * Used to add buildings to the game area
   */
  private void spawnBuildings() {
   GridPoint2[] house_one_positions = new GridPoint2[]{
            new GridPoint2(8, 61), new GridPoint2(18, 61), new GridPoint2(28, 61), new GridPoint2(38, 61),
            new GridPoint2(3, 54),
            new GridPoint2(8, 47), new GridPoint2(21, 47), new GridPoint2(33, 47), new GridPoint2(43, 47),
            new GridPoint2(3, 40),
            new GridPoint2(3, 29), new GridPoint2(13, 29), new GridPoint2(23, 29),
            new GridPoint2(8, 14), new GridPoint2(18, 14), new GridPoint2(28, 14),
            new GridPoint2(3, 7),
            new GridPoint2(8, 0), new GridPoint2(43, 0), new GridPoint2(53, 0)
    };

    GridPoint2[] house_two_positions = new GridPoint2[]{
            new GridPoint2(3, 61), new GridPoint2(13, 61), new GridPoint2(23, 61), new GridPoint2(33, 61),
            new GridPoint2(8, 54),
            new GridPoint2(3, 47), new GridPoint2(16, 47), new GridPoint2(26, 47), new GridPoint2(38, 47),
            new GridPoint2(33, 40), new GridPoint2(43, 40),
            new GridPoint2(8, 29), new GridPoint2(18, 29),
            new GridPoint2(3, 14), new GridPoint2(13, 14), new GridPoint2(23, 14),
            new GridPoint2(8, 7),
            new GridPoint2(3, 0), new GridPoint2(13, 0), new GridPoint2(48, 0)
    };

    // Spawn in type one houses
    for (GridPoint2 point: house_one_positions) {
        Entity house = ObstacleFactory.createBuilding(1);
        spawnEntityAt(house, point, true, false);
    }

    // Spawn in type one houses
    for (GridPoint2 point: house_two_positions) {
      Entity house = ObstacleFactory.createBuilding(2);
      spawnEntityAt(house, point, true, false);
    }
  }

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
      Entity barrier = ObstacleFactory.createVerticalBarrier();
      spawnEntityAt(barrier, position, true, false);
    }
    for (int y = 54; y <= 61; y += 2) { // top barrier on right
      GridPoint2 position = new GridPoint2(32, y);
      Entity barrier = ObstacleFactory.createVerticalBarrier();
      spawnEntityAt(barrier, position, true, false);
    }
    for (int y = 21; y <= 28; y += 2) { // bottom barrier on left
      GridPoint2 position = new GridPoint2(12, y);
      Entity barrier = ObstacleFactory.createVerticalBarrier();
      spawnEntityAt(barrier, position, true, false);
    }
    for (int y = 6; y <= 60; y += 2) { // Main right wall
      GridPoint2 position = new GridPoint2(61, y);
      Entity barrier = ObstacleFactory.createVerticalBarrier();
      spawnEntityAt(barrier, position, true, false);
    }

    // Horizontal Barriers
    for (int x = 41; x <= 60; x += 2) { // Top Barrier
      GridPoint2 position = new GridPoint2(x, 61);
      Entity barrier = ObstacleFactory.createHorizontalBarrier();
      spawnEntityAt(barrier, position, true, false);
    }
    for (int x = 11; x <= 13; x += 2) { // Small Barrier
      GridPoint2 position = new GridPoint2(x, 51);
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
   * Used to add tree entities to the game area
   */
  private void spawnDeadTrees() {
    GridPoint2[] tree_positions = new GridPoint2[]{
            new GridPoint2(42, 65), new GridPoint2(70, 65),
            new GridPoint2(49, 57), new GridPoint2(64, 56),
            new GridPoint2(47, 44), new GridPoint2(64, 42), new GridPoint2(72, 41),
            new GridPoint2(12, 37),
            new GridPoint2(70, 24), new GridPoint2(44, 21), new GridPoint2(67, 20),
            new GridPoint2(59, 19), new GridPoint2(43, 19), new GridPoint2(53, 14),
            new GridPoint2(44, 9), new GridPoint2(72, 9), new GridPoint2(60, 8), new GridPoint2(21, 8), new GridPoint2(66, 6), new GridPoint2(18, 3), new GridPoint2(62, 2)
    };

    for (GridPoint2 point: tree_positions) {
      Entity tree = ObstacleFactory.createDeadTree();
      spawnEntityAt(tree, point, true, false);
    }
  }

  private void spawnLamps() {
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    for (int x = 5; x < tileBounds.x * 0.75; x += 5) {
      GridPoint2 position = new GridPoint2(x, 1);
      Entity lamppost = ObstacleFactory.createLamp(1);
      spawnEntityAt(lamppost, position, true, true);
    }
  }

  private void spawnSafehouse() {
    GridPoint2 position  = new GridPoint2(56, 60);
    Entity safehouse = SafehouseFactory.createSafehouse();
    spawnEntityAt(safehouse, position, true, false);
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

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
    Array<GridPoint2> enemyPositions = new Array<>();
    enemyPositions.add(new GridPoint2(70, 6));
    enemyPositions.add(new GridPoint2(84, 6));

    for (GridPoint2 enemyPos : enemyPositions) {
      Entity spawnerEnemy = NPCFactory.createSpawnerEnemy(player, this);
      spawnerEnemy.getComponent(AITaskComponent.class).addTask(new SpawnerEnemyTask(player, 10, 5f, 6f, this, spawnerEnemy));
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
   * Spawns the small enemy
   */
  private void spawnSmallEnemy() {
    Array<GridPoint2> enemyPositions = new Array<>();
    enemyPositions.add(new GridPoint2(30, 6));
    enemyPositions.add(new GridPoint2(50, 6));

    for (GridPoint2 enemyPos : enemyPositions) {
      Entity smallEnemy = NPCFactory.createSmallEnemy(player);
      spawnEntityAt(smallEnemy, enemyPos, true, true);
    }
  }


  private void spawnLargeEnemy() {
    Array<GridPoint2> enemyPositions = new Array<>();
    enemyPositions.add(new GridPoint2(100, 6));

    for (GridPoint2 enemyPos : enemyPositions) {
      Entity largeEnemy = NPCFactory.createLargeEnemy(player);
      spawnEntityAt(largeEnemy, enemyPos, true, true);
    }
  }

  private void spawnLongRangeEnemies() {
    Array<GridPoint2> enemyPositions = new Array<>();
    enemyPositions.add(new GridPoint2(38, 6));
    enemyPositions.add(new GridPoint2(48, 4));
    enemyPositions.add(new GridPoint2(48, 8));

    for (GridPoint2 enemyPos : enemyPositions) {
      Entity archer = NPCFactory.createLongRangeEnemy(player, this);
      spawnEntityAt(archer, enemyPos, true, true);
    }
  }

  private void spawnToughLongRangeEnemies() {
    Array<GridPoint2> enemyPositions = new Array<>();
    enemyPositions.add(new GridPoint2(60, 6));
    enemyPositions.add(new GridPoint2(80, 6));

    for (GridPoint2 enemyPos : enemyPositions) {
      Entity touchArcher = NPCFactory.createToughLongRangeEnemy(player, this);
      spawnEntityAt(touchArcher, enemyPos, true, true);
    }
  }

  private void spawnPrologue(){
    StoryManager.getInstance().loadCutScene(StoryNames.PROLOGUE);
    StoryManager.getInstance().displayStory();
  }

  private void spawnTutorial(){
    StoryManager.getInstance().loadCutScene(StoryNames.TUTORIAL_GUIDE);
    StoryManager.getInstance().displayStory();
  }

  private void spawnTutorialNpc() {
    GridPoint2 pos = new GridPoint2(12,58);
    Entity npcTut = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.TUTORIAL_GUIDE, NPC_TUT_1_ATLAS_FILENAME, true);
    spawnEntityAt(npcTut, pos, true, true);
  }

  private void spawnPilotNpc() {
    GridPoint2 pos = new GridPoint2(12,52);
    Entity npcTut = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.NPC_PILOT_FIRST, NPC_PILOT_ATLAS_FILENAME, true);
    spawnEntityAt(npcTut, pos, true, true);
  }

  private void spawnInjuredNPC() {
    GridPoint2 pos = new GridPoint2(38,45);
    Entity npcTut = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.NPC_INJURED, NPC_INJURED_ATLAS_FILENAME, false);
    spawnEntityAt(npcTut, pos, true, true);
  }

  private void startTutorialAndMusic() {
    spawnTutorialNpc();
    spawnTutorial();
    playMusic();
  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
    music.setVolume(ServiceLocator.getResourceService().getMusicVolume());
    music.setLooping(true);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    loadSharedAssets();
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(cityTextureAtlases);
    resourceService.loadSounds(citySounds);
    resourceService.loadSounds(playerSounds);
    resourceService.loadSounds(enemySounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    unloadSharedAssets();
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(cityTextureAtlases);
    resourceService.unloadAssets(citySounds);
    resourceService.unloadAssets(playerSounds);
    resourceService.unloadAssets(enemySounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
    this.unloadAssets();
  }
}