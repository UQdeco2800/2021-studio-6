package com.deco2800.game.components.story;

public interface StoryBase {

    /**
     * Creates the story
     */
    void create();

    /**
     * Advance the story
     */
    void advance();

    /**
     * Displays the story. Throws exception when story has not been created yet
     */
    void display() throws Exception;

    /**
     * Dispose the story
     */
    void dispose();

    /**
     * Returns if the story has already been disposed
     * @return if the story is dead
     */
    boolean isDead();
}
