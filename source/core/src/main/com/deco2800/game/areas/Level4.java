package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.TouchTeleportComponent;
import com.deco2800.game.components.player.PlayerRangeAOEComponent;
import com.deco2800.game.components.player.PlayerRangeAttackComponent;
import com.deco2800.game.components.tasks.SpawnerEnemyTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.lighting.FlickerLightComponent;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class Level4 extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(Level3.class);
    private static final int NUM_TREES = 0;
    private static final int NUM_COBWEBS = 0;
    private static final int NUM_BUSH = 0;
    private static final int NUM_LARGE_ENEMY = 0;
    private static final int NUM_GHOSTS = 0;
    private static final int NUM_LONGRANGE = 0;
    private static final int NUM_BULLETS = 30; // Must be 5, to allow range-attack.
    private static final int NUM_SPAWNER_ENEMY = 0;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(20, 10);
    private static final float WALL_WIDTH = 0.1f;
    private static final String[] forestTextures = {
            "images/Final_Boss/boss_head.png",
            "images/Final_Boss/beam.png",
            "images/Final_Boss/laser.png",
            "images/Final_Boss/boss_head.png",
            "images/obstacle_sprite/cobweb.png",
            "images/obstacle_sprite/bush.png",
            "images/level_2/level2_grass_1.png",
            "images/level_2/level2_grass_2.png",
            "images/level_2/level2_grass_3.png",
            "images/level_2/level2_grass_4.png",
            "images/level_2/level2_grass_5.png",
            "images/level_2/level2_grass_6.png",
            "images/level_2/level2_tree_1-1.png",
            "images/level_2/level2_tree_2-1.png",
            "images/level_3/level3_grass_tiles/grass-base.png",
            "images/level_3/level3_grass_tiles/grass-1.png",
            "images/level_3/level3_grass_tiles/grass-2.png",
            "images/level_3/level3_grass_tiles/grass-3.png",
            "images/level_3/level3_grass_tiles/grass-4.png",
            "images/level_3/sand.png",
            "images/level_3/new_darker_water_tiles/water-full.png",
            "images/level_3/sand_to_water.png",
            "images/level_3/grass_to_sand.png",
            "images/level_3/grass_sand_mix.png",
            "images/Enemy_Assets/SpawnerEnemy/spawnerEgg.png",
            "images/safehouse/exterior-day1-latest.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/Final_Boss/dock.png",
            "images/Final_Boss/boat.png",
            "images/Final_Boss/healthbar_background.png",
            "images/Final_Boss/healthbar_foreground.png",
            "images/Enemy_Assets/LongRangeEnemy/blood_ball.png"
    };
    private static final String[] forestTextureAtlases = {
            "images/Final_Boss/beam.atlas",
            "images/Final_Boss/final_beam.atlas",
            "images/Final_Boss/boss_head.atlas",
            "images/Final_Boss/boat.atlas",
            "images/terrain_iso_grass.atlas",
            "images/Player_Animations/player_movement.atlas",
            "images/Player_Sprite/player_movement.atlas",
            "images/playeritems/tourch/torch.atlas",
            "images/hud/dashbar.atlas",
            "images/hud/health.atlas",
            "images/weapon/crowbar.atlas",
            "images/weapon/axe.atlas",
            "images/weapon/sledge.atlas",
            "images/weapon/machete.atlas",
            "images/weapon/baseball.atlas",
            "images/weapon/dagger.atlas",
            "images/playeritems/firecracker/firecracker.atlas"
    };

    private static final String[] forestSounds = {"sounds/Impact4.ogg", "sounds/final_boss/boss_hit.mp3", "sounds/final_boss/fire_laser.mp3"};

    private static final String BACKGROUND_MUSIC = "sounds/final_boss/boss_music.mp3";
    private static final String OCEAN_AMBIENT = "sounds/final_boss/ocean_waves.mp3";
    private static final String BOSS_DEATH_SOUND = "sounds/final_boss/boss-death-sound.mp3";
    private static final String[] LEVEL3_MUSIC = {BACKGROUND_MUSIC, BOSS_DEATH_SOUND, OCEAN_AMBIENT};
        
    private final TerrainFactory terrainFactory;

    public Level4(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
    @Override
    public void create() {
        loadAssets();

        displayUI();

        spawnTerrain();
        spawnTrees();
        player = spawnPlayer();

        spawnBullet();
        spawnFireCracker();

        playMusic();

        spawnBullet();
        spawnFinalBoss();
        spawnWaterTiles();
        spawnDock();
        spawnBoat();
    }

    private void displayUI() {
        Entity ui = new Entity();
        // Can change level name here
        ui.addComponent(new GameAreaDisplay("Level 4"));
        spawnEntity(ui);
    }

    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(TerrainType.BOSS);
        spawnEntity(new Entity().addComponent(terrain));

        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        // Left
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
                GridPoint2Utils.ZERO, false, false);
        // Right
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
                new GridPoint2(tileBounds.x, 0),false,false);
        // Top
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
                new GridPoint2(0, tileBounds.y),false,false);
        // Bottom
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
                GridPoint2Utils.ZERO, false, false);
        // Right
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        // Top
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        // Bottom
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);
    }

    private void spawnTrees() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_TREES; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity tree = ObstacleFactory.createPineTree();
            spawnEntityAt(tree, randomPos, true, false);
        }
    }

    private Entity spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        return newPlayer;
    }

    private void spawnBullet() {
        Array<Entity> bullets = new Array<>();

        for (int i = 0; i < NUM_BULLETS; i++) {
            Entity newBullet = ProjectileFactory.createBullet();
            bullets.add(newBullet);
            spawnEntity(newBullet);
        }

        player.getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
    }

    private void spawnFireCracker() {
        Entity fireCracker = ProjectileFactory.createFireCracker();
        spawnEntity(fireCracker);

        player.getComponent(PlayerRangeAOEComponent.class).addFireCracker(fireCracker);
    }


    private void spawnFinalBoss() {

        GridPoint2 bounds = terrain.getMapBounds(0);
        Entity darkness = FinalBossFactory.createDarkness(player, bounds, this);
        Entity boss = FinalBossFactory.createBossHead(player, bounds.x, this, darkness);
        GridPoint2 pos = new GridPoint2(40, 35);


        GridPoint2 bossPos = new GridPoint2(bounds.x/2, bounds.y/2 - 2);
        GridPoint2 darknessPos = new GridPoint2(bounds.x/2, bounds.y/2);
        spawnEntityAt(boss, bossPos, true, true);
        this.spawnEntityAt(darkness, darknessPos, true, true);

    }

    private void spawnWaterTiles() {
        int SHORELINE = 28;

        GridPoint2 bounds = terrain.getMapBounds(0);
        GridPoint2 waterLocation;

        for (int i = 0; i < bounds.x; i++) {
            if(i < bounds.x/2 - 1 || i > bounds.x/2 + 1) {
                Entity water = ObstacleFactory.createWaterSandTile8();
                waterLocation = new GridPoint2(i, SHORELINE);
                spawnEntityAt(water, waterLocation, true, true);
            }
        }
    }

    private void spawnDock() {
        int DOCK_LOCATION = 28;

        Entity dock = new Entity()
                .addComponent(new TextureRenderComponent("images/Final_Boss/dock.png"))
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SAFEHOUSE))
                .addComponent(new TouchTeleportComponent(PhysicsLayer.PLAYER));

        dock.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        dock.getComponent(TextureRenderComponent.class).scaleEntity();
        dock.setScale(new Vector2(2f, 3f));
        Vector2 boundingBox = dock.getScale().cpy().scl(1f, 0.1f);
        dock.getComponent(HitboxComponent.class).setAsBoxAligned(boundingBox, PhysicsComponent.AlignX.CENTER, PhysicsComponent.AlignY.TOP);

        GridPoint2 bounds = terrain.getMapBounds(0);
        GridPoint2 dockLocation = new GridPoint2(bounds.x/2, DOCK_LOCATION);
        spawnEntityAt(dock, dockLocation, true, true);
    }

    private void spawnBoat() {
        int BOAT_LOCATION = 31;

        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/Final_Boss/boat.atlas", TextureAtlas.class));
        animator.addAnimation("float", 0.3f, Animation.PlayMode.LOOP);

        Entity boat = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(animator)
                .addComponent(new PointLightComponent(Color.WHITE, 0.5f, 0, 0));

        boat.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        boat.setScale(new Vector2(2f, 2f));

        GridPoint2 bounds = terrain.getMapBounds(0);
        GridPoint2 boatLocation = new GridPoint2(bounds.x/2, BOAT_LOCATION);
        spawnEntityAt(boat, boatLocation, true, true);
        animator.startAnimation("float");
    }

    private void playMusic() {
        Music gameOverSong = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
        gameOverSong.setLooping(true);
        gameOverSong.setVolume(0.07f);
        gameOverSong.play();
    }

    @Override
    public void stopMusic() {
        ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
        Music oceanAmbient = ServiceLocator.getResourceService().getAsset(OCEAN_AMBIENT, Music.class);
        Music deathSound = ServiceLocator.getResourceService().getAsset(BOSS_DEATH_SOUND, Music.class);

        deathSound.setVolume(0.07f);
        deathSound.play();
        oceanAmbient.play();


    }

    private void loadAssets() {
        logger.debug("Loading assets");
        loadSharedAssets();
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(forestTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(playerSounds);
        resourceService.loadSounds(forestSounds);

        resourceService.loadMusic(LEVEL3_MUSIC);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        unloadSharedAssets();
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(forestTextures);
        resourceService.unloadAssets(forestTextureAtlases);
        resourceService.unloadAssets(playerSounds);
        resourceService.unloadAssets(LEVEL3_MUSIC);
    }

    @Override
    public void dispose() {

        super.dispose();
        ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
        this.unloadAssets();
    }
}