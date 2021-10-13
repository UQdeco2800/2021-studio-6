package com.deco2800.game.ui.terminal.commands;

import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;

public class SetPlayerInvincibleCommand implements Command{

    /**
     * Action a command.
     *
     * @param args command args
     * @return command was successful
     */
    @Override
    public boolean action(ArrayList<String> args) {

        switch (args.get(0)) {
            case "on":
                ServiceLocator.getGameArea().player.getComponent(PlayerCombatStatsComponent.class)
                        .invincibleStart(1000000000);
                return true;
            case "off":
                ServiceLocator.getGameArea().player.getComponent(PlayerCombatStatsComponent.class)
                        .invincibleStart(0);
                return true;
            default:
                return false;
        }

    }
}
