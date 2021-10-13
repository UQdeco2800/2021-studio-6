package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;

/**
 * A generic component for rendering an entity. Registers itself with the render service in order to
 * be rendered each frame. Child classes can implement different kinds of rendering behaviour.
 */
public abstract class RenderComponent extends Component implements Renderable, Disposable {
  private static final int DEFAULT_LAYER = 1;
  private boolean lit = true;
  private boolean registered = false;

  @Override
  public void create() {
    super.create();
    if (registered) {
      return;
    }

    if (lit) {
      ServiceLocator.getRenderService().register(this);
    } else {
      ServiceLocator.getRenderUnlitService().register(this);
    }
    registered = true;
  }

  @Override
  public void dispose() {
    if (lit) {
      ServiceLocator.getRenderService().unregister(this);
    } else {
      ServiceLocator.getRenderUnlitService().unregister(this);
    }
  }

  @Override
  public void render(SpriteBatch batch) {
    draw(batch);
  }

  @Override
  public int compareTo(Renderable o) {
    return Float.compare(getZIndex(), o.getZIndex());
  }

  @Override
  public int getLayer() {
    return DEFAULT_LAYER;
  }

  /**
   * Used to register the asset in the lit render service for interaction with lighting.
   * This happens automatically on creation as being lit is the standard.
   */
  public void setLit() {
    if (registered && !lit) {
      ServiceLocator.getRenderUnlitService().unregister(this);
    }
    lit = true;
    registered = true;
    ServiceLocator.getRenderService().register(this);
  }

  /**
   * Used to register the asset for the unlit render service. Has to remove it
   * from the lit render service as that happens automatically on creation.
   */
  public void setUnlit() {
    if (registered && lit) {
      ServiceLocator.getRenderService().unregister(this);
    }
    lit = false;
    registered = true;
    ServiceLocator.getRenderUnlitService().register(this);
  }

  @Override
  public float getZIndex() {
    // The smaller the Y value, the higher the Z index, so that closer entities are drawn in front
    return -entity.getPosition().y;
  }

  /**
   * Draw the renderable. Should be called only by the renderer, not manually.
   *
   * @param batch Batch to render to.
   */
  protected abstract void draw(SpriteBatch batch);
}
