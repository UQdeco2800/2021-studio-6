package com.deco2800.game.lighting;

import com.badlogic.box2dlights.box2dLight.DirectionalLight;
import com.badlogic.box2dlights.box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/** DirectionalLightComponent
 *  not going to implement this one gunna keep it here for now but could really get it to work right
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
