package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.ItemComponent;
import com.deco2800.game.components.npc.FriendlyNPCTriggerComponent;
import com.deco2800.game.components.npc.FriendlyNPCAnimationController;
import com.deco2800.game.components.npc.SpeechIconComponent;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.items.Items;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class FriendlyNPCFactory {

    private FriendlyNPCFactory(){
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Create a friendly NPC entity.
     * @param story the related story scene that contains the dialogue for the npc
     * @param atlasFileName the atlas file that contains the animations for the npc
     * @param wandering whether the npc should be wandering or not
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
        if (wandering) {
            animator.addAnimation("left-run", 0.1f, Animation.PlayMode.LOOP);
            animator.addAnimation("right-run", 0.1f, Animation.PlayMode.LOOP);
            animator.addAnimation("back-run", 0.1f, Animation.PlayMode.LOOP);
            animator.addAnimation("front-run", 0.1f, Animation.PlayMode.LOOP);
        }

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
                .addComponent(new SpeechIconComponent(0.6f))
                .addComponent(new FriendlyNPCAnimationController())
                .addComponent(aiComponent);

        // set the npc hitbox to be larger than normal
        npc.getComponent(HitboxComponent.class).setAsBox(new Vector2(2, 2));
        npc.setScale(new Vector2(1.1f,1.1f));

        return npc;
    }


    /**
     * Used to create shop keeper NPC in safehouse
     *
     * @return entity shopkeeper with all necessary components to trigger
     * popup box shop
     */
    public static Entity createShopkeeperNPC() {
        Entity shopkeeperNPC = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setSensor(true).setLayer(PhysicsLayer.ITEM))
            .addComponent(new ItemComponent(Items.SHOP, 1))
            .addComponent(new TextureRenderComponent("images/safehouse/shopkeeper/shopkeeperSprite.png"));
        shopkeeperNPC.getComponent(ColliderComponent.class).setAsBox(new Vector2(3, 3));

        shopkeeperNPC.setScale(new Vector2(1.5f, 1.5f));
        return shopkeeperNPC;
    }
}