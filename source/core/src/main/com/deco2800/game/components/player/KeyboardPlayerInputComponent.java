package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.GameTime;
/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean isPaused = false;  // variable for PAUSING in keyDown method
  // method requirement for player to execute long range attack
  private final Vector2 RANGE_ATTACK = Vector2.Zero.cpy();
  private IntSet downKeys = new IntSet(20);
  // Timing for dashing
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private final int DELAY_LENGTH = 2000; // in milliseconds
  private final int INVINCIBILITY_LENGTH = 400; // in milliseconds
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

    if (!isPaused) {
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
          walkDirection.add(Vector2Utils.UP);
          triggerWalkEvent();
          return true;
        case Keys.A:
          walkDirection.add(Vector2Utils.LEFT);
          triggerWalkEvent();
          return true;
        case Keys.S:
          walkDirection.add(Vector2Utils.DOWN);
          triggerWalkEvent();
          return true;
        case Keys.D:
          walkDirection.add(Vector2Utils.RIGHT);
          triggerWalkEvent();
          return true;
        case Keys.SHIFT_LEFT:
          if (!isPaused) {
            if (timeSource.getTime() >= waitEndTime) { // Check if player is allowed to dash again
              waitEndTime = timeSource.getTime() + DELAY_LENGTH; // Start timer for delay between dashes
              entity.getEvents().trigger("dash", INVINCIBILITY_LENGTH);
            }
          }
          return true;
        case Keys.SPACE:
          // If game is paused then player cannot attack
          if (!isPaused) {
            entity.getEvents().trigger("attack");
          }
          return true;
        case Keys.L:
          if (!isPaused) {
            entity.getEvents().trigger("rangeAttack", RANGE_ATTACK);
          }
          return true;
//      case Input.Buttons.LEFT:
//        System.out.println("mouse clicked");
      /*
      Pauses the game when ESC key is pressed, more like "freezes" the assets
      if anything because the ESC key just controls the time scale in the game from
      0f for "pause" and 1f for "resume".
       */
        case Keys.ESCAPE:
          if (isPaused) {
            timeSource.setTimeScale(1f);
            isPaused = false;
          } else {
            timeSource.setTimeScale(0f);
            isPaused = true;
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
}
