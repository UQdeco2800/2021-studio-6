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
 * This class determines the enemies reactions when the entity or the player are in darkness
 */
public class EnemyDarknessController extends Component {
  // Multiplies the speed of the entity when in darkness
  private static final float SPEED_MULTIPLIER = 2;
  // Divides the time between firing when entity in darkness
  private static final float FIRING_INTERVAL_DIVIDER = 3;
  // Multiplies the maximum distance that the entity can chase the player when player in darkness
  private static final float MAX_DISTANCE_MULTIPLIER = 2.5f;
  private boolean entityInDarkness = true;
  private boolean playerInDarkness = false;
  // Booleans that determine what type of entity this is
  private boolean firingEntity = false;
  private boolean chasingEntity = false;
  private boolean glowingEyeEntity = false;
  // In the light and dark walk speed
  private Vector2 defaultSpeed;
  private Vector2 inDarkSpeed;
  // In the light and dark firing duration between shots
  private float defaultFiringDuration;
  private float inDarkFiringDuration;
  // In the light and dark view distance
  private float defaultViewDistance;
  private float playerInDarkViewDistance;
  // In the light and dark max chase distance
  private float defaultChaseMaxDistance;
  private float playerInDarkChaseMaxDistance;
  private DistanceFireBulletTask fireBulletTask;
  private ChaseTask chaseTask;
  private PhysicsMovementComponent movementComponent;
  private GlowingEyesComponent glowingEyesComponent;
  // Determines whether the player entity has been loaded
  private boolean playerEntityInitialised = false;

  @Override
  public void create() {
    super.create();

    // get default speed and set speed to the speed for entity in the dark (as entity starts in darkness)
    movementComponent = this.entity.getComponent(PhysicsMovementComponent.class);
    defaultSpeed = movementComponent.getSpeed();
    inDarkSpeed = new Vector2(defaultSpeed.x * SPEED_MULTIPLIER, defaultSpeed.y * SPEED_MULTIPLIER);
    movementComponent.setSpeed(inDarkSpeed);

    List<PriorityTask> aiTasks = this.entity.getComponent(AITaskComponent.class).getTasks();
    for (PriorityTask task : aiTasks) {
      // If entity contains a fire bullet task then get the default fire duration and set the fire duration for in dark
      if (task instanceof DistanceFireBulletTask) {
        firingEntity = true;
        fireBulletTask = (DistanceFireBulletTask) task;
        defaultFiringDuration = fireBulletTask.getFireDuration();
        inDarkFiringDuration = defaultFiringDuration / FIRING_INTERVAL_DIVIDER;
        fireBulletTask.setFireDuration(inDarkFiringDuration);
      // If entity contains a chase task then get default view distance and chase distance and set to in the dark values
      } else if (task instanceof ChaseTask) {
        chasingEntity = true;
        chaseTask = (ChaseTask) task;
        defaultChaseMaxDistance = chaseTask.getMaxChaseDistance();
        defaultViewDistance = chaseTask.getViewDistance();
        playerInDarkChaseMaxDistance = defaultChaseMaxDistance * MAX_DISTANCE_MULTIPLIER;
        playerInDarkViewDistance = defaultViewDistance * MAX_DISTANCE_MULTIPLIER;
      }
    }

    // If the entity has a glowing eyes component then turn them on (as entity starts in darkness)
    glowingEyesComponent = this.entity.getComponent(GlowingEyesComponent.class);
    if (glowingEyesComponent != null) {
      glowingEyeEntity = true;
      glowingEyesComponent.displayOn();
    }

    // Add listeners for when the entity goes into darkness or into light
    entity.getEvents().addListener("inShadow", this::entityInDarkness);
    entity.getEvents().addListener("inLight", this::entityInLight);

  }

  @Override
  public void update() {
    super.update();
    // If the player entity can be found then set listeners for when the player enters darkness and light
    if (!playerEntityInitialised && ServiceLocator.getGameArea() != null && ServiceLocator.getGameArea().getPlayer() != null) {
      playerEntityInitialised = true;
      ServiceLocator.getGameArea().getPlayer().getEvents().addListener("inShadow", this::playerInDarkness);
      ServiceLocator.getGameArea().getPlayer().getEvents().addListener("inLight", this::playerInLight);
    }
  }

  /**
   * Modifies cosmetics and behaviour of the entity when the entity is in darkness
   */
  void entityInDarkness() {
    if (!entityInDarkness) {
      entityInDarkness = true;
      movementComponent.setSpeed(inDarkSpeed);
      if (firingEntity) {
        fireBulletTask.setFireDuration(inDarkFiringDuration);
      }
      if (glowingEyeEntity) {
        glowingEyesComponent.displayOn();
      }
    }
  }

  /**
   * Modifies cosmetics and behaviour of the entity when the entity is in light
   */
  void entityInLight() {
    if (entityInDarkness) {
      entityInDarkness = false;
      movementComponent.setSpeed(defaultSpeed);
      if (firingEntity) {
        fireBulletTask.setFireDuration(defaultFiringDuration);
      }
      if (glowingEyeEntity) {
        glowingEyesComponent.displayOff();
      }
    }
  }

  /**
   * Modifies behaviour of the entity when the player is in darkness
   */
  void playerInDarkness() {
    if (chasingEntity && !playerInDarkness) {
      playerInDarkness = true;
      chaseTask.setMaxChaseDistance(playerInDarkChaseMaxDistance);
      chaseTask.setViewDistance(playerInDarkViewDistance);
    }
  }

  /**
   * Modifies behaviour of the entity when the player is in light
   */
  void playerInLight() {
    if (chasingEntity && playerInDarkness) {
      playerInDarkness = false;
      chaseTask.setMaxChaseDistance(defaultChaseMaxDistance);
      chaseTask.setViewDistance(defaultViewDistance);
    }
  }
}
