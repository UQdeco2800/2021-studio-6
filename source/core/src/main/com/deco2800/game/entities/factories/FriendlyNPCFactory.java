package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.components.npc.FriendlyNPCTriggerComponent;
import com.deco2800.game.components.npc.FriendlyNPCAnimationController;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class FriendlyNPCFactory {

    private FriendlyNPCFactory(){
        throw new IllegalStateException("Utility Class");
    }



    //TODO: Replace big rock with actual Friendly NPCs
    public static Entity createNewFriendlyNPC(StoryNames story) {
        AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/Player_Animations/player_movement.atlas", TextureAtlas.class));
        animator.addAnimation("left", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("right", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("back", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("front", 0.1f, Animation.PlayMode.LOOP);

        Entity npc = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.FRIENDLY_NPC))
                .addComponent(new FriendlyNPCTriggerComponent(story))
                .addComponent(animator)
                .addComponent(new FriendlyNPCAnimationController());
        //npc.setScale(2f, 2f);
        return npc;
    }
}
