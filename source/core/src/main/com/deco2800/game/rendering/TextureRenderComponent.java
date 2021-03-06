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
  private boolean isBulletZIndex = false;
  private float bulletZIndex = 0;
  private boolean isFireFlyBugZIndex = false;
  private float FireFlyBugZIndex = 0;
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

  /**
   * For the enemy bullets to render on top of the tree and water tiles.
   * @param texturePath Internal path of static texture to render.
   * @param rotation Rotation of the enemy bullet texture.
   * @param ZIndex Zindex of the enemy bullet.
   */
  public TextureRenderComponent(String texturePath, float rotation, float ZIndex) {
    this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
    this.rotation = rotation;
    textureRegion = new TextureRegion(this.texture);
    this.isBulletZIndex = true;
    this.bulletZIndex = ZIndex;
  }

  /**
   * For the entities to render on top of other entities.
   * @param ZIndex Zindex of the firefly bug NPC.
   * @param texturePath Internal path of static texture to render.
   */
  public TextureRenderComponent(float ZIndex, String texturePath) {
    this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
    textureRegion = new TextureRegion(this.texture);
    this.isFireFlyBugZIndex = true;
    this.FireFlyBugZIndex = ZIndex;
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

  // For the enemy bullet or firefly bug to render on top of the tree terrain and water tiles.
  @Override
  public float getZIndex() {
    if (isBulletZIndex) {
      return this.bulletZIndex;
    } else if (isFireFlyBugZIndex) {
      return this.FireFlyBugZIndex;
    }
    // The smaller the Y value, the higher the Z index, so that closer entities are drawn in front
    else {
      return -entity.getPosition().y;
    }
  }
}
