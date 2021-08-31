package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.entities.factories.SafehouseFactory;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class SafehouseGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
    private static final String[] safehouseTextures = {
            "images/safehouse/exterior-day1-latest.png",
            "images/safehouse/interior-day1-tile-ground1-latest.png",
            "images/player_placeholders/BACK.png",
            "images/Player_Sprite/front.png"
    };

    private final TerrainFactory terrainFactory;

    private Entity player;

    public SafehouseGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
    @Override
    public void create() {
        loadAssets();
        displayUI();
        spawnTerrain();
        player = spawnPlayer(); // Always spawn player after spawning terrain, else NullPointerException
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Safehouse"));
        spawnEntity(ui);
    }

    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(TerrainType.SAFEHOUSE);
        spawnEntity(new Entity().addComponent(terrain));
    }


    private Entity spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        return newPlayer;
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(safehouseTextures);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(safehouseTextures);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }

    @Override
    public Entity spawnSafehouse() {
        return null;
    }
}
