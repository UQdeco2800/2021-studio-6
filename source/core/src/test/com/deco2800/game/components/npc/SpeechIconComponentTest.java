package com.deco2800.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ResourceService;
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
class SpeechIconComponentTest {
  @Mock SpriteBatch spriteBatch;

  // Should equal the ANIMATION_PER_TICK constant
  private static final float ANIMATION_PER_TICK = 0.003f;
  // Should equal the ANIMATION_MIN_RATIO constant
  private static final float ANIMATION_MIN_RATIO = 0.2f;
  private static final float NPC_X_POS = 3;
  private static final float NPC_Y_POS = 10;
  private static final float NPC_X_SCALE = 20;
  private static final float NPC_Y_SCALE = 30;
  private static final float SPEECH_Y_OFFSET = 100f;
  private static final float CALCULATE_NEXT_Y_OFFSET = NPC_Y_POS + SPEECH_Y_OFFSET - ANIMATION_MIN_RATIO * ANIMATION_PER_TICK;
  private SpeechIconComponent speechIconComponent;
  private Texture speechIconTexture;

  @BeforeEach
  void beforeEach() {
    // Setup resource locator to load speech bubble texture
    String[] speechTextures = {"images/dialogue/raw/npc_indicator.png"};
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.getResourceService().loadTextures(speechTextures);
    ServiceLocator.getResourceService().loadAll();
    speechIconTexture = ServiceLocator.getResourceService().getAsset("images/dialogue/raw/npc_indicator.png", Texture.class);

    // Create new entity with speech icon component and sample values
    speechIconComponent = new SpeechIconComponent(SPEECH_Y_OFFSET);
    Entity npc = new Entity()
        .addComponent(speechIconComponent);

    // Set some dummy position and scale values
    npc.setPosition(new Vector2(NPC_X_POS, NPC_Y_POS));
    npc.setScale(new Vector2(NPC_X_SCALE, NPC_Y_SCALE));
  }

  @Test
  void displayOffTest() {
    speechIconComponent.displayOff();
    speechIconComponent.draw(spriteBatch);

    // Verify that the draw method was never called
    verify(spriteBatch, Mockito.never()).draw(
        any(Texture.class),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyInt(),
        anyInt(),
        anyInt(),
        anyInt(),
        anyBoolean(),
        anyBoolean()
    );
  }

  @Test
  void drawAnythingTest() {
    speechIconComponent.draw(spriteBatch);

    // verify that the draw method was called
    verify(spriteBatch).draw(
        any(Texture.class),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyFloat(),
        anyInt(),
        anyInt(),
        anyInt(),
        anyInt(),
        anyBoolean(),
        anyBoolean()
    );
  }

  @Test
  void drawSpeechBubbleTest() {
    speechIconComponent.draw(spriteBatch);

    // Verify that the draw method was called with correct parameters and animation is using the correct y offset
    verify(spriteBatch).draw(
        speechIconTexture,
        NPC_X_POS,
        CALCULATE_NEXT_Y_OFFSET,
        NPC_X_SCALE /2,
        NPC_Y_SCALE /2,
        NPC_X_SCALE,
        NPC_Y_SCALE,
        1.0f,
        1.0f,
        0f,
        0,
        0,
        speechIconTexture.getWidth(),
        speechIconTexture.getHeight(),
        false,
        false
    );
  }
}