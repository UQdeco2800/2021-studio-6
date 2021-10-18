package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.tasks.DeadTask;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * This class listens to events relevant to a NPCs state and plays the animation when one
 * of the events is triggered. 
 */
public class NPCAnimationController extends Component {
  // arrays are used to easily index into the stationary and running states of the animation
  private static final String[] ANIMATIONS_LEFT = {"left", "left-run", "left-damaged", "left-run-damaged", "left-hit", "left-run-hit", "left-damaged-hit", "left-run-damaged-hit"};
  private static final String[] ANIMATIONS_RIGHT = {"right", "right-run", "right-damaged", "right-run-damaged", "right-hit", "right-run-hit", "right-damaged-hit", "right-run-damaged-hit"};
  private static final String[] ANIMATIONS_FRONT = {"front", "front-run", "front-damaged", "front-run-damaged", "front-hit", "front-run-hit", "front-damaged-hit", "front-run-damaged-hit"};
  private static final String[] ANIMATIONS_BACK = {"back", "back-run", "back-damaged", "back-run-damaged", "back-hit", "back-run-hit", "back-damaged-hit", "back-run-damaged-hit"};
  private static String currentDirectionAsText;
  private static final int STATIONARY = 0;
  private static final int WALKING = 1;
  private static final long hurtDuration = 200;
  private long hitTime;
  private int indexOffset = 0;
  private boolean hitActive = false;
  private boolean damagedActive = false;
  private boolean isDead = false;
  private boolean spawning = false;
  private long spawnTime;
  private static final long spawnDuration = 500;
  private CombatStatsComponent combatStatsComponent;
  private NPCSoundComponent npcSoundComponent;
  private GlowingEyesComponent glowingEyesComponent;
  private String[] currentDirection;
  private Vector2 currentWalkingTarget;
  AnimationRenderComponent animator;
  private String lastAnimation;
  private GameTime gameTime;

  /**
   * Creates the animator and sets default animation.
   */
  @Override
  public void create() {
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    glowingEyesComponent = this.entity.getComponent(GlowingEyesComponent.class);
    animator.startAnimation(ANIMATIONS_FRONT[STATIONARY]);
    if (glowingEyesComponent != null) {
      glowingEyesComponent.startAnimation(ANIMATIONS_FRONT[STATIONARY] + "-glow");
    }
    currentDirection = ANIMATIONS_FRONT;
    currentDirectionAsText = "front";
    gameTime = ServiceLocator.getTimeSource();


    npcSoundComponent = this.entity.getComponent(NPCSoundComponent.class);

    // If the entity has combat stats then add a listener for when it gets hit
    combatStatsComponent = this.entity.getComponent(CombatStatsComponent.class);
    if (combatStatsComponent != null) {
      this.entity.getEvents().addListener("hit", this::npcHit);
    }

    this.entity.getEvents().addListener("spawn", this::spawning);
  }

  /**
   * Extracts the current target coordinates and changes the animation to face that direction. For example if the
   * target coordinates are to the north than we will see the npc back so update the animation to the back animation.
   */
  @Override
  public void update() {
    // If game paused then don't update anything
    if (gameTime.isPaused()) {
      return;
    }
    // If the entity is dead then start the dead animation
    if (isDead) {
      if (lastAnimation != "dead") {
        animator.startAnimation("dead");
        if (glowingEyesComponent != null) {
          glowingEyesComponent.deactivate();
        }
        lastAnimation = "dead";
      }
      return;
    }

    // If the enemy spawn time has expired then switch off spawn variable
    if (spawning && gameTime.getTimeSince(spawnTime) >= spawnDuration) {
      spawning = false;
    }

    // If the entity is spawning then start the spawn animation
    if (spawning) {
      if (lastAnimation != "spawn") {
        animator.startAnimation("spawn");
        lastAnimation = "spawn";
      }
      return;
    }

    // If the enemy hit time has expired then switch off hit variable
    if (hitActive && gameTime.getTimeSince(hitTime) >= hurtDuration) {
      hitActive = false;
    }

    // If hit active and damage active then switch to hit and damaged related animation offset
    if (hitActive && damagedActive) {
      indexOffset = 6;
    // If hit active then switch to hit related animation offset
    } else if (hitActive) {
      indexOffset = 4;
    // If damaged active then switch to damage related animation offset
    } else if (damagedActive) {
      indexOffset = 2;
    // Else use the default animation offset
    } else {
      indexOffset = 0;
    }

    // Get the current walking target
    PhysicsMovementComponent npcMovement = entity.getComponent(PhysicsMovementComponent.class);
    Vector2 walkTarget = npcMovement.getTarget();
    Vector2 myPosition = entity.getPosition();

    // Account for rounding errors, if the entity is close enough to the walk position then stop
    if (walkTarget != null && Math.abs(walkTarget.x - myPosition.x) < 0.1 && Math.abs(walkTarget.y - myPosition.y) < 0.1) {
      npcMovement.setMoving(false);
    }

    // if the npc is not moving then set the animation as the stationary animation facing the same direction
    if (!npcMovement.getMoving()) {
      setDirection(currentDirection[STATIONARY + indexOffset]);
    // if there is target coordinates then update the animation direction to face it
    } else if (walkTarget != null && walkTarget != currentWalkingTarget) {
      currentWalkingTarget = walkTarget;
      updateAnimationDirection(walkTarget, WALKING + indexOffset);
    }
  }

