package com.deco2800.game.components.story;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.dialoguebox.TextDialogueBox;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.DialogueBoxFactory;
import com.deco2800.game.entities.factories.ImageSequenceFactory;
import com.deco2800.game.services.ServiceLocator;


public class CutScene extends Component implements StoryBase{
    private final CutSceneConfig config;

    private Entity dialogueEntity;
    private Entity imageEntity;

    private int index = 0;
    private boolean isDead = false;

    CutScene(CutSceneConfig config){
        this.config = config;
    }

    @Override
    public void create() {
        config.create();

        dialogueEntity = DialogueBoxFactory.createRawTextDialogue(config.dialogue);

        imageEntity = ImageSequenceFactory.createImageSequence(config.images);

        config.music.setLooping(true);
        config.music.setVolume(0.5f);
        config.music.play();
    }

    @Override
    public void advance(){
        if (index < config.getLength()){
            index++;
            imageEntity.getComponent(ImageSequence.class).advance();
            dialogueEntity.getComponent(TextDialogueBox.class).advance();
            dialogueEntity.getComponent(TextDialogueBox.class).forceUpdate();
        } else {
            dispose();
            isDead = true;
        }
    }

    @Override
    public void display() {
        ServiceLocator.getEntityService().register(imageEntity);
        ServiceLocator.getEntityService().register(dialogueEntity);
    }

    @Override
    public void dispose() {
        super.dispose();
        config.music.stop();
        config.music.dispose();
    }

    @Override
    public boolean isDead() {
        return isDead;
    }
}