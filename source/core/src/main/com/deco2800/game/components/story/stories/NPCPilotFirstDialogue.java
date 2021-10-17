package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCPilotFirstDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {
        "Hey kid!",
        "Looks like we are the only two Fireflies today...",
        "I guess people are too scared... you know, with the whole \"no people ever coming back\" thing.",
        "I say screw it! Better to have a quick death out here than die slowly in that prison of a city, am I right?",
        "* He looks down the road into the darkness ahead. Faint sounds of growling are heard in the distance. *",
        "Look, don't get close to them. Throw rocks at a distance by pressing ENTER.",
        "Also if one of them takes a bite out of you, then don't forget to heal up by pressing the number 1 key.",
        "Make sure you keep your torch well lit, as they are weaker in the light. Press T to toggle it on/off.",
        "Good luck, hopefully I will see you at the next safehouse!"
    };
    protected static final String[] PORTRAIT = {"npc-pilot-portrait"};

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
        return QUOTE.length;
    }
}
