package com.deco2800.game.components.story.stories;
import com.deco2800.game.components.story.CutSceneConfig;

/**
 * Asset paths for the test cutscene
 */
public class TestCutscene extends CutSceneConfig {
    public static final String[] dialogueTextRaw = {"Hello World!", "This is me speaking", "I love the world"};
    public static final String[] imagePathsRaw = {"title-screen/0001.png", "title-screen/0021.png",
            "title-screen/0001.png"};
    public static final String musicPathRaw = "sounds/title-screen-music.mp3";

    /**
     * Sets asset paths for use
     */
    public TestCutscene(){
        dialogueText = dialogueTextRaw;
        imagePaths = imagePathsRaw;
        musicPath = musicPathRaw;
    }

    /**
     * Returns the length of the story
     * @return the length of the story
     */
    @Override
    public int getLength() {
        return 3;
    }
}
