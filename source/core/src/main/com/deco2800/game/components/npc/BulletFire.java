package com.deco2800.game.components.npc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BulletFactory;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;

public class BulletFire extends Component {
    private Entity target;
    private HitboxComponent hitboxComponent;
    private GameArea gameArea;
    private short targetLayer;

    public BulletFire(Entity target, GameArea gameArea) {
        this.target = target;
        this.gameArea = gameArea;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collisionStart", this::fire);
        this.hitboxComponent = entity.getComponent(HitboxComponent.class);
        this.targetLayer = this.hitboxComponent.getLayer();
    }

    private void fire(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        if (target == this.target) {
            gameArea.despawnEntity(entity);
//            System.out.println("hit player");
        }

    }
}
