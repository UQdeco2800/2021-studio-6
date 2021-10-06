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
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class Level3 extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(Level3.class);
  private static final int NUM_TREES = 7;
  private static final int NUM_COBWEBS = 7;
  private static final int NUM_BUSH = 7;
  private static final int NUM_LARGE_ENEMY = 2;
  private static final int NUM_GHOSTS = 2;
  private static final int NUM_LONGRANGE = 2;
  private static final int NUM_BULLETS = 5;
  private static final int NUM_SPAWNER_ENEMY = 2;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final float WALL_WIDTH = 0.1f;
  private static final String[] forestTextures = {
    "images/playeritems/shootingammo.png", "images/playeritems/pickupammo.png",
    "images/playeritems/coin/coin1.png", "images/playeritems/coin/coin2.png",
    "images/Player_Sprite/front01.png", "images/playeritems/bandage/bandage01.png", "images/playeritems/armour.png",
      "images/playeritems/halmet.png", "images/playeritems/sword/sword1.png", "images/playeritems/dagger/dagger.png",
      "images/playeritems/ax/ax_right2.png",
      "images/playeritems/firecracker/firecracker.png",
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
    "images/Enemy_Assets/LongRangeEnemy/eye.png",
    "images/Enemy_Assets/LongRangeEnemy/blood_ball.png",
    "images/player.png",
    "images/Enemy_Assets/LargeEnemy/largeEnemy.png",
    "images/EnemyAssets/SpawnerEnemy/spawnerEnemy.png",
    "images/iso_grass_3.png",
    "images/safehouse/exterior-day1-latest.png",
    "images/hud/dashbarFull.png",
    "images/hud/healthFull.png",
    "images/grass_1.png",
    "images/grass_2.png",
    "images/grass_3.png",
          "images/Enemy_Assets/ToughLongRangeEnemy/short-rangeEnemy.png"
  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas",
    "images/Enemy_Assets/LargeEnemy/largeEnemy.atlas",
    "images/ghost.atlas",
    "images/ghostKing.atlas",
    "images/Enemy_Assets/SmallEnemy/small_enemy.atlas",
      "images/Player_Animations/player_movement.atlas",
    "images/Enemy_Assets/SpawnerEnemy/spawnerEnemy.atlas",
      "images/Player_Sprite/player_movement.atlas",
      "images/hud/dashbar.atlas",
      "images/hud/health.atlas",
      "images/weapon/sword.atlas",
      "images/weapon/axe.atlas",
      "images/weapon/dagger.atlas"  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String BACKGROUND_MUSIC = "sounds/final-boss-music.mp3";
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

    tileLevel(5, 5);
    spawnTerrain();
    //spawnTrees();
    player = spawnPlayer();
    spawnSafehouse();
    //spawnBullet();
    //spawnBomb();
    //spawnCobweb();
    //spawnBush();
    playMusic();

    //spawnLargeEnemy();
    //spawnSmallEnemy();
    //spawnBullet();
    //spawnSpawnerEnemy();

    //spawnLongRangeEnemies();
    //spawnToughLongRangeEnemies();
  }

  public Entity getPlayer() {
    return player;
  }

  private void displayUI() {
    Entity ui = new Entity();
    // Can change level name here
    ui.addComponent(new GameAreaDisplay("Level 3"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO);
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

  private class Room {
    private int x;
    private int y;
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    private Room(int x, int y) {
      this.x = x;
      this.y = y;
    }

    private int getX() {
      return this.x;
    }

    private int getY() {
      return this.y;
    }

    private void setDirection(int direction, boolean state) {
      switch(direction) {
        case 1:
          this.up = state;
          break;
        case 2:
          this.down = state;
          break;
        case 3:
          this.left = state;
          break;
        case 4:
          this.right = state;
          break;
      }
    }

    public String toString() {
      StringBuilder result = new StringBuilder();
      if (this.left) {
        result.append("<");
      }
      if (this.up) {
        result.append("^");
      }
      result.append(this.x);
      result.append(",");
      result.append(this.y);
      if (this.down) {
        result.append("v");
      }
      if (this.right) {
        result.append(">");
      }
      return result.toString();
    }

    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Room room = (Room) o;
      return this.x == room.x && this.y == room.y;
    }
  }

  /**
   * (In theory) randomly populates the level with rooms
   * Top left = 0,0
   */
  private void tileLevel(int width, int height) {
    Array<Room> rooms = new Array<>();
    Room newRoom = new Room(0, 0);
    rooms.add(newRoom);
    rooms.add(new Room(0,1));
    rooms.add(new Room(0,2));
    rooms.add(new Room(1,2));
    rooms.add(new Room(2,2));
    rooms.add(new Room(2,1));
    Room endRoom = new Room(width, height);
    //while (!newRoom.equals(endRoom)) {
    for (int i = 0; i < 11; i++) {
      if (checkSurrounded(rooms, newRoom, width, height)) {

      }
      Room previousRoom = newRoom;
      while (checkDuplicateRoom(rooms, newRoom)) {
        newRoom = generateRoom(previousRoom, width, height);
      }
      rooms.add(newRoom);
      joinRooms(newRoom, previousRoom);
    }
    for (Room room: rooms) {
      System.out.println(room.toString());
    }
  }

  private boolean checkSurrounded(Array<Room> rooms, Room room, int width, int height) {
    int matches = 0;
    int x = room.getX();
    int y = room.getY();
    Room upRoom = new Room(x, y - 1);
    if (y == 0 || checkDuplicateRoom(rooms, upRoom)) {
      matches++;
    }
    Room downRoom = new Room(x, y + 1);
    if (y == height || checkDuplicateRoom(rooms, downRoom)) {
      matches++;
    }
    Room leftRoom = new Room(x - 1, y);
    if (x == 0 || checkDuplicateRoom(rooms, leftRoom)) {
      matches++;
    }
    Room rightRoom = new Room(x + 1, y);
    if (x == width || checkDuplicateRoom(rooms, rightRoom)) {
      matches++;
    }
    return matches == 4;
  }

  private boolean checkDuplicateRoom(Array<Room> rooms, Room newRoom) {
    for (Room room: rooms) {
      if (room.equals(newRoom)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Given a previous room, creates an adjacent room for it
   * If previous room is on the bottom of map, only allow generating new rooms right or up
   * If previous room is on the right edge of map, only allow generating up or left
   * @param previousRoom previous room to join new room to
   * @return new room to be added
   */
  private Room generateRoom(Room previousRoom, int width, int height) {
    int mode;
    while (true) {
      int randInt = RandomUtils.randomInt(4);
      System.out.println(randInt);
      if (randInt == 1 && previousRoom.getY() != 0) {
        //up
        return new Room(previousRoom.getX(), previousRoom.getY() - 1);
      } else if (randInt == 2 && previousRoom.getY() < height && previousRoom.getX() < width) {
        //down
        return new Room(previousRoom.getX(), previousRoom.getY() + 1);
      } else if (randInt == 3 && previousRoom.getY() < height && previousRoom.getX() != 0) {
        //left
        return new Room(previousRoom.getX() - 1, previousRoom.getY());
      } else if (randInt == 4 && previousRoom.getX() < width) {
        //right
        return new Room(previousRoom.getX() + 1, previousRoom.getY());
      }
    }
  }

  /**
   * Given 2 rooms, join them together by setting the appropriate directions in each room
   * e.g. if room1 is left of room2, then set the "right" var of room1 and "left" var of room2 to true
   * @param room1 first room to join
   * @param room2 second room to join
   */
  private void joinRooms(Room room1, Room room2) {
    if (room1.getX() == room2.getX()) {
      if (room1.getY() + 1 == room2.getY()) {
        room1.setDirection(2, true);
        room2.setDirection(1, true);
      } else if (room1.getY() - 1 == room2.getY()) {
        room1.setDirection(1, true);
        room2.setDirection(2, true);
      }
    } else if (room1.getY() == room2.getY()) {
      if (room1.getX() + 1 == room2.getX()) {
        room1.setDirection(4, true);
        room2.setDirection(3, true);
      } else if (room1.getX() - 1 == room2.getX()) {
        room1.setDirection(3, true);
        room2.setDirection(4, true);
      }
    }
  }

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createPineTree();
      spawnEntityAt(tree, randomPos, true, false);
    }
  }

  public void spawnSafehouse() {
    GridPoint2 center = new GridPoint2(15, 15);

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
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_SPAWNER_ENEMY; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity spawnerEnemy = NPCFactory.createSpawnerEnemy(player, this);
      spawnerEnemy.getComponent(AITaskComponent.class).addTask(new SpawnerEnemyTask(getPlayer(), 10, 5f, 6f, this, spawnerEnemy));
      spawnEntityAt(spawnerEnemy, randomPos, true, true);
    }
  }
  /**
   * Spawns a small enemy from the appropriate spawner's position
   */
  public void spawnFromSpawner(Vector2 position, int maxSpawnDistance) {
    super.spawnFromSpawner(position, maxSpawnDistance);
  }

  private void spawnSmallEnemy() {//this da noo 1
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_GHOSTS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity smallEnemy = NPCFactory.createSmallEnemy(player);
      spawnEntityAt(smallEnemy, randomPos, true, true);
    }
  }


  private void spawnLargeEnemy() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_LARGE_ENEMY; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity largeEnemy = NPCFactory.createLargeEnemy(player);
      spawnEntityAt(largeEnemy, randomPos, true, true);
    }
  }

  private void spawnLongRangeEnemies() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
    for (int i = 0; i < NUM_LONGRANGE; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity archer = NPCFactory.createLongRangeEnemy(player, this);
      spawnEntityAt(archer, randomPos, true, true);
    }
  }

  private void spawnToughLongRangeEnemies() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
    for (int i = 0; i < NUM_LONGRANGE; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity touchArcher = NPCFactory.createToughLongRangeEnemy(player, this);
      spawnEntityAt(touchArcher, randomPos, true, true);
    }
  }

  private void spawnCobweb() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_COBWEBS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity cobweb = ObstacleFactory.createCobweb();
      spawnEntityAt(cobweb, randomPos, true, false);
    }
  }

  private void spawnBush() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_BUSH; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity bush = ObstacleFactory.createBush();
      spawnEntityAt(bush, randomPos, true, false);
    }
  }

  private void playMusic() {
    Music gameOverSong = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
    gameOverSong.setLooping(true);
    gameOverSong.setVolume(0.07f);
    gameOverSong.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(LEVEL3_MUSIC);

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
    resourceService.unloadAssets(LEVEL3_MUSIC);
  }

  @Override
  public void dispose() {

    super.dispose();
    ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
    this.unloadAssets();
  }
}