package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.MultiAITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.Level4;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.finalboss.LaserTimer;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.finalboss.LaserListener;
import com.deco2800.game.components.npc.ToughFireBulletListener;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * The factory for all the entities related to the final boss
 */
public class FinalBossFactory {

    /**
     * Creates the darkness for level 4
     * @param target the entity that the enemies attack
     * @param gameArea the level 4 game area
     * @return the darkness entity
     */
    public static Entity createDarkness(Entity target, Level4 gameArea) {
        Entity darkness = new Entity()
                .addComponent(new PhysicsComponent())
                //.addComponent(new PhysicsMovementComponent())
//                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.PLAYER))
//                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
//                .addComponent(new TextureRenderComponent("images/placeholder.png"))
                //.addComponent(new CombatStatsComponent(3, 0))
                .addComponent(new DisposingComponent());

//        Entity beam = createBeam();
//        Entity bossHead = createBossHead();
//        Entity spawner = createLightSpawner(target, gameArea);



        AITaskComponent aiComponent =
                new AITaskComponent()
                          .addTask(new Stage2Task(1, gameArea, target));
//                        .addTask(new Stage1Task(1, gameArea, darkness, beam, bossHead));

        darkness.addComponent(aiComponent);
//        darkness.setScale(new Vector2(40f, 5f));
        //PhysicsUtils.setScaledCollider(darkness, 0.6f, 0.3f);

        return darkness;
    }

    /**
     * Creates the boss head
     * @return the boss head
     */
    public static Entity createBossHead(Entity target) {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/Final_Boss/boss_head.atlas", TextureAtlas.class));
            animator.addAnimation("default", 1f, Animation.PlayMode.LOOP);


        MultiAITaskComponent aiComponent =
                new MultiAITaskComponent()
                        .addTask(new BossMovementTask())
                        .addTask(new FinalBossFireLaser(target));


        Entity bossHead = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(2, 2)))
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(aiComponent)
                .addComponent(new GhostAnimationController())
                .addComponent(animator)
                .addComponent(new CombatStatsComponent(1000, 0))
                .addComponent(new DisposingComponent())
                .addComponent(new LaserListener());


        bossHead.getComponent(AnimationRenderComponent.class).scaleEntity();
        bossHead.setScale(new Vector2(4f, 4f));
        bossHead.getComponent(AnimationRenderComponent.class).startAnimation("default");

        return bossHead;
    }

    /**
     * Creates the beam that the boss head shoots
     * @return the beam
     */
    public static Entity createBeam() {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/Final_Boss/beam.atlas", TextureAtlas.class));
                animator.addAnimation("default", 0.1f, Animation.PlayMode.LOOP);

        AITaskComponent aiComponent =
                new AITaskComponent();

        Entity beam = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                //.addComponent(new ColliderComponent())
                //.addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(aiComponent)
                .addComponent(new GhostAnimationController())
                .addComponent(animator)
                .addComponent(new CombatStatsComponent(1000, 5))
//                .addComponent(new DisposingComponent())
                .addComponent(new LaserTimer());

        beam.getComponent(AnimationRenderComponent.class).scaleEntity();
        beam.setScale(new Vector2(3f, 12f));
        beam.getComponent(AnimationRenderComponent.class).startAnimation("default");

        return beam;
    }

    /**
     * Creates the spawners for stage 2
     * @param target the entity that the enemies attack
     * @param gameArea the game area
     * @return the spawner entity
     */
    public static Entity createLightSpawner(Entity target, GameArea gameArea) {


        Entity spawner = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new DisposingComponent())
                .addComponent(new PointLightComponent(Colors.get("RED"), 3f, 0, 0))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new GhostAnimationController())
                .addComponent(new TextureRenderComponent("images/Enemy_Assets/SpawnerEnemy/spawnerEnemy.png"))
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new CombatStatsComponent(5, 0));;

        spawner.setScale(1f, 1f);
        return spawner;
    }
}
