package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.tasks.DistanceFireBulletTask;
import com.deco2800.game.physics.components.PhysicsMovementComponent;

import java.util.List;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class EnemyInDarknessController extends Component {
  private static final int SPEED_MULTIPLIER = 2;
  private static final int FIRING_INTERVAL_DIVIDER = 3;
  private PhysicsMovementComponent movementComponent;
  private boolean inDarkness = true;
  private boolean firingEntity = false;
  private Vector2 defaultSpeed;
  private Vector2 inDarkSpeed;
  private int defaultFiringInterval;
  private int inDarkFiringInterval;
  private DistanceFireBulletTask fireBulletTask;

  @Override
  public void create() {
    super.create();
    movementComponent = this.entity.getComponent(PhysicsMovementComponent.class);
    defaultSpeed = movementComponent.getSpeed();
    inDarkSpeed = new Vector2(defaultSpeed.x * SPEED_MULTIPLIER, defaultSpeed.y * SPEED_MULTIPLIER);
    movementComponent.setSpeed(inDarkSpeed);
    List<PriorityTask> aiTasks = this.entity.getComponent(AITaskComponent.class).getTasks();
    for (PriorityTask task : aiTasks) {
      if (task instanceof DistanceFireBulletTask) {
        firingEntity = true;
        fireBulletTask = (DistanceFireBulletTask) task;
        defaultFiringInterval = fireBulletTask.getFireInterval();
        inDarkFiringInterval = defaultFiringInterval / FIRING_INTERVAL_DIVIDER;
        fireBulletTask.setFireInterval(inDarkFiringInterval);
      }
    }
    entity.getEvents().addListener("inShadow", this::inDarkness);
    entity.getEvents().addListener("inLight", this::inLight);
  }

  void inDarkness() {
    if (!inDarkness) {
      System.out.println("Dark");
      inDarkness = true;
      movementComponent.setSpeed(inDarkSpeed);
      if (firingEntity) {
        System.out.println("Firing speed" + inDarkFiringInterval);
        fireBulletTask.setFireInterval(inDarkFiringInterval);
      }
    }
  }

  void inLight() {
    if (inDarkness) {
      System.out.println("Light");
      inDarkness = false;
      movementComponent.setSpeed(defaultSpeed);
      if (firingEntity) {
        System.out.println("Firing speed" + defaultFiringInterval);
        fireBulletTask.setFireInterval(defaultFiringInterval);
      }
    }
  }
}
