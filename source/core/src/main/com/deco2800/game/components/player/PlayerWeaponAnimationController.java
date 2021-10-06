package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.items.Directions;


public class PlayerWeaponAnimationController extends Component {

  private IndependentAnimator weaponAnimator;
  private PlayerMeleeAttackComponent meleeComponent;
  private Float[][] animationCords;
  private Vector2 offsets;

  @Override
  public void create() {
    super.create();
    offsets = new Vector2(0, 0);
  }

  /**
   * Setter has to be called after creation from wherever the Controller was
   * instanced to get around some annoying issues.
   *
   * Index is used to keep track of what health state the player is at.
   * @param cords coordinates
   */
  public void setter(Float[][] cords) {
    animationCords = cords;
    meleeComponent = this.entity.getComponent(PlayerMeleeAttackComponent.class);
    weaponAnimator = meleeComponent.getAnimator();
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("dispose", this::disposeAnimation);
  }

  /**
   * Checks to stop animation once it is finished
   */
  @Override
  public void update() {
    System.out.println(this.entity.getPosition());
    weaponAnimator.setCamera(false);
    weaponAnimator.setPositions(
            offsets.x + this.entity.getPosition().x - (this.entity.getScale().x / 2),
            offsets.y + this.entity.getPosition().y - (this.entity.getScale().y / 2));
    if (weaponAnimator.isFinished()) {
      weaponAnimator.stopAnimation();
    }
  }

  /**
   * Function gets triggered when the player attacks. Gets the direction of the last movement
   * to set which direction the attack is facing and which animation should be played.
   * Attack up should have a different z index so that it appears behind the player but still
   * in front of enemies and other objects.
   */
  void attack() {
    KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
    Directions direct = key.getDirection();
    weaponAnimator.setZIndex(10);
    switch (direct) {
      case MOVE_UP:
        weaponAnimator.setZIndex(-this.getEntity().getCenterPosition().y - 1); //Need to find the z index of player and enemy to adjust properly
//        weaponAnimator.setPositions(weaponAnimator.getPositions()[0] + animationCords[0][0], weaponAnimator.getPositions()[1] + animationCords[0][1]);
        offsets.set(animationCords[0][0], animationCords[0][1]);
        weaponAnimator.startAnimation("attackUp");
        break;
      case MOVE_DOWN:
//        weaponAnimator.setPositions(animationCords[1][0], animationCords[1][1] );
        offsets.set(animationCords[1][0], animationCords[1][1]);

        weaponAnimator.startAnimation("attackDown");
      break;
      case MOVE_LEFT:
//        weaponAnimator.setPositions(animationCords[2][0], animationCords[2][1]);
        offsets.set(animationCords[2][0], animationCords[2][1]);

        weaponAnimator.startAnimation("attackLeft");
      break;
      case MOVE_RIGHT:
//        weaponAnimator.setPositions(animationCords[3][0], animationCords[3][1]);
        offsets.set(animationCords[3][0], animationCords[3][1]);

        weaponAnimator.startAnimation("attackRight");
      break;
    }
  }

  void stop() {
    weaponAnimator.stopAnimation();
  }

  void disposeAnimation() {
    weaponAnimator.dispose();
  }

}
