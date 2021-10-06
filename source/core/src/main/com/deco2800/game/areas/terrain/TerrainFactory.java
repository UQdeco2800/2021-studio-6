package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private static final GridPoint2 MAP_SIZE = new GridPoint2(30, 30);
  private static final GridPoint2 MAP_SIZE_CITY = new GridPoint2(16, 16);
  private static final GridPoint2 MAP_SIZE_FOREST = new GridPoint2(64, 37);
  private static final GridPoint2 MAP_SIZE_FOREST2 = new GridPoint2(51, 51);
  private static final GridPoint2 MAP_SIZE_SAFEHOUSE = new GridPoint2(15, 15);
  private static final GridPoint2 MAP_SIZE_BOSS = new GridPoint2(80, 40);
  private static final int TUFT_TILE_COUNT = 30;
  private static final int ROCK_TILE_COUNT = 30;
  private static final int GRASS_TILE_COUNT = 123;

  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;

  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainOrientation.ORTHOGONAL);
  }

  /**
   * Create a terrain factory
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   * @param orientation orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
    this.orientation = orientation;
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @param terrainType Terrain to create.
   * @return Terrain component which renders the terrain.
   */
  public TerrainComponent createTerrain(TerrainType terrainType) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    switch (terrainType) {
      // Delete the FOREST_DEMO case code block once we no longer need a placeholder for other gamearea levels.
      case FOREST_DEMO:
        TextureRegion orthoGrass =
            new TextureRegion(resourceService.getAsset("images/grass_1.png", Texture.class));
        TextureRegion orthoTuft =
            new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
        TextureRegion orthoRocks =
            new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));
        return createForestDemoTerrain(0.5f, orthoGrass, orthoTuft, orthoRocks);

      // Level 1 tiles
      case CITY:
        TextureRegion cityBackground =
            new TextureRegion(resourceService.getAsset("images/grass_1.png", Texture.class));
        TextureRegion cityRoad =
            new TextureRegion(resourceService.getAsset("images/level_1/road_tile_black.png", Texture.class));
        TextureRegion citySidewalk =
            new TextureRegion(resourceService.getAsset("images/level_1/sidewalk.png", Texture.class));
        TextureRegion crackedSidewalk =
            new TextureRegion(resourceService.getAsset("images/level_1/cracked_sidewalk.png", Texture.class));
        TextureRegion cityCurbUpper =
            new TextureRegion(resourceService.getAsset("images/level_1/curbUpper.png", Texture.class));
        TextureRegion cityCurbLower =
            new TextureRegion(resourceService.getAsset("images/level_1/curbLower.png", Texture.class));
        TextureRegion crackedRoad =
            new TextureRegion(resourceService.getAsset("images/level_1/road_tile_cracked.png", Texture.class));
        TextureRegion laneMarkings =
            new TextureRegion(resourceService.getAsset("images/level_1/road_tile_white.png", Texture.class));
        return createCityTerrain(1f, cityRoad, citySidewalk, crackedSidewalk, cityCurbUpper, cityCurbLower,
            crackedRoad, cityBackground, laneMarkings);

      // Level 2 tiles
      case FOREST:
        TextureRegion grass1 =
                new TextureRegion(resourceService.getAsset("images/level_2/level2_grass_1.png", Texture.class));
        TextureRegion grass2 =
                new TextureRegion(resourceService.getAsset("images/level_2/level2_grass_2.png", Texture.class));
        TextureRegion grass3 =
                new TextureRegion(resourceService.getAsset("images/level_2/level2_grass_3.png", Texture.class));
        TextureRegion grass4 =
                new TextureRegion(resourceService.getAsset("images/level_2/level2_grass_4.png", Texture.class));
        TextureRegion grass5 =
                new TextureRegion(resourceService.getAsset("images/level_2/level2_grass_5.png", Texture.class));
        TextureRegion grass6 =
                new TextureRegion(resourceService.getAsset("images/level_2/level2_grass_6.png", Texture.class));
        TextureRegion grass7 =
                new TextureRegion(resourceService.getAsset("images/level_2/level2_grass_7.png", Texture.class));
        TextureRegion grass8 =
                new TextureRegion(resourceService.getAsset("images/level_2/level2_grass_8.png", Texture.class));
        TextureRegion backgroundTile =
                new TextureRegion(resourceService.getAsset("images/level_2/level2_background_tile.png", Texture.class));

        return createForestTerrain(1f, grass1, grass2, grass3, grass4, grass5, grass6, grass7, grass8, backgroundTile);

      // Level 3 tiles
      // TODO: Add actual level 3 tiles here
      case FOREST2:
        TextureRegion ground =
                new TextureRegion(resourceService.getAsset("images/level_3/level3_grass_test2.png", Texture.class));

        TextureRegion backgroundTile2 =
                new TextureRegion(resourceService.getAsset("images/level_2/level2_background_tile.png", Texture.class));

        return createForest2Terrain(1f, ground, backgroundTile2);

      // Safehouse tiles
      case SAFEHOUSE:
        TextureRegion orthoGround = new TextureRegion(resourceService
                        .getAsset("images/safehouse/safehouse-interior-layout.png", Texture.class));
        return createSafehouseTerrain(0.75f, orthoGround);

      case BOSS:
        TextureRegion grassB =
                new TextureRegion(resourceService.getAsset("images/grass_1.png", Texture.class));
        TextureRegion tuftB =
                new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
        TextureRegion rocksB =
                new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));
        return createBossTerrain(0.5f, grassB, tuftB, rocksB);

      default:
        System.out.println("default");
        return null;
    }
  }

  private TerrainComponent createBossTerrain(
          float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks
  ) {
    GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    TiledMap tiledMap = createBossTiles(tilePixelSize, grass, grassTuft, rocks);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  // Delete this demo terrain component once we no longer need a placeholder for other gamearea levels.
  private TerrainComponent createForestDemoTerrain(
          float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks
  ) {
    GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, grassTuft, rocks);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  /**
   * Renders the Level 1 map terrain with the appropriate tileset, map scale at an orthogonal camera angle.
   * @param tileWorldSize Scales the tile size for the map.
   * @param cityRoad Road tileset.
   * @param citySidewalk Side walk tileset.
   * @param cityCurbUpper Upper curb tileset.
   * @param cityCurbLower Lower curb tileset.
   * @param crackedRoad Cracked road tileset.
   * @param cityBackground Background city tileset.
   * @param laneMarkings Lane marking tileset.
   * @return Terrain component which renders the terrain for Level 1.
   */
  private TerrainComponent createCityTerrain(
      float tileWorldSize, TextureRegion cityRoad, TextureRegion citySidewalk, TextureRegion crackedSidewalk,
      TextureRegion cityCurbUpper, TextureRegion cityCurbLower, TextureRegion crackedRoad,
      TextureRegion cityBackground, TextureRegion laneMarkings
  ) {
    GridPoint2 tilePixelSize = new GridPoint2(cityRoad.getRegionWidth(), cityRoad.getRegionHeight());
    TiledMap tiledMap = createCityTiles(tilePixelSize, cityRoad, citySidewalk, crackedSidewalk, cityCurbUpper,
        cityCurbLower, crackedRoad, cityBackground, laneMarkings);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  /**
   * Renders the Level 2 map terrain with the appropriate tileset, map scale at an orthogonal camera angle.
   * @param tileWorldSize Scales the tileset size for the map.
   * @param grass1 Grass tileset 1.
   * @param grass2 Grass tileset 2.
   * @param grass3 Grass tileset 3.
   * @param grass4 Grass tileset 4.
   * @param grass5 Grass tileset 5.
   * @param grass6 Grass tileset 6.
   * @param grass7 Grass tileset 7.
   * @param grass8 Grass tileset 8.
   * @param backgroundTile Background tile that matches the MainGameScreen background colour.
   * @return Terrain component which renders the terrain for Level 2.
   */
  private TerrainComponent createForestTerrain(
          float tileWorldSize, TextureRegion grass1, TextureRegion grass2, TextureRegion grass3,
          TextureRegion grass4, TextureRegion grass5, TextureRegion grass6, TextureRegion grass7,
          TextureRegion grass8, TextureRegion backgroundTile
  ) {
    GridPoint2 tilePixelSize = new GridPoint2(grass1.getRegionWidth(), grass1.getRegionHeight());
    TiledMap tiledMap = createForestTiles(tilePixelSize, grass1, grass2, grass3, grass4, grass5, grass6,
            grass7, grass8, backgroundTile);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  // TODO: Add javadoc and replace placeholder tiles
  private TerrainComponent createForest2Terrain(
          float tileWorldSize, TextureRegion grass1, TextureRegion backgroundTile
  ) {
    GridPoint2 tilePixelSize = new GridPoint2(grass1.getRegionWidth(), grass1.getRegionHeight());
    TiledMap tiledMap = createForest2Tiles(tilePixelSize, grass1, backgroundTile);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  /**
   * Renders the Safehouse map terrain with the appropriate tileset, map scale at an orthogonal camera angle.
   * @param tileWorldSize
   * @param ground Safehouse floor tileset.
   * @return Terrain component which renders the terrain for Safehouse map.
   */
  private TerrainComponent createSafehouseTerrain(
          float tileWorldSize, TextureRegion ground
  ) {
    GridPoint2 tilePixelSize = new GridPoint2(ground.getRegionWidth() / 16, ground.getRegionHeight() / 9);
    System.out.println(tilePixelSize);
    TiledMap tiledMap = createSafehouseTiles(tilePixelSize, ground);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  /**
   * Renders the tilemap of the gamearea levels in either the orthogonal, isometric or hexagonal orientation.
   * In this game, it is in orthogonal.
   * @param tiledMap Tileset positions of the gamearea level.
   * @param tileScale Tileset scale.
   * @return
   */
  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    switch (orientation) {
      case ORTHOGONAL:
        return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
      case ISOMETRIC:
        return new IsometricTiledMapRenderer(tiledMap, tileScale);
      case HEXAGONAL:
        return new HexagonalTiledMapRenderer(tiledMap, tileScale);
      default:
        return null;
    }
  }

  // Delete this tilemap function once we no longer need a placeholder for other gamearea levels.
  private TiledMap createForestDemoTiles(
      GridPoint2 tileSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create base grass
    fillTiles(layer, MAP_SIZE, grassTile);

    // Add some grass and rocks
    fillTilesAtRandom(layer, MAP_SIZE, grassTuftTile, TUFT_TILE_COUNT);
    fillTilesAtRandom(layer, MAP_SIZE, rockTile, ROCK_TILE_COUNT);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private TiledMap createBossTiles(
          GridPoint2 tileSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);

    int xScale = 1;
    int yScale = 1;
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE_BOSS.x * xScale, MAP_SIZE_BOSS.y * yScale, tileSize.x, tileSize.y);

    // Create base grass
    fillTiles(layer, MAP_SIZE_BOSS, xScale, yScale, grassTile);

    // Add some grass and rocks
    fillTilesAtRandom(layer, MAP_SIZE_BOSS, grassTuftTile, TUFT_TILE_COUNT);
    fillTilesAtRandom(layer, MAP_SIZE_BOSS, rockTile, ROCK_TILE_COUNT);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  /**
   * Renders the Level 1 tilesets at their appropriate position of the grid layout map.
   * @param tileSize Scale of the tileset.
   * @param cityRoad Road tileset.
   * @param citySidewalk Side walk tileset.
   * @param cityCurbUpper Upper curb tileset.
   * @param cityCurbLower Lower curb tileset.
   * @param crackedRoad Cracked road tileset.
   * @param cityBackground Background city tileset.
   * @param laneMarkings Lane marking tileset.
   * @return Tileset map positions for Level 1.
   */
  private TiledMap createCityTiles(
      GridPoint2 tileSize, TextureRegion cityRoad, TextureRegion citySidewalk, TextureRegion crackedSidewalk,
      TextureRegion cityCurbUpper, TextureRegion cityCurbLower, TextureRegion crackedRoad,
      TextureRegion cityBackground, TextureRegion laneMarkings) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile roadTile = new TerrainTile(cityRoad);
    TerrainTile sidewalkTile = new TerrainTile(citySidewalk);
    TerrainTile crackedSidewalkTile = new TerrainTile(crackedSidewalk);
    TerrainTile curbUpperTile = new TerrainTile(cityCurbUpper);
    TerrainTile curbLowerTile = new TerrainTile(cityCurbLower);
    TerrainTile crackedRoadTile = new TerrainTile(crackedRoad);
    TerrainTile cityBackgroundTile = new TerrainTile(cityBackground);
    TerrainTile laneMarkingTile = new TerrainTile(laneMarkings);

    //Multiplier to size of map on x and y coordinates
    int xScale = 8;
    int yScale = 1;
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, tileSize.x, tileSize.y);
    fillTiles(layer, MAP_SIZE_CITY, xScale, yScale, cityBackgroundTile);

    //Fill sidewalk tiles
    GridPoint2 start = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 0, 0);
    GridPoint2 end = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 1, 0.1);
    setTilesInRegion(layer, sidewalkTile, start, end);
    //fillTilesAtRandomInRegion(layer, crackedSidewalkTile, start, end, 10);

    start = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 0, 0.6);
    end = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 1, 0.7);
    setTilesInRegion(layer, sidewalkTile, start, end);
    //fillTilesAtRandomInRegion(layer, crackedSidewalkTile, start, end, 10);

    //Set road tiles
    start = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 0, 0.15);
    end = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 1, 0.58);
    setTilesInRegion(layer, roadTile, start, end);
    fillTilesAtRandomInRegion(layer, crackedRoadTile,
        new GridPoint2(start.x + 1, start.y + 1), new GridPoint2(end.x - 1, end.y - 1), 30);

    //Fill curb tiles
    start = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 0, 0.1);
    end = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 1, 0.15);
    setTilesInRegion(layer, curbLowerTile, start, end);

    start = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 0, 0.58);
    end = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 1, 0.65);
    setTilesInRegion(layer, curbUpperTile, start, end);

    //Add lane markings
    start = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 0, 0.35);
    end = calculatePosition(MAP_SIZE_CITY.x * xScale, MAP_SIZE_CITY.y * yScale, 1, 0.4);
    setTilesInLineAtIntervals(layer, laneMarkingTile, start, end, 5, 3);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  /**
   * Renders the Level 2 tilesets at their appropriate position of the grid layout map.
   * @param grass1 Grass tileset 1.
   * @param grass2 Grass tileset 2.
   * @param grass3 Grass tileset 3.
   * @param grass4 Grass tileset 4.
   * @param grass5 Grass tileset 5.
   * @param grass6 Grass tileset 6.
   * @param grass7 Grass tileset 7.
   * @param grass8 Grass tileset 8.
   * @param backgroundTile Background tile that matches the MainGameScreen background colour.
   * @return Tileset map positions for Level 2.
   */
  private TiledMap createForestTiles(
          GridPoint2 tileSize, TextureRegion grass1, TextureRegion grass2, TextureRegion grass3,
          TextureRegion grass4, TextureRegion grass5, TextureRegion grass6, TextureRegion grass7,
          TextureRegion grass8, TextureRegion backgroundTile
  ) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile grassTile1 = new TerrainTile(grass1);
    TerrainTile grassTile2 = new TerrainTile(grass2);
    TerrainTile grassTile3 = new TerrainTile(grass3);
    TerrainTile grassTile4 = new TerrainTile(grass4);
    TerrainTile grassTile5 = new TerrainTile(grass5);
    TerrainTile grassTile6 = new TerrainTile(grass6);
    TerrainTile grassTile7 = new TerrainTile(grass7);
    TerrainTile grassTile8 = new TerrainTile(grass8);
    TerrainTile backgroundTile1 = new TerrainTile(backgroundTile);

    //Multiplier to size of map on x and y coordinates
    int xScale = 1;
    int yScale = 1;
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE_FOREST.x * xScale,
            MAP_SIZE_FOREST.y * yScale, tileSize.x, tileSize.y);
    fillTiles(layer, MAP_SIZE_FOREST, xScale, yScale, grassTile1);

    // Randomised the grass tiles location
    fillTilesAtRandom(layer, MAP_SIZE_FOREST, grassTile2, GRASS_TILE_COUNT);
    fillTilesAtRandom(layer, MAP_SIZE_FOREST, grassTile3, GRASS_TILE_COUNT);
    fillTilesAtRandom(layer, MAP_SIZE_FOREST, grassTile4, GRASS_TILE_COUNT);
    fillTilesAtRandom(layer, MAP_SIZE_FOREST, grassTile5, GRASS_TILE_COUNT);
    fillTilesAtRandom(layer, MAP_SIZE_FOREST, grassTile6, GRASS_TILE_COUNT);
    fillTilesAtRandom(layer, MAP_SIZE_FOREST, grassTile7, GRASS_TILE_COUNT);
    fillTilesAtRandom(layer, MAP_SIZE_FOREST, grassTile8, GRASS_TILE_COUNT);

    //Fill background tiles
    GridPoint2 start = new GridPoint2(0,0);
    GridPoint2 end = new GridPoint2(18,9);
    setTilesInRegion(layer, backgroundTile1, start, end);

    GridPoint2 start2 = new GridPoint2(47,0);
    GridPoint2 end2 = new GridPoint2(64,9);
    setTilesInRegion(layer, backgroundTile1, start2, end2);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  // TODO: Replace placeholder tiles with actual tiles
  private TiledMap createForest2Tiles(
          GridPoint2 tileSize, TextureRegion grass1, TextureRegion backgroundTile
  ) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile grassTile1 = new TerrainTile(grass1);
    TerrainTile backgroundTile1 = new TerrainTile(backgroundTile);

    //Multiplier to size of map on x and y coordinates
    int xScale = 1;
    int yScale = 1;
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE_FOREST2.x * xScale,
            MAP_SIZE_FOREST2.y * yScale, tileSize.x, tileSize.y);
    fillTiles(layer, MAP_SIZE_FOREST2, xScale, yScale, grassTile1);


    //Fill background tiles
    // Top part
    GridPoint2 start = new GridPoint2(0,0);
    GridPoint2 end = new GridPoint2(50,50);
    //setTilesInRegion(layer, backgroundTile1, start, end);

    // Bottom part
    GridPoint2 start2 = new GridPoint2(0,0);
    GridPoint2 end2 = new GridPoint2(50,50);
    //setTilesInRegion(layer, backgroundTile1, start2, end2);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  /**
   * Renders the Safehouse tilesets at their appropriate position of the grid layout map.
   * @param tileSize Scale of the tileset.
   * @param ground Safehouse ground tileset.
   * @return Tileset map positions for Safehouse.
   */
  private TiledMap createSafehouseTiles(
          GridPoint2 tileSize,
          TextureRegion ground
  ) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile groundTile = new TerrainTile(ground);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE_SAFEHOUSE.x, MAP_SIZE_SAFEHOUSE.y, tileSize.x, tileSize.y);

    // Create base ground
