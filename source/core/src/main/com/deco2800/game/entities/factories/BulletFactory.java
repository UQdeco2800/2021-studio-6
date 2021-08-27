package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.BulletCollider;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Factory to create 'bullets' from a source entity to a dest entity
 * The bullet currently travels in a straight line from source towards (and past) target
 */
public class BulletFactory {

    /** Creates a bullet entity firing from a source entity through a  target entity
     * @param target the target entity to be fired at
     *  @param gameArea need to spawn the entity in (could instead implement a listener
     */
    public static Entity createBullet(Entity source, Entity target, GameArea gameArea) {


        float x1 = source.getPosition().x;
        float y1 = source.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 newTarget = new Vector2(x2 - x1, y2 - y1);
        newTarget = newTarget.scl(100);
        newTarget = newTarget.add(source.getPosition());

        float rotation = (MathUtils.radiansToDegrees * MathUtils.atan2(newTarget.y - y1, newTarget.x - x1));
//        float rotation = 0;
        Entity bullet = new Entity()
                .addComponent(new TextureRenderComponent("images/blood_ball.png", rotation))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(3, 3)))
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new CombatStatsComponent(1, 2))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f))
                .addComponent(new BulletCollider(target, gameArea));
        bullet.setPosition(source.getPosition().x, source.getPosition().y);


        bullet.getComponent(PhysicsMovementComponent.class).setTarget(newTarget);
        bullet.getComponent(PhysicsMovementComponent.class).setMoving(true);
        bullet.getComponent(ColliderComponent.class).setSensor(true);
        return bullet;
    }


}
