package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ServiceLocator;

public class TorchLightingComponent extends Component implements Disposable {
  private static final int ADD_TORCH = 70;
  private HitboxComponent hitboxComponent;
  private short targetLayer;
  private Entity playerState;

  public TorchLightingComponent(short targetLayer) {
    this.targetLayer = targetLayer;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
  }

  @Override
  public void dispose() {
    this.hitboxComponent.dispose();
    this.playerState.dispose();
  }

  private void onCollisionStart(Fixture me, Fixture other) {
    if (!enabled) {
      return;
    }

    if (hitboxComponent.getFixture() != me) {
      // Not triggered by hitbox, ignore
      return;
    }
    if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
      // Doesn't match our target layer, ignore
      return;
    }
    loadPlayerData();
    playerState.getComponent(InventoryComponent.class).lightTorch(ADD_TORCH);
  }

  private void loadPlayerData() {
    playerState = ServiceLocator.getGameArea().player;
  }
}
