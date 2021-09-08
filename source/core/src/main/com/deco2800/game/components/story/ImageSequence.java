package com.deco2800.game.components.story;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.ui.UIComponent;

import java.util.List;
public class ImageSequence extends UIComponent implements StoryBase {

    private final List<Image> images;
    private int index;

    public ImageSequence(List<Image> imagePaths) {
        this.images = imagePaths;
    }

    @Override
    public void create() {
        super.create();
        index = 0;
        stage.addActor(images.get(index));
        stage.draw();
    }

    /**
     * Draw the renderable. Should be called only by the renderer, not manually.
     *
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void advance() {
        index++;
        stage.clear();
        stage.addActor(images.get(index));
        stage.draw();
    }
}
