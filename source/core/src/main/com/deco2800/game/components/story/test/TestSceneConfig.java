package com.deco2800.game.components.story.test;

import com.deco2800.game.components.story.StoryBase;
import com.deco2800.game.components.story.StoryConfig;

public abstract class TestSceneConfig implements StoryConfig {
    /**
     * Create all the assets defined
     */
    @Override
    public void create() {

    }

    /**
     * Returns a story configured with the current config file
     *
     * @return a story base object
     */
    @Override
    public StoryBase initialiseStory() {
        return new TestScene();
    }


}
