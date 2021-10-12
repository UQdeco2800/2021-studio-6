package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.factories.ItemFactory;
import com.deco2800.game.services.ServiceLocator;

/**
 * This component controls the sound effects for the NPC entity
 */
public class LootComponent extends Component {
  private int lootLevel;
  private boolean enemyDead = false;
  private boolean lootSpawned = false;
  private CombatStatsComponent combatStatsComponent;

  public LootComponent(int lootLevel) {
    this.lootLevel = lootLevel;
  }

  @Override
  public void create() {
    super.create();

    combatStatsComponent = this.entity.getComponent(CombatStatsComponent.class);
    this.entity.getEvents().addListener("hit", this::npcHit);
  }

  @Override
  public void update() {
    super.update();
    if (enemyDead && !lootSpawned) {
      lootSpawned = true;
      System.out.println("DEAD");

      Vector2 deathPos = entity.getPosition();
      GridPoint2 deathGridPoint = ServiceLocator.getGameArea().getTerrain().worldPositionToTile(deathPos);

      Entity loot = ItemFactory.createCoinPickup(5);
      ServiceLocator.getGameArea().spawnEntityAt(loot, deathGridPoint, true, false);
    }
  }

  public void npcHit() {
    if (combatStatsComponent.isDead()) {
      enemyDead = true;
    }
  }
}