//    fillTiles(layer, MAP_SIZE_SAFEHOUSE, groundTile);
    Cell cell = new Cell();
    cell.setTile(groundTile);
    layer.setCell(0, 0, cell);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  /**
   * Renders the tiles at a random position on top of the level layer.
   * @param layer Tilemap layer of the level.
   * @param mapSize Grid map size (x, y).
   * @param tile Tileset to be used to fill on top of the level map.
   * @param amount Number of tileset used to render on top of the level map.
   */
  private static void fillTilesAtRandom(
      TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  /**
   * Given a start point and an end point, randomly place a given tile somewhere within that region
   * a specified number of times
   * @param start Start point of region to place tiles in
   * @param end End point of region to place tiles in
   */
  private static void fillTilesAtRandomInRegion(
      TiledMapTileLayer layer, TerrainTile tile, GridPoint2 start, GridPoint2 end, int amount) {

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(start, end);
      //Sanity check to make sure the random tile isn't out of bounds
      if (tilePos.x > end.x || tilePos.y > end.y || tilePos.x < 0 || tilePos.y < 0) {
        continue;
      }

      Cell cell = layer.getCell(tilePos.x, tilePos.y);

      try {
        cell.setTile(tile);
      } catch (NullPointerException e) {
        String errorMessage = e + " caught. tilePos coordinates are: " + tilePos;
        System.out.println(errorMessage);
        errorMessage = "Starting coordinates are: " + start + ", ending coordinates are: " + end;
        System.out.println(errorMessage);
      }
    }
  }

  /**
   * Renders the tiles at a particular position on top of the level layer.
   * @param layer Tilemap layer of the level.
   * @param mapSize Grid map size (x, y).
   * @param tile Tileset to be used to fill on top of the level map.
   */
  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  /**
   * A version of fillTiles that allows for filling of maps that are rectangular
   * @param xScale What multiplier to apply to the map's x-axis
   * @param yScale What multiplier to apply to the map's y-axis
   */
  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, int xScale, int yScale, TerrainTile tile) {
    for (int x = 0; x < mapSize.x * xScale; x++) {
      for (int y = 0; y < mapSize.y * yScale; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  /**
   * Given the size of a map and coordinates expressed in percentages of the total map size,
   * calculate and returns the exact coordinates
   * (e.g. take a map with size x = 200, y = 100, and xPos = 80% of the map's x size, yPos = 100% of the map's y size
   * returns 160, 100)
   * @param xSize Size of the map's x-axis
   * @param ySize Size of the map's y-axis
   * @param xPos Position of x-axis requested as a percentage of the map's x size
   * @param yPos Position of y-axis requested as a percentage of the map's y size
   * @return A GridPoint containing the exact coordinates requested
   */
  private static GridPoint2 calculatePosition(int xSize, int ySize, double xPos, double yPos) {
    return new GridPoint2((int)(xSize * xPos), (int)(ySize * yPos));
  }
    //MAP_SIZE.x * xScale, MAP_SIZE.y * yScale, 1, 1

  /**
   * Given a start position and an end position of a region, fill all tiles in that region with the selected tile
   * @param start Start of region to fill
   * @param end End of region to fill
   */
  private static void setTilesInRegion(
      TiledMapTileLayer layer, TerrainTile tile, GridPoint2 start, GridPoint2 end) {
    for (int x = start.x; x < end.x; x++) {
      for (int y = start.y; y < end.y; y++) {
        Cell cell = layer.getCell(x, y);
        cell.setTile(tile);
      }
    }
  }

  /**
   * Given a start position and an end position of a line of tiles, fill tiles in that line at a specified interval
   * Currently, only the x coordinate is handled
   * @param start Start of region to fill
   * @param end End of region to fill
   * @param onInterval Number of tiles to fill before stopping
   * @param offInterval Number of tiles to skip before the function resumes filling tiles
   */
  private static void setTilesInLineAtIntervals(
      TiledMapTileLayer layer, TerrainTile tile, GridPoint2 start, GridPoint2 end, int onInterval, int offInterval) {
    for (int x = start.x; x < end.x; x++) {
      if (x % (onInterval + offInterval) >= onInterval) {
        continue;
      }
      for (int y = start.y; y < end.y; y++) {
        Cell cell = layer.getCell(x, y);
        cell.setTile(tile);
      }
    }
  }

  /**
   * This enum should contain the different terrains in your game, e.g. forest, cave, home, all with
   * the same oerientation. But for demonstration purposes, the base code has the same level in 3
   * different orientations.
   */
  public enum TerrainType {
    FOREST_DEMO,
    SAFEHOUSE,
    CITY,
    FOREST,
    BOSS,
    FOREST2
  }
}
