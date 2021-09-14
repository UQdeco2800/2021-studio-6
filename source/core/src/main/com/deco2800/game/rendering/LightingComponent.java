package com.deco2800.game.rendering;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


public class LightingComponent implements Renderable {
  public class lightSource {
    float x;
    float y;
    float offset = MathUtils.random(50.0f);
    public PointLight light;

    public lightSource(RayHandler rayHandler) {
      this.x = MathUtils.random( 2.0f) - 1;
      this.y = MathUtils.random(10.0f);
      light = new PointLight(rayHandler, 50, Color.GOLDENROD, 0.2f, this.x, this.y);
    }

    public void update() {
      this.y = this.y + 0.005f;
      if(this.y > MathUtils.PI2) {
        this.y = -MathUtils.PI2;
      }
      this.x += 0.003f;
      if(this.x > 1){
        this.x = -1;
      }

      light.setPosition(this.x, MathUtils.sin(this.y + offset)/ 1.1f);
    }
  }

  private RayHandler rayHandler;
  private Array<lightSource> lights;

  public LightingComponent(World world) {
    rayHandler = new RayHandler(world);
    lights = new Array<>();
    for(int i = 0; i < 15; i++){
      lights.add(new lightSource(rayHandler));
    }

  }

  @Override
  public void render(SpriteBatch batch) {
    rayHandler.update();
    rayHandler.render();
    for(lightSource p : lights) {
      p.update();
    }
    rayHandler.setAmbientLight(0.4f);
  }

  @Override
  public float getZIndex() {
    return 3f;
  }

  @Override
  public int getLayer() {
    return 12;
  }

  @Override
  public int compareTo(Renderable renderable) {
    return 1;
  }
}
