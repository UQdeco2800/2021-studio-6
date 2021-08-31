package com.deco2800.game.components.dialoguebox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.deco2800.game.ui.UIComponent;
import org.w3c.dom.Text;

/**
 * Text display of dialogue
 * TODO: Move texture atlas component to screen to prevent loading multiple times
 */
public class TextDialogueBox extends UIComponent implements DialogueBox{
    private Dialogue dialogue;
    private Table rootTable;
    private Label displayText;
    private TextureAtlas textureAtlas;

    private float width;
    private float height;
    private float padding;

    private static final float DEFAULT_WIDTH = 500f;
    private static final float DEFAULT_HEIGHT = 100f;
    private static final float DEFAULT_PADDING = 15f;

    /**
     * Creates a new TextDialogueBox
     * @param dialogue Dialogue to be display
     * @param width width of the box
     * @param height height of the box
     * @param padding padding applied to box
     */
    public TextDialogueBox(Dialogue dialogue, float width, float height, float padding) {
        super();
        this.dialogue = dialogue;
        this.width = width;
        this.height = height;
        this.padding = padding;
        textureAtlas = new TextureAtlas(Gdx.files.internal("images/dialogue/dialogue.atlas"));
    }

    /**
     * Creates a new TextDialogueBox
     * @param dialogue Dialogue to be displayed
     */
    public TextDialogueBox(Dialogue dialogue){
        this(dialogue, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_PADDING);
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

        rootTable.add(displayText).width(width - padding).pad(padding);

        rootTable.setSkin(skin);

        NinePatch ninePatch = textureAtlas.createPatch("dialogue-box-background");
        ninePatch.scale(0.5f,0.5f);
        NinePatchDrawable patchDrawable = new NinePatchDrawable(ninePatch);

        rootTable.setBackground(patchDrawable);



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
        rootTable.setWidth(width);
        rootTable.setHeight(height);
        rootTable.setPosition(screenWidth / 2f - rootTable.getWidth() / 2, screenHeight / 15f);
    }

    @Override
    public void dispose() {
        super.dispose();
        displayText.remove();
        rootTable.remove();
    }
}
