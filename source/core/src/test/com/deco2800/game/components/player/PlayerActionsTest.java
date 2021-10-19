package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.BulletCollisionComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.events.listeners.EventListener1;
import com.deco2800.game.extensions.GameExtension;
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
              .addComponent(new InventoryComponent(150, 5, 10, 1));
        player.create();

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
        player.getComponent(PlayerRangeAttackComponent.class).fire();
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
        Vector2 speed = new Vector2(3f, 3f); // wound state 3 = (3f,3f)
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(0); // wound state 0 = (0f,0f)
        speed = new Vector2(0f, 0f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(20); // only allows max state of 3
        speed = new Vector2(3f, 3f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);

        action = new PlayerActions(-20); // only allows min state of 0
        speed = new Vector2(0f, 0f);
        assertEquals(speed.x, action.getCurrentSpeed().x);
        assertEquals(speed.y, action.getCurrentSpeed().y);
    }

    @Test
    void shouldChangeSpeedWhenWoundChanges() {
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();
        Vector2 speed = new Vector2(3f, 3f); // wound state 3 = (3f,3f)
        assertEquals(speed.x, player.getComponent(PlayerActions.class).getCurrentSpeed().x);
        assertEquals(speed.y, player.getComponent(PlayerActions.class).getCurrentSpeed().y);

        player.getEvents().trigger("updateWound", 2);
        speed = new Vector2(4f, 4f); // wound state 2 = (4f,4f)
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

        player.getEvents().trigger("walk", new Vector2(1f, 0f)); // right walking vector
        player.getEvents().trigger("dash");
        assertTrue(player.getComponent(PlayerActions.class).isDashing());
    }

    @Test
    void shouldTriggerDashOutsideDelay() {
        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3));
        player.create();

        assertTrue(player.getComponent(PlayerActions.class).canDash());
        player.getEvents().trigger("walk", new Vector2(1f, 0f)); // right walking vector
        player.getEvents().trigger("dash");
        assertFalse(player.getComponent(PlayerActions.class).canDash());

        when(time.getTime()).thenReturn(10L); // below delay time
        assertFalse(player.getComponent(PlayerActions.class).canDash());

        when(time.getTime()).thenReturn(2000L); // on delay time
        assertTrue(player.getComponent(PlayerActions.class).canDash());

        player.getEvents().trigger("dash");
        assertFalse(player.getComponent(PlayerActions.class).canDash());

        when(time.getTime()).thenReturn(10000L); // far outside delay time
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
        assertEquals(2000, player.getComponent(PlayerActions.class).getDelayTimeRemaining()); // initial delay time set

        when(time.getTime()).thenReturn(1000L);
        assertEquals(1000, player.getComponent(PlayerActions.class).getDelayTimeRemaining()); // in delay time

        when(time.getTime()).thenReturn(2000L);
        assertEquals(0, player.getComponent(PlayerActions.class).getDelayTimeRemaining()); // exact delay time

        when(time.getTime()).thenReturn(3000L);
        assertEquals(0, player.getComponent(PlayerActions.class).getDelayTimeRemaining()); // long past delay time
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
        verify(listener).handle(100L); // dash length
    }

    @Test
    void dashShouldStopAllowAttacks() {
        PhysicsComponent physicsComponent = mock(PhysicsComponent.class);
        Body body = mock(Body.class);
        when(physicsComponent.getBody()).thenReturn(body);
        when(body.getLinearVelocity()).thenReturn(new Vector2(1f, 1f)); // random return to allow function

        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(physicsComponent);
        player.create();
        EventListener0 listener = mock(EventListener0.class);
        EventListener0 listener_2 = mock(EventListener0.class);
        player.getEvents().addListener("disableDashAttack", listener);
        player.getEvents().addListener("enableDashAttack", listener_2);
        player.getEvents().trigger("walk", new Vector2(1f, 0f));
        player.getEvents().trigger("dash");
        verify(listener).handle();

        when(time.getTime()).thenReturn(99L); // just before dash is over
        player.update();
        verifyNoInteractions(listener_2);

        when(time.getTime()).thenReturn(100L); // after dash is over
        player.update();
        verify(listener_2).handle();
    }

    @Test
    void shouldTriggerAndSetTimeForLongDash() {
        PhysicsComponent physicsComponent = mock(PhysicsComponent.class);
        Body body = mock(Body.class);
        when(physicsComponent.getBody()).thenReturn(body);
        when(body.getLinearVelocity()).thenReturn(new Vector2(1f, 1f)); // random return to allow function

        when(time.getTime()).thenReturn(0L);
        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(physicsComponent);
        player.create();
        player.getEvents().trigger("longDash", 1000L);
        assertTrue(player.getComponent(PlayerActions.class).isDashing());

        when(time.getTime()).thenReturn(999L); // before long dash end
        player.update();
        assertTrue(player.getComponent(PlayerActions.class).isDashing());

        when(time.getTime()).thenReturn(1000L); // at end of long dash
        player.update();
        assertFalse(player.getComponent(PlayerActions.class).isDashing());
    }

    @Test
    public void shouldSetDashVelocityBasedOnDirection() {
        // Setting up body to allow physics calls (most are filler calls as they are sent in getLinearVelocity)
        PhysicsComponent physicsComponent = mock(PhysicsComponent.class);
        Body body = mock(Body.class);
        Vector2 linearVelocity = new Vector2(1f, 1f);
        when(physicsComponent.getBody()).thenReturn(body);
        when(body.getLinearVelocity()).thenReturn(linearVelocity);
        when(body.getWorldCenter()).thenReturn(new Vector2(0f, 0f));
        when(body.getMass()).thenReturn(2f);

        when(time.getTime()).thenReturn(0L);
        Vector2 directionVelocity = new Vector2(20f, 0f); // dash velocity set when going right
        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(physicsComponent);
        player.create();
        player.getEvents().trigger("walk", new Vector2(1f, 0f)); // set player moving right
        player.getEvents().trigger("dash");
        player.update();
        verify(body).applyLinearImpulse(directionVelocity.cpy().sub(linearVelocity).scl(2f), new Vector2(0f, 0f), true);

        when(time.getTime()).thenReturn(2000L); // after dash end (allow re-trigger)
        directionVelocity = new Vector2(0f, 20f); // dash velocity set when going up
        player.getEvents().trigger("walk", new Vector2(0f, 1f));// set player moving up
        player.getEvents().trigger("dash");
        player.update();
        verify(body).applyLinearImpulse(directionVelocity.cpy().sub(linearVelocity).scl(2f), new Vector2(0f, 0f), true);

        when(time.getTime()).thenReturn(4000L); // after dash end (allow re-trigger)
        player.getEvents().trigger("walkStop");
        player.update();
        directionVelocity = new Vector2(10f, 10f); // dash velocity set when going diagonal
        player.getEvents().trigger("walk", new Vector2(1f, 1f)); // set player moving diagonal
        player.getEvents().trigger("dash");
        player.update();
        verify(body).applyLinearImpulse(directionVelocity.cpy().sub(linearVelocity).scl(2f), new Vector2(0f, 0f), true);
    }

    @Test
    public void shouldAttack() {
        // many mock returns to ensure sound, etc is triggered correctly
        PlayerMeleeAttackComponent melee = mock(PlayerMeleeAttackComponent.class);
        ResourceService resourceService = mock(ResourceService.class);

        ServiceLocator.registerResourceService(resourceService);

        Entity player = new Entity().addComponent(new PlayerActions(3)).addComponent(melee);
        player.create();
        player.getEvents().trigger("attack");
        
        verify(melee).meleeAttackClicked(true);
    }
}