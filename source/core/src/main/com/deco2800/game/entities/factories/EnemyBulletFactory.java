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
     * @param gameArea need to spawn the entity in
     * @param source the source of the bullet
     */
    public static void createBullet(Entity source, Entity target, GameArea gameArea) {

        float x1 = source.getPosition().x;
        float y1 = source.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 newTarget = new Vector2(x2 - x1, y2 - y1);

        Entity bullet = makeBullet(rotateVector(newTarget, source, x1, y1), newTarget, target, gameArea,
                source, x1, y1, "images/Enemy_Assets/LongRangeEnemy/blood_ball.png");

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

        Entity bulletStraight = makeBullet(rotateVector(straightTarget, source, x1, y1), straightTarget, target, gameArea,
                source, x1, y1, "images/Enemy_Assets/LongRangeEnemy/blood_ball.png");

        Entity bulletUp = makeBullet(rotateVector(upRotate, source, x1, y1), upRotate, target, gameArea,
                source, x1, y1, "images/Enemy_Assets/LongRangeEnemy/blood_ball.png");

        Entity bulletDown = makeBullet(rotateVector(downRotate, source, x1, y1), downRotate, target, gameArea,
                source, x1, y1, "images/Enemy_Assets/LongRangeEnemy/blood_ball.png");

        gameArea.spawnEntity(bulletStraight);
        gameArea.spawnEntity(bulletUp);
        gameArea.spawnEntity(bulletDown);
    }


    /**
     * Calculates the rotation of the vector for the bullet and returns it in radians
     * @param rotate the vector to the target
     * @param source the source of the bullet
     * @param x_1 the x coordinate of the source
     * @param y_1 the y coordinate of the source
     * @return the rotation of the vector in radians
     */
    private static float rotateVector(Vector2 rotate, Entity source, float x_1, float y_1) {
        rotate = rotate.scl(100);
        rotate = rotate.add(source.getPosition());

        return (MathUtils.radiansToDegrees * MathUtils.atan2(rotate.y - y_1, rotate.x - x_1));
    }

    /**
     * Makes a bullet entity with a given rotation
     * @param rotation the rotation of the bullet in radians
     * @param destination the vector the bullet follows
     * @param target the target of the bullet
     * @param gameArea the area to spawn the entity in
     * @param source the source of the bullet
     * @param x_1 the x coordinate of the source
     * @param y_1 the y coordinate of the source
     * @param imagePath the image path for the bullet texture
     * @return the bullet entity
     */
    private static Entity makeBullet(float rotation, Vector2 destination, Entity target, GameArea gameArea,
                                     Entity source, float x_1, float y_1, String imagePath) {
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

        bullet.setPosition(
                x_1 - bullet.getScale().x / 2 + source.getScale().x / 2,
                y_1  - bullet.getScale().y / 2  + source.getScale().y / 2);

        return bullet;
    }

}
