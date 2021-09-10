package com.deco2800.game.components;

import com.deco2800.game.items.Items;

/**
 * Used to give items 'behaviors' that are relevant to that specific item
 * this allows player entity to identify the kinds of items that are picked up
 * during the game
 */
public class ItemComponent extends Component {
    private Items itemType;

    public ItemComponent(Items itemType) {
        itemType = itemType;
    }

    /**
     * This will return item type to make item entity distinguishable by player
     * when item is picked up
     * @return item type designated to item entity created
     */
    public Items getItemType() {
        return itemType;
    }
}
