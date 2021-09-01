package com.deco2800.game.entities.factories;

import com.deco2800.game.components.dialoguebox.Dialogue;
import com.deco2800.game.components.dialoguebox.DialogueInputComponent;
import com.deco2800.game.components.dialoguebox.TextDialogueBox;
import com.deco2800.game.entities.Entity;

import java.util.Arrays;
import java.util.List;

/**
 * Factory to create a dialogue
 */
public class DialogueBoxFactory {
    /**
     * Creates a text dialogue from a Dialogue
     * @param dialogue Dialogue object
     * @return TextDialogue Entity
     */
    public static Entity createTextDialogue(Dialogue dialogue) {
        Entity textDialogue = new Entity()
                .addComponent(new DialogueInputComponent())
                .addComponent(new TextDialogueBox(dialogue));
        addCloseListener(textDialogue);
        return textDialogue;
    }

    /**
     * Creates a pure text dialogue from a string
     * @param dialogue Semicolon seperated string of dialogue
     * @return TextDialogue Entity
     */
    public static Entity createTextDialogue(String dialogue) {
        List<String> dialogues = Arrays.asList(dialogue.split(";"));
        return createTextDialogue(new Dialogue(dialogues));
    }

    /**
     * Adds closeDialogue listener to an entity. When received the entity disposes itself
     * @param entity entity
     */
    private static void addCloseListener(Entity entity){
        entity.getEvents().addListener("closeDialogue", entity::dispose);
    }
}
