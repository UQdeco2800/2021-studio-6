package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
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
  private int minItems;
  private int maxItems;
  private float chanceOfLoot;
  private String item;
  private boolean enemyDead = false;
  private boolean lootSpawned = false;
  private CombatStatsComponent combatStatsComponent;

  public LootComponent(String item, int minItems, int maxItems, float chanceOfLoot) {
    this.minItems = minItems;
    this.maxItems = maxItems;
    this.chanceOfLoot = chanceOfLoot;
    this.item = item;
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

      if (MathUtils.randomBoolean(chanceOfLoot)) {
        int lootQuantity = MathUtils.random(minItems, maxItems);
        Entity loot;

        if (item.equalsIgnoreCase("ammo")) {
          loot = ItemFactory.createAmmoPickup(lootQuantity);
        } else {
          loot = ItemFactory.createCoinPickup(lootQuantity);
        }

        ServiceLocator.getGameArea().spawnEntityAt(loot, deathGridPoint, true, false);
      }
    }
  }

  public void npcHit() {
    if (combatStatsComponent.isDead()) {
      enemyDead = true;
    }
  }
}
