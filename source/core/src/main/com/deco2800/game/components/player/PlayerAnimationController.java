package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.items.Directions;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;


/**
 * This class listens to events relevant to a player's state and plays the animation when one
 * of the events is triggered. Currently not implemented as the animations do
 * not yet exist. Can be expanded later on to include other animation events.
 */
public class PlayerAnimationController extends Component {
  AnimationRenderComponent animator;
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private static final long hurtDuration = 1000;
  private boolean hurtActive = false;
  private long hurtTime;
  private Directions lastDirection;
  private String lastAnimation;
  private final String[][] animationsLeft = {{"left", "left-run"}, {"left-hurt", "left-run-hurt"}};
  private final String[][] animationsRight = {{"right", "right-run"}, {"right-hurt", "right-run-hurt"}};
  private final String[][] animationsUp = {{"back", "back-run"}, {"back-hurt", "back-run-hurt"}};
  private final String[][] animationsDown = {{"front", "front-run"}, {"front-hurt", "front-run-hurt"}};
  private final String[][][] animationNoArmor = {animationsLeft, animationsRight, animationsDown, animationsUp};

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("hurt",this::animateHurt);
    lastDirection = Directions.MOVE_DOWN;
    animator.startAnimation("front");
  }

  @Override
  public void update() {
    if (timeSource.getTimeSince(hurtTime) >= hurtDuration) {
      hurtActive = false;
    }
    int idleIndex = 1;
    int hurtIndex = 0;
    int directIndex = 0;
    if (!entity.getComponent(PlayerActions.class).isMoving()) {
      idleIndex = 0;
      lastDirection = Directions.IDLE;
    }
    if (hurtActive) {
      hurtIndex = 1;
    }
    String anim;
    KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
    Directions direct = key.getDirection();
    switch (direct) {
      case MOVE_DOWN:
        directIndex = 2;
        lastDirection = direct;
        break;
      case MOVE_LEFT:
        directIndex = 0;
        lastDirection = direct;
        break;
      case MOVE_UP:
        directIndex = 3;
        lastDirection = direct;
        break;
      case MOVE_RIGHT:
        directIndex = 1;
        lastDirection = direct;
        break;
    }
    anim = animationNoArmor[directIndex][hurtIndex][idleIndex];
    if (!anim.equals(lastAnimation)) {
      animator.startAnimation(anim);
      lastAnimation = anim;
    }
  }

  void animateHurt() {
    hurtActive = true;
    hurtTime = timeSource.getTime() + hurtDuration;
  }
}
