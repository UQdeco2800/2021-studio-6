package com.deco2800.game.components.story.test;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.story.NoStoryLoadedException;
import com.deco2800.game.components.story.StoryBase;

public class TestScene extends Component implements StoryBase {
    /**
     * Advance the story. Returns true if there is story left
     *
     * @return true if there is story left
     */
    @Override
    public boolean advance() {
        return false;
    }

    /**
     * Displays the story. Throws exception when story has not been created yet
     *
     * @throws NoStoryLoadedException when story has not been created yet
     */
    @Override
    public void display() throws NoStoryLoadedException {

    }

    /**
     * Returns if the story has already been disposed
     *
     * @return if the story is dead
     */
    @Override
    public boolean isDead() {
        return false;
    }
}
