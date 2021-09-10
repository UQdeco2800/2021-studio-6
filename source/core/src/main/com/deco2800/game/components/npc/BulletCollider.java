package com.deco2800.game.components.npc;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;


/**
 * BulletCollider component detects collision between entity and target
 * Entity needs to have a HitboxComponent, and the target needs to be in the right layer
 */

public class BulletCollider extends Component {
    private Entity target;
    private HitboxComponent hitboxComponent;
    private GameArea gameArea;
    private short targetLayer;

    /**
     * Creates a collider between self entity and target entity
     * @param target the target to collide with
     * @param gameArea reference to the game area (used to spawn entity)
     */
    public BulletCollider(Entity target, GameArea gameArea, short targetLayer) {
        this.target = target;
        this.gameArea = gameArea;
        this.targetLayer = targetLayer;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collisionStart", this::collide);
        this.hitboxComponent = entity.getComponent(HitboxComponent.class);

    }

    /**
     * despawns the entity on a collision between the entity and the target
     * @param me the entity fixture
     * @param other the target entites fixture
     */
    private void collide(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        if (target == this.target) {
            gameArea.despawnEntity(entity);
        }

    }
}
