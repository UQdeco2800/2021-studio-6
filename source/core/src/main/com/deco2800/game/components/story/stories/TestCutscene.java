package com.deco2800.game.components.story.stories;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.components.dialoguebox.Dialogue;
import com.deco2800.game.components.story.CutSceneConfig;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class TestCutscene extends CutSceneConfig {

    private static final String[] dialogueText = {"Hello World!", "This is me Speaking", "I love the world"};
    private static final String[] imagePaths = {"title-screen/0001.png", "title-screen/0021.png"};
    private static final String musicPath = "sounds/title-screen-music.mp3";

    private TestCutscene(){
        throw new IllegalStateException("Config Class");
    }

    @Override
    protected void create() {
        dialogue = new Dialogue(new ArrayList<>(List.of(dialogueText)));

        images = new ArrayList<>();
        for(String path: imagePaths) {
            Image image = new Image(ServiceLocator.getResourceService().getAsset(path, Texture.class));
            images.add(image);
        }

        music = ServiceLocator.getResourceService().getAsset(musicPath, Music.class);
    }
}
