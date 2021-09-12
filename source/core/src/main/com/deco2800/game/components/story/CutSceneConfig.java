package com.deco2800.game.components.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.components.dialoguebox.Dialogue;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public abstract class CutSceneConfig implements StoryConfig{
    public Dialogue dialogue;
    public Music music;
    public List<Image> images;

    protected String[] dialogueText;
    protected String[] imagePaths;
    protected String musicPath;

    /**
     * Create all the assets defined
     */
    @Override
    public void create() {
        dialogue = new Dialogue(new ArrayList<>(List.of(dialogueText)));

        ServiceLocator.getResourceService().loadTextures(imagePaths);
        ServiceLocator.getResourceService().loadMusic(new String[]{musicPath});
        ServiceLocator.getResourceService().loadAll();

        images = new ArrayList<>();
        for(String path: imagePaths) {
            Image image = new Image(ServiceLocator.getResourceService().getAsset(path, Texture.class));
            int screenHeight = Gdx.graphics.getHeight();
            int screenWidth = Gdx.graphics.getWidth();
            image.setHeight(screenHeight);
            image.setWidth(screenWidth);
            images.add(image);
        }

        music = ServiceLocator.getResourceService().getAsset(musicPath, Music.class);
    }

    @Override
    public StoryBase initialiseStory() {
        return new CutScene(this);
    }
}
