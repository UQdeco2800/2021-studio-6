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
  private static final GridPoint2 MAP_SIZE = new GridPoint2(90, 30);
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
      case CITY:
        TextureRegion cityRoadBlack =
                new TextureRegion(resourceService.getAsset("images/level_1/road_tile_black.png", Texture.class));
        TextureRegion grass =
                new TextureRegion(resourceService.getAsset("images/grass_1.png", Texture.class));
        TextureRegion cityRoadWhite =
                new TextureRegion(resourceService.getAsset("images/level_1/road_tile_white.png", Texture.class));
        TextureRegion cityRoadCracked =
                new TextureRegion(resourceService.getAsset("images/level_1/road_tile_cracked.png", Texture.class));
        TextureRegion citySidewalk =
                new TextureRegion(resourceService.getAsset("images/level_1/placeholder_sidewalk.png", Texture.class));
        TextureRegion cityCurb =
                new TextureRegion(resourceService.getAsset("images/level_1/placeholder_curb.png", Texture.class));
        return createCityTerrain(0.5f, grass, cityRoadBlack, cityRoadWhite, cityRoadCracked, citySidewalk, cityCurb);
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
      float tileWorldSize, TextureRegion grass, TextureRegion cityRoadBlack, TextureRegion cityRoadWhite, TextureRegion cityRoadCracked, TextureRegion citySidewalk, TextureRegion cityCurb
  ) {
    GridPoint2 tilePixelSize = new GridPoint2(cityRoadBlack.getRegionWidth(), cityRoadBlack.getRegionHeight());
    TiledMap tiledMap = createCityTiles(tilePixelSize, grass, cityRoadBlack,cityRoadWhite, cityRoadCracked, citySidewalk, cityCurb);
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
    fillTiles(layer, new GridPoint2(0,0), MAP_SIZE, grassTile);

    // Add some grass and rocks
    fillTilesAtRandom(layer, new GridPoint2(0, 0), MAP_SIZE, grassTuftTile, TUFT_TILE_COUNT);
    fillTilesAtRandom(layer, new GridPoint2(0, 0), MAP_SIZE, rockTile, ROCK_TILE_COUNT);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private TiledMap createCityTiles(
      GridPoint2 tileSize, TextureRegion grass, TextureRegion cityRoadBlack, TextureRegion cityRoadWhite,
      TextureRegion cityRoadCracked, TextureRegion citySidewalk, TextureRegion cityCurb) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile roadTileBlack = new TerrainTile(cityRoadBlack);
    TerrainTile roadTileWhite = new TerrainTile(cityRoadWhite);
    TerrainTile roadCracked = new TerrainTile(cityRoadCracked);
    TerrainTile sidewalkTile = new TerrainTile(citySidewalk);
    TerrainTile curbTile = new TerrainTile(cityCurb);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);
    System.out.println("Hello");
    //Copied from above, change as needed
    fillTiles(layer, new GridPoint2(0,0), MAP_SIZE, grassTile);
    fillTiles(layer, new GridPoint2(0, 11), new GridPoint2(MAP_SIZE.x, 19), roadTileBlack);
    fillTilesAtRandom(layer, new GridPoint2(0, 12), new GridPoint2(MAP_SIZE.x - 1, 18), roadCracked, 100);

    for (int x = 0; x < MAP_SIZE.x; x++) {
      if(x % 10 < 5) {
        Cell cell = new Cell();
        cell.setTile(roadTileWhite);
        layer.setCell(x, 15, cell);
      }
    }

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
    fillTiles(layer, new GridPoint2(0,0), MAP_SIZE_SAFEHOUSE, groundTile);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private static void fillTilesAtRandom(
      TiledMapTileLayer layer, GridPoint2 minCoord, GridPoint2 maxCoord, TerrainTile tile, int amount) {

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(minCoord, maxCoord);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 minCoord, GridPoint2 maxCoord, TerrainTile tile) {
    for (int x = minCoord.x; x < maxCoord.x; x++) {
      for (int y = minCoord.y; y <= maxCoord.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
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
