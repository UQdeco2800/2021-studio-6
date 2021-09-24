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

        Entity bullet = makeBullet(rotation, newTarget, target, gameArea, "images/blood_ball.png");

        //centers the bullet to the source
        bullet.setPosition(
                x1 - bullet.getScale().x / 2 + source.getScale().x / 2,
                y1  - bullet.getScale().y / 2  + source.getScale().y / 2);


        gameArea.spawnEntity(bullet);
    }

    /**
     * Creates three bullets for the tough long range enemy. One follows a straight line towards the player, another
     * is shifted up 45 degrees from the straight one and the other is shifted down 45 degrees from the straight one.
     * @param source the source of the bullet
     * @param target the target entity to be fired at
     * @param gameArea need to spawn the entity in
     */
    public static void createToughBullet(Entity source, Entity target, GameArea gameArea) {
        float x1 = source.getPosition().x;
        float y1 = source.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 straightTarget = new Vector2(x2 - x1, y2 - y1); //vector straight through player

        //Shifted up 45 degrees from the straight vector
        double upX = (Math.cos((Math.PI)/4) * straightTarget.x) - (Math.sin((Math.PI)/4) * straightTarget.y);
        double upY = (Math.sin((Math.PI)/4) * straightTarget.x) + (Math.cos((Math.PI)/4) * straightTarget.y);
        Vector2 upRotate = new Vector2( (float) upX, (float) upY);

        //Shifted down 45 degrees from the straight vector
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

        Entity bulletStraight = makeBullet(rotation, straightTarget, target, gameArea, "images/blood_ball.png");

        Entity bulletUp = makeBullet(rotationUp, upRotate, target, gameArea, "images/blood_ball.png");

        Entity bulletDown = makeBullet(rotationDown, downRotate, target, gameArea, "images/blood_ball.png");


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

        gameArea.spawnEntity(bulletStraight);
        gameArea.spawnEntity(bulletUp);
        gameArea.spawnEntity(bulletDown);
    }

    private static Entity makeBullet(float rotation, Vector2 destination, Entity target, GameArea gameArea, String imagePath) {
        Entity bullet = new Entity()
                .addComponent(new TextureRenderComponent(imagePath, rotation))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(3, 3)))
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ENEMYBULLET))
                .addComponent(new CombatStatsComponent(1, 2))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f))
                .addComponent(new BulletCollider(target, gameArea, PhysicsLayer.PLAYER));

        bullet.setScale(0.8f, 0.8f);

        bullet.getComponent(PhysicsMovementComponent.class).setTarget(destination);
        bullet.getComponent(PhysicsMovementComponent.class).setMoving(true);
        bullet.getComponent(ColliderComponent.class).setSensor(true);

        return bullet;
    }

}
