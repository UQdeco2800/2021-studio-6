package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCPilotFirstDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {
        "Hey kid",
        "You are going to be a firefly too?",
        "If you get yourself hurt, you can use '1' to heal.",
        "Small scratches will heal itself after a few seconds.",
        "But it's best to not get hurt in the first place, so don't let those crawlers get you.",
        "Most importantly-",
        "always watch your back. *wink*",
        "I hope to see you out there rookie."
    };
    protected static final String[] PORTRAIT = {"npc-pilot-portrait"};
    private static final int LENGTH = 8;

    public NPCPilotFirstDialogue(){
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
