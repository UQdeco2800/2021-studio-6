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

/**
 * Lighting
 * The dynamic Lighting engine for the game, mostly just a wrapper for box2dLight.RayHandler;
 */
public class Lighting implements Disposable {
    private RayHandler rayHandler;

    /**
     * Lighting Constructor initalises the rayhandler (currently sets a default ambient light)
     * @param world the physics world which is passed to the rayhandler
     */
    public Lighting(World world) {
        rayHandler = new RayHandler(world);
        setAmbientLight(0f, 0f, 0f, 0.05f);
    }

    /**
     * getRayHandler
     * @return rayhandler used for rendering the lighting
     */
    public RayHandler getRayHandler() {
        return rayHandler;
    }

    /**
     * update
     * calls rayHandler.update()
     */
    public void update(){
        rayHandler.update();
    }

    /**
     * setAmbientLight
     * sets the ambient light for a scene
     * @param r red value
     * @param g green value
     * @param b blue value
     * @param a alpha value
     */
    public void setAmbientLight(float r, float g, float b, float a) {
        rayHandler.setAmbientLight(r, g, b, a);
    }

    /**
     * setCamera
     * sets the camera for the scene
     * @param camera rendering camera need for the rayhandler to draw light
     */
    public void setCamera(Camera camera) {
        rayHandler.setCombinedMatrix(camera.combined, camera.position.x , camera.position.y, camera.viewportWidth * 1, camera.viewportHeight * 1 );
    }

    /**
     * render
     * calls rayhandler.render()
     */
    public void render() {
        rayHandler.render();
    }

    @Override
    public void dispose() {
        rayHandler.dispose();
    }

}
