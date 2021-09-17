package com.deco2800.game.ui.terminal.commands;

import com.deco2800.game.components.story.StoryManager;
import com.deco2800.game.components.story.StoryNames;

import java.util.ArrayList;

public class StartEpilogueCutSceneCommand implements Command{
    /**
     * Action a command.
     *
     * @param args command args
     * @return command was successful
     */
    @Override
    public boolean action(ArrayList<String> args) {
        StoryManager storyManager = StoryManager.getInstance();
        storyManager.loadCutScene(StoryNames.EPILOGUE);
        storyManager.displayStory();
        return true;
    }
}
