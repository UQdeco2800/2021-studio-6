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
import com.deco2800.game.components.story.StoryManager;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class SafehouseGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(SafehouseGameArea.class);
  private static final int NUM_BULLETS = 5;
  private static Entity door;
  private final float WALL_WIDTH = 0.1f;
  private static final String NPC_PILOT_ATLAS_FILENAME = "images/npc_movement/pilot_npc.atlas";
  private static final String[] safehouseTextures = {
    "images/playeritems/shootingammo.png", "images/playeritems/bandage/bandage01.png",
    "images/playeritems/coin/coin1.png", "images/playeritems/coin/coin2.png",
    "images/Player_Sprite/front01.png", "images/playeritems/pickupammo.png",
    "images/safehouse/interior-day1-tile-ground1-latest.png",
    "images/safehouse/interior-day1-tile-door1-latest.png",
      "images/hud/dashbarFull.png",
      "images/hud/healthFull.png", "images/safehouse/safehouse-interior-layout.png",
      "images/safehouse/shopkeeper/shopkeeperSprite.png",
      "images/dialogue/raw/npc_indicator.png"
  };

  private static final String[] safeHouseTextureAtlases = {
      "images/Player_Animations/player_movement.atlas",
      "images/hud/dashbar.atlas",
      "images/hud/health.atlas",
      "images/weapon/sword.atlas",
      "images/weapon/dagger.atlas",
      "images/weapon/sledge.atlas",
      "images/weapon/machete.atlas",
      "images/playeritems/tourch/torch.atlas",
      "images/weapon/baseball.atlas",
      "images/weapon/axe.atlas",
      NPC_PILOT_ATLAS_FILENAME
  };

  private static final String[] safehouseSounds = {"sounds/Impact4.ogg"};
  private static final String[] playerSounds = {
          "sounds/bandage-use.ogg",
          "sounds/hurt.ogg",
          "sounds/item-pickup.ogg"
  };
  private static final String backgroundMusic = "sounds/safehouse-music.mp3";
  private static final String[] safehouseMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  private GridPoint2 tileBounds;
  private Vector2 worldBounds;

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
    spawnShopKeeper();
    spawnBullet();

    if (ServiceLocator.getGameArea().toString().contains("Level1")){
      spawnSafehouseIntro();
      spawnPilotNpc();
    }

    // Listener for safehouse intro to finish and then play music
    StoryManager.getInstance().getEntity().getEvents().addListener("story-finished:" + StoryNames.SAFEHOUSE_INTRO,
        this::playMusic);
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
    logger.info(String.valueOf(tileSize));
    tileBounds = terrain.getMapBounds(0);
    logger.info(String.valueOf(tileBounds));
    worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    // Left
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(1, 0), false,false);
    // Right
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(tileBounds.x, 0), false,false);
    // Top
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, tileBounds.y - 8),false,false);
    // Bottom
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, 1), false,false);

    // Counter walls
    // horizontal wall
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x - 8, WALL_WIDTH),
            new GridPoint2(0, 5), false,false);

    // vertical wall
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y - 5),
            new GridPoint2(5, 5), false,false);

    // Cupboard horizontal
    spawnEntityAt(
        ObstacleFactory.createWall(worldBounds.x - 8, WALL_WIDTH),
        new GridPoint2(10, 5), false,false);

    // Cupboard vertical
    spawnEntityAt(
        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y - 5),
        new GridPoint2(10, 5), false,false);
  }

  public void spawnDoor() {
    // Create entity
    door = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new DisposingComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.PARAPHERNALIA))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PARAPHERNALIA))
            .addComponent(new TouchTeleportComponent(PhysicsLayer.PLAYER));
    door.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
    door.getComponent(HitboxComponent.class).setAsBox(new Vector2(2, 2));
    door.setPosition(5.5f, 5.5f);

    // Create in the world
    ServiceLocator.getEntityService().register(door);
  }

  private Entity spawnShopKeeper() {
    GridPoint2 SHOP_KEEPER_SPAWN = new GridPoint2(2, 7);

    Entity shopKeeperNPC = FriendlyNPCFactory.createShopkeeperNPC();
    spawnEntityAt(shopKeeperNPC, SHOP_KEEPER_SPAWN, true, true);
    return shopKeeperNPC;
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, new GridPoint2(7, 2), true, true);
    return newPlayer;
  }

  private void spawnPilotNpc() {
    GridPoint2 pos = new GridPoint2(10,4);
    Entity npcTut = FriendlyNPCFactory.createNewFriendlyNPC(StoryNames.NPC_PILOT_SECOND, NPC_PILOT_ATLAS_FILENAME, false);
    spawnEntityAt(npcTut, pos, true, true);
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

  private void spawnSafehouseIntro() {
    StoryManager.getInstance().loadCutScene(StoryNames.SAFEHOUSE_INTRO);
    StoryManager.getInstance().displayStory();
  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.6f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(safehouseTextures);
    resourceService.loadTextureAtlases(safeHouseTextureAtlases);
    resourceService.loadSounds(safehouseSounds);
    resourceService.loadSounds(playerSounds);
    resourceService.loadMusic(safehouseMusic);

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
    resourceService.unloadAssets(safehouseSounds);
    resourceService.unloadAssets(playerSounds);
    resourceService.unloadAssets(safehouseMusic);
  }

  public void stopMusic() {
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
  }

  @Override
  public void dispose() {
    super.dispose();
    door.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}