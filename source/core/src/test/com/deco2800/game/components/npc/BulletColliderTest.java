package com.deco2800.game.components.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.physics.box2d.World;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class BulletColliderTest {
    PhysicsEngine physicsEngine;
    World world;
    GameTime gameTime;

    @BeforeAll
    static void beforeAll() {
        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(10f);
    }
    @Test
    void collisionWithPlayer() {

        ServiceLocator.registerPhysicsService(new PhysicsService());

        //define the player
        Entity player = new Entity().addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));

        //mock the game area
        GameArea gameArea = Mockito.mock(GameArea.class);
        doNothing().when(gameArea).despawnEntity(any(Entity.class));

        //define the bullet
        Entity bullet = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new BulletCollider(player, gameArea, PhysicsLayer.PLAYER));

        //create both player and bullet
        bullet.create();
        player.create();

        //trigger collision event
        bullet.getEvents().trigger("collisionStart",
                bullet.getComponent(HitboxComponent.class).getFixture(),
                player.getComponent(HitboxComponent.class).getFixture());

        //check the despawn entity method was called
        verify(gameArea).despawnEntity(bullet);
    }


}
