package com.deco2800.game.lighting;

import com.badlogic.box2dlights.box2dLight.PointLight;
import com.badlogic.box2dlights.box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class FlickerLightComponent extends Component implements Disposable {
  private static final Logger logger = LoggerFactory.getLogger(FlickerLightComponent.class);
  private PointLight pointLightOuter;
  private PointLight pointLightBase;
  private PointLight pointLightMiddle;
  private PointLight pointLightInner;
  private final Color colorBase;
  private final Color colorInner;
  private final Color colorMid;
  private final Color colorOut;
  private float distance;
  private float distanceInner;
  private float currentDistanceOuter;
  private float currentDistanceBase;
  private float currentDistanceMiddle;
  private float currentDistanceInner;
  float offsetx;
  float offsety;
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private static int TICK_LENGTH_OUTER = 400; // in milliseconds
  private static int TICK_LENGTH_BASE = 400; // in milliseconds
  private static int TICK_LENGTH_MIDDLE = 400; // in milliseconds
  private static int TICK_LENGTH_INNER = 400; // in milliseconds
  private long tickStartTimeOuter = 0;
  private long tickStartTimeBase = 0;
  private long tickStartTimeMiddle = 0;
  private long tickStartTimeInner = 0;
  private boolean on = true;
  private Random rand;


  public FlickerLightComponent(Color colorOut, Color colorBase, Color colorMid, Color colorInner, float distance, float offsetx, float offsety) {
    this.colorBase = colorBase;
    this.colorInner = colorInner;
    this.colorMid = colorMid;
    this.colorOut = colorOut;
    this.distance = distance;
    this.distanceInner = distance/2;
    this.currentDistanceOuter = distance;
    this.currentDistanceBase = distance - 1;
    this.currentDistanceMiddle = distanceInner;
    this.currentDistanceInner = distanceInner -1;
    this.offsetx = offsetx;
    this.offsety = offsety;
  }

  @Override
  public void create() {
    RayHandler rayHandler = ServiceLocator.getLightingService().getRayHandler();
    if (rayHandler == null) {
      logger.warn("Lighting Engine not initialised Point light will not render");
      return;
    }
    int rays = 500;
    pointLightOuter = new PointLight(rayHandler, rays, colorOut, distance, 0, 0);
    pointLightOuter.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(),
        (entity.getScale().x / 2) + this.offsetx, (entity.getScale().y / 2)+ this.offsety) ;
    pointLightOuter.setIgnoreAttachedBody(true);
    pointLightOuter.setContactFilter(PhysicsLayer.NONE, PhysicsLayer.NONE, PhysicsLayer.NONE);
    pointLightOuter.setSoftnessLength(8f);

    pointLightBase = new PointLight(rayHandler, rays, colorBase, distance - 1, 0, 0);
    pointLightBase.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(),
        (entity.getScale().x / 2) + this.offsetx, (entity.getScale().y / 2)+ this.offsety) ;
    pointLightBase.setIgnoreAttachedBody(true);
    pointLightBase.setContactFilter(PhysicsLayer.NONE, PhysicsLayer.NONE, PhysicsLayer.NONE);
    pointLightBase.setSoftnessLength(8f);


    pointLightMiddle = new PointLight(rayHandler, rays, colorMid, distanceInner, 0, 0);
    pointLightMiddle.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(),
        (entity.getScale().x / 2) + this.offsetx, (entity.getScale().y / 2)+ this.offsety) ;
    pointLightMiddle.setIgnoreAttachedBody(true);
    pointLightMiddle.setContactFilter(PhysicsLayer.NONE, PhysicsLayer.NONE, PhysicsLayer.NONE);
    pointLightMiddle.setSoftnessLength(8f);

    pointLightInner = new PointLight(rayHandler, rays, colorInner, distanceInner - 1, 0, 0);
    pointLightInner.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(),
        (entity.getScale().x / 2) + this.offsetx, (entity.getScale().y / 2)+ this.offsety) ;
    pointLightInner.setIgnoreAttachedBody(true);
    pointLightInner.setContactFilter(PhysicsLayer.NONE, PhysicsLayer.NONE, PhysicsLayer.NONE);
    pointLightInner.setSoftnessLength(8f);

    tickStartTimeOuter = ServiceLocator.getTimeSource().getTime();
    tickStartTimeBase = ServiceLocator.getTimeSource().getTime();
    tickStartTimeMiddle = ServiceLocator.getTimeSource().getTime();
    tickStartTimeInner = ServiceLocator.getTimeSource().getTime();
    rand = new Random();
  }

  @Override
  public void dispose() {
    pointLightOuter.remove();
    pointLightMiddle.remove();
    pointLightInner.remove();
    pointLightBase.remove();
  }


  public float calculateDistance(float currentDistance, float refDis) {
    if (rand.nextInt(2) > 0) {
      currentDistance += (rand.nextFloat() / 4);
    } else {
      currentDistance -= (rand.nextFloat() / 4);
    }
    if (currentDistance > (refDis + 1)) {
      currentDistance = refDis + 1;
    } else if (currentDistance < (refDis - 1)) {
      currentDistance = refDis - 1;
    }
    return currentDistance;
  }


  @Override
  public void update() {
    super.update();
    if (on && !ServiceLocator.getTimeSource().isPaused()) {


      if (timeSource.getTimeSince(tickStartTimeOuter) >= TICK_LENGTH_OUTER) {
        tickStartTimeOuter = ServiceLocator.getTimeSource().getTime();
        currentDistanceOuter = calculateDistance(currentDistanceOuter, distance);
        pointLightOuter.setDistance(currentDistanceOuter);
        TICK_LENGTH_OUTER = (int) Math.floor(Math.random() * ((300) - (100) + 1) + (100));
      }

      if (timeSource.getTimeSince(tickStartTimeBase) >= TICK_LENGTH_BASE) {
        tickStartTimeBase = ServiceLocator.getTimeSource().getTime();
        currentDistanceBase = calculateDistance(currentDistanceBase, distance - 1);
        pointLightBase.setDistance(currentDistanceBase);
        TICK_LENGTH_BASE = (int) Math.floor(Math.random() * ((300) - (100) + 1) + (100));
      }

      if (timeSource.getTimeSince(tickStartTimeMiddle) >= TICK_LENGTH_MIDDLE) {
        tickStartTimeMiddle = ServiceLocator.getTimeSource().getTime();
        currentDistanceMiddle = calculateDistance(currentDistanceMiddle, distanceInner);
        pointLightMiddle.setDistance(currentDistanceMiddle);
        TICK_LENGTH_MIDDLE = (int) Math.floor(Math.random() * ((300) - (100) + 1) + (100));
      }

      if (timeSource.getTimeSince(tickStartTimeInner) >= TICK_LENGTH_INNER) {
        tickStartTimeInner = ServiceLocator.getTimeSource().getTime();
        currentDistanceInner = calculateDistance(currentDistanceInner, distanceInner - 1);
        pointLightInner.setDistance(currentDistanceInner);
        TICK_LENGTH_INNER = (int) Math.floor(Math.random() * ((300) - (100) + 1) + (100));
      }
    }
  }

  /**
   * Function to disable the flickerlight
   */
  public void disableLights() {
    pointLightMiddle.setDistance(0);
    pointLightOuter.setDistance(0);
    pointLightInner.setDistance(0);
    pointLightBase.setDistance(0);
    on = false;
  }

  /**
   * Turns on the light component
   */
  public void turnOnLights() {
    on = true;
  }

  /**
   * Returns the base light
   * @return the base pointlight
   */
  public PointLight getPointLightBase() {
    return this.pointLightBase;
  }

  /**
   * Sets the base lighting distance
   * @param distance the new distance to set the lighting to
   */
  public void changeDistance(float distance) {
    this.distance = distance;
    this.distanceInner = distance/2;
  }

  public void changeOffsetX(float offsetx) {
    this.offsetx = offsetx;
  }
}
