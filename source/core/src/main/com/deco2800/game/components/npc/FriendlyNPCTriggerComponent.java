package com.deco2800.game.components.npc;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.story.StoryManager;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

public class FriendlyNPCTriggerComponent extends InputComponent {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(FriendlyNPCTriggerComponent.class);
    private boolean isInNPCRange = false;
    private StoryNames name;
    private static final int INTERACT_KEY = Input.Keys.SPACE;

    public FriendlyNPCTriggerComponent(StoryNames name) {
        super(6);
        this.name = name;
        logger.info("Friendly NPC {} created", name);
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collisionStart", this::collisionStart);
        entity.getEvents().addListener("collisionEnd", this::collisionEnd);
    }


    @Override
    public boolean keyDown(int keycode) {
        if (isInNPCRange && keycode == INTERACT_KEY) {
            updateAnimationDirectionToPlayer();
            StoryManager.getInstance().loadCutScene(name);
            StoryManager.getInstance().displayStory();
            return true;
        }
        return false;
    }

    private void updateAnimationDirectionToPlayer() {
        FriendlyNPCAnimationController animator = entity.getComponent(FriendlyNPCAnimationController.class);

        Vector2 myPos = entity.getPosition();
        Vector2 playerPos = ServiceLocator.getGameArea().player.getPosition();
        float xOffset = myPos.x - playerPos.x;
        float yOffset = myPos.y - playerPos.y;

        if (yOffset > 0 && yOffset > -xOffset && yOffset > xOffset) { animator.setDirection("front"); }
        if (yOffset < 0 && yOffset < -xOffset && yOffset < xOffset) { animator.setDirection("back"); }
        if (xOffset > 0 && yOffset > -xOffset && yOffset < xOffset) { animator.setDirection("left"); }
        if (xOffset < 0 && yOffset < -xOffset && yOffset > xOffset) { animator.setDirection("right"); }
    }


    private void collisionStart(Fixture me, Fixture other) {
        if (PhysicsLayer.contains(PhysicsLayer.PLAYER, other.getFilterData().categoryBits)) {
            isInNPCRange = true;
        }
    }

    private void collisionEnd(Fixture me, Fixture other){
        if (other.getFilterData().categoryBits == PhysicsLayer.PLAYER) {
            isInNPCRange = false;
        }
    }
}
