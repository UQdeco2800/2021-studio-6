package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Reusable component used to host all trigger events that are related to items
 * found player's inventory component. These events are related to effects that comes with
 * utilizing any item in inventory
 */
public class PlayerReusableComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerReusableComponent.class);
    private InventoryComponent inventory;
    private PlayerCombatStatsComponent playerStats;
    private static final int MIN_BANDAGE_NUM = 1;
    private static final int INCREASE_WOUND_STATE = 1;
    private static final int BANDAGE_USED = 1;

    public PlayerReusableComponent() {
    }

    @Override
    public void create() {
        super.create();
        inventory = entity.getComponent(InventoryComponent.class);
        playerStats = entity.getComponent(PlayerCombatStatsComponent.class);

        entity.getEvents().addListener("useBandage", this::applyBandage);
    }

    void applyBandage() {
        if (inventory.hasBandages(MIN_BANDAGE_NUM)) {
            // if player is at max wound state, do not do anything
            if (!playerStats.atMax()) {
                int currentWoundState = playerStats.getWoundState();
                int bandageCount = inventory.getBandages();

                updateBandageCount(bandageCount - BANDAGE_USED);
                playerStats.setWoundState(currentWoundState + INCREASE_WOUND_STATE);
            }
            System.out.println("bandage used triggered");
        }
    }

    void updateBandageCount(int newBandageCount) {
        inventory.setBandages(newBandageCount);
    }
}
