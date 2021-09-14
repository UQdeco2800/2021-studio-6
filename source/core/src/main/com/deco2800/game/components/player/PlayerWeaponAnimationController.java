package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.items.Directions;


public class PlayerWeaponAnimationController extends Component {

  private IndependentAnimator weaponAnimator;
  private PlayerMeleeAttackComponent meleeComponent;
  private Float[][] animationCords;

  @Override
  public void create() {
    super.create();
  }

  /**
   * Setter has to be called after creation from wherever the Controller was
   * instanced to get around some annoying issues.
   *
   * Index is used to keep track of what health state the player is at.
   */
  public void setter(Float[][] cords) {
    animationCords = cords;
    meleeComponent = this.entity.getComponent(PlayerMeleeAttackComponent.class);
    weaponAnimator = meleeComponent.getAnimator();
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("dispose", this::disposeAnimation);
  }

  @Override
  public void update() {
    if (weaponAnimator.isFinished()) {
      weaponAnimator.stopAnimation();
    }
  }

  void attack() {
    KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
    Directions direct = key.getDirection();
    switch (direct) {
      case MOVE_UP:
        //Change Z index at some point
        weaponAnimator.setPositions(animationCords[0][0], animationCords[0][1]);
        weaponAnimator.startAnimation("attackUp");
        break;
      case MOVE_DOWN:
        weaponAnimator.setPositions(animationCords[1][0], animationCords[1][1] );
        weaponAnimator.startAnimation("attackDown");
      break;
      case MOVE_LEFT:
        weaponAnimator.setPositions(animationCords[2][0], animationCords[2][1]);
        weaponAnimator.startAnimation("attackLeft");
      break;
      case MOVE_RIGHT:
        weaponAnimator.setPositions(animationCords[3][0], animationCords[3][1]);
        weaponAnimator.startAnimation("attackRight");
      break;
    }
  }

  void stop() {
    weaponAnimator.stopAnimation();
    //disposeAnimation();
  }

  void disposeAnimation() {
    weaponAnimator.dispose();
  }

}
