package com.deco2800.game.ui.terminal.commands;

import com.deco2800.game.components.dialoguebox.Dialogue;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.DialogueBoxFactory;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;

public class PrintDialogueCommand implements Command{

    /**
     * Action a command.
     *
     * @param args command args
     * @return command was successful
     */
    @Override
    public boolean action(ArrayList<String> args) {
        String dialogue = createDialogueString(args);
        Entity dialogueText = DialogueBoxFactory.createTextDialogue(dialogue);
        ServiceLocator.getEntityService().register(dialogueText);
        return true;
    }

    protected String createDialogueString(ArrayList<String> args){
        StringBuilder dialogue = new StringBuilder();
        for (String arg: args) {
            dialogue.append(arg).append(" ");
        }
        return dialogue.toString();
    }
}
