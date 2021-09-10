package com.deco2800.game.components.story;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.dialoguebox.TextDialogueBox;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.DialogueBoxFactory;
import com.deco2800.game.entities.factories.ImageSequenceFactory;
import com.deco2800.game.services.ServiceLocator;

/**
 * A story consisting of an image sequence, music and dialogue
 */
public class CutScene extends Component implements StoryBase{
    private final CutSceneConfig config;

    private Entity dialogueEntity;
    private Entity imageEntity;

    private int index = 0;
    private boolean isDead = false;

    /**
     * Creates a cutscene given a config file
     * @param config config file
     */
    CutScene(CutSceneConfig config){
        this.config = config;
    }

    /**
     * Creates the assets necessary for the cutscene
     */
    @Override
    public void create() {
        config.create();

        dialogueEntity = DialogueBoxFactory.createRawTextDialogue(config.dialogue);
        imageEntity = ImageSequenceFactory.createImageSequence(config.images);
    }

    /**
     * Advances the cutscene. Disposes of itself when there is no more story left
     */
    @Override
    public void advance(){
        if (index < config.getLength()){
            index++;
            imageEntity.getComponent(ImageSequence.class).advance();
            dialogueEntity.getComponent(TextDialogueBox.class).advance();
            dialogueEntity.getComponent(TextDialogueBox.class).forceUpdate();

            if (index == config.getLength()) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    /**
     * Shows and displays the cutscene
     */
    @Override
    public void display() {
        ServiceLocator.getEntityService().register(imageEntity);
        ServiceLocator.getEntityService().register(dialogueEntity);
        config.music.setLooping(true);
        config.music.setVolume(0.5f);
        config.music.play();
    }

    /**
     * Disposes the cutscene and related elements
     */
    @Override
    public void dispose() {
        super.dispose();
        isDead = true;
        config.music.stop();
        config.music.dispose();
    }

    /**
     * Returns if the Cutscene has already been disposed
     * @return if the cutscene is dead
     */
    @Override
    public boolean isDead() {
        return isDead;
    }
}