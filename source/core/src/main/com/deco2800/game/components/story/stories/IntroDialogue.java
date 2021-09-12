package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class IntroDialogue extends BasicDialogueConfig {
    public static final String[] QUOTE = {"OH NO!\nThe light - it's disappearing. I have to make it to the safe house before the " +
            "darkness gets to me!"};
    public static final String[] PORTRAIT = {"player-portrait"};

    public IntroDialogue(){
        dialogueText = QUOTE;
        dialogueImages = PORTRAIT;
    }

    /**
     * Returns the length of the story
     *
     * @return the length of the story
     */
    @Override
    public int getLength() {
        return 1;
    }
}
