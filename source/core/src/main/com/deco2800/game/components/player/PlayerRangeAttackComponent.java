package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.BulletCollisionComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Used to listen and wait for player's range button to be clicked (L key). Once clicked,
 * a bullet will be created from player's entity position and launched towards coordinate where range button
 * is clicked in game area. If no movement keys clicked, range attack will be launched to the right by default
 */
public class PlayerRangeAttackComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerRangeAttackComponent.class);
    private static Array<Entity> activeBullets;
    final Vector2 DEFAULT_ATK_DIR = Vector2Utils.RIGHT;
    private Vector2 longAttackDir = new Vector2(0,0);
    private static final int MAX_COORDINATE = 15;

    /**
     * Create listener on player specifically when game is loaded and ready bullets
     * for firing
     *
     */
    public PlayerRangeAttackComponent() {
    }

    /**
     * To reuse bullet shot for performance sake
     *
     * @param bulletShot is the bullet that has been shot and removed from activeBullets array
     */
    public static void restockBulletShot(Entity bulletShot) {
        activeBullets.add(bulletShot);
    }

    public static Array<Entity> getActiveBullets() {
        return activeBullets;
    }

    /**
     * Used to load after spawning in game area for firing in game
     *
     * @param bullets is the number of bullets player has to shoot
     */
    public void addBullets(Array<Entity> bullets) {
        activeBullets = new Array<>(bullets);
    }

    /**
     * Creation of listener on specified player when game is created
     */
    @Override
    public void create() {
        entity.getEvents().addListener("rangeAttack", this::fire);
    }

    @Override
    public void update() {
    }

    /**
     * Returns Vector2 coordinates that will be used for target coordinates for bullets. These vectors
     * are then scaled to reach the end of the game screen relative to player's position.
     *
     * @param playerCoord is the current player's position. It is used to launch bullet from player's position
     * @return coordinates used for bullet's target coordinates
     */
    private Vector2 scaleVector(Vector2 playerCoord) {
        float xPosPlayer = playerCoord.x;
        float yPosPlayer = playerCoord.y;
        Vector2 scaledVector = new Vector2();

        // player has not moved before
        if (longAttackDir.isZero()) {
            System.out.println("DEFAULT ATTACK RIGHT");
            scaledVector = DEFAULT_ATK_DIR.scl(MAX_COORDINATE);

        // player has moved before
        } else if (longAttackDir.epsilonEquals(Vector2Utils.RIGHT)) {
            scaledVector = new Vector2(MAX_COORDINATE,yPosPlayer);
            longAttackDir = Vector2Utils.RIGHT;

        } else if (longAttackDir.epsilonEquals(Vector2Utils.LEFT)) {
            scaledVector = new Vector2(-MAX_COORDINATE,yPosPlayer);
            longAttackDir = Vector2Utils.LEFT;;

        } else if (longAttackDir.epsilonEquals(Vector2Utils.UP)) {
            scaledVector = new Vector2(xPosPlayer, MAX_COORDINATE);
            longAttackDir = Vector2Utils.UP;

        } else if (longAttackDir.epsilonEquals(Vector2Utils.DOWN)) {
            scaledVector = new Vector2(xPosPlayer, -MAX_COORDINATE);
            longAttackDir = Vector2Utils.DOWN;
        }
        return scaledVector.cpy();
    }

    /**
     * Fires bullet projectile in a straight line based on the moving direction of player
     *
     * @param movingAttackDir would be vectors used to deploy projectiles in a direction (would only
     *                        be either north, south, east or west for now)
     */
    void fire(Vector2 movingAttackDir) {
        Vector2 playerPos = entity.getPosition();
        Vector2 bulletTargetPos;

        // when player attacks, (0,0) vector is sent over, only directional information is important now
        if (!movingAttackDir.epsilonEquals(Vector2.Zero.cpy())) {
            longAttackDir = movingAttackDir.cpy();
        }

        // check if there is ammo
        if (activeBullets.size != 0) {
            // player has not moved before, use default direction attack (to the right)
            if (longAttackDir.isZero()) {
                bulletTargetPos = DEFAULT_ATK_DIR.scl(MAX_COORDINATE).cpy();
                bulletTargetPos.y = playerPos.y;
            } else {
                // player has moved before, last button clicked to move as direction
                bulletTargetPos = scaleVector(playerPos);
            }

            // bullet shot
            if (movingAttackDir.isZero()) {
                Entity firedBullet = activeBullets.get(0);
                activeBullets.removeIndex(0);
                firedBullet.getComponent(BulletCollisionComponent.class).setBulletLaunchStatus(true);

                firedBullet.setPosition(playerPos);
                firedBullet.getComponent(PhysicsMovementComponent.class).setTarget(bulletTargetPos);
            }
        }
    }

}
