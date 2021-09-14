package com.deco2800.game.rendering;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.SortedIntMap;

/**
 * Globally accessible service for registering renderable components. Any renderable registered with
 * this service has render() called once per frame.
 */
public class RenderService implements Disposable {
  private static final int INITIAL_LAYER_CAPACITY = 4;
  private static final int INITIAL_CAPACITY = 4;
  private Stage stage;
  private DebugRenderer debugRenderer;
  private Vector2 currentView;

  /**
   * Map from layer to list of renderables, allows us to render each layer in the correct order
   */
  private final SortedIntMap<Array<Renderable>> renderables =
      new SortedIntMap<>(INITIAL_LAYER_CAPACITY);

  /**
   * Register a new renderable.
   *
   * @param renderable new renderable.
   */
  public void register(Renderable renderable) {
    int layerIndex = renderable.getLayer();
    if (!renderables.containsKey(layerIndex)) {
      renderables.put(layerIndex, new Array<>(INITIAL_CAPACITY));
    }
    Array<Renderable> layer = renderables.get(layerIndex);
    layer.add(renderable);
  }

  /**
   * Unregister a renderable.
   *
   * @param renderable renderable to unregister.
   */
  public void unregister(Renderable renderable) {
    Array<Renderable> layer = renderables.get(renderable.getLayer());
    if (layer != null) {
      layer.removeValue(renderable, true);
    }
  }

  /**
   * Trigger rendering on the given batch. This should be called only from the main renderer.
   *
   * @param batch batch to render to.
   */
  public void render(SpriteBatch batch) {

    for (Array<Renderable> layer : renderables) {
      // Sort into rendering order
      layer.sort();

      for (Renderable renderable : layer) {

        renderable.render(batch);
      }


    }

  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public Stage getStage() {
    return stage;
  }

  public void setDebug(DebugRenderer debugRenderer) {
    this.debugRenderer = debugRenderer;
  }

  public DebugRenderer getDebug() {
    return debugRenderer;
  }



  public Vector2 getPos() {
    return currentView;
  }

  public void setPos(Vector2 cameraPosition) {
    currentView = cameraPosition;
  }

  @Override
  public void dispose() {
    renderables.clear();
  }
}
