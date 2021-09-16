package com.deco2800.game.entities.factories;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.components.story.ImageSequence;
import com.deco2800.game.entities.Entity;

import java.util.List;

public class ImageSequenceFactory {

    private ImageSequenceFactory(){
        throw new IllegalStateException("Utility Class");
    }

    public static Entity createImageSequence(List<Image> imageList){
        Entity entity = new Entity()
                .addComponent(new ImageSequence(imageList));
        return entity;
    }
}
