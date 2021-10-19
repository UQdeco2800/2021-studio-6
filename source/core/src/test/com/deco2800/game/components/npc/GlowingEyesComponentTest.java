package com.deco2800.game.components.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.deco2800.game.components.DarknessDetectionComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class GlowingEyesComponentTest {
  @Mock
  DarknessDetectionComponent darknessDetectionComponent;
  @Mock
  SpriteBatch spriteBatch;

  Entity npc;
  GlowingEyesComponent glowingEyesComponent;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerTimeSource(new GameTime());
    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("images/Enemy_Assets/SmallEnemy/small_enemy.atlas"));

    npc = new Entity();
    npc.addComponent(new GlowingEyesComponent(textureAtlas));
    npc.addComponent(darknessDetectionComponent);

    glowingEyesComponent = npc.getComponent(GlowingEyesComponent.class);

  }

  @Test
  void startInLightTest() {
    when(darknessDetectionComponent.isInLight()).thenReturn(true);
    glowingEyesComponent.create();
    glowingEyesComponent.startAnimation("front-glow");

    glowingEyesComponent.draw(spriteBatch);
    verify(spriteBatch, Mockito.times(0)).draw(any(TextureRegion.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());
  }

  @Test
  void startInDarkTest() {
    when(darknessDetectionComponent.isInLight()).thenReturn(false);
    glowingEyesComponent.create();
    glowingEyesComponent.startAnimation("front-glow");

    glowingEyesComponent.draw(spriteBatch);
    verify(spriteBatch, Mockito.times(1)).draw(any(TextureRegion.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());
  }

  @Test
  void displayOffAndOnTest() {
    when(darknessDetectionComponent.isInLight()).thenReturn(false);
    glowingEyesComponent.create();
    glowingEyesComponent.startAnimation("front-glow");
    glowingEyesComponent.displayOff();

    glowingEyesComponent.draw(spriteBatch);
    verify(spriteBatch, Mockito.times(0)).draw(any(TextureRegion.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());

    glowingEyesComponent.displayOn();

    glowingEyesComponent.draw(spriteBatch);
    verify(spriteBatch, Mockito.times(1)).draw(any(TextureRegion.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());

  }

  @Test
  void deactivateTest() {
    when(darknessDetectionComponent.isInLight()).thenReturn(false);
    glowingEyesComponent.create();
    glowingEyesComponent.startAnimation("front-glow");
    glowingEyesComponent.deactivate();

    glowingEyesComponent.draw(spriteBatch);
    verify(spriteBatch, Mockito.times(0)).draw(any(TextureRegion.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());

    glowingEyesComponent.displayOn();
    glowingEyesComponent.draw(spriteBatch);
    verify(spriteBatch, Mockito.times(0)).draw(any(TextureRegion.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());

  }
}