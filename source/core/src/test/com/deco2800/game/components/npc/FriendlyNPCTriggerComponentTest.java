package com.deco2800.game.components.npc;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.story.StoryManager;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class FriendlyNPCTriggerComponentTest {

    @Mock InputService inputService;
    @Mock SpeechIconComponent speechIconComponent;
    @Mock
    NPCAnimationController animationController;
    @Mock EntityService entityService;
    GameArea gameArea;

    @Spy Entity manager;

    Entity npc;
    Entity player;

    Fixture npcFixture;
    Fixture playerFixture;

    @BeforeEach
    void setUp() {
        gameArea = new GameArea() {
            @Override
            public void create() {

            }
        };

        ServiceLocator.registerInputService(inputService);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerGameArea(gameArea);
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerTimeSource(new GameTime());

        manager = new Entity()
                .addComponent(StoryManager.getInstance());

        npc = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.FRIENDLY_NPC))
                .addComponent(new FriendlyNPCTriggerComponent(StoryNames.TEST))
                .addComponent(animationController)
                .addComponent(speechIconComponent);

        player = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));

        npc.create();
        player.create();
        npcFixture = npc.getComponent(HitboxComponent.class).getFixture();
        playerFixture = player.getComponent(HitboxComponent.class).getFixture();

        gameArea.player = player;
    }



    @Test
    void shouldDisplayStory(){
        npc.getEvents().trigger("collisionStart", npcFixture, playerFixture);
        npc.getComponent(FriendlyNPCTriggerComponent.class).keyDown(Input.Keys.E);
        assertTrue(manager.getComponent(StoryManager.class).isDisplaying());

        //Tear Down
        StoryManager.getInstance().disposeLoadedStory();
    }

    @Test
    void shouldNotDisplayStoryWhenNotColliding1() {
        npc.getEvents().trigger("collisionStart", npcFixture, playerFixture);
        npc.getEvents().trigger("collisionEnd", npcFixture, playerFixture);
        npc.getComponent(FriendlyNPCTriggerComponent.class).keyDown(Input.Keys.E);
        assertFalse(manager.getComponent(StoryManager.class).isDisplaying());
    }

    @Test
    void shouldNotDisplayStoryWhenNotColliding2() {
        npc.getComponent(FriendlyNPCTriggerComponent.class).keyDown(Input.Keys.E);
        assertFalse(manager.getComponent(StoryManager.class).isDisplaying());
    }


}