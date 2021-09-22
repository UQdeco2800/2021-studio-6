package com.deco2800.game.components.shopmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.configs.ShopItemInfoConfig;
import com.deco2800.game.files.FileLoader;
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
    private static final String BACKGROUND_FILE_PATH = "images/placeholder.png";
    private static final String SHOP_KEEPER_IMAGE_FILE_PATH = "images/Player_Sprite/front01.png";
    private static final String PLAYER_IMAGE_FILE_PATH = "images/Player_Sprite/front01.png";
    private static final String SWORD_UP_IMAGE_FILE_PATH = "images/playeritems/sword/sword2.png";
    private static final String SWORD_DOWN_IMAGE_FILE_PATH = "images/playeritems/sword/sword3.png";
    private static final String DAGGER_UP_IMAGE_FILE_PATH = "images/playeritems/dagger/dagger.png";
    private static final String DAGGER_DOWN_IMAGE_FILE_PATH = "images/playeritems/dagger/dagger.png";
    private static final String AXE_UP_IMAGE_FILE_PATH = "images/playeritems/axe/axe_left2.png";
    private static final String AXE_DOWN_IMAGE_FILE_PATH = "images/playeritems/axe/axe_right4.png";
    private static final String ARMOR_UP_IMAGE_FILE_PATH = "images/playeritems/armour.png";
    private static final String ARMOR_DOWN_IMAGE_FILE_PATH = "images/playeritems/armour.png";
    private static final String HELMET_UP_IMAGE_FILE_PATH = "images/playeritems/halmet.png";
    private static final String HELMET_DOWN_IMAGE_FILE_PATH = "images/playeritems/halmet.png";
    private static final String TORCH_UP_IMAGE_FILE_PATH = "images/playeritems/tourch/tourch.png";
    private static final String TORCH_DOWN_IMAGE_FILE_PATH = "images/playeritems/tourch/tourch.png";
    private static final String AMMO_UP_IMAGE_FILE_PATH = "images/playeritems/shootingammo.png";
    private static final String AMMO_DOWN_IMAGE_FILE_PATH = "images/playeritems/shootingammo.png";
    private static final String DASH_UP_IMAGE_FILE_PATH = "images/playeritems/shootingammo.png";
    private static final String DASH_DOWN_IMAGE_FILE_PATH = "images/playeritems/shootingammo.png";
    private static final String INVINCIBLE_UP_IMAGE_FILE_PATH = "images/playeritems/shootingammo.png";
    private static final String INVINCIBLE_DOWN_IMAGE_FILE_PATH = "images/playeritems/shootingammo.png";
    private static final String GRENADE_UP_IMAGE_FILE_PATH = "images/playeritems/firecracker/firecracker8.png";
    private static final String GRENADE_DOWN_IMAGE_FILE_PATH = "images/playeritems/firecracker/firecracker7.png";
    private static final String BANDAGE_UP_IMAGE_FILE_PATH = "images/playeritems/bandage/bandage01.png";
    private static final String BANDAGE_DOWN_IMAGE_FILE_PATH = "images/playeritems/bandage/bandage02.png";
    private static final String MENU_BUTTON_STYLE = "menu-button-large";
    private static final Logger logger = LoggerFactory.getLogger(ShopMenuDisplay.class);
    private final GdxGame game;
    private boolean isEnabled = false;
    private Table container;
    private Label itemSelectedTitleLabel, itemSelectedDescriptionLabel, pricingValueLabel,
            itemSelectedMoreInfoLabel;
    private Label bandageLabel, ammoLabel, coinLabel, feedbackLabel;
    // values below will be checked and will load values from config file that saves player's states
    private int ammoNum, coinNum, bandageNum;
    private Image background;
    private static final float MAX_BOX_WIDTH = 1200;
    private static final float MAX_BOX_HEIGHT = 450;
    private static final float SMALL_PAD_TOP = 5;
    private static final float PAD_TOP = 10;
    private static final float EXTRA_PAD_TOP = 30;
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
    private static ImageButton swordImageButton, daggerImageButton, axeImageButton, armorImageButton,
            helmetImageButton, torchImageButton, bandageImageButton, dashImageButton, invincibleImageButton;
    private ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private int itemPrice, playerCoin, itemDefenceLevel = 0;
    private String itemName;
    private ImageButton storeButtonClicked = null;

    public ShopMenuDisplay(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();

        entity.getEvents().addListener("toggleShopBox", this::toggleShopBox);
        entity.getEvents().addListener("updateItemDescription", this::updateItemDetails);
    }

    public void toggleShopBox() {
        GameTime timeSource = ServiceLocator.getTimeSource();
        ServiceLocator.getGameArea().player.getEvents().trigger("resetPlayerMovements");

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
     * Configures position of menu box
     */
    private void setShopBoxPosSize() {
        container.setPosition(stage.getWidth() / 2 - container.getWidth() / 2,
                stage.getHeight() / 2 - container.getHeight() / 2);
        background.setPosition(stage.getWidth() / 2 - container.getWidth() / 2,
                stage.getHeight() / 2 - container.getHeight() / 2);
    }

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

    private void addShopKeeperImageToTable() {
        Image shopkeeperImage = new Image(ServiceLocator.getResourceService().getAsset(SHOP_KEEPER_IMAGE_FILE_PATH,
                Texture.class));
        container.add(shopkeeperImage).colspan(FIRST_COL_NUM_TAKEN).height(TABLE_BODY_HEIGHT)
                .width(SHOPKEEPER_IMAGE_WIDTH).top();
    }

    private void addItemButtonImagesToTable() {
        Table itemsLabelImages = new Table();

        CharSequence titleText = "SWORD";
        itemSelectedTitleLabel = new Label(titleText, skin, "white");
        // for weapon label selected - default is sword. For now
        itemsLabelImages.row();
        itemsLabelImages.add(itemSelectedTitleLabel).colspan(SEC_COL_NUM_TAKEN).height(COMMON_LABEL_HEIGHT);

        // first row for weapon image buttons
        itemsLabelImages.row();
        Drawable swordUp = createImagesForButtons(SWORD_UP_IMAGE_FILE_PATH);
        Drawable swordDown = createImagesForButtons(SWORD_DOWN_IMAGE_FILE_PATH);
        swordImageButton = new ImageButton(swordUp, swordDown, swordDown);
        MenuUtility.addButtonSelectListener(entity, swordImageButton, "updateItemDescription",
                "configs/ShopSwordInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(swordImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable daggerUp = createImagesForButtons(DAGGER_UP_IMAGE_FILE_PATH);
        Drawable daggerDown = createImagesForButtons(DAGGER_DOWN_IMAGE_FILE_PATH);
        daggerImageButton = new ImageButton(daggerUp, daggerDown, daggerDown);
        MenuUtility.addButtonSelectListener(entity, daggerImageButton, "updateItemDescription",
                "configs/ShopDaggerInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(daggerImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        Drawable axeUp = createImagesForButtons(AXE_UP_IMAGE_FILE_PATH);
        Drawable axeDown = createImagesForButtons(AXE_DOWN_IMAGE_FILE_PATH);
        axeImageButton = new ImageButton(axeUp, axeDown, axeDown);
        MenuUtility.addButtonSelectListener(entity, axeImageButton, "updateItemDescription",
                "configs/ShopAxeInfo.json", Items.MELEE_WEAPONS);
        itemsLabelImages.add(axeImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

        // second row for weapon image buttons
        // armors, helmets and torches have same images for now - #TODO: change images to have borders or orientation
        itemsLabelImages.row();
        Drawable armorUp = createImagesForButtons(ARMOR_UP_IMAGE_FILE_PATH);
        Drawable armorDown = createImagesForButtons(ARMOR_DOWN_IMAGE_FILE_PATH);
        armorImageButton = new ImageButton(armorUp, armorDown, armorDown);
        MenuUtility.addButtonSelectListener(entity, armorImageButton, "updateItemDescription",
                "configs/ShopArmourInfo.json", Items.SHIELDS);
        itemsLabelImages.add(armorImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

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

        // third row for weapon image buttons
        // ammo, grenade and bandage have same images for now - #TODO: change images to have borders or orientation
        itemsLabelImages.row();
        Drawable dashUp = createImagesForButtons(DASH_UP_IMAGE_FILE_PATH);
        Drawable dashDown = createImagesForButtons(DASH_DOWN_IMAGE_FILE_PATH);
        dashImageButton = new ImageButton(dashUp, dashDown, dashDown);
        MenuUtility.addButtonSelectListener(entity, dashImageButton, "updateItemDescription",
                "configs/ShopLongDashAbilityInfo.json", Items.OTHERS);
        itemsLabelImages.add(dashImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT);

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

//        #TODO: add 3 more new weapons when done, process should be the same as how rest of weapons are loaded
//        forth row for weapon image buttons
//        itemsLabelImages.row();
//        Drawable newWeapon1Up = createImagesForButtons(GRENADE_UP_IMAGE_FILE_PATH);
//        Drawable newWeapon1Up = createImagesForButtons(GRENADE_DOWN_IMAGE_FILE_PATH);
//        ImageButton newWeapon = new ImageButton(dashUp, dashDown, dashDown);
//        itemsLabelImages.add(dashImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT).padLeft(40).padRight(40);

//        #TODO: grenade have yet to be implemented, once implemented include
//        Drawable grenadeUp = createImagesForButtons(GRENADE_UP_IMAGE_FILE_PATH);
//        Drawable grenadeDown = createImagesForButtons(GRENADE_DOWN_IMAGE_FILE_PATH);
//        ImageButton grenadeImageButton = new ImageButton(grenadeUp, grenadeDown, grenadeDown);
//        itemsLabelImages.add(grenadeImageButton).colspan(10).height(IMAGE_BUTTON_HEIGHT).padLeft(40).padRight(40);

        List<ImageButton> imageButtonList = Arrays.asList(swordImageButton, daggerImageButton, axeImageButton,
                armorImageButton, helmetImageButton, torchImageButton, bandageImageButton, dashImageButton,
                invincibleImageButton);

        container.add(itemsLabelImages).colspan(SEC_COL_NUM_TAKEN).top();
    }

    private void addItemDescriptionToTable() {
        Table itemsDescriptionLabels = new Table();

        // title of item description col - first row of it
        CharSequence titleText = "ITEM DETAILS";
        Label itemSelectedDescriptionTitle = new Label(titleText, skin, "white");
        itemsDescriptionLabels.row().colspan(THIRD_COL_NUM_TAKEN).height(TABLE_BODY_HEIGHT);
        itemsDescriptionLabels.add(itemSelectedDescriptionTitle).colspan(THIRD_COL_NUM_TAKEN)
                .height(COMMON_LABEL_HEIGHT);

        // for description of item selected - default is for sword; for now #TODO: update description when item selected by player
        // second row of item description col
        CharSequence itemDescriptionText = "The sword is the most common weapon there is but it has decent range, " +
                "damage and it is versatile enough to fend off any enemies in the game - especially night crawlers!";
        itemSelectedDescriptionLabel = new Label(itemDescriptionText, skin, "white-font");
        itemSelectedDescriptionLabel.setWrap(true);
        itemsDescriptionLabels.row().colspan(THIRD_COL_NUM_TAKEN);
        itemsDescriptionLabels.add(itemSelectedDescriptionLabel).colspan(THIRD_COL_NUM_TAKEN)
                .height(ITEM_LABEL_DESCRIPTION_HEIGHT).width(280);

        // third row of item description col (if there is one - dependent on time of item clicked)
        CharSequence itemMoreDescription = "Attack Duration: Long, Damage: 10 & Knockback: Strong";
        itemSelectedMoreInfoLabel = new Label(itemMoreDescription, skin, "white-font");
        itemSelectedMoreInfoLabel.setWrap(true);
        itemsDescriptionLabels.row().colspan(THIRD_COL_NUM_TAKEN);
        itemsDescriptionLabels.add(itemSelectedMoreInfoLabel).colspan(THIRD_COL_NUM_TAKEN)
                .height(ITEM_LABEL_MORE_INFO_HEIGHT).width(280).padTop(PAD_TOP);


        // price of item selected (clicked by user) - default is for sword; for now
        // fourth row of item description col
        // #TODO: update price of item when item selected by player
        CharSequence priceText = "PRICE: 70";
        pricingValueLabel = new Label(priceText, skin, "white");

        itemsDescriptionLabels.row().height(COMMON_LABEL_HEIGHT).padTop(EXTRA_PAD_TOP).center();
        itemsDescriptionLabels.add(pricingValueLabel).padLeft(PAD_TO_CENTER_PRICING);

        Image moneyBagImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/playeritems/coin/money bag.png", Texture.class));
        itemsDescriptionLabels.add(moneyBagImage).size(COMMON_LABEL_HEIGHT);

        container.add(itemsDescriptionLabels).colspan(THIRD_COL_NUM_TAKEN).top();
    }

    public boolean checkShopPopupVisibility() {
        return container.isVisible();
    }

    private Drawable createImagesForButtons(String filePath) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(
                new TextureRegion(ServiceLocator.getResourceService()
                        .getAsset(filePath, Texture.class)));
        drawable.setMinWidth(IMAGE_BUTTON_HEIGHT);
        drawable.setMinHeight(IMAGE_BUTTON_HEIGHT);
        return drawable;
    }

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

    private void addPlayerInfoAndBuyingButtonToTable() {
        Table playerInfoLabelsImages = new Table();

        // image of player character - might want to have a png for player's face only with no black background
        // #TODO: may want to have design of player character's face only similar to art reference
        playerInfoLabelsImages.row().colspan(FOURTH_COL_NUM_TAKEN).height(TABLE_BODY_HEIGHT);
        Image playerImage = new Image(ServiceLocator.getResourceService().getAsset(PLAYER_IMAGE_FILE_PATH,
                Texture.class));
        playerInfoLabelsImages.add(playerImage).colspan(10).height(LARGER_IMAGE_SIZE)
                .width(LARGER_IMAGE_SIZE).top();

        // #TODO: update bandage number, ammo number and coin number when shop popup box toggles
        // currently used purely for styling and testing purposes
        Table playerInfo = new Table();
        CharSequence bandageText = String.format(" x %d", 3);
        CharSequence ammoText = String.format(" x %d", 10);
        CharSequence coinText = String.format(" x %d", 100);

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
        CharSequence feedbackText = "INSUFFICIENT COINS";
        feedbackLabel = new Label(feedbackText, skin, "red");
        playerInfoLabelsImages.add(feedbackLabel).padTop(PAD_TOP);

        // button that triggers event for data checking from player current state
        playerInfoLabelsImages.row().height(COMMON_LABEL_HEIGHT).colspan(FOURTH_COL_NUM_TAKEN);
        String equipTextButton = "EQUIP";
        TextButton equipBtn = new TextButton(equipTextButton, skin, MENU_BUTTON_STYLE);
        playerInfoLabelsImages.add(equipBtn).padTop(PAD_TOP);

        container.add(playerInfoLabelsImages).colspan(FOURTH_COL_NUM_TAKEN).top();
    }

    private void updateItemDetails(String configFilename, Items itemType) {
        logger.debug("Button image clicked, buying system update");
        uncheckImageButton();

        // all item files must have the 2 following data variables
        ShopItemInfoConfig itemData =
                FileLoader.readClass(ShopItemInfoConfig.class, configFilename);
        itemName = itemData.itemName;
        String description = itemData.description;
        itemPrice = itemData.price;
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

    private void isItemPurchasable() {

    }

    private void uncheckImageButton() {
        List<ImageButton> imageButtonList = Arrays.asList(swordImageButton, daggerImageButton, axeImageButton,
                armorImageButton, helmetImageButton, torchImageButton, bandageImageButton, dashImageButton,
                invincibleImageButton);
        imageButtons.addAll(imageButtonList);

        for (ImageButton imageButton : imageButtonList) {
            if (imageButton.isChecked()) {
                if (storeButtonClicked == null) {
                    storeButtonClicked = imageButton;
                } else {
                    storeButtonClicked.setChecked(false);

                    // store new button that is clicked and set check false to old button that
                    // was already clicked in the past
                    storeButtonClicked = imageButton;
                }
            }
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        setShopBoxPosSize();
    }
}
