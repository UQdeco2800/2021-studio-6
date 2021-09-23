package com.deco2800.game.lighting;

import box2dLight.ChainLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** ChainLightComponent
 * Wraps box2dLight.ChainLight into a component you can add to an entity;
 * **/
public class ChainLightComponent extends Component implements Disposable {
    private static final Logger logger = LoggerFactory.getLogger(ChainLightComponent.class);

    private RayHandler rayHandler;
    private ChainLight chainLight;

    private Color color;

    private float distance;

    private int directionDegree;
    float[] chain;
    private int rays = 500;

    /**
     * ChainLightComponent
     * Creates a light shaped like a cone
     * @param color The color of the light
     * @param distance How far the light will travel (in meters)
     * @param directionDegree the direction the light is facing (1= right, -1 = left)
     * @param chain (x, y) vertices to chain together to make the chain must have at least 4
     *              (you need two points to make a chain example :[x1, y1, x2, y2])
     * **/
    public ChainLightComponent(Color color, float distance, int directionDegree, float[] chain) {
        this.color = color;
        this.distance = distance;
        this.directionDegree = directionDegree;
        this.chain = chain;
    }

    @Override
    public void create() {

        //@todo add condition to check if lighitng servcie and error  log if it isn't there

        rayHandler = ServiceLocator.getLightingService().getRayHandler();
        if (rayHandler == null) {
            logger.warn("Lighting Engine not initialised chain light will not render");
            return;
        }

        chainLight = new ChainLight(rayHandler, rays, this.color, distance, this.directionDegree, this.chain);
        chainLight.attachToBody(entity.getComponent(PhysicsComponent.class).getBody());
        chainLight.setIgnoreAttachedBody(true);

        chainLight.setContactFilter(PhysicsLayer.NPC, PhysicsLayer.NPC, PhysicsLayer.NPC);
        chainLight.setSoftnessLength(8f);
        logger.info("Chain light added to scene");
    }


    @Override
    public void dispose() {
        chainLight.remove();
    }


}
