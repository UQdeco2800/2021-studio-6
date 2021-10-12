package com.deco2800.game.components.npc;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.tasks.DeadTask;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * This component controls the sound effects for the NPC entity
 */
public class NPCSoundComponent extends Component {
  private Sound hit;
  private Sound dead;
  private Sound detectPlayer;
  private Sound shoot;
  private Sound meleeAttack;
  private Sound spawn;

  public void setHit(Sound hit) {
    this.hit = hit;
  }

  public void setDead(Sound dead) {
    this.dead = dead;
  }

  public void setDetectPlayer(Sound detectPlayer) {
    this.detectPlayer = detectPlayer;
  }

  public void setShoot(Sound shoot) {
    this.shoot = shoot;
  }

  public void setMeleeAttack(Sound meleeAttack) {
    this.meleeAttack = meleeAttack;
  }

  public void setSpawn(Sound spawn) {
    this.spawn = spawn;
  }

  public void playDead() {
    if (dead != null) {
      dead.play();
    }
  }

  public void playDetectPlayer() {
    if (detectPlayer != null) {
      detectPlayer.play();
    }
  }

  public void playShoot() {
    if (shoot != null) {
      shoot.play();
    }
  }

  public void playMeleeAttack() {
    if (meleeAttack != null) {
      meleeAttack.play();
    }
  }

  public void playSpawn() {
    if (spawn != null) {
      spawn.play();
    }
  }

  public void playHit() {
    if (hit != null) {
      hit.play();
    }
  }
}
