package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
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
  public final Vector2 walkDirection = Vector2.Zero.cpy();
  // Method requirement for player to execute long range attack
  private final Vector2 RangeAttack = Vector2.Zero.cpy();
  private final IntSet downKeys = new IntSet(20);
  //private final ArrayList<Integer> movementKeys = new ArrayList<>();
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  // Variable for allowing attacks
  private boolean canAttack = true;
  private boolean canDashAttack = true;
  private final ArrayList<Directions> movementDirections = new ArrayList<>();
  private Directions lastDirection = Directions.MOVE_DOWN;

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
    downKeys.add(keycode);
    int numKeysPressed = downKeys.size;

    if (timeSource == null || !timeSource.isPaused()) {
      switch (keycode) {
        case Keys.W:
          entity.getEvents().trigger("rangeAttack", Vector2Utils.UP.cpy());
          walkDirection.add(Vector2Utils.UP);
          triggerWalkEvent();
          movementDirections.add(Directions.MOVE_UP);
          return true;
        case Keys.A:
          entity.getEvents().trigger("rangeAttack", Vector2Utils.LEFT.cpy());
          walkDirection.add(Vector2Utils.LEFT);
          triggerWalkEvent();
          movementDirections.add(Directions.MOVE_LEFT);
          return true;
        case Keys.S:
          entity.getEvents().trigger("rangeAttack", Vector2Utils.DOWN.cpy());
          walkDirection.add(Vector2Utils.DOWN);
          triggerWalkEvent();
          movementDirections.add(Directions.MOVE_DOWN);
          return true;
        case Keys.D:
          entity.getEvents().trigger("rangeAttack", Vector2Utils.RIGHT.cpy());
          walkDirection.add(Vector2Utils.RIGHT);
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
        case Keys.SPACE:
            entity.getEvents().trigger("attackStart");
          return true;
        case Keys.ENTER:
            entity.getEvents().trigger("rangeAttack", RangeAttack);
          return true;
        case Keys.E:
            entity.getEvents().trigger("tryAbility", walkDirection);
          return true;
        case Keys.NUM_1:
            entity.getEvents().trigger("useBandage");
          return true;
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
    downKeys.remove(keycode);
    switch (keycode) {
      case Keys.W:
        removeMovementKey(Directions.MOVE_UP);
        walkDirection.sub(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Keys.A:
        removeMovementKey(Directions.MOVE_LEFT);
        walkDirection.sub(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.S:
        removeMovementKey(Directions.MOVE_DOWN);
        walkDirection.sub(Vector2Utils.DOWN);
        triggerWalkEvent();
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
    } else {
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

