package com.deco2800.game.components.story.stories;
import com.deco2800.game.components.story.cutscene.CutSceneConfig;

/**
 * Asset paths for the test cutscene
 */
public class TestCutscene extends CutSceneConfig {
    protected static final String[] DIALOGUE_RAW = {"Hello World!", "This is me speaking", "I love the world"};
    protected static final String[] IMAGES_RAW = {"title-screen/0001.png", "title-screen/0021.png",
            "title-screen/0001.png"};
    protected static final String MUSIC_RAW = "sounds/title-screen-music.mp3";
    private static final int LENGTH = 3;

    /**
     * Sets asset paths for use
     */
    public TestCutscene(){
        dialogueText = DIALOGUE_RAW;
        imagePaths = IMAGES_RAW;
        musicPath = MUSIC_RAW;
    }

    /**
     * Returns the length of the story
     * @return the length of the story
     */
    @Override
    public int getLength() {
        return LENGTH;
    }
}
