package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.BulletCollisionComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.HTMLDocument;


/**
 * Used to listen and wait for player's range button to be clicked (L key). Once clicked,
 * a bullet will be created from player's entity position and launched towards coordinate where range button
 * is clicked in game area. If no movement keys clicked, range attack will be launched to the right by default
 */
public class PlayerRangeAOEComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerRangeAttackComponent.class);
    // activeBullets array will be updated and reloaded with ammo entities whenever bullet collides with
    // any object in game world
    private static Array<Entity> activeBomb;
    final Vector2 DEFAULT_ATK_DIR = Vector2Utils.RIGHT;
    private Vector2 longAttackDir = new Vector2(0,0);
    private static final int MAX_COORDINATE = 1000;
    private int magazineCapacity = 5;
    private boolean currentlyReloading = false;
    private static final Vector2 HIDDEN_COORD = new Vector2(-10,-10);
    private final GameTime timeSource = ServiceLocator.getTimeSource();
    private static final long bombFuse = 4000;
    private long bombEnd = 0;
    private boolean bombActive;
    private Entity firedBomb;

    /**
     * Create listener on player specifically when game is loaded and ready bullets
     * for firing
     *
     */
    public PlayerRangeAOEComponent() {
    }

    /**
     * To reuse bullet shot for performance sake
     *
     * @param bulletShot is the bullet that has been shot and removed from activeBullets array
     */
    public static void restockBulletShot(Entity bulletShot) {
        if (activeBomb != null) {
            activeBomb.add(bulletShot);
        }
    }

    /**
     * To return bomb of entities that are spawned in the game world. These will continuously
     * be used for performance sake
     *
     * @return array of bullet entities that are currently active and ready to be used
     */
    public static Array<Entity> getActiveBombs() {
        return activeBomb;
    }

    /**
     * Used to load after spawning in game area for firing in game
     *
     * @param bombs is the number of bombs player will be able to spawn into the game world
     */
    public void addBombs(Array<Entity> bombs) {
        activeBomb = new Array<>(bombs);
    }

    /**
     * Creation of listener on specified player when game is created
     */
    @Override
    public void create() {
        entity.getEvents().addListener("rangeAOE", this::fire);
    }

    @Override
    public void update() {
        if (bombEnd < timeSource.getTime() && bombActive) {
            System.out.println("here");
            ServiceLocator.getGameArea().despawnEntity(firedBomb);
            bombActive = false;
        }
    }

    /**
     * Returns Vector2 coordinates that will be used for target coordinates for bombs. These vectors
     * are then scaled to reach the end of the game screen relative to player's position.
     *
     * @param playerCoord is the current player's position. It is used to launch bullet from player's position
     * @return coordinates used for bullet's target coordinates
     */
    public Vector2 scaleVector(Vector2 playerCoord) {
        float xPosPlayer = playerCoord.x;
        float yPosPlayer = playerCoord.y;
        Vector2 scaledVector = new Vector2();

        // player has not moved before
        if (longAttackDir.isZero()) {
            scaledVector = DEFAULT_ATK_DIR.scl(MAX_COORDINATE);

            // player has moved before
        } else if (longAttackDir.epsilonEquals(Vector2Utils.RIGHT)) {
            scaledVector = new Vector2(xPosPlayer,yPosPlayer).add(new Vector2(5,0));
            longAttackDir = Vector2Utils.RIGHT;

        } else if (longAttackDir.epsilonEquals(Vector2Utils.LEFT)) {
            scaledVector = new Vector2(xPosPlayer,yPosPlayer).add(new Vector2(-5,0));
            longAttackDir = Vector2Utils.LEFT;

        } else if (longAttackDir.epsilonEquals(Vector2Utils.UP)) {
            scaledVector = new Vector2(xPosPlayer,yPosPlayer).add(new Vector2(0,5));
            longAttackDir = Vector2Utils.UP;

        } else if (longAttackDir.epsilonEquals(Vector2Utils.DOWN)) {
            scaledVector = new Vector2(xPosPlayer,yPosPlayer).add(new Vector2(0,-5));
            longAttackDir = Vector2Utils.DOWN;
        }
        return scaledVector.cpy();
    }

    /**
     * Set the current direction player is currently facing for range attack
     *
     * @param direction player is currently facing based on what movement key is pressed
     */
    public void setDirection(Vector2 direction) {
        longAttackDir = direction.cpy();
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
     * @param movingAttackDir would be vectors used to deploy projectiles in a direction (would only
     *                        be either north, south, east or west for now)
     */
    public void fire(Vector2 movingAttackDir) {
        Vector2 playerPos = entity.getPosition();
        Vector2 bombTargetPos;


        // when player attacks, (0,0) vector is sent over, only directional information is important now
        if (!movingAttackDir.epsilonEquals(Vector2.Zero.cpy())) {
            setDirection(movingAttackDir);
        }

        // check if there are bullets left to shoot in magazine currently and if player is currently reloading
        if (this.magazineCapacity != 0 && !getReloadingStatus()) {
            // player has not moved before, use default direction attack (to the right)
            if (getDirection().isZero()) {
                bombTargetPos = DEFAULT_ATK_DIR.scl(MAX_COORDINATE).cpy();
                bombTargetPos.y = playerPos.y;
            } else {
                // player has moved before, last button clicked to move as direction
                bombTargetPos = scaleVector(playerPos);
            }

            // bomb triggred
            if (movingAttackDir.isZero() && activeBomb != null && !bombActive) {
                firedBomb = activeBomb.get(0);
                activeBomb.removeIndex(0);
                firedBomb.getComponent(BulletCollisionComponent.class).setBulletLaunchStatus(true);

                firedBomb.setPosition(playerPos);
                firedBomb.getComponent(PhysicsMovementComponent.class).setTarget(bombTargetPos);
                System.out.println(playerPos);
                System.out.println(bombTargetPos);



                // update current gun magazine
                decreaseGunMagazine();
                bombEnd = bombFuse + timeSource.getTime();
                bombActive = true;
            }
        }

        // used to let player know that there is no ammo left and it is time
        // to reload - this event is triggered in interface display
        if (this.magazineCapacity == 0) {
            entity.getEvents().trigger("gunMagazineEmpty");
        }
    }

    /**
     * Called to reload current gun magazine capacity. May not always be reloaded to 5 (the max).
     * It is dependent on ammo left in inventory
     *
     * @param ammo that will be reloaded into gun magazine
     */
    public void reloadGunMagazine(int ammo) {
        setReloadingStatus(false);
        this.magazineCapacity += ammo;
        entity.getEvents().trigger("updateGunMagImageHUD",
                this.magazineCapacity, true);
    }

    /**
     * Updates reloading status of player - used to prevent player from shooting while reloading
     *
     * @param reloading that will be used to update reloading status to prevent player from shooting
     *                  when reloading
     */
    public void setReloadingStatus(boolean reloading) {
        this.currentlyReloading = reloading;
    }

    /**
     * Returns reloading status of player
     *
     * @return true if reloading is currently being execute and false otherwise
     */
    public boolean getReloadingStatus() {
        return currentlyReloading;
    }

    /**
     * Called to check current magazine capacity for reloading and ammo reduction purposes
     *
     * @return current gun magazine - how many bombs left in current round
     */
    public int getGunMagazine() {
        return this.magazineCapacity;
    }

    /**
     * Called to decrease gun magazine when a bomb is shot by player
     */
      private void decreaseGunMagazine() {
        --this.magazineCapacity;
        //entity.getEvents().trigger("updateGunMagImageHUD",
        //this.magazineCapacity, false);
    }
}


