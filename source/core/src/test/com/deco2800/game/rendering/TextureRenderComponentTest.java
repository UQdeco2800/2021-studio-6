package com.deco2800.game.rendering;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class TextureRenderComponentTest {
  @Mock Texture texture;
  @Mock SpriteBatch spriteBatch;
  @Mock Entity entity;

  @Test
  void shouldDrawTexture() {
    when(entity.getPosition()).thenReturn(new Vector2(2f, 2f));
    when(entity.getScale()).thenReturn(new Vector2(1f, 1f));
    TextureRenderComponent component = new TextureRenderComponent(texture);
    component.setEntity(entity);
    component.render(spriteBatch);

    verify(spriteBatch).draw(texture,
            entity.getPosition().x,
            entity.getPosition().y,
            entity.getScale().x/2,
            entity.getScale().x/2,
            entity.getScale().x,
            entity.getScale().y,
            1.0f,
            1.0f,
            0,
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false);
  }
}

