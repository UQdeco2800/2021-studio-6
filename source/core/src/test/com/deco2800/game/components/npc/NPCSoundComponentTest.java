package com.deco2800.game.components.npc;

import com.badlogic.gdx.audio.Sound;
import org.junit.jupiter.api.Test;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class NPCSoundComponentTest {
  @Mock
  Sound sound;

  private NPCSoundComponent soundComponent;

  @BeforeEach
  void beforeEach() {
    soundComponent = new NPCSoundComponent();
  }

  @Test
  void playDead() {
    soundComponent.setDead(sound);
    soundComponent.playDead();
    verify(sound, Mockito.times(1)).play();
  }

  @Test
  void playDetectPlayer() {
    soundComponent.setDetectPlayer(sound);
    soundComponent.playDetectPlayer();
    verify(sound, Mockito.times(1)).play();
  }

  @Test
  void playShoot() {
    soundComponent.setShoot(sound);
    soundComponent.playShoot();
    verify(sound, Mockito.times(1)).play();
  }

  @Test
  void playMeleeAttack() {
    soundComponent.setMeleeAttack(sound);
    soundComponent.playMeleeAttack();
    verify(sound, Mockito.times(1)).play();
  }

  @Test
  void playSpawn() {
    soundComponent.setSpawn(sound);
    soundComponent.playSpawn();
    verify(sound, Mockito.times(1)).play();
  }

  @Test
  void playHit() {
    soundComponent.setHit(sound);
    soundComponent.playHit();
    verify(sound, Mockito.times(1)).play();
  }
}