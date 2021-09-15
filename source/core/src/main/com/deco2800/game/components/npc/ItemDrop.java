package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.MathUtils;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ItemFactory;
import com.deco2800.game.services.GameTime;

public class ItemDrop extends Component {
    private GameArea gameArea;
    private Entity itemDrop;
    public ItemDrop(GameArea gameArea) {
        this.gameArea = gameArea;
    }
    public void create() {
        this.entity.getEvents().addListener("dropItem", this::dropItem);
    }


    public void dropItem(){
        int itemTypeGen = MathUtils.random(10);

        if(itemTypeGen > 7){
            int amount = MathUtils.random(10);
            if(amount > 4){
                itemDrop = ItemFactory.createCoinPickup(1);
            }
            else {
                itemDrop = ItemFactory.createCoinPickup(5);
            }
        }else {

            int amount = MathUtils.random(10);
            if(amount > 4){
                itemDrop = ItemFactory.createAmmoPickup(1);
            }
            else {
                itemDrop = ItemFactory.createAmmoPickup(5);
            }
        }


        itemDrop.setPosition(entity.getCenterPosition().x - itemDrop.getScale().x / 2, entity.getCenterPosition().y - itemDrop.getScale().y / 2);
        gameArea.spawnEntity(itemDrop);
    }

    /**
     * Early update called once per frame of the game, before update(). Use this only for logic that
     * must run before other updates, such as physics. Not called if component is disabled.
     */
    public void earlyUpdate() {
        // No action by default.
    }

    /**
     * Called once per frame of the game, and should be used for most component logic. Not called if
     * component is disabled.
     */
    public void update() {
        // No action by default.
    }

    /** Called when the component is disposed. Dispose of any internal resources here. */
    public void dispose() {
        // No action by default.
    }

}
