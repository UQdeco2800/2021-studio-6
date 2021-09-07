package com.deco2800.game.components;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardLevelInputComponent extends InputComponent {
  private final GameTime timeSource = ServiceLocator.getTimeSource();

  public KeyboardLevelInputComponent() {
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
    switch (keycode) {
      case Keys.ESCAPE:
        if (timeSource != null && !timeSource.isPaused()) {
          entity.getEvents().trigger("togglepause");
        }
        return true;
      default:
        return false;
    }
  }
}
