package com.deco2800.game.components.npc;

import com.badlogic.gdx.audio.Sound;
import com.deco2800.game.components.Component;

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
      long soundId = dead.play();
      detectPlayer.setVolume(soundId, 0.6f);
    }
  }

  public void playDetectPlayer() {
    if (detectPlayer != null) {
      long soundId = detectPlayer.play();
      detectPlayer.setVolume(soundId, 0.1f);

    }
  }

  public void playShoot() {
    if (shoot != null) {
      long soundId = shoot.play();
      shoot.setVolume(soundId, 0.1f);
    }
  }

  public void playMeleeAttack() {
    if (meleeAttack != null) {
      meleeAttack.play();

    }
  }

  public void playSpawn() {
    if (spawn != null) {
      long soundId = spawn.play();
      spawn.setVolume(soundId, 0.3f);
    }
  }

  public void playHit() {
    if (hit != null) {
      long soundId = hit.play();
      hit.setVolume(soundId, 0.1f);
    }
  }
}
