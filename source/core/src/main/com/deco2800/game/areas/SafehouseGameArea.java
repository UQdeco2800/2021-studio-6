package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafehouseGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(SafehouseGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);

    private static final String[] safehouseTexture = {
            "images/safehouse/interior-day1-v1.0.png"
    };

    private Entity player;

    public SafehouseGameArea() {
        super();
    }

    @Override
    public void create() {
        loadAssets();
        displayUI();

        player = spawnPlayer();
    }

    private Entity spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        return newPlayer;
    }

    private void loadAssets() {
        logger.debug("Loading assets: Safehouse");
        ResourceService resourceService = ServiceLocator.getResourceService();
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Safehouse"));
        spawnEntity(ui);
    }
}
