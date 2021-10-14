package com.deco2800.game.lighting;

import com.badlogic.box2dlights.box2dLight.PointLight;
import com.badlogic.box2dlights.box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** PointLightComponent
 * Wraps box2dLight.PointLight into a component you can add to an entity;
 * **/
public class PointLightComponent extends Component implements Disposable {
    private static final Logger logger = LoggerFactory.getLogger(PointLightComponent.class);
    private RayHandler rayHandler;
    private PointLight pointLight;
    private Color color;
    private float distance;
    float offsetx;
    float offsety;
    private int rays = 50;

    /**
     * ConeLightComponent
     * Creates a light point light attached to an entity
     * @param color The color of the light
     * @param distance How far the light will travel (in meters)
     * @param offsetx x offset from body
     * @param offsety y offset from body
     * **/
    public PointLightComponent(Color color, float distance, float offsetx, float offsety) {
        this.color = color;
        this.distance = distance;
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
        pointLight = new PointLight(rayHandler, rays, this.color, distance, 0, 0);
        pointLight.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(),
                (entity.getScale().x / 2) + this.offsetx, (entity.getScale().y / 2)+ this.offsety) ;
        pointLight.setIgnoreAttachedBody(true);
        pointLight.setContactFilter(PhysicsLayer.NONE, PhysicsLayer.NONE, PhysicsLayer.NONE);
        pointLight.setSoftnessLength(8f);
        logger.info("Chain light added to scene");
    }
    @Override
    public void dispose() {
        pointLight.remove();
    }

    public PointLight getPointLight() {
        return this.pointLight;
    }

    public void changeDistance(int distance) {
        this.distance = distance;
        pointLight.setDistance(distance);
    }

}
