package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class EnemyInDarknessController extends Component {
  private PhysicsMovementComponent movementComponent;
  private boolean inDarkness = true;

  @Override
  public void create() {
    super.create();
    this.movementComponent = this.entity.getComponent(PhysicsMovementComponent.class);
    entity.getEvents().addListener("inShadow", this::inDarkness);
    entity.getEvents().addListener("inLight", this::inLight);
  }

  void inDarkness() {
    System.out.println("Dark");
  }

  void inLight() {
    System.out.println("Light");
  }
}
