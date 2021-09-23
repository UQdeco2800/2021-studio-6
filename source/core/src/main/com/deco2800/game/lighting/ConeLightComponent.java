package com.deco2800.game.lighting;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** ConeLightComponent
 * Wraps box2dLight.ConeLight into a component you can add to an entity;
 * **/
public class ConeLightComponent extends Component implements Disposable {
    private static final Logger logger = LoggerFactory.getLogger(ConeLightComponent.class);

    private RayHandler rayHandler;
    private ConeLight coneLight;
    private Color color;
    private float distance;
    private float offsetx;
    private float offsety;
    private float directionDegree;
    float coneDegree;
    private int rays = 500;

    /**
     * ConeLightComponent
     * Creates a light shaped like a cone
     * @param color The color of the light
     * @param distance How far the light will travel (in meters)
     * @param offsetx x offset from body
     * @param offsety y offset from body
     * @param directionDegree the direction the light is facing in degrees
     * @param coneDegree the width/size of the cone light
     * **/
    public ConeLightComponent(Color color, float distance, float offsetx, float offsety, float directionDegree, float coneDegree) {
        this.color = color;
        this.distance = distance;
        this.offsetx = offsetx;
        this.offsety = offsety;
        this.directionDegree = directionDegree;
        this.coneDegree = coneDegree;
    }

    @Override
    public void create() {

        rayHandler = ServiceLocator.getLightingService().getRayHandler();
        if (rayHandler == null) {
            logger.warn("Lighting Engine not initialised Cone light will not render");
            return;
        }
        coneLight = new ConeLight(rayHandler, rays, this.color, this.distance, 0, 0, this.directionDegree, this.coneDegree);
        coneLight.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(),
                (entity.getScale().x / 2) + this.offsetx,
                (entity.getScale().y / 2) + this.offsety,
                this.directionDegree);
        coneLight.setIgnoreAttachedBody(true);
        coneLight.setContactFilter(PhysicsLayer.NPC, PhysicsLayer.NPC, PhysicsLayer.NPC);
        coneLight.setSoftnessLength(8f);
        logger.info("Cone light added to scene");

    }

    public void updateDirection(float direction) {
        this.directionDegree = direction;
        coneLight.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(),
                entity.getScale().x / 2 + this.offsetx,
                entity.getScale().y / 2 + this.offsety, this.directionDegree);
    }
    @Override
    public void dispose() {
        coneLight.remove();
    }


}
