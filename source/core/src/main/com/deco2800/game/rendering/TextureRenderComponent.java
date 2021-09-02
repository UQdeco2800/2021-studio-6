package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.services.ServiceLocator;

/** Render a static texture. */
public class TextureRenderComponent extends RenderComponent {
  private final Texture texture;
  private TextureRegion textureRegion;
  private float rotation = 0;
  /**
   * @param texturePath internal path of static texture to render. Will be scaled to the entity's
   *     scale.
   */
  public TextureRenderComponent(String texturePath) {
    this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
    textureRegion = new TextureRegion(this.texture);
  }

  public TextureRenderComponent(String texturePath, float rotation) {
    this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
    this.rotation = rotation;
    textureRegion = new TextureRegion(this.texture);
  }

  /** @param texture Static texture to render. Will be scaled to the entity's scale. */
  public TextureRenderComponent(Texture texture) {
    this.texture = texture;
  }

  /** Scale the entity to a width of 1 and a height matching the texture's ratio */
  public void scaleEntity() {
    entity.setScale(1f, (float) texture.getHeight() / texture.getWidth());
  }



  @Override
  public void draw(SpriteBatch batch) {
    Vector2 position = entity.getPosition();
    Vector2 scale = entity.getScale();

    batch.draw(texture,
            position.x,
            position.y,
            scale.x/2,
            scale.y/2,
            scale.x,
            scale.y,
            1.0f,
            1.0f,
            this.rotation,
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false);
  }
}
