package com.deco2800.game.components.dialoguebox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.ui.UIComponent;

/**
 * Text display of dialogue
 */
public class TextDialogueBox extends UIComponent implements DialogueBox{
    private Dialogue dialogue;
    private Table rootTable;
    private Label displayText;
    private static final float WIDTH = 500f;
    private static final float HEIGHT = 100f;

    /**
     * Creates a new TextDialogueBox
     * @param dialogue Dialogue to be displayed
     */
    public TextDialogueBox(Dialogue dialogue){
        super();
        this.dialogue = dialogue;
    }

    /**
     * Creates UI and event listeners
     */
    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("advanceDialogue", this::advanceDialogue);
    }

    /**
     * Creates UI actors and styles them
     */
    private void addActors() {
        rootTable = new Table();
        displayText = new Label(dialogue.getCurrentDialogue(), skin);
        displayText.setWrap(true);
        rootTable.add(displayText).width(WIDTH);

        rootTable.setSkin(skin);
        rootTable.setBackground("list-c");
        rootTable.pack();

        stage.addActor(rootTable);
    }

    /**
     * Dialogue object being displayed
     * @return Dialogue
     */
    public Dialogue getDialogue() {
        return dialogue;
    }

    /**
     * Advances dialogue and updates text display
     */
    @Override
    public void advanceDialogue(){
        if (!dialogue.hasNext()) {
            entity.getEvents().trigger("closeDialogue");
            return;
        }
        displayText.setText(dialogue.next());
    }

    @Override
    protected void draw(SpriteBatch batch) {
        //Update positioning
        int screenHeight = Gdx.graphics.getHeight();
        int screenWidth = Gdx.graphics.getWidth();
        rootTable.setWidth(WIDTH);
        rootTable.setHeight(HEIGHT);
        rootTable.setPosition(screenWidth / 2f - rootTable.getWidth() / 2, screenHeight / 15f);
    }

    @Override
    public void dispose() {
        super.dispose();
        displayText.remove();
        rootTable.remove();
    }
}
