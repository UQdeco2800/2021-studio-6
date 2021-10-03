package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.ToughFireBulletListener;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

public class FinalBossFactory {
    public static Entity createFinalBoss(Entity target) {
        Entity boss = new Entity()
                .addComponent(new PhysicsComponent())
                //.addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new TextureRenderComponent("images/placeholder.png"))
                //.addComponent(new CombatStatsComponent(3, 0))
                .addComponent(new DisposingComponent());

        boss.setScale(new Vector2(40f, 5f));

        return boss;
    }
}
