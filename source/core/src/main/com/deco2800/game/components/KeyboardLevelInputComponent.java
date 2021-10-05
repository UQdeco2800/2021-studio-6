package com.deco2800.game.components;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.deco2800.game.components.shopmenu.ShopMenuDisplay;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardLevelInputComponent extends InputComponent {
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private ShopMenuDisplay shopMenu;

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
    shopMenu = entity.getComponent(ShopMenuDisplay.class);

    switch (keycode) {
      case Keys.ESCAPE:
        // if shop popup box is not visible, function as usual. If it is
        // ESCAPE key will function to close shop popup box instead
        if (timeSource != null && !shopMenu.checkShopPopupVisibility()) {
          entity.getEvents().trigger("togglepause");
        } else {
          shopMenu.toggleShopBox();
        }
        return true;
      default:
        return false;
    }
  }

  /**
   *
   */
}
