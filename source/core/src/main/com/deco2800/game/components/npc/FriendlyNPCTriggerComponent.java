package com.deco2800.game.components.npc;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.story.StoryManager;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.LoggerFactory;

/**
 * Input component that triggers a story when SPACE is pressed and is in range of the entity
 */
public class FriendlyNPCTriggerComponent extends InputComponent {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(FriendlyNPCTriggerComponent.class);
    private static final int STATIONARY = 0;
    private boolean isInNPCRange = false;
    private StoryNames name;
    private boolean storyAlreadyPlayed = false;
    private static final int INTERACT_KEY = Input.Keys.E;

    public FriendlyNPCTriggerComponent(StoryNames name) {
        super(6);
        this.name = name;
        logger.info("Friendly NPC {} created", name);
    }

    /**
     * Sets up component and collision listeners
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collisionStart", this::collisionStart);
        entity.getEvents().addListener("collisionEnd", this::collisionEnd);
        StoryManager.getInstance().getEntity().getEvents().addListener("story-finished:" + name, this::storyEnded);
    }


    /**
     * Called when a key is pressed
     * @param keycode key
     * @return if the input was processed in this instance
     */
    @Override
    public boolean keyDown(int keycode) {
        if (isInNPCRange && keycode == INTERACT_KEY && !StoryManager.getInstance().isDisplaying()) {
            NPCAnimationController animator = entity.getComponent(NPCAnimationController.class);

            entity.getComponent(PhysicsMovementComponent.class).setMoving(false);;
            animator.updateAnimationDirection(ServiceLocator.getGameArea().player.getPosition(), STATIONARY);

            StoryManager.getInstance().loadCutScene(name);
            StoryManager.getInstance().displayStory();

            return true;
        }
        return false;
    }

    /**
     * Removes the speech bubble icon when the story ends
     */
    private void storyEnded() {
        if (!storyAlreadyPlayed) {
            logger.debug("NPC {} - story marked as read", name);
            entity.getComponent(SpeechIconComponent.class).displayOff();
            storyAlreadyPlayed = true;
        }
    }

    /**
     * Sets isInNPCRange to true if player is in range
     * @param me the entity
     * @param other the other entity being collided with me
     */
    private void collisionStart(Fixture me, Fixture other) {
        if (PhysicsLayer.contains(PhysicsLayer.PLAYER, other.getFilterData().categoryBits)) {
            isInNPCRange = true;
        }
    }

    /**
     * Sets isInNPCRange to false if player is in range
     * @param me the entity
     * @param other the other entity being collided with me
     */
    private void collisionEnd(Fixture me, Fixture other){
        if (other.getFilterData().categoryBits == PhysicsLayer.PLAYER) {
            isInNPCRange = false;
        }
    }
}
