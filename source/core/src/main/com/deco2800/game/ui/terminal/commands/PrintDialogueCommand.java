package com.deco2800.game.ui.terminal.commands;

import com.deco2800.game.components.dialoguebox.Dialogue;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.DialogueFactory;
import com.deco2800.game.services.ServiceLocator;
import net.dermetfan.gdx.physics.box2d.PositionController;

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
        StringBuilder dialogue = new StringBuilder();
        for (String arg: args) {
            dialogue.append(arg).append(" ");
        }
        Entity dialogueText = DialogueFactory.createTextDialogue(dialogue.toString());
        ServiceLocator.getEntityService().register(dialogueText);
        return true;
    }
}
