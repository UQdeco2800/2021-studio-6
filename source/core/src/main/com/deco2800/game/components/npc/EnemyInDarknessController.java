package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
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
  private Vector2 defaultSpeed;
  private Vector2 inDarkSpeed;

  @Override
  public void create() {
    super.create();
    movementComponent = this.entity.getComponent(PhysicsMovementComponent.class);
    defaultSpeed = movementComponent.getSpeed();
    inDarkSpeed = new Vector2(defaultSpeed.x * 2, defaultSpeed.y * 2);
    movementComponent.setSpeed(inDarkSpeed);
    entity.getEvents().addListener("inShadow", this::inDarkness);
    entity.getEvents().addListener("inLight", this::inLight);
  }

  void inDarkness() {
    if (!inDarkness) {
      System.out.println("Dark");
      System.out.println(inDarkSpeed);
      inDarkness = true;
      movementComponent.setSpeed(inDarkSpeed);
    }
  }

  void inLight() {
    if (inDarkness) {
      System.out.println("Light");
      System.out.println(defaultSpeed);
      inDarkness = false;
      movementComponent.setSpeed(defaultSpeed);
    }
  }
}
