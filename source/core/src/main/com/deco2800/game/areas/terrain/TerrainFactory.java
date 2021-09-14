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
  private static final GridPoint2 MAP_SIZE_SAFEHOUSE = new GridPoint2(15, 15);
  private static final int TUFT_TILE_COUNT = 30;
  private static final int ROCK_TILE_COUNT = 30;

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
   * @param terrainType Terrain to create
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TerrainType terrainType) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    switch (terrainType) {
      case FOREST_DEMO:
        TextureRegion orthoGrass =
            new TextureRegion(resourceService.getAsset("images/grass_1.png", Texture.class));
        TextureRegion orthoTuft =
            new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
        TextureRegion orthoRocks =
            new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));
        return createForestDemoTerrain(0.5f, orthoGrass, orthoTuft, orthoRocks);
      case CITY:
        TextureRegion cityRoad =
            new TextureRegion(resourceService.getAsset("images/level_1/placeholder_road.png", Texture.class));
        TextureRegion citySidewalk =
            new TextureRegion(resourceService.getAsset("images/level_1/placeholder_sidewalk.png", Texture.class));
        TextureRegion cityCurb =
            new TextureRegion(resourceService.getAsset("images/level_1/placeholder_curb.png", Texture.class));
        return createCityTerrain(0.5f, cityRoad, citySidewalk, cityCurb);
      case FOREST_DEMO_ISO:
        TextureRegion isoGrass =
            new TextureRegion(resourceService.getAsset("images/iso_grass_1.png", Texture.class));
        TextureRegion isoTuft =
            new TextureRegion(resourceService.getAsset("images/iso_grass_2.png", Texture.class));
        TextureRegion isoRocks =
            new TextureRegion(resourceService.getAsset("images/iso_grass_3.png", Texture.class));
        return createForestDemoTerrain(1f, isoGrass, isoTuft, isoRocks);
      case FOREST_DEMO_HEX:
        TextureRegion hexGrass =
            new TextureRegion(resourceService.getAsset("images/hex_grass_1.png", Texture.class));
        TextureRegion hexTuft =
            new TextureRegion(resourceService.getAsset("images/hex_grass_2.png", Texture.class));
        TextureRegion hexRocks =
            new TextureRegion(resourceService.getAsset("images/hex_grass_3.png", Texture.class));
        return createForestDemoTerrain(1f, hexGrass, hexTuft, hexRocks);
      case SAFEHOUSE:
        TextureRegion orthoGround = new TextureRegion(resourceService
                        .getAsset("images/safehouse/interior-day1-tile-ground1-latest.png", Texture.class));
        return createSafehouseTerrain(1f, orthoGround);
      default:
        System.out.println("default");
        return null;
    }
  }

  private TerrainComponent createForestDemoTerrain(
          float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks
  ) {
    GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, grassTuft, rocks);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  private TerrainComponent createCityTerrain(
      float tileWorldSize, TextureRegion cityRoad, TextureRegion citySidewalk, TextureRegion cityCurb
  ) {
    GridPoint2 tilePixelSize = new GridPoint2(cityRoad.getRegionWidth(), cityRoad.getRegionHeight());
    TiledMap tiledMap = createCityTiles(tilePixelSize, cityRoad, citySidewalk, cityCurb);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  private TerrainComponent createSafehouseTerrain(
          float tileWorldSize, TextureRegion ground
  ) {
    GridPoint2 tilePixelSize = new GridPoint2(ground.getRegionWidth(), ground.getRegionHeight());
    TiledMap tiledMap = createSafehouseTiles(tilePixelSize, ground);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

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

  private TiledMap createCityTiles(
      GridPoint2 tileSize, TextureRegion cityRoad, TextureRegion citySidewalk, TextureRegion cityCurb) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile roadTile = new TerrainTile(cityRoad);
    TerrainTile sidewalkTile = new TerrainTile(citySidewalk);
    TerrainTile curbTile = new TerrainTile(cityCurb);
    int xScale = 2;
    int yScale = 1;
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x * xScale, MAP_SIZE.y * yScale, tileSize.x, tileSize.y);
    fillTiles(layer, MAP_SIZE, xScale, yScale, roadTile);

    GridPoint2 start = calculatePosition(MAP_SIZE.x * xScale, MAP_SIZE.y * yScale, 0, 0);
    GridPoint2 end = calculatePosition(MAP_SIZE.x * xScale, MAP_SIZE.y * yScale, 1, 0.2);
    setTilesInRegion(layer, sidewalkTile, start, end);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private TiledMap createSafehouseTiles(
          GridPoint2 tileSize,
          TextureRegion ground
  ) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile groundTile = new TerrainTile(ground);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE_SAFEHOUSE.x, MAP_SIZE_SAFEHOUSE.y, tileSize.x, tileSize.y);

    // Create base ground
    fillTiles(layer, MAP_SIZE_SAFEHOUSE, groundTile);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

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
   * This enum should contain the different terrains in your game, e.g. forest, cave, home, all with
   * the same oerientation. But for demonstration purposes, the base code has the same level in 3
   * different orientations.
   */
  public enum TerrainType {
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX,
    SAFEHOUSE,
    CITY
  }
}
