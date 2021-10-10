package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.player.PlayerRangeAOEComponent;
import com.deco2800.game.components.player.PlayerRangeAttackComponent;
import com.deco2800.game.components.tasks.SpawnerEnemyTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.screens.MainGameScreen;
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
    private static final int NUM_BULLETS = 5; // Must be 5, to allow range-attack.
    private static final int NUM_SPAWNER_ENEMY = 0;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(30, 10);
    private static final float WALL_WIDTH = 0.1f;
    private static final String[] forestTextures = {
            "images/Final_Boss/boss_head.png", "images/Final_Boss/beam.png",
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
            "images/safehouse/exterior-day1-latest.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png"
    };
    private static final String[] forestTextureAtlases = {
            "images/Final_Boss/beam.atlas",
            "images/Final_Boss/boss_head.atlas"
    };
    private static final String[] forestSounds = {"sounds/Impact4.ogg"};
    private static final String[] playerSounds = {
            "sounds/bandage-use.ogg",
            "sounds/hurt.ogg",
            "sounds/item-pickup.ogg"
    };
    private static final String BACKGROUND_MUSIC = "sounds/final-boss-music.mp3";
    private static final String[] LEVEL3_MUSIC = {BACKGROUND_MUSIC};
        
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
        //spawnSafehouse();
        spawnBullet();
        spawnBomb();
        spawnCobweb();
        spawnBush();
        //playMusic();
        spawnLargeEnemy();
        spawnSmallEnemy();
        spawnBullet();
        spawnSpawnerEnemy();

        spawnLongRangeEnemies();
        spawnToughLongRangeEnemies();
        spawnFinalBoss();
        //playMusic();


    }

    public Entity getPlayer() {
        return player;
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

    public void spawnSafehouse() {
        GridPoint2 center = new GridPoint2(15, 15);

        Entity safehouse = SafehouseFactory.createSafehouse();
        // Position is currently procedurally (kidding, just randomly) generated.
        spawnEntityAt(safehouse, center, true, false);
    }

    private Entity spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        return newPlayer;
    }

    private void spawnBullet() {
        Array<Entity> bullets = new Array<>();

        for (int i = 0; i < NUM_BULLETS; i++) {
            Entity newBullet = BulletFactory.createBullet();
            bullets.add(newBullet);
            spawnEntity(newBullet);
        }

        player.getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
    }

    private void spawnBomb() {
        Array<Entity> bombs = new Array<>();

        for (int i = 0; i < NUM_BULLETS; i++) {
            Entity newBomb = BombFactory.createBomb();
            bombs.add(newBomb);
            spawnEntity(newBomb);
        }

        getPlayer().getComponent(PlayerRangeAOEComponent.class).addBombs(bombs);
    }

    /**
     * Spawns the spawner enemy
     */
    private void spawnSpawnerEnemy() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_SPAWNER_ENEMY; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity spawnerEnemy = NPCFactory.createSpawnerEnemy(player, this);
            spawnerEnemy.getComponent(AITaskComponent.class).addTask(new SpawnerEnemyTask(getPlayer(), 10, 5f, 6f, this, spawnerEnemy));
            spawnEntityAt(spawnerEnemy, randomPos, true, true);
        }
    }
    /**
     * Spawns a small enemy from the appropriate spawner's position
     */
    public void spawnFromSpawner(Vector2 position, int maxSpawnDistance) {
        super.spawnFromSpawner(position, maxSpawnDistance);
    }


    private void spawnSmallEnemy() {//this da noo 1
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_GHOSTS; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity smallEnemy = NPCFactory.createSmallEnemy(player);
            spawnEntityAt(smallEnemy, randomPos, true, true);
        }
    }


    private void spawnLargeEnemy() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_LARGE_ENEMY; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity largeEnemy = NPCFactory.createLargeEnemy(player);
            spawnEntityAt(largeEnemy, randomPos, true, true);
        }
    }

    private void spawnLongRangeEnemies() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
        for (int i = 0; i < NUM_LONGRANGE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity archer = NPCFactory.createLongRangeEnemy(player, this);
            spawnEntityAt(archer, randomPos, true, true);
        }
    }

    private void spawnToughLongRangeEnemies() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
        for (int i = 0; i < NUM_LONGRANGE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity touchArcher = NPCFactory.createToughLongRangeEnemy(player, this);
            spawnEntityAt(touchArcher, randomPos, true, true);
        }
    }

    private void spawnFinalBoss() {
        Entity darkness = FinalBossFactory.createDarkness(player, this);
        GridPoint2 bounds = terrain.getMapBounds(0);

        Entity boss = FinalBossFactory.createBossHead(player, bounds.x);
        GridPoint2 pos = new GridPoint2(40, 35);

        spawnEntityAt(boss, pos, true, true);
        this.spawnEntity(darkness);
        //Entity bossHead = FinalBossFactory.createBossHead();
        //pos = new GridPoint2(bounds.x/2, (int) Math.round(bounds.y*0.9));
        //spawnEntityAt(bossHead, pos, true, true); //remove this stuff once stage 1 is done
    }

    private void spawnCobweb() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_COBWEBS; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity cobweb = ObstacleFactory.createCobweb();
            spawnEntityAt(cobweb, randomPos, true, false);

        }
    }

    private void spawnBush() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_BUSH; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity bush = ObstacleFactory.createBush();
            spawnEntityAt(bush, randomPos, true, false);
        }
    }

    private void playMusic() {
        Music gameOverSong = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
        gameOverSong.setLooping(true);
        gameOverSong.setVolume(0.07f);
        gameOverSong.play();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        loadSharedAssets();
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(forestTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(forestSounds);
        resourceService.loadSounds(playerSounds);

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
        resourceService.unloadAssets(forestSounds);
        resourceService.unloadAssets(LEVEL3_MUSIC);
    }

    @Override
    public void dispose() {

        super.dispose();
        ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
        this.unloadAssets();
    }
}