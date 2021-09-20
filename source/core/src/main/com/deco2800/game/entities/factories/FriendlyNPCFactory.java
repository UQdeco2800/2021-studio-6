package com.deco2800.game.entities.factories;

import com.deco2800.game.components.npc.FriendlyNPCTriggerComponent;
import com.deco2800.game.components.story.StoryNames;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

public class FriendlyNPCFactory {

    private FriendlyNPCFactory(){
        throw new IllegalStateException("Utility Class");
    }

    //TODO: Replace big rock with actual Friendly NPCs
    public static Entity createNewFriendlyNPC(StoryNames story) {
        Entity npc = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.FRIENDLY_NPC))
                .addComponent(new FriendlyNPCTriggerComponent(story))
                .addComponent(new TextureRenderComponent("images/playeritems/pickupammo.png"));
        npc.setScale(2f, 2f);
        return npc;
    }
}
