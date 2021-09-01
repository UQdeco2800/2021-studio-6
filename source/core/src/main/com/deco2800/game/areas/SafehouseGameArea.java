package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.TouchTeleportComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
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
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(2, 2);
    private static final String[] safehouseTextures = {
            "images/safehouse/interior-day1-tile-ground1-latest.png",
            "images/safehouse/interior-day1-tile-door1-latest.png",
            "images/player_placeholders/BACK.png",
            "images/Player_Sprite/front.png",
            "images/player.png"
    };

    private final TerrainFactory terrainFactory;

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
        spawnDoor();
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

    public static void spawnDoor() {
        GridPoint2 center = new GridPoint2(15, 15);

        // Create entity
        Entity door = new Entity()
                .addComponent(new TextureRenderComponent("images/safehouse/interior-day1-tile-door1-latest.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.PARAPHERNALIA))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PARAPHERNALIA))
                .addComponent(new TouchTeleportComponent(PhysicsLayer.PLAYER));
        door.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        door.getComponent(TextureRenderComponent.class).scaleEntity();
        door.scaleHeight(2.5f);
        door.setPosition(10, 10);
        //PhysicsUtils.setScaledCollider(door, 0.3f, 0.5f);

        // Create in the world
        ServiceLocator.getEntityService().register(door);

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
}