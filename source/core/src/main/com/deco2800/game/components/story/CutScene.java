package com.deco2800.game.components.story;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.dialoguebox.Dialogue;
import com.deco2800.game.components.dialoguebox.TextDialogueBox;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.DialogueBoxFactory;
import com.deco2800.game.entities.factories.ImageSequenceFactory;
import com.deco2800.game.services.ServiceLocator;

import java.util.List;

public abstract class CutScene extends Component implements StoryBase{
    private final Dialogue dialogue;
    private final Music music;
    private final List<Image> images;

    private Entity dialogueEntity;
    private Entity imageEntity;

    CutScene(Dialogue dialogue, Music music, List<Image> images){
        this.dialogue = dialogue;
        this.music = music;
        this.images = images;
    }

    @Override
    public void create() {
        dialogueEntity = DialogueBoxFactory.createTextDialogue(dialogue);

        imageEntity = ImageSequenceFactory.createImageSequence(images);

        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
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