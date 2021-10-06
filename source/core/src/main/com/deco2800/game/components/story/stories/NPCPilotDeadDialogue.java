package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCPilotDeadDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {
        "Guess I'm on my own again...",
        "Didn't even ask what his name was...",
        "Rest in peace Firefly."
    };
    protected static final String[] PORTRAIT = null;

    public NPCPilotDeadDialogue(){
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
