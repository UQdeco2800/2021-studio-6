package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.items.Directions;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import java.util.ArrayList;


/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  public Vector2 walkDirection = Vector2.Zero.cpy();
  // Method requirement for player to execute long range attack
  private final Vector2 RangeAttack = Vector2.Zero.cpy();
  //private final ArrayList<Integer> movementKeys = new ArrayList<>();
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  // Variable for allowing attacks
  private boolean canAttack = true;
  private boolean canDashAttack = true;
  private final ArrayList<Directions> movementDirections = new ArrayList<>();
  private Directions lastDirection = Directions.MOVE_DOWN;
  // bug where if player presses movement keys before game initiates - player will moving in the opposite direction
  // of the key pressed prior to game starting. Tracking if each key has been clicked would be the temporary solution
  private boolean up, down, left, right = false;

  public KeyboardPlayerInputComponent() {
    super(5);
  }

  /**
   * Adding relevant listeners to the component
   */
  @Override
  public void create() {
    super.create();
    entity.getEvents().addListener("enableAttack", this::enableAttack);
    entity.getEvents().addListener("disableAttack", this::disableAttack);
    entity.getEvents().addListener("enableDashAttack", this::enableDashAttack);
    entity.getEvents().addListener("disableDashAttack", this::disableDashAttack);
    entity.getEvents().addListener("resetPlayerMovements", this::resetConditions);
  }

  /**
   * This is used to reset all checking conditions for player movement. This method is used
   * purely to bugs from arising due player pressing movement keys before or after game has been paused or started
   */
  private void resetConditions() {
    up = false;
    down = false;
    left = false;
    right = false;
    walkDirection = Vector2.Zero.cpy();
    entity.getEvents().trigger("walkStop");
  }

  /**
   * Triggers player events on specific keycodes.
   * Split up into 2 separate if statements that depend on other variables to reduce complexity.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    if (timeSource == null || !timeSource.isPaused()) {
      switch (keycode) {
        case Keys.W:
          walkDirection.add(Vector2Utils.UP);
          up = true;
          triggerWalkEvent();
          movementDirections.add(Directions.MOVE_UP);
          return true;
        case Keys.A:
          walkDirection.add(Vector2Utils.LEFT);
          left = true;
          triggerWalkEvent();
          movementDirections.add(Directions.MOVE_LEFT);
          return true;
        case Keys.S:
          walkDirection.add(Vector2Utils.DOWN);
          down = true;
          triggerWalkEvent();
          movementDirections.add(Directions.MOVE_DOWN);
          return true;
        case Keys.D:
          walkDirection.add(Vector2Utils.RIGHT);
          right = true;
          triggerWalkEvent();
          movementDirections.add(Directions.MOVE_RIGHT);
          return true;
        case Keys.R:
          entity.getEvents().trigger("reload");
          return true;
        case Keys.SHIFT_LEFT:
          entity.getEvents().trigger("dash");
          return true;
      }
    }

    if ((timeSource == null || !timeSource.isPaused()) && canAttack && canDashAttack) {
      switch (keycode) {
        //case Keys.G:
          //entity.getEvents().trigger("rangeAOE", RangeAttack);
          //return true;
        case Keys.SPACE:
          entity.getEvents().trigger("attackStart");
          return true;
        case Keys.ENTER:
          entity.getEvents().trigger("rangeAttack");
          return true;
        case Keys.Q:
          entity.getEvents().trigger("tryAbility", walkDirection);
          return true;
        case Keys.NUM_1:
          entity.getEvents().trigger("useBandage");
          return true;
        default:
          return false;
      }
    }
    return false;
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
    if (timeSource == null || !timeSource.isPaused() && (up || down || left || right)) {
      switch (keycode) {
        case Keys.W:
          removeMovementKey(Directions.MOVE_UP);
          walkDirection.sub(Vector2Utils.UP);
          triggerWalkEvent();
          up = false;
          return true;
        case Keys.A:
          removeMovementKey(Directions.MOVE_LEFT);
          walkDirection.sub(Vector2Utils.LEFT);
          triggerWalkEvent();
          left = false;
          return true;
        case Keys.S:
          removeMovementKey(Directions.MOVE_DOWN);
          walkDirection.sub(Vector2Utils.DOWN);
          triggerWalkEvent();
          down = false;
          return true;
        case Keys.D:
          removeMovementKey(Directions.MOVE_RIGHT);
          walkDirection.sub(Vector2Utils.RIGHT);
          triggerWalkEvent();
          return true;
        default:
          return false;
      }
    }
    return false;
  }

  /**
   * Removes the necessary movement key from the list of current keys
   *
   * @param direction the direction to remove from movementKeys
   */
  private void removeMovementKey(Directions direction) {
    movementDirections.removeIf(movementKey -> movementKey == direction);
  }

  /**
   * Triggers to the movement controller the players current walking state and direction
   */
  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else if (up || down || right || left) {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }

  /**
   * Sets the player able to attack
   */
  void enableAttack() {
    this.canAttack = true;
  }

  /**
   * Sets the player to be unable to attack
   */
  void disableAttack() {
    this.canAttack = false;
  }


  /**
   * Sets the player able to attack
   */
  void enableDashAttack() {
    this.canDashAttack = true;
  }

  /**
   * Sets the player to be unable to attack
   */
  void disableDashAttack() {
    this.canDashAttack = false;
  }

  /**
   * Gets the direction the player is currently walking in
   * @return the players current walking direction
   */
  public Vector2 getVector() {
    return walkDirection;
  }

  /**
   * Getting the last direction the player moved in
   * @return the direction enum that represents the last direction the player moved in
   */
  public Directions getDirection() {
    if (movementDirections.size() > 0) {
      lastDirection = movementDirections.get(movementDirections.size() - 1);
      return movementDirections.get(movementDirections.size() - 1);
    }
    return lastDirection;
  }
}

