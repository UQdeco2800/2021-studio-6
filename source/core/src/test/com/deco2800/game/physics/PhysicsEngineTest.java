package com.deco2800.game.physics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.deco2800.game.components.BulletCollisionComponent;
import com.deco2800.game.components.player.PlayerRangeAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class PhysicsEngineTest {
  @Mock GameTime gameTime;
  @Mock World world;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(gameTime);
  }

  @Test
  void shouldNotStepWithoutEnoughTime() {
    when(gameTime.getDeltaTime()).thenReturn(0f);
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    engine.update();
    verify(world, times(0)).step(anyFloat(), anyInt(), anyInt());
  }

  @Test
  void shouldStepOnceAfterTime() {
    when(gameTime.getDeltaTime()).thenReturn(0.02f);
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);

    engine.update();
    verify(world, times(1)).step(anyFloat(), anyInt(), anyInt());
  }

  @Test
  void shouldStepMultipleAfterLongTime() {
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    when(gameTime.getDeltaTime()).thenReturn(0.06f);

    engine.update();
    verify(world, times(3)).step(anyFloat(), anyInt(), anyInt());
  }

  @Test
  void shouldCreateBody() {
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    BodyDef bodyDef = new BodyDef();
    Body body = engine.createBody(bodyDef);
    verify(world).createBody(bodyDef);

    engine.destroyBody(body);
    engine.update();
    verify(world).destroyBody(body);
  }

  @Test
  void shouldCreateJoint() {
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    JointDef jointDef = new JointDef();
    Joint joint = engine.createJoint(jointDef);
    verify(world).createJoint(jointDef);

    engine.destroyJoint(joint);
    verify(world).destroyJoint(joint);
  }

  @Test
  void shouldDisposeWorld() {
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    engine.dispose();
    verify(world).dispose();
  }

  @Test
  void disposeReuseQueueShouldBeEmpty() {
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);

    assertTrue(engine.getReuseQueue().isEmpty());
  }

  @Test
  void reuseQueueShouldReuse() {
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    EntityService entityService = new EntityService();
    PhysicsService physicsService = new PhysicsService(engine);

    ServiceLocator.registerTimeSource(gameTime);
    ServiceLocator.registerEntityService(entityService);
    ServiceLocator.registerPhysicsService(physicsService);

    assertTrue(engine.getReuseQueue().isEmpty());
    Entity testDud1 = new Entity()
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new BulletCollisionComponent())
            .addComponent(new PlayerRangeAttackComponent());
    testDud1.create();
    engine.addToReuseQueue(testDud1);

    assertFalse(engine.getReuseQueue().isEmpty());
    assertEquals(1,engine.getReuseQueue().size());

    engine.update();
    assertTrue(engine.getReuseQueue().isEmpty());
  }

}
