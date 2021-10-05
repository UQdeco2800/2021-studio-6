package com.deco2800.game.components.shopmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerAbilitiesComponent;
import com.deco2800.game.components.player.PlayerMeleeAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.entities.configs.ShopItemInfoConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.items.Abilities;
import com.deco2800.game.items.Items;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.MenuUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used to display a popup box for shop when player intends to purchase
 * an item, weapon, armor and etc.
 */
public class ShopMenuDisplay extends UIComponent {
    private static final String BACKGROUND_FILE_PATH = "images/safehouse/shopScreenTempSelections.png";
    private static final String SHOP_KEEPER_IMAGE_FILE_PATH = "images/safehouse/shopkeeper/portrait.png";
    private static final String PLAYER_IMAGE_FILE_PATH = "images/Player_Sprite/front01.png";
    private static final String CROWBAR_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopCrowbarSelected.png";
    private static final String CROWBAR_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopCrowbar.png";
    private static final String DAGGER_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopDaggerSelected.png";
    private static final String DAGGER_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopDagger.png";
    private static final String AXE_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopAxeSelected.png";
    private static final String AXE_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopAxe.png";
    private static final String ARMOR_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopArmourSelected.png";
    private static final String ARMOR_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopArmour.png";
    private static final String HELMET_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopHelmetSelected.png";
    private static final String HELMET_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopHelmet.png";
    private static final String TORCH_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopTorchSelected.png";
    private static final String TORCH_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopTorch.png";
    private static final String DASH_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopDashSelected.png";
    private static final String DASH_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopDash.png";
    private static final String INVINCIBLE_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopInvincibilitySelected.png";
    private static final String INVINCIBLE_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopInvincibility.png";
    private static final String GRENADE_UP_IMAGE_FILE_PATH = "images/playeritems/firecracker/firecracker8.png";
    private static final String GRENADE_DOWN_IMAGE_FILE_PATH = "images/playeritems/firecracker/firecracker7.png";
    private static final String BANDAGE_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopBandageSelected.png";
    private static final String BANDAGE_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopBandage.png";
    private static final String MACHETE_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopMacheteSelected.png";
    private static final String MACHETE_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopMachete.png";
    private static final String BASEBALL_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopBatSelected.png";
    private static final String BASEBALL_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopBat.png";
    private static final String SLEDGE_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopHammerSelected.png";
    private static final String SLEDGE_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopHammer.png";
    private static final String MENU_BUTTON_STYLE = "menu-button-large";
    private static final Logger logger = LoggerFactory.getLogger(ShopMenuDisplay.class);
    private final GdxGame game;
    private boolean isEnabled = false;
    private Table container;
    private Label itemSelectedTitleLabel, itemSelectedDescriptionLabel, pricingValueLabel,
            itemSelectedMoreInfoLabel;
    private Label bandageLabel, ammoLabel, coinLabel;
    private Image background;
    private static final float MAX_BOX_WIDTH = 1200;
    private static final float MAX_BOX_HEIGHT = 450;
    private static final float SMALL_PAD_TOP = 5;
    private static final float PAD_TOP = 10;
    private static final float EXTRA_PAD_TOP = 30;
    private static final float EXTRA_PAD_BOTTOM = 30;
    private static final float PAD_TO_CENTER_PRICING = 38;
    private static final float TABLE_HEADER_HEIGHT = 50;
    private static final float COMMON_LABEL_HEIGHT = 50;
    private static final float ITEM_LABEL_DESCRIPTION_HEIGHT = 150;
    private static final float ITEM_LABEL_MORE_INFO_HEIGHT = 60;
    private static final float TABLE_BODY_HEIGHT = 350;
    private static final float SHOPKEEPER_IMAGE_WIDTH = 280;
    private static final float COMMON_IMAGE_SIZE = 100;
    private static final float LARGER_IMAGE_SIZE = 160;
    private static final int COL_NUM = 120;
    private static final int FIRST_COL_NUM_TAKEN = 30;
    private static final int SEC_COL_NUM_TAKEN = 30;
    private static final int THIRD_COL_NUM_TAKEN = 30;
    private static final int FOURTH_COL_NUM_TAKEN = 30;
    private static final int IMAGE_BUTTON_HEIGHT = 70;
    private static final float BANDAGE_SIDE_LENGTH = 50f;
    private static final int NO_FUNDS_INDEX = 0;
    private static final int EQUIPPED_INDEX = 1;
    public static final int ADD_BANDAGE = 1;
    private static final int PURCHASED_INDEX = 2;
    private ImageButton crowbarImageButton, daggerImageButton, axeImageButton, armorImageButton,
            helmetImageButton, torchImageButton, bandageImageButton, dashImageButton, invincibleImageButton,
            macheteImageButton, baseballImageButton, sledgeImageButton;
    private final ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private int itemPrice, itemDefenceLevel = 0;
    private String itemName;
    private ImageButton storeButtonClicked = null;
    // values below will be checked and will load values from config file that saves player's states
    private int playerAmmo, playerGold, playerBandage, playerDefenceLevel;
    private String playerAbility, playerMeleeWeaponType, playerArmorType;
    private Items typeOfItem;
    private Stack feedbackStack;
    private Entity playerState;

