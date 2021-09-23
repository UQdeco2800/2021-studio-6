package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class TutorialDiologue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {"Greetings, young man.",
            "So you want to become a firefly?",
            "Use W A S D key to move, up, left, down, right.",
            "Press Shift key to dash a short distance. The blue bar indicates cooldown for your dash.",
            "Press Space to attack enemies with your melee weapon.",
            "Press Enter to throw rocks. If you run out of rocks, you can press R key to reload.",
            "Good luck have fun!"};
    protected static final String[] PORTRAIT = {"npc-tut1-portrait"};
    private static final int LENGTH = 7;

    public TutorialDiologue(){
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
