package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.TouchTeleportComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Factory to create safehouse paraphernalia entities.
 *
 * <p>Each safehouse paraphernalia entity type should have a creation method that returns a corresponding entity.
 */
public class SafehouseParaphernaliaFactory {
    /**
     * Creates a door entity.
     *
     * @return entity
     */
    public static Entity createDoor() {
        Entity door =
                new Entity()
                        .addComponent(
                                new TextureRenderComponent(
                                        "images/safehouse/interior-day1-tile-door1-latest.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.SAFEHOUSEPARAPHERNALIA))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SAFEHOUSEPARAPHERNALIA))
                        .addComponent(new TouchTeleportComponent(PhysicsLayer.SAFEHOUSEPARAPHERNALIA));
        door.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        door.getComponent(TextureRenderComponent.class).scaleEntity();
        door.scaleHeight(2.5f);
        PhysicsUtils.setScaledCollider(door, 0.5f, 0.2f);
        return door;
    }
}