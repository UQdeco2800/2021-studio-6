package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCInjuredDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE = {
        "* cough cough *",
        "\"Join the Fireflies\", they said...",
        "\"You can really make a difference\", they said...",
        "Don't believe that crap.",
        "There is no safe haven. It's all lies to give us some false hope.",
        "* The injured person coughs trying not to choke on their own blood *",
        "The darkness will consume us all eventually...",
        "Why even try?..."
        };
    protected static final String[] PORTRAIT = {"injured-portrait"};

    public NPCInjuredDialogue(){
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
