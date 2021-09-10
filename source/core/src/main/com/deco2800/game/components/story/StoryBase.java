package com.deco2800.game.components.story;

public interface StoryBase {
    /**
     * Advance the story
     */
    void advance();

    /**
     * Display the story
     */
    void display();

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
