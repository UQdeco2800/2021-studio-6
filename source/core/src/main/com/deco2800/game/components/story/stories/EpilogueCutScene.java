package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.cutscene.CutSceneConfig;

public class EpilogueCutScene extends CutSceneConfig {

    protected static final String[] DIALOGUE_RAW =
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
                    "The possibility of meeting other Fireflies, perhaps even some of my friends who have made the journey before me.",
                    "An uneasy wind started to blow from the east, rocking the sail back and forth.",
                    "Probably just a storm blowing in...",
                    "The water seemed to be changing colour...",
                    "Becoming darker and darker each second.",
                    "The darkness faintly moved around the boat...",
                    "It was clear what it was...",

                    //Screen 3
                    "A shadow crawler!",
            };

    protected static final String DOCK_IMAGE = "epilogue/1-the-docks.png";
    protected static final String GLOW_IMAGE = "epilogue/2-the-glow.png";
    protected static final String SHADOW_IMAGE = "epilogue/3-the-shadow.png";
    protected static final String[] IMAGES_RAW =
            {
                    //Screen 0
                    DOCK_IMAGE,
                    DOCK_IMAGE,
                    DOCK_IMAGE,
                    DOCK_IMAGE,

                    //Screen 1
                    GLOW_IMAGE,
                    GLOW_IMAGE,
                    GLOW_IMAGE,
                    GLOW_IMAGE,
                    GLOW_IMAGE,
                    GLOW_IMAGE,
                    GLOW_IMAGE,
                    GLOW_IMAGE,
                    GLOW_IMAGE,

                    //Screen 3
                    SHADOW_IMAGE,
                    SHADOW_IMAGE,
                    SHADOW_IMAGE
            };
    protected static final String MUSIC_RAW = "sounds/title-screen-music.mp3";
    private static final int LENGTH = 16;

    public EpilogueCutScene() {
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
