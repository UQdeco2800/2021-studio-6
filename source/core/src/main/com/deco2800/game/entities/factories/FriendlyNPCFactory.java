package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.npc.FriendlyNPCTriggerComponent;
import com.deco2800.game.components.npc.FriendlyNPCAnimationController;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class FriendlyNPCFactory {

    private FriendlyNPCFactory(){
        throw new IllegalStateException("Utility Class");
    }


    public static Entity createNewFriendlyNPC(StoryNames story, boolean wandering) {
        AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/npc_movement/npc_movement.atlas", TextureAtlas.class));
        animator.addAnimation("left", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("right", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("back", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("front", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("left-run", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("right-run", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("back-run", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("front-run", 0.1f, Animation.PlayMode.LOOP);

        AITaskComponent aiComponent = new AITaskComponent();
        if (wandering) {
            aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));
        }

        Entity npc = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.FRIENDLY_NPC))
                .addComponent(new DisposingComponent())
                .addComponent(new FriendlyNPCTriggerComponent(story))
                .addComponent(animator)
                .addComponent(new FriendlyNPCAnimationController())
                .addComponent(aiComponent);

        npc.getComponent(HitboxComponent.class).setAsBox(new Vector2(2, 2));

        return npc;
    }
}
