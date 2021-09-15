package com.deco2800.game.components.story.basicdialogue;

import com.deco2800.game.components.dialoguebox.Dialogue;
import com.deco2800.game.components.dialoguebox.DialogueImage;
import com.deco2800.game.components.story.StoryBase;
import com.deco2800.game.components.story.StoryConfig;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicDialogueConfig implements StoryConfig {

    public Dialogue dialogue;

    protected String[] dialogueText;
    protected String[] dialogueImages;

    /**
     * Create all the assets defined
     */
    @Override
    public void create() {
        if (dialogueImages == null) {
            dialogue = new Dialogue(new ArrayList<>(List.of(dialogueText)));
        } else {
            dialogue = new DialogueImage(new ArrayList<>(List.of(dialogueText)),
                    new ArrayList<>(List.of(dialogueImages)));
        }
    }

    /**
     * Returns a story configured with the current config file
     *
     * @return a story base object
     */
    @Override
    public StoryBase initialiseStory() {
        return new BasicDialogue(this);
    }

}
