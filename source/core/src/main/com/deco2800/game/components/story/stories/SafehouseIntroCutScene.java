package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.cutscene.CutSceneConfig;

public class SafehouseIntroCutScene extends CutSceneConfig {

    public static final String[] DIALOGUE_RAW =
            {
                //Screen 0
                "I've made it to the first Firefly safehouse... Looks like there is someone inside.",
                "Maybe they'll have some stuff to sell.",
                "I'm desperate for some new gear, it's the only way I'm gonna survive...",
                "I'll stay here and rest for a bit before I head out again."
            };
    public static final String[] IMAGES_RAW =
            {
                //Screen 0
                "images/cutscenes/safehouse-scene.png",
                "images/cutscenes/safehouse-scene.png",
                "images/cutscenes/safehouse-scene.png",
                "images/cutscenes/safehouse-scene.png",
            };
    public static final String MUSIC_RAW = "sounds/title-screen-music.mp3";

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
        return DIALOGUE_RAW.length;
    }
}
