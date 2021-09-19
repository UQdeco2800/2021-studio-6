package com.deco2800.game.components.shopmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.GdxGame;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.MenuUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to display a popup box for shop when player intends to purchase
 * an item, weapon, armor and etc.
 */
public class ShopMenuDisplay extends UIComponent {
    private static final String BACKGROUND_FILE_PATH = "images/placeholder.png";
    private static final String MENU_BUTTON_STYLE = "menu-button-large";
    private static final Logger logger = LoggerFactory.getLogger(ShopMenuDisplay.class);
    private final GdxGame game;
    private boolean isEnabled = false;
    private Table container;
    private Label shopTitleLabel;
    private Image background;
    private static final float MAX_BOX_WIDTH = 1000;
    private static final float MAX_BOX_HEIGHT = 600;
    private static final float TABLE_HEADER_HEIGHT = 50;
    private static final int COL_NUM = 100;
    private static final int CELL_PADDING = 10;

    public ShopMenuDisplay(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();

        entity.getEvents().addListener("toggleShopBox", this::toggleShopBox);
    }

    public void toggleShopBox() {
        GameTime timeSource = ServiceLocator.getTimeSource();

        if (!isEnabled) {
            timeSource.pause();
            container.setVisible(true);
            background.setVisible(true);
            background.toFront();
            container.toFront();
            isEnabled = true;
        } else {
            container.setVisible(false);
            background.setVisible(false);
            background.toBack();
            container.toBack();
            timeSource.unpause();
            isEnabled = false;
        }
    }

    /**
     * Creates all relevant tables related to the shop popup box in game
     */
    private void addActors() {
        background = new Image(ServiceLocator.getResourceService().getAsset(BACKGROUND_FILE_PATH, Texture.class));
        container = new Table();

        container.row().colspan(COL_NUM).expandX().height(TABLE_HEADER_HEIGHT);
        createTableTitle();
        container.setSize(MAX_BOX_WIDTH,MAX_BOX_HEIGHT);
        background.setSize(MAX_BOX_WIDTH,MAX_BOX_HEIGHT);
        container.debug();

        stage.addActor(background);
        stage.addActor(container);
        container.setVisible(false);
        background.setVisible(false);
    }

    /**
     * Configures position of menu box
     */
    private void setShopBoxPosSize() {
        container.setPosition(stage.getWidth() / 2 - container.getWidth() / 2,
                stage.getHeight() / 2 - container.getHeight() / 2);
        background.setPosition(stage.getWidth() / 2 - container.getWidth() / 2,
                stage.getHeight() / 2 - container.getHeight() / 2);
    }

    private void createTableTitle() {
        CharSequence titleText = "SHOP";
        shopTitleLabel = new Label(titleText, skin, "white");
        container.add(shopTitleLabel).colspan(99).align(Align.center).padLeft(100);

        TextButton closeBtn = new TextButton("X", skin, MENU_BUTTON_STYLE);
        // Closes shop box when close button is clicked
        MenuUtility.addButtonSelectListener(entity, closeBtn, "toggleShopBox");
        container.add(closeBtn).colspan(1).align(Align.right);
    }

    public boolean checkShopPopupVisibility() {
        return container.isVisible();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        setShopBoxPosSize();
    }
}
