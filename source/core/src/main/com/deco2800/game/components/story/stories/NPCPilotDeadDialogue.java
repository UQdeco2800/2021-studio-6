package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCPilotDeadDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {
        "Dead person"
    };
    protected static final String[] PORTRAIT = {"npc-pilot-portrait"};
    private static final int LENGTH = 1;

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
        return LENGTH;
    }
}
