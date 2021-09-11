package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An entity with this assigned component will cause
 * a specified effect / action on targeted entities (ie. Player).
 *
 * <p>Requires, HitboxComponent on this entity.</p>
 */
public class EntityEffectsComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(EntityEffectsComponent.class); // Logging instance for class.

    private short targetLayer;                  // only detect collisions on specific PhysicsLayer
    private short action;                       // action / effect to apply on target.
    private HitboxComponent hitboxComponent;    // Collision sensor instance.

    /**
     * Effects available to be applied on entities.
     */
    public static class Effect {
        public static final short NONE = 0;
        public static final short MOVEMENT = (1 << 1);
        public static final short HEALTH = (1 << 2);

        public static boolean contains(short filterBits, short layer) {
            return (filterBits & layer) != 0;
        }

        private Effect() {
            throw new IllegalStateException("Instantiating static util class");
        }
    }

    /**
     * Create a EntityEffects component which performs
     * a specified effect / action on when collision with a targeted entity.
     * @param targetLayer The PhysicsLayer of the target's collider.
     */
    public EntityEffectsComponent(short targetLayer, short action) {
        this.targetLayer = targetLayer;
        this.action = action;
    }

    /**
     * Component initialization.
     */
    public void create() {
        // Register collision event listeners.
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);

        // Save entity hitbox.
        this.hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    /**
     * Handle the collisionStart event.
     * @param me The entity with this assigned component.
     * @param other The colliding entity.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (this.hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        //TODO: Extract target entity for info...
        //Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        String target = "Not Implemented Yet";

        // if me == BUSH, apply HEALTH effect on player.
        if(this.action == Effect.HEALTH) {
            //TODO: Write implementation of effects onto desired entity.
//            logger.info("Apply HEALTH effect onto entity: " + target.toString());
        }

        // if me == COBWEB, apply MOVEMENT effect on player.
        if(this.action == Effect.MOVEMENT) {
//            logger.info("Apply MOVEMENT effect onto entity: " + target.toString());
        }
    }

}
