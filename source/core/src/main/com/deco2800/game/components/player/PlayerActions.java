package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  // Speed Modification
  private Vector2 MaxSpeed; // Metres per second
  private final float[] woundSpeeds = new float[] {0f, 5f, 4f, 3f}; // Dead, MW, LW, Healthy

  private PhysicsComponent physicsComponent;
  private PlayerMeleeAttackComponent playerMeleeAttackComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;


  /**
   * Basic constructor for setting the initial player speed based on saved wound state
   */
  public PlayerActions (int woundState) {
    MaxSpeed = new Vector2((float) woundState, (float) woundState);
  }

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    playerMeleeAttackComponent = entity.getComponent(PlayerMeleeAttackComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("updateWound", this::speedModification);
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MaxSpeed);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  /**
   * Alters the players speed whenever wound state changes.
   *
   * @param woundState new woundState of the player
   */
  void speedModification(int woundState) {
    MaxSpeed = new Vector2(woundSpeeds[woundState], woundSpeeds[woundState]);
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
