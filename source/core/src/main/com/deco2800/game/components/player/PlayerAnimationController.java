package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.items.Directions;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;


/**
 * This class listens to events relevant to a player's state and plays the animation when one
 * of the events is triggered. Animations are stored in arrays to allow indexing.
 */
public class PlayerAnimationController extends Component {
  AnimationRenderComponent animator;
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private static final long hurtDuration = 500;
  private boolean hurtActive = false;
  private long hurtTime;
  private Directions lastDirection;
  private String lastAnimation;
  private final String[][] animationsLeft = {{"left", "left-run"}, {"left-hurt", "left-run-hurt"}};
  private final String[][] animationsRight = {{"right", "right-run"}, {"right-hurt", "right-run-hurt"}};
  private final String[][] animationsUp = {{"back", "back-run"}, {"back-hurt", "back-run-hurt"}};
  private final String[][] animationsDown = {{"front", "front-run"}, {"front-hurt", "front-run-hurt"}};
  private final String[][][] animationNoArmor = {animationsLeft, animationsRight, animationsDown, animationsUp};
  private final String[][] animationsLeftHelmet = {{"left-helmet", "left-run-helmet"}, {"left-hurt", "left-run-hurt"}};
  private final String[][] animationsRightHelmet = {{"right-helmet", "right-run-helmet"}, {"right-hurt", "right-run-hurt"}};
  private final String[][] animationsUpHelmet = {{"back-helmet", "back-run-helmet"}, {"back-hurt", "back-run-hurt"}};
  private final String[][] animationsDownHelmet = {{"front-helmet", "front-run-helmet"}, {"front-hurt", "front-run-hurt"}};
  private final String[][][] animationHelmet = {animationsLeftHelmet, animationsRightHelmet, animationsDownHelmet, animationsUpHelmet};
  private final String[][] animationsLeftArmour = {{"left-armour", "left-run-armour"}, {"left-hurt", "left-run-hurt"}};
  private final String[][] animationsRightArmour = {{"right-armour", "right-run-armour"}, {"right-hurt", "right-run-hurt"}};
  private final String[][] animationsUpArmour = {{"back-armour", "back-run-armour"}, {"back-hurt", "back-run-hurt"}};
  private final String[][] animationsDownArmour = {{"front-armour", "front-run-armour"}, {"front-hurt", "front-run-hurt"}};
  private final String[][][] animationArmour = {animationsLeftArmour, animationsRightArmour, animationsDownArmour, animationsUpArmour};
  private final String[][][][] animations = {animationNoArmor, animationHelmet, animationArmour};

  /**
   * Sets up relevant event trigger for getting hurt and starts default animation.
   */
  @Override
  public void create() {
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("hurt",this::animateHurt);
    animator.startAnimation("front");
  }

  /**
   * Player animations are updated and checked each update call. Goes through a
   * number of if statements and checks to determine the correct index location for
   * the relevant player animation. This index system was used to reduce complexity,
   * as otherwise the function would require an obscene amount of nested if statements
   * and switches.
   *
   * Checks if the player is moving, if they are hurt and what type of armour they have on.
   * Also checks if the identical animation is already in progress.
   */
  @Override
  public void update() {
    if (timeSource.getTimeSince(hurtTime) >= hurtDuration) {
      hurtActive = false;
    }
    int idleIndex = 1;
    int hurtIndex = 0;
    int directIndex = 0;
    int armourIndex = 0;
    if (!entity.getComponent(PlayerActions.class).isMoving()) {
      idleIndex = 0;
    }
    if (hurtActive) {
      hurtIndex = 1;
    }
    switch (entity.getComponent(PlayerCombatStatsComponent.class).getDefenceLevel()) {
      case 1:
        hurtIndex = 0;
        armourIndex = 1;
        break;
      case 2:
        hurtIndex = 0;
        armourIndex = 2;
        break;
      default:
        break;
    }
    String anim;
    KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
    Directions direct = key.getDirection();
    switch (direct) {
      case MOVE_DOWN:
        directIndex = 2;
        break;
      case MOVE_LEFT:
        directIndex = 0;
        break;
      case MOVE_UP:
        directIndex = 3;
        break;
      case MOVE_RIGHT:
        directIndex = 1;
        break;
    }
    anim = animations[armourIndex][directIndex][hurtIndex][idleIndex];
    if (!anim.equals(lastAnimation)) {
      animator.startAnimation(anim);
      lastAnimation = anim;
    }
  }

  /**
   * Gets trigger when the player gets hurt and sets local variable for it.
   */
  void animateHurt() {
    hurtActive = true;
    hurtTime = timeSource.getTime();
  }
}
