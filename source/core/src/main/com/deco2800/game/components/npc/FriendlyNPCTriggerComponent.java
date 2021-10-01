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
    private static final int INTERACT_KEY = Input.Keys.SPACE;

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
    }


    /**
     * Called when a key is pressed
     * @param keycode key
     * @return if the input was processed in this instance
     */
    @Override
    public boolean keyDown(int keycode) {
        if (isInNPCRange && keycode == INTERACT_KEY) {
            FriendlyNPCAnimationController animator = entity.getComponent(FriendlyNPCAnimationController.class);
            entity.getComponent(PhysicsMovementComponent.class).setTarget(entity.getPosition());
            animator.updateAnimationDirection(ServiceLocator.getGameArea().player.getPosition(), STATIONARY);

            StoryManager.getInstance().loadCutScene(name);
            StoryManager.getInstance().displayStory();

            return true;
        }
        return false;
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
