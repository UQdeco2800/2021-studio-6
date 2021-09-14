package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.items.Directions;


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
    KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
    Directions direct = key.getDirection();
    switch (direct) {
      case MOVE_UP:
        weaponAnimator.setPositions(0.6f, -0.3f);
        weaponAnimator.startAnimation("attackUp");
        break;
      case MOVE_DOWN:
        weaponAnimator.setPositions(0.7f, 0.6f);
        weaponAnimator.startAnimation("attackDown");
      break;
      case MOVE_LEFT:
        weaponAnimator.setPositions(1.5f, 0.2f);
        weaponAnimator.startAnimation("attackLeft");
      break;
      case MOVE_RIGHT:
        weaponAnimator.setPositions(-0.2f, 0.2f);
        weaponAnimator.startAnimation("attackRight");
      break;
    }
  }

  void disposeAnimation() {
    weaponAnimator.dispose();
  }


}
