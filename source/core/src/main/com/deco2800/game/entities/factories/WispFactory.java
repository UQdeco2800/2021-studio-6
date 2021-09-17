package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.MoveInCircle;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;

public class WispFactory {


    public static Entity createWisp(int startIndex) {

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new MoveInCircle(new Vector2(-0.5f, -0.5f), 0.7f, startIndex));

        Entity wisp = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(0.5f, 0.5f)))
                .addComponent(new PointLightComponent(Colors.get("ORANGE"), 0.4f, 0, 0))
                .addComponent(aiComponent);

        return wisp;
    }
}
