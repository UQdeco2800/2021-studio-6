package com.deco2800.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.GdxGame;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FadeTransitionScreen extends ScreenAdapter {
    private static final Logger logger =
            LoggerFactory.getLogger(FadeTransitionScreen.class);
    private final GdxGame game;
    private ScreenAdapter screenCurrent;
    private ScreenAdapter screenNext;
    private ShapeRenderer renderer = new ShapeRenderer();

    //At 1.0f, go to next screen
    private float fadeMagnitude = 0;

    //Fading in if true, Fading out if false
    private boolean fadeIn = true;

    public FadeTransitionScreen(ScreenAdapter current,
                                ScreenAdapter next, GdxGame game){
        this.screenCurrent = current;
        this.screenNext = next;

        game.setScreen(next);
        game.setScreen(current);

        this.game = game;

        logger.debug("Transition screen initiated.");


    }

    @Override
    public void render(float delta) {
        // Sets background to black
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);


        if (fadeIn == true) {
            screenCurrent.render(Gdx.graphics.getDeltaTime());
        } else {
            screenNext.render(Gdx.graphics.getDeltaTime());
        }

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1,1,1,fadeMagnitude);
        renderer.rect(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.end();
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

        if (fadeMagnitude >= 1) {
            fadeIn = false;
        } else if (fadeMagnitude <= 0 && fadeIn == false) {
            game.setScreen(screenNext);
        }
        fadeMagnitude += fadeIn == true ? 0.01 : -0.01;


    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        logger.info("Game paused");
    }

    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    @Override
    public void dispose() {}

}

