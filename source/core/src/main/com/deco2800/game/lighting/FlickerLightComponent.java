package com.deco2800.game.lighting;

import box2dLight.PointLight;
import box2dLight.RayHandler;
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
  private RayHandler rayHandler;
  private PointLight pointLightBase;
  private PointLight pointLightSecond;
  private Color colorBase;
  private Color colorUp ;
  private float distance;
  private float distanceInner;
  float offsetx;
  float offsety;
  private int rays = 500;
  private final GameTime timeSource = ServiceLocator.getTimeSource();
  private static int TICK_LENGTH = 400; // in milliseconds
  private static int TICK_LENGTH_INNER = 400; // in milliseconds
  private long tickStartTime = 0;
  private long tickStartTimeInner = 0;
  private boolean on = true;


  public FlickerLightComponent(Color color1, Color color2, float distance, float offsetx, float offsety) {
    this.colorBase = color1;
    this.colorUp = color2;
    this.distance = distance;
    this.distanceInner = distance/2;
    this.offsetx = offsetx;
    this.offsety = offsety;
  }

  @Override
  public void create() {
    rayHandler = ServiceLocator.getLightingService().getRayHandler();
    if (rayHandler == null) {
      logger.warn("Lighting Engine not initialised Point light will not render");
      return;
    }
    pointLightBase = new PointLight(rayHandler, rays, this.colorBase, distance, 0, 0);
    pointLightBase.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(),
        (entity.getScale().x / 2) + this.offsetx, (entity.getScale().y / 2)+ this.offsety) ;
    pointLightBase.setIgnoreAttachedBody(true);
    pointLightBase.setContactFilter(PhysicsLayer.NPC, PhysicsLayer.NPC, PhysicsLayer.NPC);
    pointLightBase.setSoftnessLength(8f);


    pointLightSecond = new PointLight(rayHandler, rays, colorUp, distanceInner, 0, 0);
    pointLightSecond.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(),
        (entity.getScale().x / 2) + this.offsetx, (entity.getScale().y / 2)+ this.offsety) ;
    pointLightSecond.setIgnoreAttachedBody(true);
    pointLightSecond.setContactFilter(PhysicsLayer.NPC, PhysicsLayer.NPC, PhysicsLayer.NPC);
    pointLightSecond.setSoftnessLength(8f);

    tickStartTime = ServiceLocator.getTimeSource().getTime();
    tickStartTimeInner = ServiceLocator.getTimeSource().getTime();
  }

  @Override
  public void dispose() {
    pointLightBase.remove();
    pointLightSecond.remove();
  }

  @Override
  public void update() {
    super.update();
    if (on) {
      Random rand = new Random();
      float currentDistance = distance;
      if (timeSource.getTimeSince(tickStartTime) >= TICK_LENGTH) {
        tickStartTime = ServiceLocator.getTimeSource().getTime();
        if (rand.nextInt(2) > 0) {
          currentDistance += (rand.nextFloat() / 2);
        } else {
          currentDistance -= (rand.nextFloat() / 2);
        }
        if (currentDistance > (distance + 1)) {
          currentDistance = distance + 1;
        } else if (currentDistance < (distance - 1)) {
          currentDistance = distance - 1;
        }
        pointLightBase.setDistance(currentDistance);
        TICK_LENGTH = (int) Math.floor(Math.random() * ((300) - (100) + 1) + (100));
      }
      if (timeSource.getTimeSince(tickStartTimeInner) >= TICK_LENGTH_INNER) {
        tickStartTimeInner = ServiceLocator.getTimeSource().getTime();
        currentDistance = distanceInner;
        if (rand.nextInt(2) > 0) {
          currentDistance += (rand.nextFloat() / 2);
        } else {
          currentDistance -= (rand.nextFloat() / 2);
        }
        if (currentDistance > (distanceInner + 1)) {
          currentDistance = distanceInner + 1;
        } else if (currentDistance < (distanceInner - 1)) {
          currentDistance = distanceInner - 1;
        }
        pointLightSecond.setDistance(currentDistance);
        TICK_LENGTH_INNER = (int) Math.floor(Math.random() * ((300) - (100) + 1) + (100));
      }
    }
  }

  public void disableLights() {
    pointLightSecond.setDistance(0);
    pointLightBase.setDistance(0);
    on = false;
  }

  public void turnOnLights() {
    on = true;
  }

  public PointLight getPointLightBase() {
    return this.pointLightBase;
  }

  public void changeDistance(float distance) {
    this.distance = distance;
    this.distanceInner = distance/2;
  }

}
