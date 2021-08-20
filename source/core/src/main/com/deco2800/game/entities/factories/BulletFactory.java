package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.BulletFire;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.TextureRenderComponent;

public class BulletFactory {
    public static Entity createBullet(Entity source, Entity target, GameArea gameArea) {
        Entity bullet = new Entity()
                                .addComponent(new TextureRenderComponent("images/heart.png"))
                                .addComponent(new PhysicsComponent())
                                .addComponent(new PhysicsMovementComponent(new Vector2(3, 3)))
                                .addComponent(new ColliderComponent())
                                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                                .addComponent(new CombatStatsComponent(1, 2))
                                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0))
                                .addComponent(new BulletFire(target, gameArea));

        float x1 = source.getPosition().x;
        float y1 = source.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 newTarget = new Vector2(x2 - x1, y2 - y1);
        newTarget = newTarget.scl(100);
        newTarget = newTarget.add(source.getPosition());

        bullet.setPosition(source.getPosition().x, source.getPosition().y);

        bullet.getComponent(PhysicsMovementComponent.class).setTarget(newTarget);
        bullet.getComponent(PhysicsMovementComponent.class).setMoving(true);
        bullet.getComponent(ColliderComponent.class).setSensor(true);
        return bullet;
    }

    private static Entity createBaseNPC(Entity target) {
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(target, 10, 3f, 4f));
        Entity npc =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
                        .addComponent(aiComponent);

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }


}
