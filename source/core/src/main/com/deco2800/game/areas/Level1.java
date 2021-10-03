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

import java.util.ArrayList;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class Level1 extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(Level1.class);
  private static final int NUM_LARGE_ENEMY = 2;
  private static final int NUM_SMALL_ENEMY = 2;
  private static final int NUM_SPAWNER_ENEMY = 2;
  private static final int NUM_LONGRANGE = 2;
  private static final int NUM_BULLETS = 5;
  // this can be removed - this is purely for testing purposes
  private static final int NUM_AMMO_PICKUPS = 10;
  private static final int NUM_COIN_PICKUPS = 5;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 5);

  private static final float WALL_WIDTH = 0.1f;
  private static final String npcSampleAtlasFilename = "images/npc_movement/npc_movement.atlas";
  private static final String npcTut1AtlasFilename = "images/npc_movement/tut_npc1.atlas";
  private static final String[] forestTextures = {
    "images/Player_Sprite/front01.png",
    "images/obstacle_sprite/cobweb.png",
    "images/obstacle_sprite/bush.png", "images/playeritems/bandage/bandage01.png", "images/playeritems/armour.png",
    "images/playeritems/shootingammo.png", "images/playeritems/pickupammo.png",
    "images/playeritems/coin/coin1.png", "images/playeritems/coin/coin2.png",
    "images/playeritems/halmet.png", "images/playeritems/sword/sword.png", "images/playeritems/dagger/dagger.png",
      "images/playeritems/firecracker/firecracker.png", "images/playeritems/axe/axe.png",
      "images/playeritems/machete/machete.png", "images/playeritems/sledge/sledge.png","images/playeritems/bat/baseball.png",
    "images/tree.png",
    "images/ghost_king.png",
    "images/ghost_1.png",
    "images/grass_1.png",
    "images/grass_2.png",
    "images/grass_3.png",
    "images/level_1/road_tile_black.png",
    "images/level_1/sidewalk.png",
    "images/level_1/curbUpper.png",
    "images/level_1/curbLower.png",
    "images/level_1/road_tile_cracked.png",
    "images/level_1/placeholder_road.png",
    "images/level_1/placeholder_curb.png",
    "images/level_1/road_tile_white.png",
    "images/level_1/building2-day1-latest.png",
    "images/hex_grass_1.png",
    "images/hex_grass_2.png",
    "images/hex_grass_3.png",
    "images/iso_grass_1.png",
    "images/iso_grass_2.png",
    "images/iso_grass_3.png",
    "images/iso_grass_3.png",
    "images/gunman.png",
    "images/Enemy_Assets/LongRangeEnemy/eye.png",
    "images/Enemy_Assets/LongRangeEnemy/blood_ball.png",
    "images/player.png",
    "images/Enemy_Assets/LargeEnemy/largeEnemy.png",
    "images/Enemy_Assets/SpawnerEnemy/spawnerEnemy.png",
    "images/iso_grass_3.png",
    "images/safehouse/exterior-day1-latest.png",
    "images/hud/dashbarFull.png",
    "images/hud/healthFull.png",
    "images/level_1/leaving_city_sign.png",
    "images/level_1/forest_sign.png",
    "images/Enemy_Assets/ToughLongRangeEnemy/short-rangeEnemy.png"
  };

  private static final String[] cityTextureAtlases = {
      "images/terrain_iso_grass.atlas",
      "images/Enemy_Assets/LargeEnemy/largeEnemy.atlas",
      "images/ghost.atlas",
      "images/ghostKing.atlas",
      "images/Enemy_Assets/SmallEnemy/small_enemy.atlas",
      "images/Player_Animations/player_movement.atlas",
      "images/Enemy_Assets/SpawnerEnemy/spawnerEnemy.atlas",
      "images/player.atlas",
      "images/Player_Sprite/player_movement.atlas",
      "images/hud/dashbar.atlas",
      "images/hud/health.atlas",
      "images/weapon/sword.atlas",
      "images/weapon/axe.atlas",
      "images/weapon/dagger.atlas",
      "images/weapon/sledge.atlas",
      "images/weapon/machete.atlas",
      "images/playeritems/tourch/torch.atlas",
      "images/weapon/baseball.atlas",
      npcSampleAtlasFilename,
      npcTut1AtlasFilename
  };
  private static final String[] citySounds = {"sounds/Impact4.ogg"};
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
    spawnIntroDialogue();

    spawnBullet();
    spawnBomb();
    spawnLargeEnemy();
    spawnSmallEnemy();
    spawnSpawnerEnemy();
    //spawnBullet();

    spawnLongRangeEnemies();
    spawnToughLongRangeEnemies();

    spawnNPC();
    spawnNPC1();

    //Listener for prologue finished to play music
    StoryManager.getInstance().getEntity().getEvents().addListener("story-finished:" + StoryNames.PROLOGUE,
            this::playMusic);

    // this is used for testing purposes for player pick up
    spawnPickupItems();
  }

  public Entity getPlayer() {
    return player;
  }

  private void displayUI() {
    Entity ui = new Entity();
    // Can change level name here
    ui.addComponent(new GameAreaDisplay("Level 1"));
    spawnEntity(ui);
  }

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

  private void spawnPickupItems() {
    GridPoint2 minPos = new GridPoint2(25, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(15, 6);

    for (int i = 0; i < NUM_AMMO_PICKUPS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int randomAmmoQuantity = RandomUtils.randomInt(5);
      Entity pickupAmmo = ItemFactory.createAmmoPickup(randomAmmoQuantity);
      spawnEntityAt(pickupAmmo, randomPos, true, false);
    }

    for (int i = 0; i < NUM_COIN_PICKUPS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      int randomCoinQuantity = RandomUtils.randomInt(5);
      Entity pickupCoin = ItemFactory.createCoinPickup(randomCoinQuantity);
      spawnEntityAt(pickupCoin, randomPos, true, false);
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

  private void spawnBuildings() {
    GridPoint2 tileBounds = terrain.getMapBounds(0);

    for (int x = 3; x < tileBounds.x * 0.75; x += 7) {
      GridPoint2 position = new GridPoint2(x, (int) (tileBounds.y * 0.7));

      Entity house = ObstacleFactory.createBuilding();
      spawnEntityAt(house, position, true, false);
    }
  }

  private void spawnSigns() {
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    GridPoint2 position  = new GridPoint2(tileBounds.x - 28, tileBounds.y - 5);
    Entity sign = ObstacleFactory.createObject("images/level_1/leaving_city_sign.png", 4f);
    spawnEntityAt(sign, position, true, false);
    position  = new GridPoint2(tileBounds.x - 2, tileBounds.y - 5);
    sign = ObstacleFactory.createObject("images/level_1/forest_sign.png", 3f);
    spawnEntityAt(sign, position, true, false);
  }

  private void spawnBarriers() {
    for (int i = 0; i < 11; i++) {
      if (i == 4 || i == 5 || i == 6) {
        //leave a gap in the middle
        continue;
      }
      GridPoint2 position = new GridPoint2(18, i);
      Entity barrier = ObstacleFactory.createObject("images/level_1/placeholder_curb.png", 1f);
      spawnEntityAt(barrier, position, true, true);
    }
  }

  private void spawnSafehouse() {
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    GridPoint2 position  = new GridPoint2(tileBounds.x - 5, tileBounds.y - 5);

    Entity safehouse = SafehouseFactory.createSafehouse();
    spawnEntityAt(safehouse, position, true, false);
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);

    // this will be removed - purely for testing
    GridPoint2 SHOP_KEEPER_SPAWN = new GridPoint2(5, 5);

    Entity shopKeeperNPC = NPCFactory.createShopkeeperNPC();
    spawnEntityAt(shopKeeperNPC, SHOP_KEEPER_SPAWN, true, true);
    return newPlayer;
  }

  private void spawnBullet() {
    Array<Entity> bullets = new Array<>();

    for (int i = 0; i < NUM_BULLETS; i++) {
      Entity newBullet = BulletFactory.createBullet();
      bullets.add(newBullet);
      spawnEntity(newBullet);
    }

    getPlayer().getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
  }

  private void spawnBomb() {
    Array<Entity> bombs = new Array<>();

    for (int i = 0; i < NUM_BULLETS; i++) {
      Entity newBomb = BombFactory.createBomb();
      bombs.add(newBomb);
      spawnEntity(newBomb);
    }

    getPlayer().getComponent(PlayerRangeAOEComponent.class).addBombs(bombs);
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
      spawnerEnemy.getComponent(AITaskComponent.class).addTask(new SpawnerEnemyTask(getPlayer(), 10, 5f, 6f, this, spawnerEnemy));
      spawnEntityAt(spawnerEnemy, enemyPos, true, true);
    }
    /*
    GridPoint2 minPos = new GridPoint2(0, 0).add(1, 1);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(15, 6);

    for (int i = 0; i < NUM_SPAWNER_ENEMY; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity spawnerEnemy = NPCFactory.createSpawnerEnemy(player, this);
      spawnerEnemy.getComponent(AITaskComponent.class).addTask(new SpawnerEnemyTask(getPlayer(), 10, 5f, 6f, this, spawnerEnemy));
      spawnEntityAt(spawnerEnemy, randomPos, true, true);
    }
    */
  }
  /**
   * Spawns a small enemy from the appropriate spawner's position
   */
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
    /*
    GridPoint2 minPos = new GridPoint2(0, 0).add(1, 1);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(15, 6);

    for (int i = 0; i < NUM_SMALL_ENEMY; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity smallEnemy = NPCFactory.createSmallEnemy(player);
      spawnEntityAt(smallEnemy, randomPos, true, true);
    }
    */
  }


  private void spawnLargeEnemy() {
    Array<GridPoint2> enemyPositions = new Array<>();
    enemyPositions.add(new GridPoint2(100, 6));

    for (GridPoint2 enemyPos : enemyPositions) {
      Entity largeEnemy = NPCFactory.createLargeEnemy(player);
      spawnEntityAt(largeEnemy, enemyPos, true, true);
    }
    /*
    GridPoint2 minPos = new GridPoint2(0, 0).add(1, 1);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(15, 6);

    for (int i = 0; i < NUM_LARGE_ENEMY; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity largeEnemy = NPCFactory.createLargeEnemy(player);
      spawnEntityAt(largeEnemy, randomPos, true, true);
    }
    */
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
    /*
    GridPoint2 minPos = new GridPoint2(0, 0).add(1, 1);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(15, 6);

    for (int i = 0; i < NUM_LONGRANGE; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity archer = NPCFactory.createLongRangeEnemy(player, this);
      spawnEntityAt(archer, randomPos, true, true);
    }
    */
  }

  private void spawnToughLongRangeEnemies() {
    Array<GridPoint2> enemyPositions = new Array<>();
    enemyPositions.add(new GridPoint2(60, 6));
    enemyPositions.add(new GridPoint2(80, 6));

    for (GridPoint2 enemyPos : enemyPositions) {
      Entity touchArcher = NPCFactory.createToughLongRangeEnemy(player, this);
      spawnEntityAt(touchArcher, enemyPos, true, true);
    }
    /*
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(15, 6);

    for (int i = 0; i < NUM_LONGRANGE; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity touchArcher = NPCFactory.createToughLongRangeEnemy(player, this);
      spawnEntityAt(touchArcher, randomPos, true, true);
    }
    */
  }

  private void spawnIntroDialogue(){
    StoryManager.getInstance().loadCutScene(StoryNames.PROLOGUE);
    StoryManager.getInstance().displayStory();
  }

  private void spawnNPC() {
    GridPoint2 pos = new GridPoint2(10,2);
    Entity npc = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.TOWN_GUIDE, npcSampleAtlasFilename, true);
    spawnEntityAt(npc, pos, true, true);
  }

  private void spawnNPC1() {
    GridPoint2 pos = new GridPoint2(12,8);
    Entity npcTut = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.TUTORIAL_GUIDE, npcTut1AtlasFilename, false);
    spawnEntityAt(npcTut, pos, true, true);
  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(cityTextureAtlases);
    resourceService.loadSounds(citySounds);
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
    resourceService.unloadAssets(cityTextureAtlases);
    resourceService.unloadAssets(citySounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
    this.unloadAssets();
  }
}
