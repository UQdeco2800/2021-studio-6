package com.deco2800.game.components.story.basicdialogue;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.dialoguebox.TextDialogueBox;
import com.deco2800.game.components.story.NoStoryLoadedException;
import com.deco2800.game.components.story.StoryBase;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.DialogueBoxFactory;
import com.deco2800.game.services.ServiceLocator;

/**
 * Basic dialogue (Sprint 1 dialogue box)
 */
public class BasicDialogue extends Component implements StoryBase {
    private final BasicDialogueConfig config;

    private Entity dialogue;

    private int index = 0;
    private boolean isDead = false;


    BasicDialogue(BasicDialogueConfig config){
        this.config = config;
    }

    /**
     * Creates the story
     */
    @Override
    public void create() {
        config.create();
        dialogue = DialogueBoxFactory.createRawTextDialogue(config.dialogue);
    }

    /**
     * Advance the story
     */
    @Override
    public boolean advance() {
        if (index < config.getLength()) {
            index++;
            dialogue.getComponent(TextDialogueBox.class).advance();

            if (index == config.getLength()) {
                dispose();
            }
            return true;
        } else {
            dispose();
            return false;
        }
    }

    /**
     * Displays the story. Throws exception when story has not been created yet
     */
    @Override
    public void display() throws NoStoryLoadedException {
        ServiceLocator.getEntityService().register(dialogue);
    }

    /**
     * Dispose the story
     */
    @Override
    public void dispose() {
        super.dispose();
        isDead = true;
        dialogue.dispose();
    }

    /**
     * Returns if the story has already been disposed
     *
     * @return if the story is dead
     */
    @Override
    public boolean isDead() {
        return isDead;
    }
}
