package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.DistanceFireBulletTask;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;

import java.util.List;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class EnemyDarknessController extends Component {
  private static final float SPEED_MULTIPLIER = 2;
  private static final float FIRING_INTERVAL_DIVIDER = 3;
  private static final float MAX_DISTANCE_MULTIPLIER = 2.5f;
  private PhysicsMovementComponent movementComponent;
  private boolean entityInDarkness = true;
  private boolean playerInDarkness = false;
  private boolean firingEntity = false;
  private boolean chasingEntity = false;
  private Vector2 defaultSpeed;
  private Vector2 inDarkSpeed;
  private int defaultFiringInterval;
  private int inDarkFiringInterval;
  private float defaultViewDistance;
  private float defaultChaseMaxDistance;
  private float playerInDarkViewDistance;
  private float playerInDarkChaseMaxDistance;
  private DistanceFireBulletTask fireBulletTask;
  private ChaseTask chaseTask;
  private boolean playerEntityInitialised = false;

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
        inDarkFiringInterval = (int) (defaultFiringInterval / FIRING_INTERVAL_DIVIDER);
        fireBulletTask.setFireInterval(inDarkFiringInterval);
      } else if (task instanceof ChaseTask) {
        chasingEntity = true;
        chaseTask = (ChaseTask) task;
        defaultChaseMaxDistance = chaseTask.getMaxChaseDistance();
        defaultViewDistance = chaseTask.getViewDistance();
        playerInDarkChaseMaxDistance = defaultChaseMaxDistance * MAX_DISTANCE_MULTIPLIER;
        playerInDarkViewDistance = defaultViewDistance * MAX_DISTANCE_MULTIPLIER;
      }
    }
    entity.getEvents().addListener("inShadow", this::entityInDarkness);
    entity.getEvents().addListener("inLight", this::entityInLight);

  }

  @Override
  public void update() {
    super.update();
    if (!playerEntityInitialised && ServiceLocator.getGameArea() != null && ServiceLocator.getGameArea().getPlayer() != null) {
      playerEntityInitialised = true;
      ServiceLocator.getGameArea().getPlayer().getEvents().addListener("inShadow", this::playerInDarkness);
      ServiceLocator.getGameArea().getPlayer().getEvents().addListener("inLight", this::playerInLight);
    }
  }

  void entityInDarkness() {
    if (!entityInDarkness) {
      entityInDarkness = true;
      movementComponent.setSpeed(inDarkSpeed);
      if (firingEntity) {;
        fireBulletTask.setFireInterval(inDarkFiringInterval);
      }
    }
  }

  void entityInLight() {
    if (entityInDarkness) {
      entityInDarkness = false;
      movementComponent.setSpeed(defaultSpeed);
      if (firingEntity) {
        fireBulletTask.setFireInterval(defaultFiringInterval);
      }
    }
  }


  void playerInDarkness() {
    if (chasingEntity && !playerInDarkness) {
      playerInDarkness = true;
      chaseTask.setMaxChaseDistance(playerInDarkChaseMaxDistance);
      chaseTask.setViewDistance(playerInDarkViewDistance);
    }
  }

  void playerInLight() {
    if (chasingEntity && playerInDarkness) {
      playerInDarkness = false;
      chaseTask.setMaxChaseDistance(defaultChaseMaxDistance);
      chaseTask.setViewDistance(defaultViewDistance);
    }
  }
}
