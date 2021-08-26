package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to listen and wait for player's range button to be clicked (LMB). Once clicked,
 * a bullet will be created from player's entity position and launched towards coordinate where range button
 * is clicked in game area
 */
public class PlayerRangeAttackComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerRangeAttackComponent.class);
    private static Array<Entity> activeBullets;
    private Vector2 playerPos, mouseClicked;
    float DEFINED_PRECISION = 1/6f;

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
        entity.getEvents().addListener("playerRangeAttack", this::fire);
    }

    @Override
    public void update() {
    }

//    public void update(Entity bulletUpdate) {
//        bulletUpdate.update();
//
//        for (Entity bullet : activeBullets) {
//            Vector2 currentBulletPos = bullet.getPosition();
//            Vector2 targetBullPos = bullet.
//                    getComponent(PhysicsMovementComponent.class).getTarget();
//
//            // check if bullet is still within game screen or it has reached designated clicked coordinates
//            if ((bullet.getPosition().y > Gdx.graphics.getHeight()) ||
//                   ( targetBullPos.len() - currentBulletPos.len() < DEFINED_PRECISION)) {
//                bullet.getComponent(PhysicsMovementComponent.class).setTarget(
//                        Vector2.Zero.cpy()
//                );
//                activeBullets.removeValue(bullet, true);
//            }
//        }
//    }

    /**
     * Fires bullet projectile from player to destination clicked in game
     */
    void fire() {
        float xMouseCoord = Gdx.input.getX();
        float yMouseCoord = Gdx.input.getY();
        mouseClicked = new Vector2(xMouseCoord, yMouseCoord);
        playerPos = this.entity.getPosition();
        System.out.println("x coord mouse clicked : " + xMouseCoord);
        System.out.println("y coord mouse clicked : " + yMouseCoord);

        if (activeBullets.size != 0) {
            Entity firedBullet = activeBullets.pop();
            System.out.println("Bullet fired " + firedBullet);
            firedBullet.setPosition(getEntity().getPosition());
        }
        // bullet will be fired and updated
//        Entity firingBullet = activeBullets.get(0);
//        activeBullets.removeRange(0,0);
//
//
//        // firing bullet
//        firingBullet.setPosition(playerPos);
//        firingBullet.getComponent(PhysicsMovementComponent.class).setTarget(mouseClicked);
    }
}
