package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.GameTime;
/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  // method requirement for player to execute long range attack
  private final Vector2 RangeAttack = Vector2.Zero.cpy();
  private IntSet downKeys = new IntSet(20);
  // Timing for dashing
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private static final int DelayLength = 2000; // in milliseconds
  private static final int InvincibilityLength = 400; // in milliseconds
  private long waitEndTime;

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

    if (keycode == Keys.D) {
      entity.getEvents().trigger("rangeAttack", Vector2Utils.RIGHT.cpy());
    } else if (keycode == Keys.A) {
      entity.getEvents().trigger("rangeAttack", Vector2Utils.LEFT.cpy());
    } else if (keycode == Keys.W) {
      entity.getEvents().trigger("rangeAttack", Vector2Utils.UP.cpy());
    } else if (keycode == Keys.S) {
      entity.getEvents().trigger("rangeAttack", Vector2Utils.DOWN.cpy());
    }

    switch (keycode) {
      case Keys.W:
        this.getEntity().getEvents().trigger("moveUp");
        walkDirection.add(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Keys.A:
        this.getEntity().getEvents().trigger("moveLeft");
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.S:
        this.getEntity().getEvents().trigger("moveDown");
        walkDirection.add(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Keys.D:
        this.getEntity().getEvents().trigger("moveRight");
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      case Keys.SHIFT_LEFT:
        if (canDash() && !walkDirection.isZero()) { // Check if player is allowed to dash again & moving
          waitEndTime = timeSource.getTime() + DelayLength; // Start timer for delay between dashes
          entity.getEvents().trigger("dash", InvincibilityLength);
        }
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("attack");
        return true;
      case Keys.L:
        entity.getEvents().trigger("rangeAttack", RangeAttack);
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
    if (downKeys.isEmpty()) {
      this.getEntity().getEvents().trigger("still");
    }
    switch (keycode) {
      case Keys.W:
        walkDirection.sub(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Keys.A:
        walkDirection.sub(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.S:
        walkDirection.sub(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Keys.D:
        walkDirection.sub(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      default:
        return false;
    }
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }

  /**
   * Lets you know whether the player is currently able to dash or still needs to wait
   *
   * @return a true or false as to whether the player can currently dash
   */
  public boolean canDash() {
    return (timeSource.getTime() >= waitEndTime);
  }
}
