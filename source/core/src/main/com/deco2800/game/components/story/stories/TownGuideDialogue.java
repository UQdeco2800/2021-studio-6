package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class TownGuideDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE = {"Welcome stranger!", "There is a safe house at the end of the street, good luck!"};
    protected static final String[] PORTRAIT = {"npc-portrait"};
    private static final int LENGTH = 2;

    public TownGuideDialogue(){
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
