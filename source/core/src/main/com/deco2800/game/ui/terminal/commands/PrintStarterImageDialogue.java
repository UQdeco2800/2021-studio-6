package com.deco2800.game.ui.terminal.commands;

import com.deco2800.game.components.story.StoryManager;
import com.deco2800.game.components.story.StoryNames;
import java.util.ArrayList;

public class PrintStarterImageDialogue extends PrintDialogueCommand{

    @Override
    public boolean action(ArrayList<String> args) {
        StoryManager.getInstance().loadCutScene(StoryNames.INTRO_DIALOGUE);
        StoryManager.getInstance().displayStory();
        return true;
    }

}
