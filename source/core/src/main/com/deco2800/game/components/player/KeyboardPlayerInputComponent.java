package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean isPaused = false;  // variable for PAUSING in keyDown method

  public KeyboardPlayerInputComponent() {
    super(5);
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
      System.out.println("Mouse clicked");
    }
    return false;
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
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
      case Keys.SPACE:
        // If game is paused then player cannot attack
        if (!isPaused) {
          entity.getEvents().trigger("attack");
        }
        return true;
      case Input.Buttons.LEFT:
        System.out.println("mouse clicked");
      /*
      Pauses the game when ESC key is pressed, more like "freezes" the assets
      if anything because the ESC key just controls the time scale in the game from
      0f for "pause" and 1f for "resume".
       */
      case Keys.ESCAPE:
        if (isPaused) {
          System.out.println("Resume Game - setTimeScale to 1f");
          ServiceLocator.getTimeSource().setTimeScale(1f);
          isPaused = false;
        } else {
          System.out.println("Pause Game - setTimeScale to 0f");
          ServiceLocator.getTimeSource().setTimeScale(0f);
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
