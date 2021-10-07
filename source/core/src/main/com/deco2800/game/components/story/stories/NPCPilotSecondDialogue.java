package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCPilotSecondDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {
        "Well, look what the cat dragged in...",
        "You actually made it!",
        "Didn't think you had it in you, kiddo.",
        "One of those little buggers took a chunk out of my arm on the way over...",
        "Selfish bastards...",
        "So... The strange man over there runs the safehouse. Looks like he will sell you anything you need if you have the coin.",
        "I wouldn't trust him though... I mean, have you seen his eyes?",
        "They aren't human...",
        "Anyway... I have heard rumours that someone left a sailboat at the Old Eastpoint Docks...",
        "What do you say? We could take the boat, head to the Safe Haven together?",
        "I just need to make a quick stop somewhere first, so I won't be at the next safehouse.",
        "See you at the docks kid. Be quick or I'll leave without you!"
    };
    protected static final String[] PORTRAIT = {"npc-pilot-injured-portrait"};

    public NPCPilotSecondDialogue(){
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
