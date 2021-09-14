package com.deco2800.game.components.story;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.deco2800.game.input.InputComponent;

/**
 * Input component for story input
 */
public class StoryInputComponent extends InputComponent {
    private static final int ADVANCE_KEY = Input.Keys.SPACE;
    private static final int SKIP_KEY = Input.Keys.ESCAPE;

    public StoryInputComponent() {
        super(7);
    }

    /**
     * Triggers advance dialogue event on ADVANCE_KEY
     * @return whether input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        if (StoryManager.getInstance().isDisplaying()) {
            if (keycode == SKIP_KEY) {
                StoryManager.getInstance().disposeLoadedStory();
                return true;
            }

            if (keycode == ADVANCE_KEY) {
                entity.getEvents().trigger(StoryManager.ADVANCE_LISTENER);
                return true;
            }
        }
        return false;
    }
}
