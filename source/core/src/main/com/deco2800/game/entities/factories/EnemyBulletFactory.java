package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.BulletCollider;
import com.deco2800.game.components.npc.ToughFireBulletListener;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create 'bullets' from a source entity to a dest entity
 * The bullet currently travels in a straight line from source towards (and past) target
 */
public class EnemyBulletFactory {

    /** Creates a bullet entity firing from a source entity through a  target entity
     * @param target the target entity to be fired at
     * @param source the source of the bullet
     * @param textureFileName the filename for the texture
     */
    public static void createBullet(Entity source, Entity target, String textureFileName) {

        float x1 = source.getPosition().x;
        float y1 = source.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 newTarget = new Vector2(x2 - x1, y2 - y1);

        Entity bullet = makeBullet(rotateTexture(newTarget, source, x1, y1), newTarget, target,
                source, x1, y1, textureFileName);

        GameArea gameArea = ServiceLocator.getGameArea();
        gameArea.spawnEntity(bullet);
    }

    /**
     * Creates three bullets for the tough long range enemy. One follows a straight line towards the player, another
     * is shifted up 45 degrees from the straight vector and the other is shifted down 45 degrees from the straight vectpr.
     * @param source the source of the bullet
     * @param target the target entity to be fired at
     * @param textureFileName the filename for the texture
     */
    public static void createToughBullet(Entity source, Entity target, String textureFileName) {
        float x1 = source.getPosition().x;
        float y1 = source.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 straightTarget = new Vector2(x2 - x1, y2 - y1); //vector straight through player

        //Shifted up 45 degrees from the straight vector
        Vector2 upRotate = rotateVector(straightTarget, Math.PI/4);

        //Shifted down 45 degrees from the straight vector
        Vector2 downRotate = rotateVector(straightTarget, -(Math.PI)/4);

        Entity bulletStraight = makeBullet(rotateTexture(straightTarget, source, x1, y1), straightTarget, target,
                source, x1, y1, textureFileName);

        Entity bulletUp = makeBullet(rotateTexture(upRotate, source, x1, y1), upRotate, target,
                source, x1, y1, textureFileName);

        Entity bulletDown = makeBullet(rotateTexture(downRotate, source, x1, y1), downRotate, target,
                source, x1, y1, textureFileName);

        GameArea gameArea = ServiceLocator.getGameArea();
        gameArea.spawnEntity(bulletStraight);
        gameArea.spawnEntity(bulletUp);
        gameArea.spawnEntity(bulletDown);
    }


    /**
     * Calculates the rotation of the bullet texture to point at its target
     * @param rotate the vector to the target
     * @param source the source of the bullet
     * @param x1 the x coordinate of the source
     * @param y1 the y coordinate of the source
     * @return the rotation of the texture
     */
    private static float rotateTexture(Vector2 rotate, Entity source, float x1, float y1) {
        rotate = rotate.scl(100);
        rotate = rotate.add(source.getPosition());

        return (MathUtils.radiansToDegrees * MathUtils.atan2(rotate.y - y1, rotate.x - x1));
    }

    /**
     * Rotates a vector using this formula
     *  x2=cosβx1−sinβy1
     *  y2=sinβx1+cosβy1
     *
     * @param target the target vector to rotate
     * @param rotation the amount of rotation in radians
     * @return a new vector rotated by rotation
     */
    private static Vector2 rotateVector(Vector2 target, double rotation) {
        double upX = (Math.cos(rotation) * target.x) - (Math.sin(rotation) * target.y);
        double upY = (Math.sin(rotation) * target.x) + (Math.cos(rotation) * target.y);
        return new Vector2( (float) upX, (float) upY);
    }

    /**
     * Makes a bullet entity with a given rotation
     * @param rotation the rotation of the bullet in radians
     * @param destination the vector the bullet follows
     * @param target the target of the bullet
     * @param source the source of the bullet
     * @param x1 the x coordinate of the source
     * @param y1 the y coordinate of the source
     * @param imagePath the image path for the bullet texture
     * @return the bullet entity
     */
    private static Entity makeBullet(float rotation, Vector2 destination, Entity target,
                                     Entity source, float x1, float y1, String imagePath) {

        // Create the bullet entity
        Entity bullet = new Entity()
                .addComponent(new TextureRenderComponent(imagePath, rotation, 75))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(3, 3)))
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ENEMYBULLET))
                .addComponent(new CombatStatsComponent(1, 2))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f))
                .addComponent(new BulletCollider(target, ServiceLocator.getGameArea(), PhysicsLayer.PLAYER));

        // If it is a bloodball then add the light component
        if (source.getComponent(ToughFireBulletListener.class) == null) {
            bullet.addComponent(new PointLightComponent(new Color(0x691e47aa), 0.5f, 0, 0));
            PhysicsUtils.setEntityPhysics(bullet, 0f, 0.2f, 0.2f, 0f, 0f);
        } else {
            PhysicsUtils.setEntityPhysics(bullet, 0f, 0.1f, 0.1f, 0f, 0f);
        }

        // Make the bull smaller with a small hitbox
        bullet.setScale(0.5f, 0.5f);
        PhysicsUtils.setEntityPhysics(bullet, 0f, 0.2f, 0.2f, 0f, 0f);

        bullet.getComponent(PhysicsMovementComponent.class).setTarget(destination);
        bullet.getComponent(PhysicsMovementComponent.class).setMoving(true);
        bullet.getComponent(ColliderComponent.class).setSensor(true);

        bullet.setPosition(
                x1 - bullet.getScale().x / 2 + source.getScale().x / 2,
                y1  - bullet.getScale().y / 2  + source.getScale().y / 2);

        return bullet;
    }

    private EnemyBulletFactory() {
        throw new IllegalStateException("Factory class");
    }
}
