package com.deco2800.game.components.dialoguebox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.ui.UIComponent;

public class TextDialogueBox extends UIComponent {
    private Dialogue dialogue;
    private Table rootTable;
    private Label displayText;

    public TextDialogueBox(Dialogue dialogue){
        super();
        this.dialogue = dialogue;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        rootTable = new Table();
        displayText = new Label(dialogue.getCurrentDialogue(), skin, "font");
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void dispose() {
        super.dispose();
        displayText.remove();
        rootTable.remove();
    }
}
