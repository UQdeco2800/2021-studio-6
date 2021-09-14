package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.IndependentAnimator;


public class PlayerWeaponAnimationController extends Component {

  private IndependentAnimator weaponAnimator;
  private PlayerMeleeAttackComponent meleeComponent;

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
  public void setter() {
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
    int directionMove = meleeComponent.getLastDirection();
    switch (directionMove) {
      case 1:
        weaponAnimator.setPositions(0.6f, -0.3f);
        weaponAnimator.startAnimation("attackUp");
        break;
      case 2:
        weaponAnimator.setPositions(0.7f, 0.6f);
        weaponAnimator.startAnimation("attackDown");
      break;
      case 3:
        weaponAnimator.setPositions(1.5f, 0.2f);
        weaponAnimator.startAnimation("attackLeft");
      break;
      case 4:
        weaponAnimator.setPositions(-0.2f, 0.2f);
        weaponAnimator.startAnimation("attackRight");
      break;
    }
  }

  void disposeAnimation() {
    weaponAnimator.dispose();
  }


}
