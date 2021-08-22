package com.deco2800.game.components.dialoguebox;


import com.deco2800.game.components.Component;

public class Dialogue extends Component {
    private String[] allDialogue;
    private int dialogueIndex;

    public Dialogue(String[] allDialogue) {
        this.allDialogue = allDialogue;
        dialogueIndex = 0;
    }

    public void next() {
        dialogueIndex++;
    }

    public String getCurrentDialogue() {
        return allDialogue[dialogueIndex];
    }
}
