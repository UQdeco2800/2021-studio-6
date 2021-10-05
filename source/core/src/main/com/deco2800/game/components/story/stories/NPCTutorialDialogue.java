package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCTutorialDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {"*Cough Cough* Well, hello there.",
            "I heard you wished to go out there and join the Fireflies.",
            "So I came to give you some advice.",
            "Use 'W', 'A', 'S', 'D' key to move, up, left, down, right.",
            "If you ever need to talk to someone, just press 'E' key.",
            "Press 'Shift' key to dash a short distance.",
            "Press 'E' to use your special ability. The blue bar indicates its cooldown.",
            "Press 'Space' to attack enemies with your melee weapon.",
            "Press 'Enter' to throw rocks. If you run out of rocks, you can press 'R' key to reload.",
            "I wish you best of luck on your journey, and may the light guide you."};
    protected static final String[] PORTRAIT = {"npc-tutorial-portrait"};
    private static final int LENGTH = 10;

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