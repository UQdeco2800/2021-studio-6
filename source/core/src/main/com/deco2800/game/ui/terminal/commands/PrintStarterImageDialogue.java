package com.deco2800.game.ui.terminal.commands;

import com.deco2800.game.components.dialoguebox.Dialogue;
import com.deco2800.game.components.dialoguebox.DialogueImage;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.DialogueBoxFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;

public class PrintStarterImageDialogue extends PrintDialogueCommand{

    @Override
    public boolean action(ArrayList<String> args) {
        String quote = "OH NO!\nThe light - it's disappearing. I have to make it to the safe house before the " +
                "darkness gets to me!";
        ArrayList<String> a = new ArrayList<>();
        a.add(quote);
        Dialogue dialogue = new DialogueImage(a, "player-portrait");
        Entity dialogueEntity = DialogueBoxFactory.createTextDialogue(dialogue);
        ServiceLocator.getEntityService().register(dialogueEntity);
        return true;
    }

}
