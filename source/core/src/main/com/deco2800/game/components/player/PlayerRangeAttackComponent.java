package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;
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
    private OrthographicCamera orthoCam;
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

    /**
     * Fires bullet projectile from player to destination clicked in game
     */
    void fire() {
        float xMouseCoord = Gdx.input.getX();
        float yMouseCoord = Gdx.input.getY();

        System.out.println("Screen width and height : " + Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
        // currently this is game screen coordinates - will need to convert to game world coordinate
        System.out.println("Mouse clicks val : " + xMouseCoord + " " + yMouseCoord);

//        float ratio = (float) screenHeight / screenWidth;
//        camera.viewportWidth = gameWidth;
//        camera.viewportHeight = gameWidth * ratio;

        float ratio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        orthoCam = new OrthographicCamera(15f, 15f * ratio);

        Vector3 mouseClickedScreenCoord = new Vector3(new Vector2(xMouseCoord,yMouseCoord), 0);
        mouseClickedScreenCoord = orthoCam.unproject(mouseClickedScreenCoord);
        System.out.println("Bullet clicked " + mouseClickedScreenCoord);

        playerPos = this.entity.getPosition();
        System.out.println("Player position " + playerPos);
        System.out.println("==============================");

//        temp = orthoCam.unproject(mouseClickedScreenCoord)
//        System.out.println("mouse : " + mouseClicked);
//        System.out.println(playerPos);
//        System.out.println("x coord mouse clicked : " + xMouseCoord);
//        System.out.println("y coord mouse clicked : " + yMouseCoord);

        if (activeBullets.size != 0) {
            Entity firedBullet = activeBullets.pop();
            // fire bullet from current position
            firedBullet.setPosition(playerPos);
            firedBullet.getComponent(PhysicsMovementComponent.class).setTarget(new Vector2(Gdx.graphics.getWidth() - xMouseCoord, Gdx.graphics.getHeight() - yMouseCoord));
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
