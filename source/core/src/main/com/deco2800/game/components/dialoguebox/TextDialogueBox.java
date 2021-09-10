package com.deco2800.game.components.dialoguebox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.deco2800.game.components.story.StoryBase;
import com.deco2800.game.ui.UIComponent;

/**
 * Text display of dialogue
 * TODO: Move texture atlas component to screen to prevent loading multiple times
 */
public class TextDialogueBox extends UIComponent {
    private Dialogue dialogue;
    protected Table rootTable;
    private Label displayText;
    protected TextureAtlas textureAtlas;

    private float width;
    private float height;
    private float padding;

    private static final float DEFAULT_WIDTH = 1000f;
    private static final float DEFAULT_HEIGHT = 100f;
    private static final float DEFAULT_PADDING = 15f;
    private static final float IMAGE_OFFSET = 250f;

    private boolean isDead = false;

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
        entity.getEvents().addListener("advanceDialogue", this::advance);
    }

    /**
     * Creates UI actors and styles them
     */
    private void addActors() {
        rootTable = new Table();

        //Create background
        rootTable.setSkin(skin);
        addBackground(rootTable);

        //Add Text
        displayText = new Label(dialogue.getCurrentDialogue(), skin);
        displayText.setWrap(true);

        //Adjust text width if there is an image or not
        float labelWidth = width - padding;
        if (dialogue.getClass() == DialogueImage.class) {
            labelWidth -= IMAGE_OFFSET;
        }
        rootTable.add(displayText).width(labelWidth).pad(padding);

        //Add Image
        if (dialogue.getClass() == DialogueImage.class) {
            DialogueImage dialogueImage = (DialogueImage) dialogue;
            Image image = new Image(textureAtlas.findRegion(dialogueImage.getCurrentImagePath()));
            rootTable.add(image).height(250f).width(250f);
        }

        rootTable.pack();
        stage.addActor(rootTable);
    }

    /**
     * Adds the dialogue box background to the table
     * @param table table
     */
    protected void addBackground(Table table){
        NinePatch ninePatch = textureAtlas.createPatch("dialogue-box-background");
        ninePatch.scale(0.5f,0.5f);
        NinePatchDrawable patchDrawable = new NinePatchDrawable(ninePatch);
        table.setBackground(patchDrawable);
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
    public void advance(){
        if (!dialogue.hasNext()) {
            dispose();
            return;
        }
        displayText.setText(dialogue.next());
    }

    /**
     * Removes the dialogue box then adds it again in its current state
     */
    public void forceUpdate(){
        if (!isDead) {
            rootTable.remove();
            stage.addActor(rootTable);
        }
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
        isDead = true;
        displayText.remove();
        rootTable.remove();
    }

}
