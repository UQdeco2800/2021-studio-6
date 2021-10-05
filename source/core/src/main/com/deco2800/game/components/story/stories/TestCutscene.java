package com.deco2800.game.components.story.stories;
import com.deco2800.game.components.story.test.TestSceneConfig;

/**
 * Asset paths for the test cutscene
 */
public class TestCutscene extends TestSceneConfig {
    private static final int LENGTH = 3;

    /**
     * Sets asset paths for use
     */
    public TestCutscene(){
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
