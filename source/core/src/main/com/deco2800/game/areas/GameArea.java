package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
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
  protected void spawnEntityAt(
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

  public Entity getPlayer() {
    return player;
  }
}
