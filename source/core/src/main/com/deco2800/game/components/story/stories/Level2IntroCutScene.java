package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.cutscene.CutSceneConfig;

public class Level2IntroCutScene extends CutSceneConfig {

    public static final String[] DIALOGUE_RAW =
            {
                //Screen 0
                "I've finally made it out of the city.",
                "The Safe Haven is glowing brighter and bigger.",
                "I'm getting closer...",

                //Screen 1
                "I'll have to keep following the path of the Fireflies and get to the next safehouse if I want to make it."
            };
    public static final String[] IMAGES_RAW =
            {
                    //Screen 0
                    "images/cutscenes/level2Intro-scene-1.png",
                    "images/cutscenes/level2Intro-scene-1.png",
                    "images/cutscenes/level2Intro-scene-1.png",

                    //Screen 1
                    "images/cutscenes/level2Intro-scene-2.png"
            };
    public static final String MUSIC_RAW = "sounds/title-screen-music.mp3";

    public Level2IntroCutScene() {
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
        return DIALOGUE_RAW.length;
    }
}
