package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCPilotDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {
        "Hey kid",
        "You are going to be a firefly too?",
        "Well, as someone who's been there and back...",
        "You better watch out for the shadow crawlers.",
        "It's hard to see them in the dark,",
        "and by the time you see them with your light",
        "You'll be done for!!!",
        "Stay safe out there, the glow is a promised land.",
        "This badge is a proof of bravery... *goes on about his achievements*"
    };
    protected static final String[] PORTRAIT = {"npc-pilot-portrait"};
    private static final int LENGTH = 9;

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
