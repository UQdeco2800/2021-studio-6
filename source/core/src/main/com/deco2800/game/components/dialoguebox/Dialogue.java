package com.deco2800.game.components.dialoguebox;


import com.deco2800.game.components.Component;

import java.util.List;

/**
 * Dialogue container storing text
 * <p>Has similar functionality to an Iterator. This has been created in case that
 * advanced functionality is needed later</p>
 */
public class Dialogue extends Component {
    private final List<String> allDialogue;
    private int dialogueIndex;

    /**
     * Creates a new Dialogue
     * @param allDialogue List containing text to be shown
     */
    public Dialogue(List<String> allDialogue) {
        this.allDialogue = allDialogue;
        dialogueIndex = 0;
    }

    /**
     * Advances dialogue by one and returns it
     * @return Next text in dialogue. Repeats last text if at end.
     */
    public String next() {
        if (hasNext()) {
            dialogueIndex++;
        }
        return getCurrentDialogue();
    }

    /**
     * Returns whether there is dialogue left.
     * @return True if there is dialogue left. False if else.
     */
    public boolean hasNext() {
        return dialogueIndex < allDialogue.size() - 1;
    }

    /**
     * Gets the current dialogue text
     * @return String containing dialogue
     */
    public String getCurrentDialogue() {
        return allDialogue.get(dialogueIndex);
    }
}
