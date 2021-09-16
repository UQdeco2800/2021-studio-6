package com.deco2800.game.components.story;

/**
 * A story base object is what the manager calls upon to control the story.
 */
public interface StoryBase {

    /**
     * Creates the story
     */
    void create();

    /**
     * Advance the story. Returns true if there is story left
     */
    boolean advance();

    /**
     * Displays the story. Throws exception when story has not been created yet
     */
    void display() throws NoStoryLoadedException;

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
