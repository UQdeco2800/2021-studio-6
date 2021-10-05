package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCDeadDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE = {"...", "*It's a dead body...*", "I think I knew them,",
            "and they left a few days ago..."};
    protected static final String[] PORTRAIT = null;
    private static final int LENGTH = 3;

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
