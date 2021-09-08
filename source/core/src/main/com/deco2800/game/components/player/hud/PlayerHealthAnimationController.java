package com.deco2800.game.components.player.hud;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.rendering.IndependentAnimator;

public class PlayerHealthAnimationController  extends Component{

  private IndependentAnimator healthAnimator;
    private int index;

    @Override
    public void create() {
      super.create();
    }

    public void setter() {
      index = 1;
      PlayerStatsDisplay statsDisplayHealth = this.entity.getComponent(PlayerStatsDisplay.class);
      healthAnimator = statsDisplayHealth.getHealthAnimator();
      entity.getEvents().addListener("health", this::change);
      entity.getEvents().addListener("hit", this::hit);
      entity.getEvents().addListener("heal", this::heal);
      entity.getEvents().addListener("start", this::start);
    }

    void start() {
      healthAnimator.startAnimation("health1");
    }

  void change(int set) {
    System.out.println("change");
    healthAnimator.startAnimation("health" + set);
  }

    void hit(int damage) {
      index += damage;
      System.out.println("hit");
      healthAnimator.startAnimation("health" + index);
    }

  void heal() {
      index--;
      if (index < 1) {
        index = 1;
      }
    healthAnimator.startAnimation("health" + index);
   }
  }
