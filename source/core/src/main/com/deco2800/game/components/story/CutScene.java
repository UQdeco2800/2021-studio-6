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

    CutScene(CutSceneConfig config){
        this.config = config;
    }

    @Override
    public void create() {
        config.create();

        dialogueEntity = DialogueBoxFactory.createTextDialogue(config.dialogue);

        imageEntity = ImageSequenceFactory.createImageSequence(config.images);

        config.music.setLooping(true);
        config.music.setVolume(0.5f);
        config.music.play();
    }

    @Override
    public void advance(){
        imageEntity.getComponent(ImageSequence.class).advance();
        dialogueEntity.getComponent(TextDialogueBox.class).advance();
    }

    public void display() {
        ServiceLocator.getEntityService().register(dialogueEntity);
        ServiceLocator.getEntityService().register(imageEntity);
    }

}