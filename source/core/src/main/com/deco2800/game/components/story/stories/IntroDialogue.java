package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class IntroDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE = {"OH NO!\nThe light - it's disappearing. I have to make it to the safe house before the " +
            "darkness gets to me!"};
    protected static final String[] PORTRAIT = {"player-portrait"};
    private static final int LENGTH = 1;

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
        return LENGTH;
    }
}
