package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCDeadDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE = {"...", "*It looks like some poor Firefly didn't make it...*"};
    protected static final String[] PORTRAIT = null;
    private static final int LENGTH = 2;

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
        return LENGTH;
    }
}
