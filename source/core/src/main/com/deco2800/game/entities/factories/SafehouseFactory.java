package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.TouchTeleportComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

// Test commit

public class SafehouseFactory {
    /**
     * Creates a Safehouse entity
     * @return Safehouse entity of given width and height
     */
    public static Entity createSafehouse() {
        Entity safehouse = new Entity()
                .addComponent(new TextureRenderComponent("images/safehouse/exterior-day1-v1.0.png"))
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.SAFEHOUSE))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SAFEHOUSE))
                .addComponent(new TouchTeleportComponent(PhysicsLayer.PLAYER));
        safehouse.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        safehouse.getComponent(TextureRenderComponent.class).scaleEntity();
        safehouse.scaleHeight(2.5f);
        PhysicsUtils.setScaledCollider(safehouse, 0.5f, 0.2f);
        return safehouse;
    }
}