    public ShopMenuDisplay(GdxGame game) {
        this.game = game;

    }

    @Override
    public void create() {
        super.create();
        addActors();

        entity.getEvents().addListener("toggleShopBox", this::toggleShopBox);
        entity.getEvents().addListener("updateItemDescription", this::updateItemDetails);
        entity.getEvents().addListener("purchaseItem", this::isItemPurchasable);
    }

    /**
     * Creates and destroy shop popup box. If player collides with shop keeper entity, popup pox will be
     * created and when player closest (with esc or clicking X button), popup box closes as well
     */
    public void toggleShopBox() {
        GameTime timeSource = ServiceLocator.getTimeSource();
        ServiceLocator.getGameArea().player.getEvents().trigger("resetPlayerMovements");

        if (!isEnabled) {
            loadPlayerData();
            timeSource.pause();

            // feedback tend to remain on shop menu display in different safe house areas, this ensures it doesn't
            hideFeedbackLabels();

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
     * Creates all relevant tables related to the shop popup box in game. Mainly consist of 4 major columns -
     * a column for a full blown image of shop keeper image, a column for all items that can be bought from
     * shop, a column for description of each item which dynamically changes upon clicking a different weapon on
     * popup box and a column for player's current inventory and buttons to purchase items (along with feedback
     * when a purchase is attempted by player). All labels, buttons and image buttons are added to container
     * (table)
     */
    private void addActors() {
        background = new Image(ServiceLocator.getResourceService().getAsset(BACKGROUND_FILE_PATH, Texture.class));
        container = new Table();

        addHeaderToTable();
        addShopKeeperImageToTable();
        addItemButtonImagesToTable();
        addItemDescriptionToTable();
        addPlayerInfoAndBuyingButtonToTable();

        stage.addActor(background);
        stage.addActor(container);
        container.setSize(MAX_BOX_WIDTH,MAX_BOX_HEIGHT);
        background.setSize(MAX_BOX_WIDTH,MAX_BOX_HEIGHT);
        container.setVisible(false);
        background.setVisible(false);
    }

    /**
     * Configures position of menu box to the middle of screen
     */
    private void setShopBoxPosSize() {
        container.setPosition(stage.getWidth() / 2 - container.getWidth() / 2,
                stage.getHeight() / 2 - container.getHeight() / 2);
        background.setPosition(stage.getWidth() / 2 - container.getWidth() / 2,
                stage.getHeight() / 2 - container.getHeight() / 2);
    }

    /**
     * Adds title of popup box along with button to close popup box
     */
    private void addHeaderToTable() {
        CharSequence titleText = "SHOP";
        Label shopTitleLabel = new Label(titleText, skin, "white");
        container.row().colspan(COL_NUM).height(TABLE_HEADER_HEIGHT);
        container.add(shopTitleLabel).colspan(119).expandX().padLeft(100);

        TextButton closeBtn = new TextButton("X", skin, MENU_BUTTON_STYLE);
        // Closes shop box when close button is clicked
        MenuUtility.addButtonSelectListener(entity, closeBtn, "toggleShopBox");
        container.add(closeBtn).colspan(1).align(Align.right).expandX().row();
    }

    /**
     * Creates the first column of the table which only include image of shop keeper
     */
    private void addShopKeeperImageToTable() {
        Image shopkeeperImage = new Image(ServiceLocator.getResourceService().getAsset(SHOP_KEEPER_IMAGE_FILE_PATH,
                Texture.class));
        container.add(shopkeeperImage).colspan(FIRST_COL_NUM_TAKEN).height(TABLE_BODY_HEIGHT)
                .width(SHOPKEEPER_IMAGE_WIDTH).top();
    }

    /**
     * Creates image buttons for items that can be clicked and bought from shop
     */
    private void addItemButtonImagesToTable() {
        Table itemsLabelImages = new Table();

        // by default - crowbar will be pre-selected first
        CharSequence titleText = "BASEBALL";
        itemName = "BAT";
        typeOfItem = Items.MELEE_WEAPONS;
        itemSelectedTitleLabel = new Label(titleText, skin, "white");
        // for weapon label selected - default is baseball
        itemsLabelImages.row();
        itemSelectedTitleLabel.setAlignment(Align.center);
        itemsLabelImages.add(itemSelectedTitleLabel).colspan(SEC_COL_NUM_TAKEN).height(COMMON_LABEL_HEIGHT).width(250);

        // first row
        // machete, crowbar and sledge
        itemsLabelImages.row().padTop(PAD_TOP);
        Drawable daggerUp = createImagesForButtons(DAGGER_UP_IMAGE_FILE_PATH);
        Drawable daggerDown = createImagesForButtons(DAGGER_DOWN_IMAGE_FILE_PATH);
        daggerImageButton = new ImageButton(daggerUp, daggerDown, daggerDown);
        MenuUtility.addButtonSelectListener(entity, daggerImageButton, "updateItemDescription",
                "configs/ShopDaggerInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(daggerImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable baseballUp = createImagesForButtons(BASEBALL_UP_IMAGE_FILE_PATH);
        Drawable baseballDown = createImagesForButtons(BASEBALL_DOWN_IMAGE_FILE_PATH);
        baseballImageButton = new ImageButton(baseballUp, baseballDown, baseballDown);
        MenuUtility.addButtonSelectListener(entity, baseballImageButton, "updateItemDescription",
                "configs/ShopBaseballInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(baseballImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        // indicate that this button is by default - clicked
        baseballImageButton.setChecked(true);
        baseballImageButton.setDisabled(true);
        storeButtonClicked = baseballImageButton;

        Drawable axeUp = createImagesForButtons(AXE_UP_IMAGE_FILE_PATH);
        Drawable axeDown = createImagesForButtons(AXE_DOWN_IMAGE_FILE_PATH);
        axeImageButton = new ImageButton(axeUp, axeDown, axeDown);
        MenuUtility.addButtonSelectListener(entity, axeImageButton, "updateItemDescription",
                "configs/ShopAxeInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(axeImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        // second row
        // machete, crowbar and sledge
        itemsLabelImages.row().padTop(PAD_TOP);
        Drawable macheteUp = createImagesForButtons(MACHETE_UP_IMAGE_FILE_PATH);
        Drawable macheteDown = createImagesForButtons(MACHETE_DOWN_IMAGE_FILE_PATH);
        macheteImageButton = new ImageButton(macheteUp, macheteDown, macheteDown);
        MenuUtility.addButtonSelectListener(entity, macheteImageButton, "updateItemDescription",
                "configs/ShopMacheteInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(macheteImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable crowbarUp = createImagesForButtons(CROWBAR_UP_IMAGE_FILE_PATH);
        Drawable crowbarDown = createImagesForButtons(CROWBAR_DOWN_IMAGE_FILE_PATH);
        crowbarImageButton = new ImageButton(crowbarUp, crowbarDown, crowbarDown);
        MenuUtility.addButtonSelectListener(entity, crowbarImageButton, "updateItemDescription",
                "configs/ShopCrowbarInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(crowbarImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable sledgeUp = createImagesForButtons(SLEDGE_UP_IMAGE_FILE_PATH);
        Drawable sledgeDown = createImagesForButtons(SLEDGE_DOWN_IMAGE_FILE_PATH);
        sledgeImageButton = new ImageButton(sledgeUp, sledgeDown, sledgeDown);
        MenuUtility.addButtonSelectListener(entity, sledgeImageButton, "updateItemDescription",
                "configs/ShopSledgeInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(sledgeImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        // third row
        // helmet, torch and dash ability
        itemsLabelImages.row().padTop(PAD_TOP);;
        Drawable helmetUp = createImagesForButtons(HELMET_UP_IMAGE_FILE_PATH);
        Drawable helmetDown = createImagesForButtons(HELMET_DOWN_IMAGE_FILE_PATH);
        helmetImageButton = new ImageButton(helmetUp, helmetDown, helmetDown);
        MenuUtility.addButtonSelectListener(entity, helmetImageButton, "updateItemDescription",
                "configs/ShopHelmetInfo.json", Items.SHIELDS);
        itemsLabelImages.add(helmetImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable torchUp = createImagesForButtons(TORCH_UP_IMAGE_FILE_PATH);
        Drawable torchDown = createImagesForButtons(TORCH_DOWN_IMAGE_FILE_PATH);
        torchImageButton = new ImageButton(torchUp, torchDown, torchDown);
        MenuUtility.addButtonSelectListener(entity, torchImageButton, "updateItemDescription",
                "configs/ShopTorchInfo.json", Items.OTHERS);
        itemsLabelImages.add(torchImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable dashUp = createImagesForButtons(DASH_UP_IMAGE_FILE_PATH);
        Drawable dashDown = createImagesForButtons(DASH_DOWN_IMAGE_FILE_PATH);
        dashImageButton = new ImageButton(dashUp, dashDown, dashDown);
        MenuUtility.addButtonSelectListener(entity, dashImageButton, "updateItemDescription",
                "configs/ShopLongDashAbilityInfo.json", Items.OTHERS);
        itemsLabelImages.add(dashImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        // fourth row
        // armor, bandage and invincibility
        itemsLabelImages.row().padTop(PAD_TOP);;
        Drawable armorUp = createImagesForButtons(ARMOR_UP_IMAGE_FILE_PATH);
        Drawable armorDown = createImagesForButtons(ARMOR_DOWN_IMAGE_FILE_PATH);
        armorImageButton = new ImageButton(armorUp, armorDown, armorDown);
        MenuUtility.addButtonSelectListener(entity, armorImageButton, "updateItemDescription",
                "configs/ShopArmourInfo.json", Items.SHIELDS);
        itemsLabelImages.add(armorImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable bandageUp = createImagesForButtons(BANDAGE_UP_IMAGE_FILE_PATH);
        Drawable bandageDown = createImagesForButtons(BANDAGE_DOWN_IMAGE_FILE_PATH);
        bandageImageButton = new ImageButton(bandageUp, bandageDown, bandageDown);
        MenuUtility.addButtonSelectListener(entity, bandageImageButton, "updateItemDescription",
                "configs/ShopBandageInfo.json", Items.OTHERS);
        itemsLabelImages.add(bandageImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable invincibleUp = createImagesForButtons(INVINCIBLE_UP_IMAGE_FILE_PATH);
        Drawable invincibleDown = createImagesForButtons(INVINCIBLE_DOWN_IMAGE_FILE_PATH);
        invincibleImageButton = new ImageButton(invincibleUp, invincibleDown, invincibleDown);
        MenuUtility.addButtonSelectListener(entity, invincibleImageButton, "updateItemDescription",
                "configs/ShopInvincibilityAbilityInfo.json", Items.OTHERS);
        itemsLabelImages.add(invincibleImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        container.add(itemsLabelImages).colspan(SEC_COL_NUM_TAKEN).top();
    }

    /**
     * Creates third column of table - name of item and description of item clicked. Dynamically
     * changes with different item clicked
     */
    private void addItemDescriptionToTable() {
        Table itemsDescriptionLabels = new Table();

        // title of item description col - first row of it
        CharSequence titleText = "ITEM DETAILS";
        Label itemSelectedDescriptionTitle = new Label(titleText, skin, "white");
        itemsDescriptionLabels.row().colspan(THIRD_COL_NUM_TAKEN).height(TABLE_BODY_HEIGHT);
        itemsDescriptionLabels.add(itemSelectedDescriptionTitle).colspan(THIRD_COL_NUM_TAKEN)
                .height(COMMON_LABEL_HEIGHT);

        // for description of item selected - default is for sword;
        // second row of item description col
        CharSequence itemDescriptionText = "The sword is the most common weapon there is but it has decent range " +
                "damage and it is versatile enough to fend off any enemies in the game - especially night crawlers!";
        itemSelectedDescriptionLabel = new Label(itemDescriptionText, skin, "white-font");
        itemSelectedDescriptionLabel.setWrap(true);
        itemsDescriptionLabels.row().colspan(THIRD_COL_NUM_TAKEN);
        itemsDescriptionLabels.add(itemSelectedDescriptionLabel).colspan(THIRD_COL_NUM_TAKEN)
                .height(ITEM_LABEL_DESCRIPTION_HEIGHT).width(280);

        // third row of item description col (if there is one - dependent on time of item clicked)
        CharSequence itemMoreDescription = "Attack Duration: Decent, Damage: 5 & Knockback: Moderate";
        itemSelectedMoreInfoLabel = new Label(itemMoreDescription, skin, "white-font");
        itemSelectedMoreInfoLabel.setWrap(true);
        itemsDescriptionLabels.row().colspan(THIRD_COL_NUM_TAKEN);
        itemsDescriptionLabels.add(itemSelectedMoreInfoLabel).colspan(THIRD_COL_NUM_TAKEN)
                .height(ITEM_LABEL_MORE_INFO_HEIGHT).width(280).padTop(PAD_TOP);


        // price of item selected (clicked by user) - default is for sword; for now
        // fourth row of item description col
        CharSequence priceText = "PRICE: 50";
        pricingValueLabel = new Label(priceText, skin, "white");

        itemsDescriptionLabels.row().height(COMMON_LABEL_HEIGHT).padTop(EXTRA_PAD_TOP).center();
        itemsDescriptionLabels.add(pricingValueLabel).padLeft(PAD_TO_CENTER_PRICING);

        Image moneyBagImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/playeritems/coin/money bag.png", Texture.class));
        itemsDescriptionLabels.add(moneyBagImage).size(COMMON_LABEL_HEIGHT);

        container.add(itemsDescriptionLabels).colspan(THIRD_COL_NUM_TAKEN).top();
    }

    /**
     * Checks of shop popup box is visible. This is used to prevent pause menu from appearing
     * when shop popup box appears
     * @return true when shop is visible and false otherwise
     */
    public boolean checkShopPopupVisibility() {
        return container.isVisible();
    }

    /**
     * Creates a drawable object to be used for image buttons.
     * @param filePath related to image uploaded with image button
     * @return drawable images for image button
     */
    private Drawable createImagesForButtons(String filePath) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(
                new TextureRegion(ServiceLocator.getResourceService()
                        .getAsset(filePath, Texture.class)));
        drawable.setMinWidth(IMAGE_BUTTON_HEIGHT);
        drawable.setMinHeight(IMAGE_BUTTON_HEIGHT);
        return drawable;
    }

    /**
     * Creates first half of fourth column of table which gives user information of player's current state
     * @param playerInfo table involved for better styling of rows
     * @param coinImage that is used to display player's coin
     * @param bandageImage that is used to display player's bandage
     * @param ammoImage that is used to display player's ammo
     */
    private void createPlayerInfoLabels(Table playerInfo, Image coinImage, Image bandageImage, Image ammoImage) {
        playerInfo.row();
        playerInfo.add(coinImage).colspan(10).height(50);
        playerInfo.add(coinLabel).colspan(10).left();
        playerInfo.row().padTop(SMALL_PAD_TOP);
        playerInfo.add(bandageImage).size(BANDAGE_SIDE_LENGTH).colspan(10).height(50);
        playerInfo.add(bandageLabel).colspan(10).left();
        playerInfo.row().padTop(SMALL_PAD_TOP);
        playerInfo.add(ammoImage).colspan(10).height(50);
        playerInfo.add(ammoLabel).colspan(10).left();
    }

    /**
     * Creates second half of fourth column (calls first half first before creating the second half) - which
     * has relevant buttons that will enable player to click to purchase clicked item and receive feedback
     * if an item can be bought or not
     */
    private void addPlayerInfoAndBuyingButtonToTable() {
        Table playerInfoLabelsImages = new Table();

        // image of player character
        playerInfoLabelsImages.row().colspan(FOURTH_COL_NUM_TAKEN).height(TABLE_BODY_HEIGHT);
        Image playerImage = new Image(ServiceLocator.getResourceService().getAsset(PLAYER_IMAGE_FILE_PATH,
                Texture.class));
        playerInfoLabelsImages.add(playerImage).colspan(10).height(LARGER_IMAGE_SIZE)
                .width(LARGER_IMAGE_SIZE).top();

        // uses most current player state now
        Table playerInfo = new Table();
        CharSequence bandageText = String.format(" x %d", playerBandage);
        CharSequence ammoText = String.format(" x %d", playerAmmo);
        CharSequence coinText = String.format(" x %d", playerGold);

        bandageLabel = new Label(bandageText, skin, "white-font");
        ammoLabel = new Label(ammoText, skin, "white-font");
        coinLabel = new Label(coinText, skin, "white-font");
        Image bandageImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/playeritems/bandage/bandage01.png", Texture.class));
        Image ammoImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/playeritems/shootingammo.png", Texture.class));
        Image coinImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/playeritems/coin/coin1.png", Texture.class));

        // create rows and size relevant images along with labels for player info
        createPlayerInfoLabels(playerInfo, coinImage, bandageImage, ammoImage);
        playerInfoLabelsImages.add(playerInfo).colspan(20).height(30);

        // create buttons that will trigger event to load data and check if item selected can be
        // purchased and feedback will be produced depending on if conditions were met
        // feedback will not be visible initially - until equip button is clicked
        playerInfoLabelsImages.row().height(COMMON_LABEL_HEIGHT).colspan(FOURTH_COL_NUM_TAKEN);

        Label noFundsLabel = new Label("NOT ENOUGH COINS", skin, "red");
        Label equippedLabel = new Label("ALREADY EQUIPPED", skin, "white-font");
        Label successPurchaseLabel = new Label("SUCCESSFUL PURCHASE", skin, "green");
        equippedLabel.setVisible(false);
        equippedLabel.setAlignment(Align.center);
        noFundsLabel.setVisible(false);
        noFundsLabel.setAlignment(Align.center);
        successPurchaseLabel.setVisible(false);
        successPurchaseLabel.setAlignment(Align.center);
        feedbackStack = new Stack();
        feedbackStack.add(noFundsLabel);
        feedbackStack.add(equippedLabel);
        feedbackStack.add(successPurchaseLabel);
        playerInfoLabelsImages.add(feedbackStack).padTop(EXTRA_PAD_TOP).padBottom(EXTRA_PAD_BOTTOM).width(300).center();

        // button that triggers event for data checking from player current state
        playerInfoLabelsImages.row().height(COMMON_LABEL_HEIGHT).colspan(FOURTH_COL_NUM_TAKEN);
        String equipTextButton = "PURCHASE";
        TextButton equipBtn = new TextButton(equipTextButton, skin, MENU_BUTTON_STYLE);
        MenuUtility.addButtonSelectListener(entity, equipBtn, "purchaseItem");
        playerInfoLabelsImages.add(equipBtn).padTop(PAD_TOP);

        container.add(playerInfoLabelsImages).colspan(FOURTH_COL_NUM_TAKEN).top();
    }

    /**
     * Will be triggered whenever any of the image button (for each item displayed in second row) is clicked
     * this method is called to dynamically change the text and title of information positioned in the third
     * column of the table
     * @param configFilename called based on specific item that is clicked on shop popup box
     * @param itemType used to categorize the kinds of item that are clicked for better logic management
     */
    private void updateItemDetails(String configFilename, Items itemType) {
        logger.info("Button image clicked, buying system update");
        uncheckImageButton();
        hideFeedbackLabels();

        // all item files must have the 2 following data variables
        ShopItemInfoConfig itemData =
                FileLoader.readClass(ShopItemInfoConfig.class, configFilename);
        itemName = itemData.itemName;
        String description = itemData.description;
        itemPrice = itemData.price;
        typeOfItem = itemType;
        CharSequence priceText = String.format("PRICE: %d", itemPrice);

        itemSelectedTitleLabel.setText(itemName);
        pricingValueLabel.setText(priceText);
        itemSelectedDescriptionLabel.setText(description);

        if (itemType == Items.MELEE_WEAPONS) {
            String knockbackMelee = itemData.knockback;
            String attackLength = itemData.attackLength;
            int attackDamage = itemData.attackDamage;
            CharSequence itemMoreDescription = String.format("Attack Duration: %s, Damage: %d & Knockback: %s",
                    attackLength, attackDamage, knockbackMelee);
            itemSelectedMoreInfoLabel.setText(itemMoreDescription);

        } else if (itemType == Items.SHIELDS) {
            itemDefenceLevel = itemData.defenceLevel;
            CharSequence itemMoreDescription = String.format("Effect: %s",
                    itemData.effects);
            itemSelectedMoreInfoLabel.setText(itemMoreDescription);
        } else if (itemType == Items.OTHERS) {
            CharSequence itemMoreDescription = String.format("Effect: %s",
                    itemData.effects);
            itemSelectedMoreInfoLabel.setText(itemMoreDescription);
        }
    }

    /**
     * Called when player attempts to purchase an item. 3 kinds of feedback can be displayed depending on
     * player's state - when an item has been successfully purchased, when an item is already equipped and when
     * player does not have sufficient funds to make the purchase. This method dynamically resets and change
     * the feedback label accordingly for user feedback
     */
    private void isItemPurchasable() {
        hideFeedbackLabels();
        if (playerMeleeWeaponType.equals(itemName) || typeOfItem == Items.SHIELDS || playerAbility.equals(itemName)) {
            // player may have different types of armor, and player's armor may be worse than
            // armor item clicked in shop which player can then purchase - there needs to be a separate logic for this
            if (typeOfItem == Items.SHIELDS) {
                if (itemDefenceLevel <= playerDefenceLevel) {
                    feedbackStack.getChild(EQUIPPED_INDEX).setVisible(true);
                } else {
                    if (playerGold >= itemPrice) {
                        feedbackStack.getChild(PURCHASED_INDEX).setVisible(true);
                        processPurchasedItem();
                    } else {
                        feedbackStack.getChild(NO_FUNDS_INDEX).setVisible(true);
                    }
                }

            } else {
                feedbackStack.getChild(EQUIPPED_INDEX).setVisible(true);
            }

        } else if (playerGold >= itemPrice) {
            feedbackStack.getChild(PURCHASED_INDEX).setVisible(true);
            processPurchasedItem();

        } else {
            feedbackStack.getChild(NO_FUNDS_INDEX).setVisible(true);
        }
    }

    /**
     * Process purchased item to update player data. This method can only be called when player has sufficient amount
     * of coins to purchase a clicked item
     */
    private void processPurchasedItem() {
        if (typeOfItem == Items.MELEE_WEAPONS) {
            // melee weapon needs to be registered in item enum file to be valid
            if (Items.checkMeleeWeapon(itemName)) {
                String weaponConfigFile = Items.getWeaponFilepath(itemName);
                Items meleeWeaponType = Items.getMeleeWeapon(itemName);
                playerState.getComponent(PlayerMeleeAttackComponent.class).setWeapon(weaponConfigFile);
                playerState.getComponent(PlayerMeleeAttackComponent.class).setMeleeWeaponType(meleeWeaponType);
            }
        } else if (typeOfItem == Items.SHIELDS) {
            // armor type need to registered just like melee weapon
            if (Items.checkShieldType(itemName)) {
                int defenceLevel = Items.getDefenceLevel(itemName);
                playerState.getComponent(PlayerCombatStatsComponent.class).setDefenceLevel(defenceLevel);

            }
        } else {
            // other item type includes abilities and bandages
            if (Abilities.checkAbility(itemName)) {
                Abilities ability = Abilities.getAbility(itemName);
                playerState.getComponent(PlayerAbilitiesComponent.class).setAbility(ability);
            } else if (itemName.equals(Items.BANDAGE.toString())) {
                playerState.getComponent(InventoryComponent.class).addBandages(ADD_BANDAGE);
            }
        }
        int updatePlayerGold = playerGold - itemPrice;
        playerState.getComponent(InventoryComponent.class).setGold(updatePlayerGold);
        loadPlayerData();
    }

    /**
     * Hides all feedback labels. Currently, there are 3 labels stacked on each other in the same position
     * whenever user attempts to purchase, all labels will be hidden again before a label is set to be visible.
     * This method ensures no 2 label can be visible at the same time.
     */
    private void hideFeedbackLabels() {
        for (int i = 0; i < 3; i++) {
            feedbackStack.getChild(i).setVisible(false);
        }
    }

    /**
     * Used to reset the image allocated for each image button in the third column. This method ensures that
     * when a different image button is clicked, the previous clicked image button would be resetted to its original
     * 'down' image and it keeps track of the first image button that was clicked - to ensure that if a button is
     * already clicked - and is clicked again - nothing will happen for performance sake.
     */
    private void uncheckImageButton() {
        List<ImageButton> imageButtonList = Arrays.asList(crowbarImageButton, daggerImageButton, axeImageButton,
                armorImageButton, helmetImageButton, torchImageButton, bandageImageButton, dashImageButton,
                invincibleImageButton, macheteImageButton, baseballImageButton, sledgeImageButton);
        imageButtons.addAll(imageButtonList);

        for (ImageButton imageButton : imageButtons) {
            if (imageButton.isChecked()) {
                if (storeButtonClicked == null) {
                    storeButtonClicked = imageButton;
                } else {
                    if (!imageButton.equals(storeButtonClicked)) {
                        // store new button that is clicked and set check false to old button that
                        // was already clicked in the past
                        storeButtonClicked.setChecked(false);
                        storeButtonClicked.setDisabled(false);
                        storeButtonClicked = imageButton;
                        storeButtonClicked.setDisabled(true);
                    }
                }
            }
        }
    }

    /**
     * This is used to load player's most current data and update all relevant labels on shop popup box.
     */
    private void loadPlayerData() {
        playerState = ServiceLocator.getGameArea().player;
        playerAmmo = playerState.getComponent(InventoryComponent.class).getAmmo();
        playerGold = playerState.getComponent(InventoryComponent.class).getGold();
        playerBandage = playerState.getComponent(InventoryComponent.class).getBandages();
        playerDefenceLevel = playerState.getComponent(PlayerCombatStatsComponent.class).getDefenceLevel();
        playerAbility = playerState.getComponent(PlayerAbilitiesComponent.class).getAbility().toString();;
        playerMeleeWeaponType = playerState.getComponent(PlayerMeleeAttackComponent.class).getMeleeWeaponType()
                .toString();
        playerArmorType = Items.getArmorType(playerDefenceLevel);

        CharSequence bandageText = String.format(" x %d", playerBandage);
        CharSequence ammoText = String.format(" x %d", playerAmmo);
        CharSequence coinText = String.format(" x %d", playerGold);
        bandageLabel.setText(bandageText);
        ammoLabel.setText(ammoText);
        coinLabel.setText(coinText);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        setShopBoxPosSize();
    }
}
