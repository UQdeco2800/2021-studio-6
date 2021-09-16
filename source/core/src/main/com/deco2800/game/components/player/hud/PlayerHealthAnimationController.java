package com.deco2800.game.components.player.hud;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.PlayerInterfaceDisplay;
import com.deco2800.game.rendering.IndependentAnimator;

public class PlayerHealthAnimationController  extends Component{

  private IndependentAnimator healthAnimator;
    private boolean hit;

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
      hit = false;
      PlayerInterfaceDisplay statsDisplayHealth = this.entity.getComponent(PlayerInterfaceDisplay.class);
      healthAnimator = statsDisplayHealth.getHealthAnimator();
      entity.getEvents().addListener("health", this::changeHealthAnimation);
      entity.getEvents().addListener("dispose", this::disposeAnimation);
      healthAnimator.startAnimation("health1");
    }

  /**
   * Single function that handles changing the health interface animation.
   * @param index The index of the animation to be set. Low index is higher health, high index is lower health.
   *              Calculated and triggered from the PlayerCombatStatsComponent.
   */
  void changeHealthAnimation(int index) {
      if (!hit) {
        entity.getEvents().trigger("firstHit");
      }
      hit = true;
    healthAnimator.startAnimation("health" + index);
  }

  void disposeAnimation() {
    healthAnimator.dispose();
  }

  }
