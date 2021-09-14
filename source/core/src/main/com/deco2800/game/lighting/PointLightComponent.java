package com.deco2800.game.lighting;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

public class PointLightComponent extends Component implements Disposable {
    private RayHandler rayHandler;
    private PointLight pointLight;

    private Color color;
    private float distance;
    float offsetx;
    float offsety;
    private int rays = 500;

    public PointLightComponent(Color color, float distance, float offsetx, float offsety) {
        this.color = color;
        this.distance = distance;
        this.offsetx = offsetx;
        this.offsety = offsety;
    }

    @Override
    public void create() {
        rayHandler = ServiceLocator.getLightingService().getRayHandler();
        pointLight = new PointLight(rayHandler, rays, Color.ORANGE, distance, 0f, 0f);
        pointLight.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(), offsetx, offsety);
    }
    @Override
    public void dispose() {
        pointLight.remove();
    }


}
