package com.deco2800.game.components.story;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class StoryManagerTest {

    StoryManager storyManager = StoryManager.getInstance();

    @Test
    void returnNullOnNoStoryLoaded() {
        assertNull(storyManager.displayStory());
    }

    @Test
    void allStoryNamesUsed() {
        StoryNames[] storyNames = StoryNames.values();

        //The plus 1 is to account for the NONE story.
        assertEquals(storyNames.length, storyManager.getScenesConfigs().size() + 1);
    }
}