package com.deco2800.game.lighting;

import box2dLight.DirectionalLight;
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
public class DirectionalLightComponent extends Component implements Disposable {
    private RayHandler rayHandler;
    private DirectionalLight directionalLight;

    private Color color;

    private float distance;
    private float offsetx;
    private float offsety;
    private float directionDegree;

    private int rays = 500;
    /**
     * ConeLightComponent
     * Creates a light shaped like a cone
     * @param color The color of the light
     * @param offsetx x offset from body
     * @param offsety y offset from body
     * @param directionDegree the direction the light is facing
     * **/
    public DirectionalLightComponent(Color color, float offsetx, float offsety, float directionDegree) {
        this.color = color;
        this.offsetx = offsetx;
        this.offsety = offsety;
        this.directionDegree = directionDegree;
    }

    @Override
    public void create() {

        //@todo add condition to check if lighitng servcie and error  log if it isn't there
        rayHandler = ServiceLocator.getLightingService().getRayHandler();

        directionalLight = new DirectionalLight(rayHandler, rays, this.color, this.directionDegree);
        directionalLight.attachToBody(entity.getComponent(PhysicsComponent.class).getBody());
        directionalLight.setIgnoreAttachedBody(true);


        directionalLight.setContactFilter(PhysicsLayer.NPC, PhysicsLayer.NPC, PhysicsLayer.NPC);
        directionalLight.setSoftnessLength(8f);
    }

    public void updateDirection(float direction) {
        this.directionDegree = direction;
        directionalLight.attachToBody(entity.getComponent(PhysicsComponent.class).getBody());
    }
    @Override
    public void dispose() {
        directionalLight.remove();
    }


}
