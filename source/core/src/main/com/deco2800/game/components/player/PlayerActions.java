package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private PhysicsComponent physicsComponent;
  private PlayerMeleeAttackComponent playerMeleeAttackComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  // Speed Modification
  private Vector2 maxSpeed; // Metres per second
  private final float[] woundSpeeds = new float[] {0f, 5f, 4f, 3f}; // Dead, MW, LW, Healthy
  // Dashing
  private final float dashSpeed = 20f;
  private boolean dashing = false;
  private Vector2 dashVelocity;
  // Long Dashing
  private boolean longDashing = false;
  private long longDashEndTime = 0;
  // Timing for dashing
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private static final int delayLength = 2000; // in milliseconds
  private static final int dashLength = 100; // in milliseconds
  private long delayEndTime = 0;
  private long dashEndTime = 0;


  /**
   * Basic constructor for setting the initial player speed based on saved wound state
   *
   * @param woundState is the current wound state of player which affects the movement speed of player
   *                   in game world
   */
  public PlayerActions (int woundState) {
    setSpeed(woundState);
  }

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    playerMeleeAttackComponent = entity.getComponent(PlayerMeleeAttackComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("updateWound", this::setSpeed);
    entity.getEvents().addListener("dash", this::dash);
    entity.getEvents().addListener("longDash", this::longDash);
  }

  @Override
  public void update() {
    if (dashing) {
      updateDash(dashEndTime);
    } else if (longDashing) {
      updateDash(longDashEndTime);
    } else if (moving) {
      updateSpeed();
    }
  }

  /**
   * When player is set to moving the player will be moved in the key direction during update
   */
  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(maxSpeed);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  /**
   * When player is set to dashing, walking will be overwritten
   * Until the end of the dash time the player will be moved in the dash direction every update
   */
  private void updateDash(long endTime) {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = dashVelocity.cpy().sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    if (endTime <= timeSource.getTime()) { // stop dash at end of time
      dashing = false;
      this.longDashing = false;
      this.entity.getEvents().trigger("enableAttack");
    }
  }

  /**
   * Set the players speed. Allows values from 0 to 3 inclusive, values being set to the closest valid number when outside the range (same logic as playercombatstats component)
   *
   * @param woundState new woundState of the player
   */
  private void setSpeed(int woundState) {
    if (woundState <= 3 && woundState >=  0) {
      maxSpeed = new Vector2(woundSpeeds[woundState], woundSpeeds[woundState]);
    } else if (woundState > 3) {
      maxSpeed = new Vector2(woundSpeeds[3], woundSpeeds[3]);
    } else {
      maxSpeed = new Vector2(woundSpeeds[0], woundSpeeds[0]);
    }
  }

  /**
   * Lets you know whether the player is currently able to dash or still needs to wait
   *
   * @return a true or false as to whether the player can currently dash
   */
  public boolean canDash() {
    return (timeSource.getTime() >= delayEndTime);
  }

  /**
   * Lets you know whether the player is currently able to dash or still needs to wait
   *
   * @return long
   */
  public long getDelayTimeRemaining() {
    if ((delayEndTime-timeSource.getTime()) > 0) {
      return (delayEndTime-timeSource.getTime());
    } else {
      return 0;
    }
  }

  /**
   * Returns the whether the player is currently dashing
   *
   * @return whether the player is in the dashing state
   */
  public boolean isDashing() {
    return dashing;
  }

  /**
   * Returns the current max speed of the player
   *
   * @return the players current speed
   */
  public Vector2 getCurrentSpeed() {
    return maxSpeed;
  }

  /**
   * Sets the player to dashing if the current cooldown has passed
   */
  void dash() {
    if (canDash() && !walkDirection.isZero()) { // Check if player is allowed to dash again & moving
      delayEndTime = timeSource.getTime() + delayLength;
      dashEndTime = timeSource.getTime() + dashLength;
      if (walkDirection.cpy().x == 0f || walkDirection.cpy().y == 0f) { // Making dash length equal for all axis'
        dashVelocity = walkDirection.cpy().scl(dashSpeed);
      } else {
        dashVelocity = walkDirection.cpy().scl(dashSpeed/2, dashSpeed/2);
      }
      this.entity.getEvents().trigger("invincible", dashLength);
      this.entity.getEvents().trigger("disableAttack");
      this.dashing = true;
    }
  }

  /**
   * Sets the player to start a long dash
   * @param endTime is the time to end the dash according to the game clock
   */
  void longDash(long endTime) {
    longDashEndTime = endTime;
    if (walkDirection.cpy().x == 0f || walkDirection.cpy().y == 0f) { // Making dash length equal for all axis'
      dashVelocity = walkDirection.cpy().scl(dashSpeed*2);
    } else {
      dashVelocity = walkDirection.cpy().scl(dashSpeed, dashSpeed);
    }
    this.entity.getEvents().trigger("invincible", endTime-timeSource.getTime());
    this.entity.getEvents().trigger("disableAttack");
    this.longDashing = true;
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Vector2 direction) {
    this.walkDirection = direction;
    moving = true;
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
    playerMeleeAttackComponent.meleeAttackClicked(true);
  }
}