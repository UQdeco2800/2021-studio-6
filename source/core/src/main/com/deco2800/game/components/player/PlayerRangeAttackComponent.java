package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.deco2800.game.areas.GameArea;
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
    private Entity player;
    private GameArea gameArea;

    /**
     * Create listener on player specifically
     *
     */
    public PlayerRangeAttackComponent() {
    }

    /**
     * Creation of listener on specified player when game is created
     */
    @Override
    public void create() {
        entity.getEvents().addListener("playerRangeAttack", this::fire);
    }

    /**
     * Fires bullet projectile from player to destination clicked in game
     */
    void fire() {
        int xMouseCoord = Gdx.input.getX();
        int yMouseCoord = Gdx.input.getY();
        System.out.println("x coord mouse clicked : " + xMouseCoord);
        System.out.println("y coord mouse clicked : " + yMouseCoord);
    }
}
