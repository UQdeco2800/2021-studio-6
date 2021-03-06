package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.BulletAnimationController;
import com.deco2800.game.components.BulletCollisionComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.items.Directions;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Used to listen and wait for player's range button to be clicked (Enter key). Once clicked,
 * a bullet will be created from player's entity position and launched towards coordinate where range button
 * is clicked in game area. If no movement keys clicked, range attack will be launched to the right by default
 */
public class PlayerRangeAttackComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerRangeAttackComponent.class);
    // activeBullets array will be updated and reloaded with ammo entities whenever bullet collides with
    // any object in game world
    private static Array<Entity> activeBullets;
    private Vector2 longAttackDir = new Vector2(0,0);
    private static final int MAX_COORDINATE = 1000;
    private int magazineCapacity = 5;
    private boolean currentlyReloading = false;

    /**
     * Create listener on player specifically when game is loaded and ready bullets
     * for firing
     *
     */
    public PlayerRangeAttackComponent() {
    }


    /**
     * Creation of listener on specified player when game is created
     */
    @Override
    public void create() {
        entity.getEvents().addListener("rangeAttack", this::fire);
    }

        /**
         * To reuse bullet shot for performance sake
         *
         * @param bulletShot is the bullet that has been shot and removed from activeBullets array
         */
    public static void restockBulletShot(Entity bulletShot) {
        if (activeBullets != null) {
            activeBullets.add(bulletShot);
        }
    }

    /**
     * Sets bullet magazine count of player
     *
     * @param bullet number that will be used to set bullet count in magazine of player
     */
    public void setBulletMagazine(int bullet) {
        magazineCapacity = bullet;
    }

    /**
     * To return bullet of entities that are spawned in the game world. These will continuously
     * be used for performance sake
     *
     * @return array of bullet entities that are currently active and ready to be used
     */
    public static Array<Entity> getActiveBullets() {
        return activeBullets;
    }

    /**
     * Used to load after spawning in game area for firing in game
     *
     * @param bullets is the number of bullets player will be able to spawn into the game world
     */
    public void addBullets(Array<Entity> bullets) {
        activeBullets = new Array<>(bullets);
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
    public Vector2 scaleVector(Vector2 playerCoord) {
        float xPosPlayer = playerCoord.x;
        float yPosPlayer = playerCoord.y;
        setDirection(playerCoord);
        Vector2 scaledVector = new Vector2();
        KeyboardPlayerInputComponent key = this.getEntity().getComponent(KeyboardPlayerInputComponent.class);
        if (key != null) {
            Directions direct = key.getDirection();
            switch (direct) {
                case MOVE_UP:
                    scaledVector = new Vector2(xPosPlayer, MAX_COORDINATE);
                    longAttackDir = Vector2Utils.UP;
                    break;
                case MOVE_DOWN:
                    scaledVector = new Vector2(xPosPlayer, -MAX_COORDINATE);
                    longAttackDir = Vector2Utils.DOWN;
                    break;
                case MOVE_LEFT:
                    scaledVector = new Vector2(-MAX_COORDINATE,yPosPlayer);
                    longAttackDir = Vector2Utils.LEFT;
                    break;
                case MOVE_RIGHT:
                    scaledVector = new Vector2(MAX_COORDINATE,yPosPlayer);
                    longAttackDir = Vector2Utils.RIGHT;
                    break;
            }
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
     * @return direction player is currently facing
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
        Vector2 bulletTargetPos;

        // check if there are bullets left to shoot in magazine currently and if player is currently reloading
        if (this.magazineCapacity != 0 && !getReloadingStatus()) {
            bulletTargetPos = scaleVector(playerPos);

            // bullet shot
            if (activeBullets != null) {
                PlayerSoundComponent pcs = entity.getComponent(PlayerSoundComponent.class);
                if(pcs != null) { pcs.playShoot(); }

                Entity firedBullet = activeBullets.get(0);
                activeBullets.removeIndex(0);
                firedBullet.getComponent(BulletCollisionComponent.class).setBulletLaunchStatus(true);
                firedBullet.getComponent(PhysicsMovementComponent.class).setMoving(true);

                firedBullet.setPosition(playerPos);
                firedBullet.getComponent(PhysicsMovementComponent.class).setTarget(bulletTargetPos);
                if (firedBullet.getComponent(BulletAnimationController.class) != null) {
                    firedBullet.getComponent(BulletAnimationController.class).setDirection(getDirection());
                }

                // update current gun magazine
                decreaseGunMagazine();
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
     * @return current gun magazine - how many bullets left in current round
     */
    public int getGunMagazine() {
        return this.magazineCapacity;
    }

    /**
     * Called to decrease gun magazine when a bullet is shot by player
     */
    private void decreaseGunMagazine() {
        --this.magazineCapacity;
        entity.getEvents().trigger("updateGunMagImageHUD",
                this.magazineCapacity, false);
    }
}
