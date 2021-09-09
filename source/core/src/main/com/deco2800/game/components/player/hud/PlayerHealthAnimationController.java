package com.deco2800.game.components.player.hud;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.rendering.IndependentAnimator;

public class PlayerHealthAnimationController  extends Component{

  private IndependentAnimator healthAnimator;
    private int index;
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
      index = 1;
      PlayerStatsDisplay statsDisplayHealth = this.entity.getComponent(PlayerStatsDisplay.class);
      healthAnimator = statsDisplayHealth.getHealthAnimator();
      entity.getEvents().addListener("health", this::change);
      entity.getEvents().addListener("hit", this::hit);
      entity.getEvents().addListener("heal", this::heal);
      entity.getEvents().addListener("dispose", this::disposeAnimation);
      healthAnimator.startAnimation("health1");
    }


  void change(int set) {
      if (!hit) {
        entity.getEvents().trigger("firstHit");
      }
      hit = true;
    healthAnimator.startAnimation("health" + set);
  }

    void hit(int damage) {
      index += damage;
      healthAnimator.startAnimation("health" + index);
    }

  void heal() {
      index--;
      if (index < 1) {
        index = 1;
      }
    healthAnimator.startAnimation("health" + index);
   }


  void disposeAnimation() {
    healthAnimator.dispose();
  }

  }
