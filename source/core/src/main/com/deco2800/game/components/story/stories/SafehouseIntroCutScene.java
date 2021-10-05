package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.cutscene.CutSceneConfig;

public class SafehouseIntroCutScene extends CutSceneConfig {

    public static final String[] DIALOGUE_RAW =
            {
                    //Screen 0
                    "Oh a safehouse"
            };
    public static final String[] IMAGES_RAW =
            {
                    //Screen 0
                    "images/cutscenes/safehouse-scene.png"
            };
    public static final String MUSIC_RAW = "sounds/title-screen-music.mp3";
    private static final int LENGTH = 1;

    public SafehouseIntroCutScene() {
        dialogueText = DIALOGUE_RAW;
        imagePaths = IMAGES_RAW;
        musicPath = MUSIC_RAW;
    }

    /**
     * Returns the length of the story
     *
     * @return the length of the story
     */
    @Override
    public int getLength() {
        return LENGTH;
    }
}
