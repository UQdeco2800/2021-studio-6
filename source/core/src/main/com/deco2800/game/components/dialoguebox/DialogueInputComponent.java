package com.deco2800.game.components.dialoguebox;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.deco2800.game.input.InputComponent;

/**
 * Input handler for dialogue
 */
public class DialogueInputComponent extends InputComponent {
    private static final int ADVANCE_KEY = Input.Keys.SPACE;

    public DialogueInputComponent() {
        super(7);
    }

    /**
     * Triggers advance dialogue event on ADVANCE_KEY
     * @return whether input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == ADVANCE_KEY){
            entity.getEvents().trigger("advanceDialogue");
            return true;
        }
        return false;
    }
}
