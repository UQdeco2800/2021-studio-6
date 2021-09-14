package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.cutscene.CutSceneConfig;

public class EpilogueCutScene extends CutSceneConfig {

    public static final String[] DIALOGUE =
            {
                    //Screen 0
                    "There it is!",
                    "A lonely sail boat... My way across the ocean to the place that promises hope.",
                    "A life in the light...",
                    "A life without fear...",

                    //Screen 1
                    "I sailed into the glowing horizon.",
                    "The ever expanding light was beautiful.",
                    "The anticipation...",
                    "The hope...",
                    "The possibility of meeting other fireflies, perhaps even some of my friends who have made the journey before me.",
                    "An uneasy wind started to blow from the east, rocking the sail back and forth.",
                    "Probably just a storm blowing in...",
                    "The water seemed to be changing colour...",
                    "Becoming darker and darker each second.",
                    "The darkness faintly moved around the boat...",
                    "It was clear what it was...",

                    //Screen 3
                    "A shadow crawler!",
            };
    public static final String[] IMAGES =
            {
                    //Screen 0
                    "epilogue/1-the-docks.png",
                    "epilogue/1-the-docks.png",
                    "epilogue/1-the-docks.png",
                    "epilogue/1-the-docks.png",

                    //Screen 1
                    "epilogue/2-the-glow.png",
                    "epilogue/2-the-glow.png",
                    "epilogue/2-the-glow.png",
                    "epilogue/2-the-glow.png",
                    "epilogue/2-the-glow.png",
                    "epilogue/2-the-glow.png",
                    "epilogue/2-the-glow.png",
                    "epilogue/2-the-glow.png",
                    "epilogue/2-the-glow.png",

                    //Screen 3
                    "epilogue/3-the-shadow.png",
                    "epilogue/3-the-shadow.png",
                    "epilogue/3-the-shadow.png"
            };
    public static final String MUSIC = "sounds/title-screen-music.mp3";

    public EpilogueCutScene() {
        dialogueText = DIALOGUE;
        imagePaths = IMAGES;
        musicPath = MUSIC;
    }

    /**
     * Returns the length of the story
     *
     * @return the length of the story
     */
    @Override
    public int getLength() {
        return 16;
    }
}
