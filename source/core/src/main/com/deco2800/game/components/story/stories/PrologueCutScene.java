package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.cutscene.CutSceneConfig;

public class PrologueCutScene extends CutSceneConfig {

    public static final String[] DIALOGUE =
            {
                    //Screen 0
                    "Darkness",
                    "Daunting, terrifying, and deadly.",

                    //Screen 1
                    "It all started with the lights...",
                    "First flickering...",
                    "Then gone. Leaving us in darkness.",

                    //Screen 2
                    "You don't want to be left in the dark...",
                    "That's where the shadow crawlers thrive.",
                    "Creatures of the unknown.",
                    "Minions of some greater evil.",
                    "They destroy light, take it away, leaving darkness in their wake - killing anything in their way.",

                    //Screen 3
                    "They went for the smaller towns first, forcing many to abandon their homes.",
                    "People fled to the cities where the light provided them safety.",
                    "For now...",

                    //Screen 4
                    "But there is a light of hope, or so we believe...",
                    "On the horizon... the Safe Haven",
                    "Some people venture forth into the endless night, trying to reach the Safe Haven.",
                    "Risking the shadows, hoping to find a permanent home in the light.",
                    "They are the fireflies.",

                    //Screen 5
                    "Leaving a path of light and setting up safehouses along the way for others to follow.",
                    "No one knows what's at the Safe Haven.",
                    "No one knows if any firefly has ever made it.",

                    //Screen 6
                    "But what other choice do we have?",
                    "The shadow crawlers are getting closer to the city every day.",
                    "The darkness is coming.",
                    "I have to leave and follow the fireflies."
            };
    public static final String[] IMAGES =
            {
                    //Screen 0
                    "utils/blackpixel.png",
                    "utils/blackpixel.png",

                    //Screen 1
                    "prologue/1 simple/1.1.png",
                    "prologue/1 simple/1.2.png",
                    "prologue/1 simple/1.3.png",

                    //Screen 2
                    "utils/blackpixel.png",
                    "utils/blackpixel.png",
                    "prologue/2.png",
                    "prologue/2.png",
                    "prologue/2.png",

                    //Screen 3
                    "prologue/3 simple/3.0.png",
                    "prologue/3 simple/3.2.png",
                    "prologue/3 simple/3.2.png",

                    //Screen 4
                    "utils/blackpixel.png",
                    "prologue/4.png",
                    "prologue/4.png",
                    "prologue/4.png",
                    "prologue/4.png",

                    //Screen 5
                    "prologue/5.png",
                    "prologue/5.png",
                    "prologue/5.png",

                    //Screen 6
                    "prologue/6/6 background.png",
                    "prologue/6/6 basic.png",
                    "prologue/6/6 basic.png",
                    "utils/blackpixel.png"
            };
    public static final String MUSIC = "sounds/title-screen-music.mp3";

    public PrologueCutScene() {
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
        return 25;
    }
}
