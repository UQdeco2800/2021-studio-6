package com.deco2800.game.services;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.input.InputService;
import com.deco2800.game.lighting.Lighting;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simplified implementation of the Service Locator pattern:
 * https://martinfowler.com/articles/injection.html#UsingAServiceLocator
 *
 * <p>Allows global access to a few core game services. Warning: global access is a trap and should
 * be used extremely sparingly. Read the README for details.
 */
public class ServiceLocator {
  private static final Logger logger = LoggerFactory.getLogger(ServiceLocator.class);
  private static EntityService entityService;
  private static RenderService renderService;
  private static RenderService renderUnlitService;
  private static PhysicsService physicsService;
  private static GameTime timeSource;
  private static InputService inputService;
  private static ResourceService resourceService;
  private static Lighting lightingService;
  private static GameArea gameArea;


  public static EntityService getEntityService() {
    return entityService;
  }

  public static RenderService getRenderService() {
    return renderService;
  }

  public static RenderService getRenderUnlitService() {
    return renderUnlitService;
  }

  public static PhysicsService getPhysicsService() {
    return physicsService;
  }

  public static GameTime getTimeSource() {
    return timeSource;
  }

  public static InputService getInputService() {
    return inputService;
  }

  public static ResourceService getResourceService() {
    return resourceService;
  }

  public static Lighting getLightingService() { return lightingService; }

  public static void registerLightingService(Lighting service) {
    logger.debug("Registering lighting service {}", service);
    lightingService = service;
  }
  public static GameArea getGameArea() {
    return gameArea;
  }

  public static void registerEntityService(EntityService service) {
    logger.debug("Registering entity service {}", service);
    entityService = service;
  }

  public static void registerRenderService(RenderService service) {
    logger.debug("Registering render service {}", service);
    renderService = service;
  }

  public static void registerUnlitRenderService(RenderService service) {
    logger.debug("Registering Unlit Render service {}", service);
    renderUnlitService = service;
  }

  public static void registerPhysicsService(PhysicsService service) {
    logger.debug("Registering physics service {}", service);
    physicsService = service;
  }

  public static void registerTimeSource(GameTime source) {
    logger.debug("Registering time source {}", source);
    timeSource = source;
  }

  public static void registerInputService(InputService source) {
    logger.debug("Registering input service {}", source);
    inputService = source;
  }

  public static void registerResourceService(ResourceService source) {
    logger.debug("Registering resource service {}", source);
    resourceService = source;
  }


  public static void registerGameArea(GameArea source) {
    logger.debug("Registering local game area reference {}", source);
    gameArea = source;
  }


  public static void clear() {
    entityService = null;
    renderService = null;
    renderUnlitService = null;
    physicsService = null;
    timeSource = null;
    inputService = null;
    lightingService = null;
    gameArea = null;
  }

  private ServiceLocator() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
