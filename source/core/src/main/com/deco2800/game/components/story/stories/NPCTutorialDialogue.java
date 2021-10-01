package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCTutorialDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {"Greetings, young man.",
            "I heard you wanted to become a firefly.",
            "So I came to give you some advice.",
            "Use W A S D key to move, up, left, down, right.",
            "If you ever need to talk to someone, just press E.",
            "Press Shift key to dash a short distance. The blue bar indicates cooldown for your dash.",
            "Press Space to attack enemies with your melee weapon.",
            "Press Enter to throw rocks. If you run out of rocks, you can press R key to reload.",
            "I wish you best of luck in your journey."};
    protected static final String[] PORTRAIT = {"npc-tut1-portrait"};
    private static final int LENGTH = 9;

    public NPCTutorialDialogue(){
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
