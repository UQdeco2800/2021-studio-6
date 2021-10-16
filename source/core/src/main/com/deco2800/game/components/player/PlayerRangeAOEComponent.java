package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.FireCrackerCollisionComponent;
import com.deco2800.game.components.FirecrackerAnimationController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.items.Directions;
import com.deco2800.game.lighting.FlickerLightComponent;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This enables player to launch fire cracker and once it is launched, it continuously checks if fire cracker has
 * reached designated coordinates and upon reaching, it activates the explosion effect of the ability
 */
public class PlayerRangeAOEComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerRangeAOEComponent.class);
    private Vector2 longAttackDir = new Vector2(1,0);
    private static final int EXPLOSION_COORDINATE = 5;
    private boolean fireCrackerLaunched = false;
    private static final Vector2 HIDDEN_COORD = new Vector2(-10,-10);
    private final GameTime timeSource = ServiceLocator.getTimeSource();
    private static long timeToExplode = 0;
    private long AOE_END = 0;
    private Entity fireCracker;
    private Vector2 fireCrackerTargetPos = new Vector2(0,0);
    private Vector2 playerPos = new Vector2(0,0);
    private Directions direct;

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
        // fire cracker is launched
        if (fireCrackerLaunched) {
            Vector2 fireCrackerPos = fireCracker.getPosition();
            fireCracker.getComponent(FlickerLightComponent.class).disableLights();

            // this stops entity from continuously vibrating in same position
            if (fireCrackerTargetPos.epsilonEquals(fireCrackerPos, 1)) {
                fireCracker.getComponent(ColliderComponent.class).setSensor(false);
                fireCracker.getComponent(PhysicsMovementComponent.class).setMoving(false);
                logger.debug("Fire cracker has reached target coordinates");
            }

            // time to explode in game has been reached - this allows collision component
            // to start damaging any enemies within area of effect
            if (timeToExplode < timeSource.getTime()) {
                // NPCs within AOE will receive damage
                fireCracker.getComponent(ColliderComponent.class).setSensor(true);
                fireCracker.getComponent(FireCrackerCollisionComponent.class).setExplosion(true);
                fireCracker.getEvents().trigger("explosionStart");
                fireCracker.getComponent(FlickerLightComponent.class).changeDistance(8);
                fireCracker.getComponent(FlickerLightComponent.class).turnOnLights();
                fireCrackerLaunched = false;
                logger.debug("Fire cracker should explode");
            }
        }
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
     * are then scaled to the specified coordinate relative to player's position.
     *
     * @param playerCoord is the current player's position. It is used to launch bullet from player's position
     * @return coordinates used for bullet's target coordinates
     */
    public Vector2 getFireCrackerTargetCoord(Vector2 playerCoord) {
        float xPosPlayer = playerCoord.x;
        float yPosPlayer = playerCoord.y;
        Vector2 scaledVector = new Vector2();
        KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
        if (key != null) {
            direct = key.getDirection();
            switch (direct) {
                case MOVE_UP:
                    scaledVector = new Vector2(xPosPlayer, yPosPlayer + EXPLOSION_COORDINATE);
                    break;
                case MOVE_DOWN:
                    scaledVector = new Vector2(xPosPlayer, yPosPlayer - EXPLOSION_COORDINATE);
                    break;
                case MOVE_LEFT:
                    scaledVector = new Vector2(xPosPlayer  - EXPLOSION_COORDINATE,yPosPlayer);
                    break;
                case MOVE_RIGHT:
                    scaledVector = new Vector2(xPosPlayer + EXPLOSION_COORDINATE,yPosPlayer);
                    break;
            }
        }
        return scaledVector.cpy();
    }

    /**
     * Fires bullet projectile in a straight line based on the moving direction of player
     *
     * @param explosionTime it takes for explosion to occur
     */
    public void fire(long explosionTime) {
        playerPos = entity.getPosition();
        fireCrackerTargetPos = getFireCrackerTargetCoord(playerPos);
        fireCrackerLaunched = true;
        timeToExplode = timeSource.getTime() + explosionTime;

        // launch fire cracker and allow it to collide with any object in game (for now)
        fireCracker.setPosition(playerPos);
        fireCracker.getComponent(ColliderComponent.class).setSensor(true);
        fireCracker.getComponent(PhysicsMovementComponent.class).setMoving(true);
        fireCracker.getComponent(PhysicsMovementComponent.class).setTarget(fireCrackerTargetPos.cpy());
        fireCracker.getEvents().trigger("firecrackerStart", direct);

        logger.info("Fire cracker ability activated and will explode in " + explosionTime + "ms");
        IndependentAnimator explosionAnimator =
            new IndependentAnimator(
                ServiceLocator.getResourceService()
                    .getAsset("images/playeritems/firecracker/firecracker.atlas", TextureAtlas.class), true);
        explosionAnimator.addAnimation("explosion", 0.1f, Animation.PlayMode.NORMAL);
        explosionAnimator.addAnimation("explosionLoop", 0.1f, Animation.PlayMode.LOOP);
        explosionAnimator.setCamera(false);
        explosionAnimator.setScale(2, 2);

        fireCracker.getComponent(FirecrackerAnimationController.class).setAnimator(explosionAnimator);
        logger.info("Fire cracker ability activated and will explodie in " + explosionTime);
    }
}


