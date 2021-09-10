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
public class EnemyBulletFactory {

    /** Creates a bullet entity firing from a source entity through a  target entity
     * @param target the target entity to be fired at
     * @param gameArea need to spawn the entity in (could instead implement a listener
     * @param source the source of the bullet
     * @return the bullet entity
     */
    public static void createBullet(Entity source, Entity target, GameArea gameArea) {

        float x1 = source.getPosition().x;
        float y1 = source.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 newTarget = new Vector2(x2 - x1, y2 - y1);

        newTarget = newTarget.scl(100);
        newTarget = newTarget.add(source.getPosition());

        float rotation = (MathUtils.radiansToDegrees * MathUtils.atan2(newTarget.y - y1, newTarget.x - x1));

        Entity bullet = new Entity()
                .addComponent(new TextureRenderComponent("images/blood_ball.png", rotation))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(3, 3)))
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new CombatStatsComponent(1, 2))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f))
                .addComponent(new BulletCollider(target, gameArea));
        bullet.setScale(0.8f, 0.8f);
        //centers the bullet to the source
        bullet.setPosition(
                x1 - bullet.getScale().x / 2 + source.getScale().x / 2,
                y1  - bullet.getScale().y / 2  + source.getScale().y / 2);


        bullet.getComponent(PhysicsMovementComponent.class).setTarget(newTarget);
        bullet.getComponent(PhysicsMovementComponent.class).setMoving(true);
        bullet.getComponent(ColliderComponent.class).setSensor(true);
        gameArea.spawnEntity(bullet);
    }

    public static void createToughBullet(Entity source, Entity target, GameArea gameArea) {
        float x1 = source.getPosition().x;
        float y1 = source.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 straightTarget = new Vector2(x2 - x1, y2 - y1); //straight through player

        double upX = (Math.cos((Math.PI)/4) * straightTarget.x) - (Math.sin((Math.PI)/4) * straightTarget.y);
        double upY = (Math.sin((Math.PI)/4) * straightTarget.x) + (Math.cos((Math.PI)/4) * straightTarget.y);
        Vector2 upRotate = new Vector2( (float) upX, (float) upY);
        //upratate cos(45)* stragithTarget.x + sing

        //downratate cos(-45)* stragithTarget.x + sing
        double downX = (Math.cos(-(Math.PI)/4) * straightTarget.x) - (Math.sin(-(Math.PI)/4) * straightTarget.y);
        double downY = (Math.sin(-(Math.PI)/4) * straightTarget.x) + (Math.cos(-(Math.PI)/4) * straightTarget.y);
        Vector2 downRotate = new Vector2( (float) downX, (float) downY);


        straightTarget = straightTarget.scl(100);
        straightTarget = straightTarget.add(source.getPosition());

        downRotate = downRotate.scl(100);
        downRotate = downRotate.add(source.getPosition());

        upRotate = upRotate.scl(100);
        upRotate = upRotate.add(source.getPosition());

        float rotation = (MathUtils.radiansToDegrees * MathUtils.atan2(straightTarget.y - y1, straightTarget.x - x1));
        float rotationUp = (MathUtils.radiansToDegrees * MathUtils.atan2(upRotate.y - y1, upRotate.x - x1));
        float rotationDown = (MathUtils.radiansToDegrees * MathUtils.atan2(downRotate.y - y1, downRotate.x - x1));

        Entity bulletStraight = new Entity()
                .addComponent(new TextureRenderComponent("images/blood_ball.png", rotation))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(3, 3)))
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new CombatStatsComponent(1, 2))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f))
                .addComponent(new BulletCollider(target, gameArea));

        Entity bulletUp = new Entity()
                .addComponent(new TextureRenderComponent("images/blood_ball.png", rotationUp))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(3, 3)))
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new CombatStatsComponent(1, 2))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f))
                .addComponent(new BulletCollider(target, gameArea));

        Entity bulletDown = new Entity()
                .addComponent(new TextureRenderComponent("images/blood_ball.png", rotationDown))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(3, 3)))
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new CombatStatsComponent(1, 2))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f))
                .addComponent(new BulletCollider(target, gameArea));


        bulletStraight.setScale(0.8f, 0.8f);
        bulletUp.setScale(0.8f, 0.8f);
        bulletDown.setScale(0.8f, 0.8f);

        //centers the bullet to the source
        bulletStraight.setPosition(
                x1 - bulletStraight.getScale().x / 2 + source.getScale().x / 2,
                y1  - bulletStraight.getScale().y / 2  + source.getScale().y / 2);

        bulletUp.setPosition(
                x1 - bulletUp.getScale().x / 2 + source.getScale().x / 2,
                y1  - bulletUp.getScale().y / 2  + source.getScale().y / 2);

        bulletDown.setPosition(
                x1 - bulletDown.getScale().x / 2 + source.getScale().x / 2,
                y1  - bulletDown.getScale().y / 2  + source.getScale().y / 2);


        bulletStraight.getComponent(PhysicsMovementComponent.class).setTarget(straightTarget);
        bulletStraight.getComponent(PhysicsMovementComponent.class).setMoving(true);
        bulletStraight.getComponent(ColliderComponent.class).setSensor(true);

        bulletUp.getComponent(PhysicsMovementComponent.class).setTarget(upRotate);
        bulletUp.getComponent(PhysicsMovementComponent.class).setMoving(true);
        bulletUp.getComponent(ColliderComponent.class).setSensor(true);

        bulletDown.getComponent(PhysicsMovementComponent.class).setTarget(downRotate);
        bulletDown.getComponent(PhysicsMovementComponent.class).setMoving(true);
        bulletDown.getComponent(ColliderComponent.class).setSensor(true);

        gameArea.spawnEntity(bulletStraight);
        gameArea.spawnEntity(bulletUp);
        gameArea.spawnEntity(bulletDown);
    }

}
