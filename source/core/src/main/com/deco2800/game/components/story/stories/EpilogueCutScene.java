package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.cutscene.CutSceneConfig;

public class EpilogueCutScene extends CutSceneConfig {

    public static final String[] DIALOGUE =
            {
                    //Screen 0
                    "There it was, a lonely sail boat afloat on the deserted harbour. A sign of hope, a sign of relief.",
                    "It's time to tell the others.",
                    "It's time to go home.",

                    //Screen 1
                    "Sailing into the glowing horizon, it was the most beautiful feeling ever.",
                    "The light got bigger and bigger, it was almost blinding.",
                    "The anticipation, the possibility of more fireflies and the tales each one beholds!",
                    "An unusual current swept the ocean, rocking the sail back and forth.",
                    "Just a bad storm I presumed, until I saw the ocean bed change color.",
                    "It became darker by the second",
                    "The colour faintly moved around the boat.",
                    "It wasn't any big fish that lurked the sea. It was nothing like ive ever seen before.",
                    "It was clear what it was. It was a...",

                    //Screen 3
                    "... A shadow!",
            };
    public static final String[] IMAGES =
            {
                    //Screen 0
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

                    //Screen 3
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
        return 13;
    }
}
