package com.deco2800.game.components.shopmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    private Table container, headerTable ,shopKeeperTable, itemsButtonTable, itemsDescriptionTable;
    private Label shopTitleLabel;
    private Image background;

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
        logger.info("SHOP BOX TOGGLE CALL");
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

        createTableTitle();
        container.row();
        container.add(headerTable).center();
        container.padTop(0);
        container.padBottom(0);
        container.setSize(500,500);
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
        float stageWidth = stage.getWidth();
        float stageHeight = stage.getHeight();

        container.setPosition(stage.getWidth() / 2 - container.getWidth() / 2,
                stage.getHeight() / 2 - container.getHeight() / 2);
        background.setPosition(stage.getWidth() / 2 - container.getWidth() / 2,
                stage.getHeight() / 2 - container.getHeight() / 2);
    }

    private void createTableTitle() {
        headerTable = new Table();
        CharSequence titleText = "SHOP";
        shopTitleLabel = new Label(titleText, skin, "large");
        headerTable.add(shopTitleLabel);

        TextButton closeBtn = new TextButton("Close", skin, MENU_BUTTON_STYLE);

        // Closes shop box when close button is clicked
        MenuUtility.addButtonSelectListener(entity, closeBtn, "toggleShopBox");
        headerTable.add(closeBtn);
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }
}
