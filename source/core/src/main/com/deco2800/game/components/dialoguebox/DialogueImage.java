package com.deco2800.game.components.dialoguebox;

import java.util.ArrayList;
import java.util.List;

public class DialogueImage extends Dialogue{

    private final List<String> imagePaths;

    /**
     * Creates a new DialogueImage with multiple images
     *
     * @param allDialogue List containing text to be shown
     * @param imagePaths List containing images to be shown
     */
    public DialogueImage(List<String> allDialogue, List<String> imagePaths) {
        super(allDialogue);
        this.imagePaths = imagePaths;
    }

    /**
     * Creates a new DialogueImage with a single image
     *
     * @param allDialogue List containing text to be shown
     * @param imagePath image to be shown
     */
    public DialogueImage(List<String> allDialogue, String imagePath) {
        super(allDialogue);
        imagePaths = new ArrayList<>();
        for (int i = 0; i < allDialogue.size(); i++) {
            imagePaths.add(imagePath);
        }
    }

    /**
     * Gets the current image
     * @return path to image
     */
    public String getCurrentImagePath(){
        return imagePaths.get(getDialogueIndex());
    }
}
