package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.cutscene.CutSceneConfig;

public class Level3IntroCutScene extends CutSceneConfig {

    public static final String[] DIALOGUE_RAW =
            {
                    //Screen 0
                    "I'm so close",
                    "I can see the shoreline through the trees",
                    "The glow is so bright now",
                    "I need to get through this last stretch and make it to the docks"
            };
    public static final String[] IMAGES_RAW =
            {
                    //Screen 0
                    "images/cutscenes/level3Intro-scene-1.png",
                    "images/cutscenes/level3Intro-scene-1.png",
                    "images/cutscenes/level3Intro-scene-1.png",
                    "images/cutscenes/level3Intro-scene-1.png"
            };
    public static final String MUSIC_RAW = "sounds/title-screen-music.mp3";
    private static final int LENGTH = 4;

    public Level3IntroCutScene() {
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
