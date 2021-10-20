package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class GameArea implements Disposable {
  protected TerrainComponent terrain;
  protected List<Entity> areaEntities;
  public Entity player;

  protected static final String[] playerAssets = {
      "images/Player_Sprite/25.png",
      "images/playeritems/bandage/bandage01.png",
      "images/playeritems/armour.png",
      "images/playeritems/rock/ammo.png",
      "images/playeritems/rock/pickupammo.png",
      "images/playeritems/rock/thrown.png",
      "images/playeritems/coin/coin1.png",
      "images/playeritems/coin/coin2.png",
      "images/playeritems/halmet.png",
      "images/playeritems/dagger/dagger.png",
      "images/playeritems/firecracker/firecracker.png",
      "images/playeritems/axe/axe.png",
      "images/playeritems/machete/machete.png",
      "images/hud/dashbarFull.png",
      "images/hud/healthFull.png",
      "images/playeritems/sledge/sledge.png",
      "images/playeritems/bat/baseball.png"
  };

  protected static final String[] playerAtlas = {
      "images/Player_Animations/player_movement.atlas",
      "images/player.atlas",
      "images/Player_Sprite/player_movement.atlas",
      "images/hud/dashbar.atlas",
      "images/hud/health.atlas",
      "images/weapon/crowbar.atlas",
      "images/weapon/axe.atlas",
      "images/weapon/dagger.atlas",
      "images/weapon/sledge.atlas",
      "images/weapon/machete.atlas",
      "images/playeritems/tourch/torch.atlas",
      "images/weapon/baseball.atlas",
      "images/playeritems/rock/rock.atlas",
      "images/playeritems/firecracker/firecracker.atlas",
      "images/playeritems/Bubble/invincibility.atlas"
  };

  protected static final String[] enemyAssets = {
      "images/Enemy_Assets/LongRangeEnemy/bloodball_purple.png",
      "images/Enemy_Assets/ToughLongRangeEnemy/tough-projectile.png"
  };

  protected static final String[] enemyAtlas = {
      "images/Enemy_Assets/LargeEnemy/largeEnemy.atlas",
      "images/Enemy_Assets/SmallEnemy/small_enemy.atlas",
      "images/Enemy_Assets/SpawnerEnemy/spawnerEnemy.atlas",
      "images/Enemy_Assets/ToughLongRangeEnemy/toughLongRangeEnemy.atlas",
      "images/Enemy_Assets/LongRangeEnemy/longRangeEnemy.atlas",
  };


  protected static final String[] enemySounds = {
          "sounds/enemies/ToughLongRangeEnemy/hit.mp3",
          "sounds/enemies/ToughLongRangeEnemy/dead.mp3",
          "sounds/enemies/ToughLongRangeEnemy/shoot.wav",
          "sounds/enemies/LongRangeEnemy/dead.mp3",
          "sounds/enemies/LongRangeEnemy/shoot.mp3",
          "sounds/enemies/LargeEnemy/hit.mp3",
          "sounds/enemies/LargeEnemy/dead.mp3",
          "sounds/enemies/LargeEnemy/detectPlayer.mp3",
          "sounds/enemies/LargeEnemy/meleeAttack.mp3",
          "sounds/enemies/SmallEnemy/dead.mp3",
          "sounds/enemies/SmallEnemy/detectPlayer.wav",
          "sounds/enemies/SmallEnemy/meleeAttack.mp3",
          "sounds/enemies/SpawnerEnemy/hit.mp3",
          "sounds/enemies/SpawnerEnemy/dead.mp3",
          "sounds/enemies/SpawnerEnemy/spawn.wav"
  };

  protected static final String[] playerSounds = {
          "sounds/bandage-use.ogg",
          "sounds/hurt.ogg",
          "sounds/item-pickup.ogg",
          "sounds/weapon-axe.wav",
          "sounds/weapon-sword.ogg",
          "sounds/weapon-dagger.wav",
          "sounds/slingshot.ogg",
          "sounds/dash.ogg",
          "sounds/Impact4.ogg"
  };

  protected GameArea() {
    areaEntities = new ArrayList<>();
  }

  /** Create the game area in the world. */
  public abstract void create();

  /** Dispose of all internal entities in the area */
  public void dispose() {
    for (Entity entity : areaEntities) {
      entity.dispose();
    }
    areaEntities.clear();
  }

  /**
   * Despawns and disposes an entity from the gameArea
   * @param entity The entity to despawn
   */
  public void despawnEntity(Entity entity) {
    int entityIdx = areaEntities.indexOf(entity);
    //array out of bounds check if something tries to despawn twice
    if (entityIdx != -1) {
      Entity temp = areaEntities.remove(entityIdx);
      temp.dispose();
    }
  }

  /**
   * Spawn entity at its current position
   *
   * @param entity Entity (not yet registered)
   */
  public void spawnEntity(Entity entity) {
    areaEntities.add(entity);
    ServiceLocator.getEntityService().register(entity);
  }

  /**
   * Spawn entity on a given tile. Requires the terrain to be set first.
   *
   * @param entity Entity (not yet registered)
   * @param tilePos tile position to spawn at
   * @param centerX true to center entity X on the tile, false to align the bottom left corner
   * @param centerY true to center entity Y on the tile, false to align the bottom left corner
   */
  public void spawnEntityAt(
          Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
    Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
    float tileSize = terrain.getTileSize();
    if (centerX) {
      worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
    }
    if (centerY) {
      worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;
    }
    entity.setPosition(worldPos);
    spawnEntity(entity);
  }

  /**
   * Spawns a small enemy from the appropriate spawner's position
   * @param position position to spawn
   * @param maxSpawnDistance maximum spawn distance
   */
  public void spawnFromSpawner(Vector2 position, int maxSpawnDistance) {
    int x = Math.round(position.x);
    int y = Math.round(position.y);

    GridPoint2 minPos = new GridPoint2(x - maxSpawnDistance, y - maxSpawnDistance);
    GridPoint2 maxPos = new GridPoint2(x + maxSpawnDistance, y + maxSpawnDistance);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity smallEnemy = NPCFactory.createSmallEnemy(player);
    spawnEntityAt(smallEnemy, randomPos, true, true);
  }

  /**
   * Used to load assets shared between all levels
   */
  public void loadSharedAssets() {
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(playerAssets);
    resourceService.loadTextures(enemyAssets);
    resourceService.loadSounds(playerSounds);
    resourceService.loadSounds(enemySounds);
    resourceService.loadTextureAtlases(playerAtlas);
    resourceService.loadTextureAtlases(enemyAtlas);
  }

  /**
   * Used to unload assets shared between all levels
   */
  public void unloadSharedAssets() {
      ResourceService resourceService = ServiceLocator.getResourceService();
      resourceService.unloadAssets(playerAssets);
      resourceService.unloadAssets(enemyAssets);
      resourceService.unloadAssets(enemySounds);
      resourceService.unloadAssets(playerAtlas);
      resourceService.unloadAssets(enemyAtlas);
    resourceService.unloadAssets(playerSounds);
    resourceService.unloadAssets(enemySounds);
  }

  public Entity getPlayer() {
    return player;
  }

  public TerrainComponent getTerrain() {
    return terrain;
  }
}
