package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;


/**
 * This class determines the enemies reactions when the entity or the player are in darkness
 */
public class EnemyReactionsController extends Component {
  private GameTime gameTime;

  /**
   * Creates the animator and sets default animation.
   */
  @Override
  public void create() {
    gameTime = ServiceLocator.getTimeSource();
  }

  /**
   * Extracts the current target coordinates and changes the animation to face that direction. For example if the
   * target coordinates are to the north than we will see the npc back so update the animation to the back animation.
   */
  @Override
  public void update() {
    if (gameTime.isPaused()) {
      return;
    }
  }
}
