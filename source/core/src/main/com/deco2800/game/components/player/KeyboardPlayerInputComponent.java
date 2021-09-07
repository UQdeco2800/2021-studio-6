package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import com.deco2800.game.input.InputComponent;
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
  private final ArrayList<Integer> movementKeys = new ArrayList<>();
  private final GameTime timeSource = ServiceLocator.getTimeSource();

  public KeyboardPlayerInputComponent() {
    super(5);
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    downKeys.add(keycode);
    int numKeysPressed = downKeys.size;

    if (timeSource == null || !timeSource.isPaused()) {
      if (keycode == Keys.D) {
        entity.getEvents().trigger("rangeAttack", Vector2Utils.RIGHT.cpy());
      } else if (keycode == Keys.A) {
        entity.getEvents().trigger("rangeAttack", Vector2Utils.LEFT.cpy());
      } else if (keycode == Keys.W) {
        entity.getEvents().trigger("rangeAttack", Vector2Utils.UP.cpy());
      } else if (keycode == Keys.S) {
        entity.getEvents().trigger("rangeAttack", Vector2Utils.DOWN.cpy());
      }
    }

    switch (keycode) {
      case Keys.W:
        movementKeys.add(keycode);
        walkDirection.add(Vector2Utils.UP);
        triggerWalkEvent();
        animationHandle();
        return true;
      case Keys.A:
        movementKeys.add(keycode);
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        animationHandle();
        return true;
      case Keys.S:
        movementKeys.add(keycode);
        walkDirection.add(Vector2Utils.DOWN);
        triggerWalkEvent();
        animationHandle();
        return true;
      case Keys.D:
        movementKeys.add(keycode);
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        animationHandle();
        return true;
      case Keys.SHIFT_LEFT:
        if (timeSource == null || !timeSource.isPaused()) {
          entity.getEvents().trigger("dash");
        }
        return true;
      case Keys.SPACE:
        if ((timeSource == null || !timeSource.isPaused()) && !this.entity.getComponent(PlayerActions.class).isDashing()) {
          entity.getEvents().trigger("attack");
        }
        return true;
      case Keys.ENTER:
        if ((timeSource == null || !timeSource.isPaused()) && !this.entity.getComponent(PlayerActions.class).isDashing()) {
          entity.getEvents().trigger("rangeAttack", RangeAttack);
        }
        return true;
      default:
        return false;
    }
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
        removeMovementKey(keycode);
        walkDirection.sub(Vector2Utils.UP);

        triggerWalkEvent();
        animationHandle();
        return true;
      case Keys.A:
        removeMovementKey(keycode);
        walkDirection.sub(Vector2Utils.LEFT);
        triggerWalkEvent();
        animationHandle();
        return true;
      case Keys.S:
        removeMovementKey(keycode);
        walkDirection.sub(Vector2Utils.DOWN);
        triggerWalkEvent();
        animationHandle();
        return true;
      case Keys.D:
        removeMovementKey(keycode);
        walkDirection.sub(Vector2Utils.RIGHT);
        triggerWalkEvent();
        animationHandle();
        return true;
      default:
        return false;
    }

  }

  /**
   * Function to handle trigger the required player animation. Gets called after
   * each change to player movement i.e. keydown or keyup.
   */
  private void animationHandle() {
    if (movementKeys.size() > 0) {
      switch (movementKeys.get(movementKeys.size() - 1)) {
        case Keys.W:
          this.getEntity().getEvents().trigger("moveUp");
          break;
        case Keys.A:
          this.getEntity().getEvents().trigger("moveLeft");
          break;
        case Keys.S:
          this.getEntity().getEvents().trigger("moveDown");
          break;
        case Keys.D:
          this.getEntity().getEvents().trigger("moveRight");
          break;
      }
    }
  }

  /**
   * Removes the necessary movement key from the list of current keys
   *
   * @param keycode the keycode to remove from movementKeys
   */
  private void removeMovementKey(int keycode) {
    movementKeys.removeIf(movementKey -> movementKey == keycode);
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }
}
