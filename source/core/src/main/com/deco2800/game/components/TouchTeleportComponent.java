package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.screens.MainGameScreen;

/**
 * When this entity touches a valid object's hitbox, teleport them to another level or area.
 *
 * <p>Requires, HitboxComponent on this entity.
 */
public class TouchTeleportComponent extends Component {
    private short targetLayer;
    private HitboxComponent hitboxComponent;

    /**
     * Create a component which teleports entities on collision.
     * @param targetLayer The physics later of the target's collider.
     */
    public TouchTeleportComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Try to teleport entity
            MainGameScreen.changeLevel();
        }


    }
}