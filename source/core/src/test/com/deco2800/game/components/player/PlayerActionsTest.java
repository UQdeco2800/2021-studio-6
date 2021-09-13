package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.BulletCollisionComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.events.listeners.EventListener1;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.badlogic.gdx.math.Vector2;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class PlayerActionsTest {
    @Mock GameTime time;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(time);
    }

    @Test
    void shouldReload() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity();
        player.addComponent(new PlayerActions(3))
              .addComponent(new PlayerRangeAttackComponent())
              .addComponent(new InventoryComponent(150, 5, 10));
        player.create();
//
        Array<Entity> bullets = new Array<>();
        int NUM_BULLETS = 5;
        for (int i = 0; i < NUM_BULLETS; i++) {
            Entity newBullet = new Entity()
                    .addComponent(new PhysicsMovementComponent(new Vector2(5f, 5f)))
                    .addComponent(new BulletCollisionComponent());
            newBullet.create();
            // hide bullet out of game screen
            newBullet.setPosition(-10,-10);
            bullets.add(newBullet);
        }
        player.getComponent(PlayerRangeAttackComponent.class).addBullets(bullets);
        player.getComponent(PlayerRangeAttackComponent.class).fire(Vector2.Zero.cpy());
        assertEquals(4,player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine());
        assertFalse(player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus());

        // reloading period starts now, reloading status set to true
        player.getEvents().trigger("reload");
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus());
        assertEquals(4,player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine());

        // should still be reloading
        when(time.getTime()).thenReturn(1500L);
        player.update();
        assertTrue(player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus());
        assertEquals(4,player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine());

        // finished reloading
        when(time.getTime()).thenReturn(2100L);
        player.update();
        assertFalse(player.getComponent(PlayerRangeAttackComponent.class).getReloadingStatus());
        assertEquals(5,player.getComponent(PlayerRangeAttackComponent.class).getGunMagazine());
    }

    @Test
    void shouldSetSpeed() {
        PlayerActions action = new PlayerActions(3);
        Vector2 speed = new Vector2(3f, 3f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(0);
        speed = new Vector2(0f, 0f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(20);
        speed = new Vector2(3f, 3f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(-20);
        speed = new Vector2(0f, 0f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);
    }

    @Test
    void shouldChangeSpeedWhenWoundChanges() {
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        Vector2 speed = new Vector2(3f, 3f);
        assertEquals(speed.x, player.getComponent(PlayerActions.class).getCurrentSpeed().x);
        assertEquals(speed.y, player.getComponent(PlayerActions.class).getCurrentSpeed().y);

        player.getEvents().trigger("updateWound", 2);
        speed = new Vector2(4f, 4f);
        assertEquals(speed.x, player.getComponent(PlayerActions.class).getCurrentSpeed().x);
        assertEquals(speed.y, player.getComponent(PlayerActions.class).getCurrentSpeed().y);
    }

    @Test
    void shouldTriggerDashOnlyWhenMoving() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        assertFalse(player.getComponent(PlayerActions.class).isDashing());
        player.getEvents().trigger("dash");
        assertFalse(player.getComponent(PlayerActions.class).isDashing());

        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        assertTrue(player.getComponent(PlayerActions.class).isDashing());
    }

    @Test
    void shouldTriggerDashOutsideDelay() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();

        // Regular trigger
        assertTrue(player.getComponent(PlayerActions.class).canDash());
        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        assertFalse(player.getComponent(PlayerActions.class).canDash());

        when(time.getTime()).thenReturn(10L);
        assertFalse(player.getComponent(PlayerActions.class).canDash());

        when(time.getTime()).thenReturn(2990L);
        assertTrue(player.getComponent(PlayerActions.class).canDash());

        player.getEvents().trigger("dash");
        assertFalse(player.getComponent(PlayerActions.class).canDash());

        when(time.getTime()).thenReturn(10000L);
        assertTrue(player.getComponent(PlayerActions.class).canDash());
    }

    @Test
    void shouldGetRemainingDelayTime() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        assertEquals(0, player.getComponent(PlayerActions.class).getDelayTimeRemaining());

        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        assertEquals(2000, player.getComponent(PlayerActions.class).getDelayTimeRemaining());

        when(time.getTime()).thenReturn(1000L);
        assertEquals(1000, player.getComponent(PlayerActions.class).getDelayTimeRemaining());

        when(time.getTime()).thenReturn(3000L);
        assertEquals(0, player.getComponent(PlayerActions.class).getDelayTimeRemaining());
    }

    @Test
    void dashShouldTriggerInvincible() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        EventListener1 listener = mock(EventListener1.class);
        player.getEvents().addListener("invincible", listener);
        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        verify(listener).handle(100L);
    }

    @Test
    void dashShouldStopAttacks() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        EventListener0 listener = mock(EventListener0.class);
        player.getEvents().addListener("disableDashAttack", listener);
        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        verify(listener).handle();
    }

    @Test
    void shouldTriggerAndSetTimeForLongDash() {
        PhysicsComponent physicsComponent = mock(PhysicsComponent.class);
        Body body = mock(Body.class);
        when(physicsComponent.getBody()).thenReturn(body);
        when(body.getLinearVelocity()).thenReturn(new Vector2(1f, 1f));

        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(physicsComponent);
        player.create();
        player.getEvents().trigger("longDash", 1000L);
        assertTrue(player.getComponent(PlayerActions.class).isDashing());

        when(time.getTime()).thenReturn(999L);
        player.update();
        assertTrue(player.getComponent(PlayerActions.class).isDashing());

        when(time.getTime()).thenReturn(1000L);
        player.update();
        assertFalse(player.getComponent(PlayerActions.class).isDashing());
    }

    @Test
    public void shouldSetDashVelocityBasedOnDirection() {
        // Setting up body to allow physics calls
        PhysicsComponent physicsComponent = mock(PhysicsComponent.class);
        Body body = mock(Body.class);
        Vector2 linearVelocity = new Vector2(1f, 1f);
        when(physicsComponent.getBody()).thenReturn(body);
        when(body.getLinearVelocity()).thenReturn(linearVelocity);
        when(body.getWorldCenter()).thenReturn(new Vector2(0f, 0f));
        when(body.getMass()).thenReturn(2f);

        when(time.getTime()).thenReturn(0L);
        Vector2 directionVelocity = new Vector2(20f, 0f);
        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(physicsComponent);
        player.create();
        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        player.update();
        verify(body).applyLinearImpulse(directionVelocity.cpy().sub(linearVelocity).scl(2f), new Vector2(0f, 0f), true);

        when(time.getTime()).thenReturn(2000L);
        directionVelocity = new Vector2(0f, 20f);
        player.getEvents().trigger("walk", new Vector2(0f, 1f));
        player.getEvents().trigger("dash");
        player.update();
        verify(body).applyLinearImpulse(directionVelocity.cpy().sub(linearVelocity).scl(2f), new Vector2(0f, 0f), true);

        when(time.getTime()).thenReturn(4000L);
        player.getEvents().trigger("walkStop");
        player.update();
        directionVelocity = new Vector2(10f, 10f);
        player.getEvents().trigger("walk", new Vector2(1f, 1f));
        player.getEvents().trigger("dash");
        player.update();
        verify(body).applyLinearImpulse(directionVelocity.cpy().sub(linearVelocity).scl(2f), new Vector2(0f, 0f), true);
    }

    @Test
    public void shouldAttack() {
        PlayerMeleeAttackComponent melee = mock(PlayerMeleeAttackComponent.class);
        ResourceService resourceService = mock(ResourceService.class);
        Sound sound = mock(Sound.class);
        ServiceLocator.registerResourceService(resourceService);
        when(resourceService.getAsset("sounds/Impact4.ogg", Sound.class)).thenReturn(sound);
        when(sound.play()).thenReturn(0L);

        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(melee);
        player.create();
        player.getEvents().trigger("attack");

        verify(resourceService).getAsset("sounds/Impact4.ogg", Sound.class);
        verify(melee).meleeAttackClicked(true);
    }
}