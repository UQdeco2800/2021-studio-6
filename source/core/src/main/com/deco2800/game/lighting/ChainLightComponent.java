package com.deco2800.game.lighting;

import box2dLight.ChainLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/** ConeLightComponent
 *
 * **/
public class ChainLightComponent extends Component implements Disposable {
    private RayHandler rayHandler;
    private ChainLight chainLight;

    private Color color;

    private float distance;

    private float directionDegree;
    float[] chain;
    private int rays = 500;
    /**
     * ConeLightComponent
     * Creates a light shaped like a cone
     * @param color The color of the light
     * @param distance How far the light will travel (in meters)
     * @param directionDegree the direction the light is facing
     * @param chain (x, y)
     * **/
    public ChainLightComponent(Color color, float distance, float directionDegree, float[] chain) {
        this.color = color;
        this.distance = distance;

        this.directionDegree = directionDegree;
        this.chain = chain;
    }

    @Override
    public void create() {

        //@todo add condition to check if lighitng servcie and error  log if it isn't there
        rayHandler = ServiceLocator.getLightingService().getRayHandler();

        chainLight = new ChainLight(rayHandler, rays, this.color, distance, this.directionDegree, this.chain);
        chainLight.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(), entity.getScale().x / 2, entity.getScale().y / 2, this.directionDegree);
        chainLight.setIgnoreAttachedBody(true);

        chainLight.setContactFilter(PhysicsLayer.NPC, PhysicsLayer.NPC, PhysicsLayer.NPC);
        chainLight.setSoftnessLength(8f);
    }


    @Override
    public void dispose() {
        chainLight.remove();
    }


}
