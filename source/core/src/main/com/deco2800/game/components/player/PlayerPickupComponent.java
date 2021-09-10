package com.deco2800.game.components.player;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Component;

/**
 * Gives player entity the ability to pick up items that are spawned
 * during the game. When items are picked up, player entity will update
 * relevant data variables in the inventory component
 */
public class PlayerPickupComponent extends Component {

    public PlayerPickupComponent() {

    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collisionStart", this::tryPickUpItem);
    }

    private void tryPickUpItem(Fixture me, Fixture other) {

    }
}