  /**
   * Updates the direction the animation is facing based on the coordinates the animation should be facing towards.
   * Chooses the correct animation state that should be set as well.
   * @param lookPosition the coordinates that the animation should face
   * @param state whether the npc is currently walking (value of 1) or stationary (value of 0)
   */
  public void updateAnimationDirection(Vector2 lookPosition, int state) {
    Vector2 myPos = entity.getPosition();
    float xOffset = lookPosition.x - myPos.x;
    float yOffset = lookPosition.y - myPos.y;

    // if the coordinates are in a 45 degree cone above the NPC then set the animation to be the back
    if (yOffset > 0 && yOffset > -xOffset && yOffset > xOffset) {
      setDirection(ANIMATIONS_BACK[state]);
      currentDirection = ANIMATIONS_BACK;
      currentDirectionAsText = "back";
    }
    // if the coordinates are in a 45 degree cone below the NPC then set the animation to be the front
    else if (yOffset <= 0 && yOffset <= -xOffset && yOffset <= xOffset) {
      setDirection(ANIMATIONS_FRONT[state]);
      currentDirection = ANIMATIONS_FRONT;
      currentDirectionAsText = "front";
    }
    // if the coordinates are in a 45 degree cone to the right the NPC then set the animation to be the right
    else if (xOffset > 0 && yOffset > -xOffset && yOffset <= xOffset) {
      setDirection(ANIMATIONS_RIGHT[state]);
      currentDirection = ANIMATIONS_RIGHT;
      currentDirectionAsText = "right";
    }
    // if the coordinates are in a 45 degree cone to the left of the NPC then set the animation to be the left
    else if (xOffset < 0 && yOffset <= -xOffset && yOffset > xOffset) {
      setDirection(ANIMATIONS_LEFT[state]);
      currentDirection = ANIMATIONS_LEFT;
      currentDirectionAsText = "left";
    }
  }

  /**
   * Starts a directional animation and stores as the last animation played
   * @param direction the animation state to set the direction to
   */
  public void setDirection(String direction) {
    if (!direction.equals(lastAnimation)) {
      this.entity.getEvents().trigger(currentDirectionAsText);
      animator.startAnimation(direction);
      if (glowingEyesComponent != null) {
        glowingEyesComponent.startAnimation(direction + "-glow");
      }
      lastAnimation = direction;
    }
  }

  /**
   * Flags that the spawner is currently spawning
   */
  public void spawning() {
    spawnTime = gameTime.getTime();
    spawning = true;
  }

  /**
   * Check whether the NPC should be hit and then register the hit. Check if the entity is now dead.
   */
  private void npcHit() {
    hitActive = true;
    hitTime = gameTime.getTime();

    // If the entity wasn't dead but they are dead now then set entity as dead
    if (!isDead && combatStatsComponent.isDead()) {
      isDead = true;
      npcSoundComponent.playDead();

      // Switch off all the components that would interfere with the player, as this entity is dead
      AITaskComponent aiTaskComponent = entity.getComponent(AITaskComponent.class);
      aiTaskComponent.addTask(new DeadTask());
      ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);
      TouchAttackComponent touchAttackComponent = entity.getComponent(TouchAttackComponent.class);
      HitboxComponent hitboxComponent = entity.getComponent(HitboxComponent.class);
      if (hitboxComponent != null) {
        hitboxComponent.setLayer(PhysicsLayer.NONE);
      }
      if (colliderComponent != null) {
        colliderComponent.setLayer(PhysicsLayer.NONE);
      }
      if (touchAttackComponent != null) {
        touchAttackComponent.disable();
      }
    // If the entities life is now less than half health, then set them as damaged
    } else if (!damagedActive && combatStatsComponent.getHealth() <= combatStatsComponent.getMaxHealth()/2) {
      damagedActive = true;
    }

    // If the enemy isn't dead then play the hit sound effect
    if (!isDead) {
      npcSoundComponent.playHit();
    }
  }
}
