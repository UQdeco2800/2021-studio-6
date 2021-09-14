package com.deco2800.game.lighting;

import box2dLight.PointLight;
import box2dLight.ConeLight;
import box2dLight.RayHandler;
import box2dLight.Light;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;

public class Lighting implements Disposable {
    private RayHandler rayHandler;
    private ArrayList<Light> pointLight;
    private float time;

    public Lighting(World world) {
        rayHandler = new RayHandler(world);
    }
    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public void update(){
        rayHandler.update();
    }

    public void setCamera(Camera camera) {
        rayHandler.setCombinedMatrix(camera.combined, camera.position.x , camera.position.y, camera.viewportWidth * 1, camera.viewportHeight * 1 );
    }
    public void render() {
        rayHandler.render();
        rayHandler.setAmbientLight(0.2f, 0.3f, 0.4f, 0.1f);
    }

    @Override
    public void dispose() {
        rayHandler.dispose();
    }

}
