package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private PhysicsComponent physicsComponent;
  private PlayerMeleeAttackComponent playerMeleeAttackComponent;
  private PlayerRangeAttackComponent playerRangeAttackComponent;
  private InventoryComponent inventory;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  // Speed Modification
  private Vector2 maxSpeed; // Metres per second
  private final float[] woundSpeeds = new float[] {0f, 5f, 4f, 3f}; // Dead, MW, LW, Healthy
  // Dashing
  private final float dashSpeed = 20f;
  private boolean dashing = false;
  private Vector2 dashVelocity;
  // Timing for dashing
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private static final int delayLength = 2000; // in milliseconds
  private static final int dashLength = 100; // in milliseconds
  private long delayEndTime = 0;
  private long dashEndTime = 0;
  // Timing for reloading along with additional variables relevant to reloading
  private static final int delayReloadLength = 3000; // in milliseconds
  private final int BULLET_MAGAZINE_FULL = 5;
  private int ammoToReload;

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
    entity.getEvents().addListener("dash", this::regularDash);
    entity.getEvents().addListener("longDash", this::longDash);
    entity.getEvents().addListener("reload", this::reload);
  }

  @Override
  public void update() {
    if (dashing) {
      updateDash();
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
  private void updateDash() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = dashVelocity.cpy().sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    if (dashEndTime <= timeSource.getTime()) { // stop dash at end of time
      dashing = false;
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
  void regularDash() {
    if (canDash() && !walkDirection.isZero()) { // Check if player is allowed to dash again & moving
      delayEndTime = timeSource.getTime() + delayLength;
      dashEndTime = timeSource.getTime() + dashLength;
      setDash(dashSpeed);
    }
  }

  /**
   * Sets the player to start a long dash
   * @param endTime is the time to end the dash according to the game clock
   */
  void longDash(long endTime) {
    dashEndTime = endTime;
    setDash(dashSpeed*2);
  }

  /**
   * Reloads player's range attack weapon for firing again
   */
  void reload() {
    inventory = entity.getComponent(InventoryComponent.class);
    playerRangeAttackComponent = entity.getComponent(PlayerRangeAttackComponent.class);
    boolean reloadingStatus = playerRangeAttackComponent.getReloadingStatus();

    // used to check number of ammo required from inventory for deduction
    int magazineNum = playerRangeAttackComponent.getGunMagazine();
    int ammoLeft = inventory.getAmmo();

    Timer reloadTimer = new Timer(true);
    // if bullet magazine is full or reloading is currently occurring or there is no ammo, do nothing
    if (magazineNum != BULLET_MAGAZINE_FULL && !reloadingStatus && ammoLeft >= 1) {
      playerRangeAttackComponent.setReloadingStatus(true);

      // acquire ammo to reload and update ammo in inventory
      ammoToReload = getAmmo(magazineNum, ammoLeft, inventory);

      // simulate a reloading period
      reloadTimer.schedule( new TimerTask() {
        public void run() {
          playerRangeAttackComponent.reloadGunMagazine(ammoToReload);
          cancel();
        }
      }, delayReloadLength);
    }
  }

  /**
   * Gets ammo for reloading and updates ammo in player's inventory component
   *
   * @param magazineNum which entails the current number of bullets left in player's magazine upon
   *                    reloading
   * @param ammoLeft informs the number of ammo left in inventory of player currently
   * @param inventory component that is attached to player that has information on ammo count and methods
   *                  for updating ammo account
   * @return ammo for reloading that will be scheduled for reloading
   */
  public int getAmmo(int magazineNum, int ammoLeft, InventoryComponent inventory) {
    // magazine empty and there is enough ammo in inventory
    if (magazineNum == 0 && ammoLeft > 5) {
      ammoToReload = BULLET_MAGAZINE_FULL;
      inventory.addAmmo(-ammoToReload);
    } else {

      // if there is enough ammo to refill sub-empty magazine, refill magazine to fullest -
      // else there isn't enough, empty ammo inventory and take whatever that is left
      ammoToReload = BULLET_MAGAZINE_FULL - magazineNum;
      if (inventory.hasAmmo(ammoToReload)) {
        inventory.addAmmo(-ammoToReload);
      } else {
        ammoToReload = ammoLeft;
        inventory.setAmmo(0);
      }
    }
    return ammoToReload;
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

  /**
   * Sets the players dash velocity based on direction and speed
   * @param mainSpeed the horizontal/vertical speed of the dash
   */
  private void setDash(float mainSpeed) {
    if (walkDirection.cpy().x == 0f || walkDirection.cpy().y == 0f) { // Making dash length equal for all axis'
      dashVelocity = walkDirection.cpy().scl(mainSpeed);
    } else {
      dashVelocity = walkDirection.cpy().scl(mainSpeed/2, mainSpeed/2);
    }
    this.entity.getEvents().trigger("invincible", dashEndTime - timeSource.getTime());
    this.entity.getEvents().trigger("disableAttack");
    this.dashing = true;
  }
}