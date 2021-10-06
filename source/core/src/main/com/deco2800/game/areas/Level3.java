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

import java.util.Objects;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class Level3 extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(Level3.class);
  private static final int NUM_TREES = 0;
  private static final int NUM_COBWEBS = 0;//change these back
  private static final int NUM_BUSH = 0;
  private static final int NUM_LARGE_ENEMY = 0;
  private static final int NUM_GHOSTS = 0;
  private static final int NUM_LONGRANGE = 0;
  private static final int NUM_BULLETS = 5; // Must be 5, to allow range-attack.
  private static final int NUM_SPAWNER_ENEMY = 0;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(1, 41);
  private static final float WALL_WIDTH = 0.1f;
  private static final String[] forestTextures = {
    "images/playeritems/shootingammo.png", "images/playeritems/pickupammo.png",
    "images/playeritems/coin/coin1.png", "images/playeritems/coin/coin2.png",
    "images/Player_Sprite/front01.png", "images/playeritems/bandage/bandage01.png", "images/playeritems/armour.png",
    "images/playeritems/halmet.png", "images/playeritems/sword/sword.png", "images/playeritems/dagger/dagger.png",
    "images/playeritems/machete/machete.png", "images/playeritems/sledge/sledge.png","images/playeritems/bat/baseball.png",
    "images/playeritems/axe/axe.png",
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
    "images/level_2/level2_background_tile.png",
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
    "images/Enemy_Assets/ToughLongRangeEnemy/short-rangeEnemy.png",
    "images/level_3/level3_dead_group_pine_tree.png",
    "images/level_3/level3_brown_group_pine_tree.png",
    "images/level_2/level2_tree_2_group_ver1.png",

          // TODO: test grass for level 3
          "images/level_3/level3_grass_test2.png",
          "images/level_3/level3_grass_test3.png",
          "images/level_3/level3_grass_test4.png",
  };

  /**
   * Texture atlases file path for Level 3.
   */
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
    "images/weapon/sledge.atlas",
    "images/playeritems/tourch/torch.atlas",
    "images/weapon/machete.atlas",
    "images/weapon/baseball.atlas",
    "images/weapon/dagger.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String[] playerSounds = {
          "sounds/bandage-use.ogg",
          "sounds/hurt.ogg",
          "sounds/item-pickup.ogg"
  };
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
    tileLevel(5, 5);
    spawnTerrain();
    spawnTerrainTriGreenPineTrees();
    spawnTerrainTriBrownPineTrees();
    spawnSafehouse();
    spawnCobweb();
    spawnBush();

    // Spawn player related entities
    player = spawnPlayer();
    spawnBullet();
    spawnBomb();
     spawnPickupItems();
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
   * Gets the player entity for Level 3.
   * @return Player entity.
   */
  public Entity getPlayer() {
    return player;
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
    width--;
    height--;
    Array<Room> rooms = new Array<>();
    Room newRoom = new Room(0, 0);
    rooms.add(newRoom);
    Room endRoom = new Room(width, 0);
    Room previousRoom;
    for (int i = 0; !newRoom.equals(endRoom); i++) {
      if (checkSurrounded(rooms, newRoom, width, height)) {
        previousRoom = rooms.get(
            getPreviousNonSurroundedRoom(rooms, i, width, height));
      } else {
        previousRoom = newRoom;
      }
      for (int j = 0; checkDuplicateRoom(rooms, newRoom); j++) {
        newRoom = generateRoom(previousRoom, width, height);
        if (j >= 100) {
          for (Room room: rooms) {
            System.out.println(room.toString());
          }
          throw new IllegalStateException("Infinite loop detected");
        }
      }
      rooms.add(newRoom);
      joinRooms(newRoom, previousRoom);
    }
    for (Room room: rooms) {
      System.out.println(room.toString());
    }
  }

  private int getPreviousNonSurroundedRoom(Array<Room> rooms, int index, int width, int height) {
    int i;
    for (i = index; i >= 0; i--) {
      Room room = rooms.get(i);
      if (!checkSurrounded(rooms, room, width, height)) {
        break;
      }
    }
    return i;
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
      new GridPoint2(36, 42), new GridPoint2(54, 42), new GridPoint2(36, 41),
      new GridPoint2(54, 41), new GridPoint2(36, 40), new GridPoint2(54, 40),
      new GridPoint2(36, 39), new GridPoint2(54, 39), new GridPoint2(36, 38),
      new GridPoint2(54, 38), new GridPoint2(36, 37), new GridPoint2(54, 37),
      new GridPoint2(19, 36), new GridPoint2(20, 36), new GridPoint2(21, 36),
      new GridPoint2(22, 36), new GridPoint2(23, 36), new GridPoint2(24, 36),
      new GridPoint2(25, 36), new GridPoint2(26, 36), new GridPoint2(27, 36),
      new GridPoint2(28, 36), new GridPoint2(29, 36), new GridPoint2(30, 36),
      new GridPoint2(31, 36), new GridPoint2(32, 36), new GridPoint2(33, 36),
      new GridPoint2(34, 36), new GridPoint2(35, 36), new GridPoint2(36, 36),
      new GridPoint2(54, 36), new GridPoint2(54, 35), new GridPoint2(24, 30),
      new GridPoint2(25, 30), new GridPoint2(26, 30), new GridPoint2(27, 30),
      new GridPoint2(28, 30), new GridPoint2(29, 30), new GridPoint2(30, 30),
      new GridPoint2(31, 30), new GridPoint2(32, 30), new GridPoint2(33, 30),
      new GridPoint2(34, 30), new GridPoint2(35, 30), new GridPoint2(36, 30),
      new GridPoint2(36, 29), new GridPoint2(36, 28), new GridPoint2(54, 28),
      new GridPoint2(36, 27), new GridPoint2(54, 27), new GridPoint2(36, 26),
      new GridPoint2(54, 26), new GridPoint2(36, 25), new GridPoint2(54, 25),
      new GridPoint2(19, 24), new GridPoint2(20, 24), new GridPoint2(21, 24),
      new GridPoint2(22, 24), new GridPoint2(23, 24), new GridPoint2(24, 24),
      new GridPoint2(25, 24), new GridPoint2(26, 24), new GridPoint2(27, 24),
      new GridPoint2(28, 24), new GridPoint2(29, 24), new GridPoint2(30, 24),
      new GridPoint2(36, 24), new GridPoint2(54, 24), new GridPoint2(36, 23),
      new GridPoint2(54, 23), new GridPoint2(36, 22), new GridPoint2(54, 22),
      new GridPoint2(36, 21), new GridPoint2(54, 21), new GridPoint2(36, 20),
      new GridPoint2(36, 19), new GridPoint2(24, 18), new GridPoint2(25, 18),
      new GridPoint2(26, 18), new GridPoint2(27, 18), new GridPoint2(28, 18),
      new GridPoint2(29, 18), new GridPoint2(30, 18), new GridPoint2(31, 18),
      new GridPoint2(32, 18), new GridPoint2(33, 18), new GridPoint2(34, 18),
      new GridPoint2(35, 18), new GridPoint2(36, 18), new GridPoint2(37, 18),
      new GridPoint2(38, 18), new GridPoint2(39, 18), new GridPoint2(40, 18),
      new GridPoint2(41, 18), new GridPoint2(54, 18), new GridPoint2(54, 17),
      new GridPoint2(54, 16), new GridPoint2(54, 15), new GridPoint2(54, 14),
      new GridPoint2(54, 13), new GridPoint2(54, 12), new GridPoint2(54, 11),
      new GridPoint2(54, 10), new GridPoint2(18, 9), new GridPoint2(19, 9),
      new GridPoint2(20, 9), new GridPoint2(21, 9), new GridPoint2(22, 9),
      new GridPoint2(23, 9), new GridPoint2(24, 9), new GridPoint2(25, 9),
      new GridPoint2(26, 9), new GridPoint2(27, 9), new GridPoint2(28, 9),
      new GridPoint2(29, 9), new GridPoint2(30, 9), new GridPoint2(31, 9),
      new GridPoint2(32, 9), new GridPoint2(33, 9), new GridPoint2(34, 9),
      new GridPoint2(35, 9), new GridPoint2(36, 9), new GridPoint2(54, 9),
      new GridPoint2(36, 8), new GridPoint2(54, 8), new GridPoint2(36, 7),
      new GridPoint2(54, 7), new GridPoint2(36, 6), new GridPoint2(54, 6),
      new GridPoint2(36, 5), new GridPoint2(54, 5), new GridPoint2(36, 4),
      new GridPoint2(54, 4), new GridPoint2(36, 3), new GridPoint2(54, 3),
      new GridPoint2(36, 2), new GridPoint2(54, 2), new GridPoint2(36, 1),
      new GridPoint2(54, 1), new GridPoint2(36, 0), new GridPoint2(37, 0),
      new GridPoint2(38, 0), new GridPoint2(39, 0), new GridPoint2(40, 0),
      new GridPoint2(41, 0), new GridPoint2(42, 0), new GridPoint2(43, 0),
      new GridPoint2(44, 0), new GridPoint2(45, 0), new GridPoint2(46, 0),
      new GridPoint2(47, 0), new GridPoint2(48, 0), new GridPoint2(49, 0),
      new GridPoint2(50, 0), new GridPoint2(51, 0), new GridPoint2(52, 0),
      new GridPoint2(53, 0), new GridPoint2(54, 0),
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
      Entity newBullet = BulletFactory.createBullet();
      bullets.add(newBullet);
      spawnEntity(newBullet);
    }

    player.getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
  }

  /**
   * Spawns the bomb entity when player uses them.
   */
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
    GridPoint2[] spawnLocations = {
      new GridPoint2(50, 37),
      new GridPoint2(33, 33),
      new GridPoint2(52, 21),
      new GridPoint2(13, 13),
      new GridPoint2(41, 6),
    };

    for (int i = 0; i < spawnLocations.length; i++) {
      Entity spawnerEnemy = NPCFactory.createSpawnerEnemy(player, this);
      spawnerEnemy.getComponent(AITaskComponent.class).addTask(new SpawnerEnemyTask(getPlayer(), 10, 5f, 6f, this, spawnerEnemy));
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
      new GridPoint2(47, 40),
      new GridPoint2(25, 34),
      new GridPoint2(28, 32),
      new GridPoint2(3, 31),
      new GridPoint2(25, 28),
      new GridPoint2(33, 27),
      new GridPoint2(44, 25),
      new GridPoint2(41, 21),
      new GridPoint2(29, 20),
      new GridPoint2(51, 17),
      new GridPoint2(46, 16),
      new GridPoint2(5, 14),
      new GridPoint2(15, 6),
      new GridPoint2(46, 4),
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
      new GridPoint2(43, 39),
      new GridPoint2(46, 37),
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
      new GridPoint2(38, 32),
      new GridPoint2(41, 30),
      new GridPoint2(52, 28),
      new GridPoint2(46, 27),
      new GridPoint2(2, 25),
      new GridPoint2(20, 18),
      new GridPoint2(42, 15),
      new GridPoint2(13, 5),
    };

    GridPoint2[] bandageSpawnLocations = {
      new GridPoint2(21, 30),
      new GridPoint2(43, 28),
      new GridPoint2(46, 14),
      new GridPoint2(9, 11),
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

//    for (int i = 0; i < bandageSpawnLocations.length; i++) {
//      int randomAmmoQuantity = RandomUtils.randomInt(5);
//      Entity pickupAmmo = ItemFactory.ba;
//      spawnEntityAt(pickupAmmo, ammoSpawnLocations[i], true, false);
//    }

    for (int i = 0; i < coinSpawnLocations.length; i++) {
      int randomCoinQuantity = RandomUtils.randomInt(5);
      Entity pickupCoin = ItemFactory.createCoinPickup(randomCoinQuantity);
      spawnEntityAt(pickupCoin, coinSpawnLocations[i], true, false);
    }
  }

  private void spawnLevelThreeIntro() {
    StoryManager.getInstance().loadCutScene(StoryNames.LEVEL3_INTRO);
    StoryManager.getInstance().displayStory();
  }

  private void playMusic() {
    Music gameOverSong = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
    gameOverSong.setLooping(true);
    gameOverSong.setVolume(0.3f);
    gameOverSong.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadSounds(playerSounds);
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
    resourceService.unloadAssets(playerSounds);
    resourceService.unloadAssets(LEVEL3_MUSIC);
  }

  @Override
  public void dispose() {

    super.dispose();
    ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
    this.unloadAssets();
  }
}