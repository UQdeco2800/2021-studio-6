package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCPilotDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {
        "Hey kid",
        "You heading to the safehouse?",
        "Watch out for the green eggsacks",
        "They spawn those little shadow crawler bastards!",
        "Sure they may look small...",
        "But when there are three coming straight for you...",
        "You may as well kiss your ass goodbye!"
    };
    protected static final String[] PORTRAIT = {"npc-pilot-portrait"};
    private static final int LENGTH = 7;

    public NPCPilotDialogue(){
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
