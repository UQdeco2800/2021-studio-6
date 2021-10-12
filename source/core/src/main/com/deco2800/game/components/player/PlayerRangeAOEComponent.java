package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.items.Directions;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This enables player to launch fire cracker and once it is launched, it continuously checks if fire cracker has
 * reached designated coordinates and upon reaching, it activates the explosion effect of the ability
 */
public class PlayerRangeAOEComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerRangeAOEComponent.class);
    private Vector2 longAttackDir = new Vector2(0,0);
    private static final int EXPLOSION_COORDINATE = 2;
    private boolean fireCrackerLaunched = false;
    private static final Vector2 HIDDEN_COORD = new Vector2(-10,-10);
    private final GameTime timeSource = ServiceLocator.getTimeSource();
    private static final long AOE_DURATION = 5000;
    private long AOE_END = 0;
    private Entity fireCracker;

    /**
     * Create listener on player specifically when game is loaded and ready fire cracker for firing
     */
    public PlayerRangeAOEComponent() {
    }

    /**
     * Creation of listener on specified player when game is created
     */
    @Override
    public void create() {
        entity.getEvents().addListener("fireCracker", this::fire);
    }

    @Override
    public void update() {

    }

    /**
     * Adds fire cracker entity that will be launched. This method is used to reset fire cracker entity after it
     * explodes
     *
     * @param fireCracker that is involved in fire cracker ability
     */
    public void addFireCracker(Entity fireCracker) {
        this.fireCracker = fireCracker;
    }

    /**
     * Returns Vector2 coordinates that will be used for target coordinates for bombs. These vectors
     * are then scaled to reach the end of the game screen relative to player's position.
     *
     * @param playerCoord is the current player's position. It is used to launch bullet from player's position
     * @return coordinates used for bullet's target coordinates
     */
    public Vector2 scaleAndSetVector(Vector2 playerCoord) {
        float xPosPlayer = playerCoord.x;
        float yPosPlayer = playerCoord.y;
        Vector2 scaledVector = new Vector2();
        KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
        if (key != null) {
            Directions direct = key.getDirection();
            switch (direct) {
                case MOVE_UP:
                    scaledVector = new Vector2(xPosPlayer, yPosPlayer + EXPLOSION_COORDINATE);
                    longAttackDir = Vector2Utils.UP;
                    break;
                case MOVE_DOWN:
                    scaledVector = new Vector2(xPosPlayer, yPosPlayer - EXPLOSION_COORDINATE);
                    longAttackDir = Vector2Utils.DOWN;
                    break;
                case MOVE_LEFT:
                    scaledVector = new Vector2(xPosPlayer  - EXPLOSION_COORDINATE,yPosPlayer);
                    longAttackDir = Vector2Utils.LEFT;
                    break;
                case MOVE_RIGHT:
                    scaledVector = new Vector2(xPosPlayer + EXPLOSION_COORDINATE,yPosPlayer);
                    longAttackDir = Vector2Utils.RIGHT;
                    break;
            }
        }
        return scaledVector.cpy();
    }

    /**
     * Get direction player is currently facing
     *
     * @return direction playerr is currently facing
     */
    public Vector2 getDirection() {
        return longAttackDir.cpy();
    }


    /**
     * Fires bullet projectile in a straight line based on the moving direction of player
     *
     */
    public void fire() {
        Vector2 playerPos = entity.getPosition();
        Vector2 fireCrackerTargetPos;

        logger.info("Fire cracker ability activated");
    }
}


