package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCInjuredDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE = {"*urggh... help*", "My legs, I can't feel them anymore.", "I'm gonna di-"};
    protected static final String[] PORTRAIT = {"injured-portrait"};
    private static final int LENGTH = 3;

    public NPCInjuredDialogue(){
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
