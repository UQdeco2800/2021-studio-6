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

    /**
     * Create a friendly NPC entity.
     * @return entity
     */
    public static Entity createNewFriendlyNPC(StoryNames story, String atlasFileName, boolean wandering) {

        // create an animator and add each movement directions animation
        AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(atlasFileName, TextureAtlas.class));
        animator.addAnimation("left", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("right", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("back", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("front", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("left-run", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("right-run", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("back-run", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("front-run", 0.1f, Animation.PlayMode.LOOP);

        // add the wandering task AI component
        AITaskComponent aiComponent = new AITaskComponent();
        if (wandering) {
            aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));
        }

        // create the friendly npc entity
        Entity npc = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.FRIENDLY_NPC))
                .addComponent(new DisposingComponent())
                .addComponent(new FriendlyNPCTriggerComponent(story))
                .addComponent(animator)
                .addComponent(new FriendlyNPCAnimationController())
                .addComponent(aiComponent);

        // set the npc hitbox to be larger than normal
        npc.getComponent(HitboxComponent.class).setAsBox(new Vector2(2, 2));

        return npc;
    }
}