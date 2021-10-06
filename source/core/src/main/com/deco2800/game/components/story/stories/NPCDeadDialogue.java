package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCDeadDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE = {
        "Looks like this person has been half eaten...",
        "* You notice a familiar bracelet on there left arm *",
        "Charlie? No it can't be...",
        "They left only a few days ago...",
        "I...",
        "I'm sorry old friend...",
        "I should have gone with you..."
        };
    protected static final String[] PORTRAIT = null;

    public NPCDeadDialogue(){
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
        return QUOTE.length;
    }
}
