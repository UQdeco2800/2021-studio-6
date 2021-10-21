package com.deco2800.game.entities.factories;

import static java.lang.Float.NaN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class ObstacleFactoryTest {
    @Mock ResourceService resources;
    @Mock Texture texture;

    @BeforeEach
    void before() {
        ServiceLocator.registerResourceService(resources);
        ServiceLocator.registerPhysicsService(new PhysicsService()); // FIGURE OUT HOW TO MOCK
    }

    //    PhysicsUtils.setScaledCollider(tree, 0.4f, 1f); CAN I TEST THIS??


    @Test
    void shouldCreateBigTree() {
        when(resources.getAsset("images/level_2/level2_tree_1-1.png", Texture.class)).thenReturn(texture);
        Entity tree = ObstacleFactory.createBigTree();
        // Check it has the correct components
        assertNotNull(tree.getComponent(TextureRenderComponent.class));
        assertNotNull(tree.getComponent(PhysicsComponent.class));
        assertNotNull(tree.getComponent(ColliderComponent.class));
        // Check component settings
        assertEquals(tree.getComponent(ColliderComponent.class).getLayer(), PhysicsLayer.OBSTACLE);
        verify(resources).getAsset("images/level_2/level2_tree_1-1.png", Texture.class);
        assertEquals(new Vector2(NaN, 2.5f), tree.getScale());
        assertEquals(BodyDef.BodyType.StaticBody, tree.getComponent(PhysicsComponent.class).getBody().getType());
    }

    @Test
    void shouldCreateVerticalBarrier() {
        when(resources.getAsset("images/level_1/road_barrier.png", Texture.class)).thenReturn(texture);
        Entity vbar = ObstacleFactory.createVerticalBarrier();
        // Check it has the correct components
        assertNotNull(vbar.getComponent(TextureRenderComponent.class));
        assertNotNull(vbar.getComponent(PhysicsComponent.class));
        assertNotNull(vbar.getComponent(ColliderComponent.class));
        // Check component settings
        assertEquals(vbar.getComponent(ColliderComponent.class).getLayer(), PhysicsLayer.OBSTACLE);
        verify(resources).getAsset("images/level_1/road_barrier.png", Texture.class);
        assertEquals(new Vector2(NaN, 2f), vbar.getScale());
        assertEquals(BodyDef.BodyType.StaticBody, vbar.getComponent(PhysicsComponent.class).getBody().getType());
    }

    @Test
    void shouldCreateHorizontalBarrier() {
        when(resources.getAsset("images/level_1/horizontal_road_barrier.png", Texture.class)).thenReturn(texture);
        Entity hbar = ObstacleFactory.createHorizontalBarrier();
        // Check it has the correct components
        assertNotNull(hbar.getComponent(TextureRenderComponent.class));
        assertNotNull(hbar.getComponent(PhysicsComponent.class));
        assertNotNull(hbar.getComponent(ColliderComponent.class));
        // Check component settings
        assertEquals(hbar.getComponent(ColliderComponent.class).getLayer(), PhysicsLayer.OBSTACLE);
        verify(resources).getAsset("images/level_1/horizontal_road_barrier.png", Texture.class);
        assertEquals(new Vector2(2f, NaN), hbar.getScale());
        assertEquals(BodyDef.BodyType.StaticBody, hbar.getComponent(PhysicsComponent.class).getBody().getType());
    }

    @Test
    void shouldCreateDeadTree() {
        when(resources.getAsset("images/level_1/dead_tree1-day1-latest.png", Texture.class)).thenReturn(texture);
        Entity tree = ObstacleFactory.createDeadTree();
        // Check it has the correct components
        assertNotNull(tree.getComponent(TextureRenderComponent.class));
        assertNotNull(tree.getComponent(PhysicsComponent.class));
        assertNotNull(tree.getComponent(ColliderComponent.class));
        // Check component settings
        assertEquals(tree.getComponent(ColliderComponent.class).getLayer(), PhysicsLayer.OBSTACLE);
        verify(resources).getAsset("images/level_1/dead_tree1-day1-latest.png", Texture.class);
        assertEquals(new Vector2(NaN, 3.5f), tree.getScale());
        assertEquals(BodyDef.BodyType.StaticBody, tree.getComponent(PhysicsComponent.class).getBody().getType());
    }

    @Test
    void shouldCreateBuilding() {
        when(resources.getAsset("images/level_1/building2-day1-latest.png", Texture.class)).thenReturn(texture);
        Entity building = ObstacleFactory.createBuilding(1);
        // Check it has the correct components
        assertNotNull(building.getComponent(TextureRenderComponent.class));
        assertNotNull(building.getComponent(PhysicsComponent.class));
        assertNotNull(building.getComponent(ColliderComponent.class));
        // Check component settings
        assertEquals(building.getComponent(ColliderComponent.class).getLayer(), PhysicsLayer.WALL);
        verify(resources).getAsset("images/level_1/building2-day1-latest.png", Texture.class);
        assertEquals(new Vector2(NaN, 9f), building.getScale());
        assertEquals(BodyDef.BodyType.StaticBody, building.getComponent(PhysicsComponent.class).getBody().getType());
    }

    @Test
    void shouldCreateHouse2() {
        when(resources.getAsset("images/level_1/building3-day1-latest.png", Texture.class)).thenReturn(texture);
        Entity building = ObstacleFactory.createBuilding(2);
        // Check it switches the asset correctly
        verify(resources).getAsset("images/level_1/building3-day1-latest.png", Texture.class);
    }

    @Test
    void shouldCreateLamp() {
        when(resources.getAsset("images/level_1/street_lamp.png", Texture.class)).thenReturn(texture);
        Entity lamp = ObstacleFactory.createLamp(0);
        // Check it has the correct components
        assertNotNull(lamp.getComponent(TextureRenderComponent.class));
        assertNotNull(lamp.getComponent(PhysicsComponent.class));
        assertNotNull(lamp.getComponent(ColliderComponent.class));
        assertNotNull(lamp.getComponent(HitboxComponent.class));
        assertNotNull(lamp.getComponent(PointLightComponent.class));
        // Check component settings
        assertEquals(lamp.getComponent(ColliderComponent.class).getLayer(), PhysicsLayer.OBSTACLE);
        assertEquals(new Vector2(NaN, 2f), lamp.getScale());
        assertEquals(BodyDef.BodyType.StaticBody, lamp.getComponent(PhysicsComponent.class).getBody().getType());
        verify(resources).getAsset("images/level_1/street_lamp.png", Texture.class);

    }

    /*
    @Test
    void shouldCreateVinedLamp() {
        when(resources.getAsset("images/level_1/street_lamped_vined.png", Texture.class)).thenReturn(texture);
        Entity lamp = ObstacleFactory.createLamp(1);
        // Check it switches the asset correctly
        verify(resources).getAsset("images/level_1/street_lamped_vined.png", Texture.class);
    }
    */
}
