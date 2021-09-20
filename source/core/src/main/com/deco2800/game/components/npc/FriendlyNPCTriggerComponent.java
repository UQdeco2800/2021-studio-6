package com.deco2800.game.components.npc;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.story.StoryManager;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;

public class FriendlyNPCTriggerComponent extends InputComponent {

    private boolean isInNPCRange = false;
    private StoryNames name;
    private static final int INTERACT_KEY = Input.Keys.SPACE;

    public FriendlyNPCTriggerComponent(StoryNames name) {
        super(6);
        this.name = name;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collisionStart", this::collisionStart);
        entity.getEvents().addListener("collisionEnd", () -> isInNPCRange = false);
    }


    @Override
    public boolean keyDown(int keycode) {
        if (isInNPCRange && keycode == INTERACT_KEY) {
            StoryManager.getInstance().loadCutScene(name);
            StoryManager.getInstance().displayStory();
            return true;
        }
        return false;
    }

    private void collisionStart(Fixture me, Fixture other) {
        if (PhysicsLayer.contains(PhysicsLayer.PLAYER, other.getFilterData().categoryBits)) {
            isInNPCRange = true;
        }
    }
}
