package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;
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
     * Scale vector position for bullet targets to reach the end of the screen based on player's position
     * in game
     *
     * @param directionCheck are vectors that will be scaled depending on keys pressed by user
     */
    private void registerDirection(Vector2 directionCheck) {

        if (directionCheck.epsilonEquals(Vector2Utils.RIGHT)) {
            System.out.println("RIGHT REGISTER");
            longAttackDir = Vector2Utils.RIGHT;

        } else if (directionCheck.epsilonEquals(Vector2Utils.LEFT)) {
            System.out.println("LEFT REGISTER");
            longAttackDir = Vector2Utils.LEFT;;

        } else if (directionCheck.epsilonEquals(Vector2Utils.UP)) {
            System.out.println("UP REGISTER");
            longAttackDir = Vector2Utils.UP;

        } else if (directionCheck.epsilonEquals(Vector2Utils.DOWN)) {
            System.out.println("DOWN REGISTER");
            longAttackDir = Vector2Utils.DOWN;
        }
    }

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
            System.out.println("MOVE ATTACK RIGHT");
            scaledVector = new Vector2(MAX_COORDINATE,yPosPlayer);
            longAttackDir = Vector2Utils.RIGHT;

        } else if (longAttackDir.epsilonEquals(Vector2Utils.LEFT)) {
            System.out.println("MOVE ATTACK LEFT");
            scaledVector = new Vector2(-MAX_COORDINATE,yPosPlayer);
            longAttackDir = Vector2Utils.LEFT;;

        } else if (longAttackDir.epsilonEquals(Vector2Utils.UP)) {
            System.out.println("MOVE ATTACK UP");
            scaledVector = new Vector2(xPosPlayer, MAX_COORDINATE);
            longAttackDir = Vector2Utils.UP;

        } else if (longAttackDir.epsilonEquals(Vector2Utils.DOWN)) {
            System.out.println("MOVE ATTACK DOWN");
            scaledVector = new Vector2(xPosPlayer, -MAX_COORDINATE);
            longAttackDir = Vector2Utils.DOWN;
        }
        return scaledVector.cpy();
    }

    /**
     * Fires bullet projectile from player to destination clicked in game
     *
     * @param movingAttackDir would be vectors used to deploy projectiles in a direction (would only
     *                        be either north, south, east or west for now)
     */
    void fire(Vector2 movingAttackDir) {
        System.out.println(movingAttackDir);
        Vector2 playerPos = this.entity.getPosition();
        Vector2 bulletTargetPos = new Vector2();

        registerDirection(movingAttackDir);

        // player has not moved before, use default direction attack (to the right)
        if (longAttackDir.isZero()) {
            System.out.println("DEFAULT ATTACK PLAYER NO MOVE");
            bulletTargetPos = DEFAULT_ATK_DIR.scl(MAX_COORDINATE).cpy();
            bulletTargetPos.y = playerPos.y;
        } else {
            System.out.println("DIRECTIONAL ATTACK PLAYER MOVE");
        // player has moved before, last button clicked to move as direction
            bulletTargetPos = scaleVector(playerPos);
        }

        // bullet shot and there is ammo
        if (activeBullets.size != 0 && movingAttackDir.isZero()) {
            Entity firedBullet = activeBullets.pop();
            firedBullet.setPosition(playerPos);
            System.out.println("BULLET TRAJECTORY " + bulletTargetPos);
            firedBullet.getComponent(PhysicsMovementComponent.class).setTarget(bulletTargetPos);
        }
    }

}
