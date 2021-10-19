package com.deco2800.game.components.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ItemFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class LootComponentTest {
  @Mock
  GameArea gameArea;
  @Mock
  TerrainComponent terrainComponent;
  @Mock
  ResourceService resourceService;

  public static final Vector2 DEAD_POSITION_VECTOR = new Vector2(1,1);
  public static final GridPoint2 DEAD_POSITION_GRID = new GridPoint2(1, 1);
  private Entity npc;
  private CombatStatsComponent combatStatsComponent;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerGameArea(gameArea);
    ServiceLocator.registerResourceService(resourceService);
    ServiceLocator.registerPhysicsService(new PhysicsService());

    npc = new Entity();
    npc.addComponent(new CombatStatsComponent(2,1));
    npc.setPosition(DEAD_POSITION_VECTOR);
    combatStatsComponent = npc.getComponent(CombatStatsComponent.class);

  }

  @Test
  void enemyAliveDoNothingTest() {
    npc.addComponent(new LootComponent("Coins", 1, 1, 1));
    LootComponent lootComponent = npc.getComponent(LootComponent.class);
    lootComponent.create();

    // Enemy is not initially dead so loot shouldn't spawn
    lootComponent.update();
    verify(gameArea, Mockito.times(0)).spawnEntityAt(any(Entity.class), any(GridPoint2.class), anyBoolean(), anyBoolean());

    // Enemy is still not dead so loot shouldn't spawn
    combatStatsComponent.hit(1);
    lootComponent.update();
    verify(gameArea, Mockito.times(0)).spawnEntityAt(any(Entity.class), any(GridPoint2.class), anyBoolean(), anyBoolean());
  }

  @Test
  void enemyDeadCheckLootMethodCallsTest() {
    Texture texture = new Texture(Gdx.files.internal("images/playeritems/coin/coin1.png"));
    npc.addComponent(new LootComponent("Coins", 1, 1, 1));
    LootComponent lootComponent = npc.getComponent(LootComponent.class);
    lootComponent.create();

    when(gameArea.getTerrain()).thenReturn(terrainComponent);
    when(terrainComponent.worldPositionToTile(DEAD_POSITION_VECTOR)).thenReturn(DEAD_POSITION_GRID);
    when(resourceService.getAsset(anyString(), any())).thenReturn(texture);

    combatStatsComponent.hit(1);
    combatStatsComponent.hit(1);

    // Enemy dead so loot should spawn
    lootComponent.update();
    verify(gameArea, Mockito.times(1)).spawnEntityAt(any(Entity.class), any(GridPoint2.class), anyBoolean(), anyBoolean());

    // Enemy was already dead so no extra loot should spawn
    lootComponent.update();
    verify(gameArea, Mockito.times(1)).spawnEntityAt(any(Entity.class), any(GridPoint2.class), anyBoolean(), anyBoolean());
  }

  @Test
  void enemyDeadNoLootChanceTest() {
    npc.addComponent(new LootComponent("Coins", 1, 1, 0));
    LootComponent lootComponent = npc.getComponent(LootComponent.class);
    lootComponent.create();

    when(gameArea.getTerrain()).thenReturn(terrainComponent);
    when(terrainComponent.worldPositionToTile(DEAD_POSITION_VECTOR)).thenReturn(DEAD_POSITION_GRID);

    combatStatsComponent.hit(1);
    combatStatsComponent.hit(1);

    // Enemy dead so loot should spawn
    lootComponent.update();
    verify(gameArea, Mockito.times(0)).spawnEntityAt(any(Entity.class), any(GridPoint2.class), anyBoolean(), anyBoolean());

  }
}