package com.deco2800.game.components.shopmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
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

import com.deco2800.game.entities.factories.DialogueBoxFactory;

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
    private static final String PLAYER_IMAGE_FILE_PATH = "images/Player_Sprite/25.png";
    private static final String PLAYER_FULL_ARMOR_IMAGE_FILE_PATH = "images/Player_Sprite/34.png";
    private static final String PLAYER_ARMOR_IMAGE_FILE_PATH = "images/Player_Sprite/31.png";
    private static final String PLAYER_HELMET_IMAGE_FILE_PATH = "images/Player_Sprite/28.png";
    private static final String CROWBAR_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopCrowbar.png";
    private static final String CROWBAR_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopCrowbarSelected.png";
    private static final String DAGGER_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopDagger.png";
    private static final String DAGGER_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopDaggerSelected.png";
    private static final String AXE_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopAxe.png";
    private static final String AXE_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopAxeSelected.png";
    private static final String ARMOR_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopArmour.png";
    private static final String ARMOR_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopArmourSelected.png";
    private static final String HELMET_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopHelmet.png";
    private static final String HELMET_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopHelmetSelected.png";
    private static final String FUEL_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopFuel.png";
    private static final String FUEL_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopSelectedFuel.png";
    private static final String DASH_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopDash.png";
    private static final String DASH_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopDashSelected.png";
    private static final String INVINCIBLE_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopInvincibility.png";
    private static final String INVINCIBLE_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopInvincibilitySelected.png";
    private static final String FIRECRACKER_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopFirecracker.png";
    private static final String FIRECRACKER_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons" +
            "/shopFirecrackerSelected" +
            ".png";
    private static final String MACHETE_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopMachete.png";
    private static final String MACHETE_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopMacheteSelected.png";
    private static final String BASEBALL_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopBat.png";
    private static final String BASEBALL_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopBatSelected.png";
    private static final String SLEDGE_UP_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopHammer.png";
    private static final String SLEDGE_DOWN_IMAGE_FILE_PATH = "images/safehouse/itemIcons/shopHammerSelected.png";
    private static final String MENU_BUTTON_STYLE = "menu-button-large";
    private static final String EQUIPPED_TEXT = "EQUIPPED";
    private static final String PURCHASE_TEXT = "PURCHASE";
    private static final String NO_FUNDS_TEXT = "NO FUNDS";

    private static final String DAGGER_TEXT = "A good little weapon to use in a tight spot, if you ask me.";
    private static final String BAT_TEXT = "This was from an old game called 'Baseball', back before all this happened.";
    private static final String AXE_TEXT = "Used to use this to chop wood back home before winter. Pretty good " +
        "at splitting skulls as well I would imagine.";
    private static final String MACHETE_TEXT = "There's something simplistically deadly about a sharp blade.";
    private static final String CROWBAR_TEXT = "Nothing is quite as reliable as a sturdy piece of metal. Good for " +
        "everything from prying open cars to starting a revolution.";
    private static final String SLEDGE_TEXT = "You can lift this thing, right? Well, not returns either way.";
    private static final String HELMET_TEXT = "Because who needs anything from the neck down?";
    private static final String TORCH_TEXT = "You'll be wanting this out there. Trust me, you don't want to get stuck in the dark...";
    private static final String LONG_DASH_TEXT = "Sometimes, you just need a burst of speed to get you places.";
    private static final String ARMOUR_TEXT = "It'll set you back a bit, but this is the best protection money can buy! " +
        "Or at least, that I can offer...";
    private static final String FIRE_CRACKER_TEXT = "Throw fire crackers and kill enemies by burning them!";
    private static final String INVINCIBILITY_TEXT = "This here is a handy piece of tech they cooked up just as everything" +
        " started to go downhill. No one quite understands how it works anymore but it should still help you out in a tight spot.";
    private static final Logger logger = LoggerFactory.getLogger(ShopMenuDisplay.class);
    private static Image playerImage;
    private Image playerFullArmorImage;
    private Image playerHelmetImage;
    private Image playerChestImage;
    private final GdxGame game;
    private boolean isEnabled = false;
    private Table container;
    private Label itemSelectedTitleLabel;
    private Label itemSelectedDescriptionLabel;
    private Label pricingValueLabel;
    private Label itemSelectedMoreInfoLabel;
    private Label bandageLabel, ammoLabel, coinLabel;
    private Image background;
    private static final float MAX_BOX_WIDTH = 1200;
    private static final float MAX_BOX_HEIGHT = 450;
    private static final float SMALL_PAD_TOP = 5;
    private static final float PAD_TOP = 10;
    private static final float PAD_LEFT = 40;
    private static final float EXTRA_PAD_TOP = 30;
    private static final float EXTRA_LARGE_PAD_TOP = 120;
    private static final float PAD_TO_CENTER_PRICING = 38;
    private static final float TABLE_HEADER_HEIGHT = 50;
    private static final float COMMON_LABEL_HEIGHT = 50;
    private static final float ITEM_LABEL_DESCRIPTION_HEIGHT = 150;
    private static final float ITEM_LABEL_MORE_INFO_HEIGHT = 60;
    private static final float TABLE_BODY_HEIGHT = 350;
    private static final float SHOPKEEPER_IMAGE_WIDTH = 250;
    private static final float LARGER_IMAGE_SIZE = 160;
    private static final int COL_NUM = 120;
    private static final int FIRST_COL_NUM_TAKEN = 30;
    private static final int SEC_COL_NUM_TAKEN = 30;
    private static final int THIRD_COL_NUM_TAKEN = 30;
    private static final int FOURTH_COL_NUM_TAKEN = 30;
    private static final int IMAGE_BUTTON_HEIGHT = 70;
    private static final float BANDAGE_SIDE_LENGTH = 50f;

    private static final int ADD_TORCH = 100;
    private static final int OFFSET_X_IMG_GROUP = 20;
    private static final int OFFSET_Y_IMG_GROUP = 20;
    private ImageButton crowbarImageButton;
    private ImageButton daggerImageButton;
    private ImageButton axeImageButton;
    private ImageButton armorImageButton;
    private ImageButton helmetImageButton;
    private ImageButton fuelImageButton;
    private ImageButton fireCrackerImageButton;
    private ImageButton dashImageButton;
    private ImageButton invincibleImageButton;
    private ImageButton macheteImageButton;
    private ImageButton baseballImageButton;
    private ImageButton sledgeImageButton;
    private final ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private final ArrayList<Image> images = new ArrayList<>();
    private int itemPrice;
    private String itemName;
    private ImageButton storeButtonClicked = null;
    // values below will be checked and will load values from config file that saves player's states
    private int playerAmmo;
    private int  playerGold;
    private int  playerBandage;
    private int  playerDefenceLevel;
    private String playerAbility;
    private String playerMeleeWeaponType;
    private String playerArmorType;
    private final String UPDATE = "updateItemDescription";
    private final String WHITE_COLOR = "white";
    private final String HUD_FORMAT = " x %d";
    private final String HELMET = "HELMET";
    private final String CHEST = "CHEST";
    private Items typeOfItem;
    private Entity playerState;
    private TextButton equipBtn;
    Entity shopkeeperSpeech;

    public ShopMenuDisplay(GdxGame game) {
        this.game = game;

    }

    @Override
    public void create() {
        super.create();
        addActors();

        entity.getEvents().addListener("toggleShopBox", this::toggleShopBox);
        entity.getEvents().addListener(UPDATE, this::updateItemDetails);
        entity.getEvents().addListener("purchaseItem", this::isItemPurchasable);
    }

    /**
     * Creates and destroy shop popup box. If player collides with shop keeper entity, popup pox will be
     * created and when player closest (with esc or clicking X button), popup box closes as well
     */
    public void toggleShopBox() {
        GameTime timeSource = ServiceLocator.getTimeSource();
        ServiceLocator.getGameArea().player.getEvents().trigger("resetPlayerMovements");

        if (shopkeeperSpeech!= null) {
            shopkeeperSpeech.dispose();
        }

        if (!isEnabled) {
            loadPlayerData();
            updateImagesVisibility(playerDefenceLevel);
            timeSource.pause();

            // feedback tend to remain on shop menu display in different safe house areas, this ensures it doesn't
            // indicate that this button is clicked by default - clicked
            baseballImageButton.setChecked(true);
            baseballImageButton.setDisabled(true);
            storeButtonClicked = baseballImageButton;
            updateItemDetails("configs/ShopBaseballInfo.json", Items.MELEE_WEAPONS);
            updatePurchaseButton();

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
        Label shopTitleLabel = new Label(titleText, skin, WHITE_COLOR);
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

        // by default - baseball will be pre-selected first
        CharSequence titleText = "BASEBALL";
        itemName = "BAT";
        typeOfItem = Items.MELEE_WEAPONS;
        itemSelectedTitleLabel = new Label(titleText, skin, WHITE_COLOR);
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
        MenuUtility.addButtonSelectListener(entity, daggerImageButton, UPDATE,
                "configs/ShopDaggerInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(daggerImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable baseballUp = createImagesForButtons(BASEBALL_UP_IMAGE_FILE_PATH);
        Drawable baseballDown = createImagesForButtons(BASEBALL_DOWN_IMAGE_FILE_PATH);
        baseballImageButton = new ImageButton(baseballUp, baseballDown, baseballDown);
        MenuUtility.addButtonSelectListener(entity, baseballImageButton, UPDATE,
                "configs/ShopBaseballInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(baseballImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        // indicate that this button is by default - clicked
        baseballImageButton.setChecked(true);
        baseballImageButton.setDisabled(true);
        storeButtonClicked = baseballImageButton;

        Drawable axeUp = createImagesForButtons(AXE_UP_IMAGE_FILE_PATH);
        Drawable axeDown = createImagesForButtons(AXE_DOWN_IMAGE_FILE_PATH);
        axeImageButton = new ImageButton(axeUp, axeDown, axeDown);
        MenuUtility.addButtonSelectListener(entity, axeImageButton, UPDATE,
                "configs/ShopAxeInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(axeImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        // second row
        // machete, crowbar and sledge
        itemsLabelImages.row().padTop(PAD_TOP);
        Drawable macheteUp = createImagesForButtons(MACHETE_UP_IMAGE_FILE_PATH);
        Drawable macheteDown = createImagesForButtons(MACHETE_DOWN_IMAGE_FILE_PATH);
        macheteImageButton = new ImageButton(macheteUp, macheteDown, macheteDown);
        MenuUtility.addButtonSelectListener(entity, macheteImageButton, UPDATE,
                "configs/ShopMacheteInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(macheteImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable crowbarUp = createImagesForButtons(CROWBAR_UP_IMAGE_FILE_PATH);
        Drawable crowbarDown = createImagesForButtons(CROWBAR_DOWN_IMAGE_FILE_PATH);
        crowbarImageButton = new ImageButton(crowbarUp, crowbarDown, crowbarDown);
        MenuUtility.addButtonSelectListener(entity, crowbarImageButton, UPDATE,
                "configs/ShopCrowbarInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(crowbarImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable sledgeUp = createImagesForButtons(SLEDGE_UP_IMAGE_FILE_PATH);
        Drawable sledgeDown = createImagesForButtons(SLEDGE_DOWN_IMAGE_FILE_PATH);
        sledgeImageButton = new ImageButton(sledgeUp, sledgeDown, sledgeDown);
        MenuUtility.addButtonSelectListener(entity, sledgeImageButton, UPDATE,
                "configs/ShopSledgeInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(sledgeImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        // third row
        // long dash, fire cracker and invincibility
        // armor, bandage and invincibility
        itemsLabelImages.row().padTop(PAD_TOP);
        Drawable dashUp = createImagesForButtons(DASH_UP_IMAGE_FILE_PATH);
        Drawable dashDown = createImagesForButtons(DASH_DOWN_IMAGE_FILE_PATH);
        dashImageButton = new ImageButton(dashUp, dashDown, dashDown);
        MenuUtility.addButtonSelectListener(entity, dashImageButton, UPDATE,
                "configs/ShopLongDashAbilityInfo.json", Items.OTHERS);
        itemsLabelImages.add(dashImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable fireCrackerUp = createImagesForButtons(FIRECRACKER_UP_IMAGE_FILE_PATH);
        Drawable fireCrackerDown = createImagesForButtons(FIRECRACKER_DOWN_IMAGE_FILE_PATH);
        fireCrackerImageButton = new ImageButton(fireCrackerUp, fireCrackerDown, fireCrackerDown);
        MenuUtility.addButtonSelectListener(entity, fireCrackerImageButton, UPDATE,
                "configs/ShopFireCrackerAbilityInfo.json", Items.OTHERS);
        itemsLabelImages.add(fireCrackerImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable invincibleUp = createImagesForButtons(INVINCIBLE_UP_IMAGE_FILE_PATH);
        Drawable invincibleDown = createImagesForButtons(INVINCIBLE_DOWN_IMAGE_FILE_PATH);
        invincibleImageButton = new ImageButton(invincibleUp, invincibleDown, invincibleDown);
        MenuUtility.addButtonSelectListener(entity, invincibleImageButton, UPDATE,
                "configs/ShopInvincibilityAbilityInfo.json", Items.OTHERS);
        itemsLabelImages.add(invincibleImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        // fourth row
        // helmet, chest and fuel
        itemsLabelImages.row().padTop(PAD_TOP);
        Drawable helmetUp = createImagesForButtons(HELMET_UP_IMAGE_FILE_PATH);
        Drawable helmetDown = createImagesForButtons(HELMET_DOWN_IMAGE_FILE_PATH);
        helmetImageButton = new ImageButton(helmetUp, helmetDown, helmetDown);
        MenuUtility.addButtonSelectListener(entity, helmetImageButton, UPDATE,
                "configs/ShopHelmetInfo.json", Items.SHIELDS);
        itemsLabelImages.add(helmetImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable armorUp = createImagesForButtons(ARMOR_UP_IMAGE_FILE_PATH);
        Drawable armorDown = createImagesForButtons(ARMOR_DOWN_IMAGE_FILE_PATH);
        armorImageButton = new ImageButton(armorUp, armorDown, armorDown);
        MenuUtility.addButtonSelectListener(entity, armorImageButton, UPDATE,
                "configs/ShopArmourInfo.json", Items.SHIELDS);
        itemsLabelImages.add(armorImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable fuelUp = createImagesForButtons(FUEL_UP_IMAGE_FILE_PATH);
        Drawable fuelDown = createImagesForButtons(FUEL_DOWN_IMAGE_FILE_PATH);
        fuelImageButton = new ImageButton(fuelUp, fuelDown, fuelDown);
        MenuUtility.addButtonSelectListener(entity, fuelImageButton, UPDATE,
                "configs/ShopTorchInfo.json", Items.OTHERS);
        itemsLabelImages.add(fuelImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

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
        Label itemSelectedDescriptionTitle = new Label(titleText, skin, WHITE_COLOR);
        itemsDescriptionLabels.row().colspan(THIRD_COL_NUM_TAKEN).height(TABLE_BODY_HEIGHT);
        itemsDescriptionLabels.add(itemSelectedDescriptionTitle).colspan(THIRD_COL_NUM_TAKEN)
                .height(COMMON_LABEL_HEIGHT);

        // second row of item description col
        CharSequence itemDescriptionText = "The baseball is the most common weapon there is but it has decent range " +
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
        pricingValueLabel = new Label(priceText, skin, WHITE_COLOR);

        itemsDescriptionLabels.row().height(COMMON_LABEL_HEIGHT).padTop(EXTRA_PAD_TOP).center();
        itemsDescriptionLabels.add(pricingValueLabel).padLeft(PAD_TO_CENTER_PRICING);

        Image moneyBagImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/playeritems/coin/money-bag.png", Texture.class));
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
        Group imageGroup = new Group();
        playerImage = new Image(ServiceLocator.getResourceService().getAsset(PLAYER_IMAGE_FILE_PATH,
                Texture.class));
        playerFullArmorImage = new Image(ServiceLocator.getResourceService().getAsset(PLAYER_FULL_ARMOR_IMAGE_FILE_PATH,
                Texture.class));
        playerHelmetImage = new Image(ServiceLocator.getResourceService().getAsset(PLAYER_HELMET_IMAGE_FILE_PATH,
                Texture.class));
        playerChestImage =
                new Image(ServiceLocator.getResourceService().getAsset(PLAYER_ARMOR_IMAGE_FILE_PATH,
                Texture.class));

        // configure sizes of images
        List<Image> imageList = Arrays.asList(playerImage, playerFullArmorImage, playerHelmetImage,
                playerChestImage);
        images.addAll(imageList);
        resizeImages();

        // add all image to same group and overlap them
        imageGroup.addActor(playerImage);
        imageGroup.addActor(playerFullArmorImage);
        imageGroup.addActor(playerHelmetImage);
        imageGroup.addActor(playerChestImage);
        imageGroup.setPosition(OFFSET_X_IMG_GROUP,OFFSET_Y_IMG_GROUP);
        playerInfoLabelsImages.add(imageGroup).colspan(10).height(LARGER_IMAGE_SIZE)
                .width(LARGER_IMAGE_SIZE).top();

        // uses most current player state now
        Table playerInfo = new Table();
        CharSequence bandageText = String.format(HUD_FORMAT, playerBandage);
        CharSequence ammoText = String.format(HUD_FORMAT, playerAmmo);
        CharSequence coinText = String.format(HUD_FORMAT, playerGold);

        bandageLabel = new Label(bandageText, skin, "white-font");
        ammoLabel = new Label(ammoText, skin, "white-font");
        coinLabel = new Label(coinText, skin, "white-font");
        Image bandageImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/playeritems/bandage/bandage01.png", Texture.class));
        Image ammoImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/playeritems/rock/pickupammo.png", Texture.class));
        Image coinImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/playeritems/coin/coin1.png", Texture.class));

        // create rows and size relevant images along with labels for player info
        createPlayerInfoLabels(playerInfo, coinImage, bandageImage, ammoImage);
        playerInfoLabelsImages.add(playerInfo).colspan(20).height(30);

        // create buttons that will trigger event to load data and check if item selected can be
        // purchased and feedback will be produced depending on if conditions were met - initially baseball
        // will be selected first which turns button into "EQUIPPED"

        // button that triggers event for data checking from player current state
        playerInfoLabelsImages.row().height(COMMON_LABEL_HEIGHT).colspan(FOURTH_COL_NUM_TAKEN);
        equipBtn = new TextButton(EQUIPPED_TEXT, skin, MENU_BUTTON_STYLE);
        MenuUtility.addButtonSelectListener(entity, equipBtn, "purchaseItem");
        playerInfoLabelsImages.add(equipBtn).padTop(EXTRA_LARGE_PAD_TOP).padLeft(PAD_LEFT);

        container.add(playerInfoLabelsImages).colspan(FOURTH_COL_NUM_TAKEN).top();
    }

    /**
     * Will be trigged when a diffrient piece of equipment is selected. Will update text box
     * @param item the item selected by the player
     */
    private void updateShopSpeech(String item){
        String[] items = {"DAGGER", "BAT", "AXE", "MACHETE", "CROWBAR", "SLEDGE",
        HELMET, "TORCH", "LONG_DASH", CHEST,"FIRE_CRACKER", "INVINCIBILITY"};
        String[] speech  = {DAGGER_TEXT,BAT_TEXT,AXE_TEXT,MACHETE_TEXT,CROWBAR_TEXT,SLEDGE_TEXT,HELMET_TEXT,TORCH_TEXT,
            LONG_DASH_TEXT,ARMOUR_TEXT,FIRE_CRACKER_TEXT,INVINCIBILITY_TEXT};
        int index = Arrays.asList(items).indexOf(item);

        shopkeeperSpeech = DialogueBoxFactory.createTextDialogue(speech[index]);
        shopkeeperSpeech.create();

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

        // all item files must have the 2 following data variables
        ShopItemInfoConfig itemData =
                FileLoader.readClass(ShopItemInfoConfig.class, configFilename);
        itemName = itemData.itemName;
        String description = itemData.description;
        itemPrice = itemData.price;
        typeOfItem = itemType;

        if (shopkeeperSpeech!= null) {
            shopkeeperSpeech.dispose();
        }

        // update shopkeeper speec based on item selected
        updateShopSpeech(itemName);

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
            int itemDefenceLevel = itemData.defenceLevel;
            CharSequence itemMoreDescription = String.format("Effect: %s",
                    itemData.effects);
            itemSelectedMoreInfoLabel.setText(itemMoreDescription);
        } else if (itemType == Items.OTHERS) {
            CharSequence itemMoreDescription = String.format("Effect: %s",
                    itemData.effects);
            itemSelectedMoreInfoLabel.setText(itemMoreDescription);
        }

        // item clicked will be checked against items player current hold and purchase button will be updated based on
        // that. This needs to be done as a pre-requisite
        updatePurchaseButton();
    }

    /**
     * Called when player attempts to purchase an item. If player has sufficient amount of coins - a transaction will
     * occur and this updates current player state - this then allows the purchase button to be updated based on the
     * most recent player data
     */
    private void isItemPurchasable() {
        if (playerGold >= itemPrice) {
            if (!itemName.equals(Items.TORCH.toString())) {
                setEquippedButton();
            }
            processPurchasedItem();
        } else {
            setNoFundsButton();
        }
    }

    /**
     * This will dynamically set button to not be clickable and set its' text to be "EQUIPPED"
     */
    private void setEquippedButton() {
        equipBtn.setText(EQUIPPED_TEXT);
        equipBtn.setDisabled(true);
    }

    /**
     * This will dynamically set button to be clickable and set its' text to be "PURCHASE"
     */
    private void setPurchaseButton() {
        equipBtn.setText(PURCHASE_TEXT);
        equipBtn.setDisabled(false);
    }

    /**
     * This will dynamically set button to be clickable and set its' text to be "MORE COINS"
     */
    private void setNoFundsButton() {
        equipBtn.setText(NO_FUNDS_TEXT);
        equipBtn.setDisabled(false);
    }

    /**
     * Used to check what player current has and updates the purchase button dynamically. Will disable button if
     * player has item equipped already
     */
    private void updatePurchaseButton() {
        if (playerMeleeWeaponType.equals(itemName) || typeOfItem == Items.SHIELDS || playerAbility.equals(itemName)) {
            // player may have different types of armor, and player's armor may be worse than
            // armor item clicked in shop which player can then purchase - there needs to be a separate logic for this
            if (typeOfItem == Items.SHIELDS) {
                if (playerArmorType.equals(itemName)) {
                    setEquippedButton();
                } else {
                    // player has both chest and helmet
                    if (playerDefenceLevel == Items.getDefenceLevel("ARMOUR")) {
                        setEquippedButton();
                    } else {
                        setPurchaseButton();
                    }
                }

            // player already has ability or melee weapon item clicked in shop UI
            } else {
                setEquippedButton();
            }
        } else {
            setPurchaseButton();
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

                // player currently has helmet or chest equipped only
                if (playerDefenceLevel == Items.getDefenceLevel(HELMET) ||
                        playerDefenceLevel == Items.getDefenceLevel(CHEST)) {
                    int fullArmorLevel = 5;
                    playerState.getComponent(PlayerCombatStatsComponent.class).setDefenceLevel(fullArmorLevel);

                // player not equipped with anything
                } else {
                    int defenceLevel = Items.getDefenceLevel(itemName);
                    playerState.getComponent(PlayerCombatStatsComponent.class).setDefenceLevel(defenceLevel);
                }
            }
        } else {
            // other item type includes abilities and bandages
            if (Abilities.checkAbility(itemName)) {
                Abilities ability = Abilities.getAbility(itemName);
                playerState.getComponent(PlayerAbilitiesComponent.class).setAbility(ability);
            } else if (itemName.equals(Items.TORCH.toString())) {
                playerState.getComponent(InventoryComponent.class).addTorch(ADD_TORCH);
            }
        }
        int updatePlayerGold = playerGold - itemPrice;
        playerState.getComponent(InventoryComponent.class).setGold(updatePlayerGold);
        loadPlayerData();
        updateImagesVisibility(playerDefenceLevel);
    }

    /**
     * This updates the image on the right of the shop UI to display what armor player is currently wearing
     * @param defenceLevel of player which is used to display correct image to be visible
     */
    private void updateImagesVisibility(int defenceLevel) {
        for (Image image : images) {
            image.setVisible(false);
        }

        if (defenceLevel == Items.getDefenceLevel(HELMET)) {
            playerHelmetImage.setVisible(true);
        } else if (defenceLevel == Items.getDefenceLevel("ARMOUR")) {
            playerFullArmorImage.setVisible(true);
        } else if (defenceLevel == Items.getDefenceLevel(CHEST)) {
            playerChestImage.setVisible(true);
        } else {
            playerImage.setVisible(true);
        }
    }

    /**
     * Resizes images for visual on shop UI
     */
    private void resizeImages() {
        for (Image image : images) {
            image.setHeight(LARGER_IMAGE_SIZE);
            image.setWidth(LARGER_IMAGE_SIZE);
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
                armorImageButton, helmetImageButton, fuelImageButton, fireCrackerImageButton, dashImageButton,
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
        playerAbility = playerState.getComponent(PlayerAbilitiesComponent.class).getAbility().toString();
        playerMeleeWeaponType = playerState.getComponent(PlayerMeleeAttackComponent.class).getMeleeWeaponType()
                .toString();
        playerArmorType = Items.getArmorType(playerDefenceLevel);

        CharSequence bandageText = String.format(HUD_FORMAT, playerBandage);
        CharSequence ammoText = String.format(HUD_FORMAT, playerAmmo);
        CharSequence coinText = String.format(HUD_FORMAT, playerGold);
        bandageLabel.setText(bandageText);
        ammoLabel.setText(ammoText);
        coinLabel.setText(coinText);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        setShopBoxPosSize();
    }
}
