package com.deco2800.game.components.story;

public interface StoryConfig {
    /**
     * Create all the assets defined
     */
    void create();

    /**
     * Returns a story configured with the current config file
     * @return a story base object
     */
    StoryBase initialiseStory();

    /**
     * Returns the length of the story
     * @return the length of the story
     */
    int getLength();
}
