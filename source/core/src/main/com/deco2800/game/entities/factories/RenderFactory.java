package com.deco2800.game.entities.factories;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ServiceLocator;

public class RenderFactory {

  public static Entity createCamera() {
    return new Entity().addComponent(new CameraComponent());
  }

  public static Renderer createRenderer() {
    Entity camera = createCamera();
    ServiceLocator.getEntityService().register(camera);
    CameraComponent camComponent = camera.getComponent(CameraComponent.class);

    return new Renderer(camComponent, true, null);
  }

  /**
   * Creates the renderer specifically for render after the lighting system
   * @param camComponent The camera component created in createRenderer
   * @param stage the stage from the pre-existing renderer
   * @return the new unlitRenderer service
   */
  public static Renderer createUnlitRenderer(CameraComponent camComponent, Stage stage) {
    return new Renderer(camComponent, false, stage);
  }

  private RenderFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
