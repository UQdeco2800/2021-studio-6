package com.deco2800.game.components.story.stories;

import com.deco2800.game.components.story.basicdialogue.BasicDialogueConfig;

public class NPCTutorialDialogue extends BasicDialogueConfig {
    protected static final String[] QUOTE= {
        "Oh thank the heavens, another Firefly.",
        "I don't look it, but I used to be a Firefly like you.",
        "We were actually one of the first groups to go out into the darkness...",
        "But... by the time it was just me... well I...",
        "* He stares blankly into nothingness for a second before focusing back on you *",
        "Anyway you probably already know how things work, but here is a quick run down.",
        "To move around use the W, A, S, and D keys.",
        "To dash you press LEFT SHIFT.",
        "If you want to use your special ability you can press the Q key.",
        "Use it wisely as it has a long cooldown. When the blue bar in the bottom-left is full then you can use it again.",
        "If any Shadow Crawlers get close, press SPACE to use your melee weapon.",
        "Oh, and there is one other Firefly going out today, he's that guy in the jacket over there.",
        "I think you should go talk to him by pressing the E key.",
        "If you make it to the Safe Haven... Please get them to send help immediately.",
        "The city isn't going to last much longer...",
        "May the light stay with you."
        };
    protected static final String[] PORTRAIT = {"npc-tutorial-portrait"};

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
        return QUOTE.length;
    }
}
