package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.MultiAITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.Level4;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.DisposingComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.finalboss.FinalBossHealthBar;
import com.deco2800.game.components.finalboss.LaserTimer;
import com.deco2800.game.components.npc.BossAnimationController;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.finalboss.LaserListener;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.components.tasks.FinalBossFireLaser.FinalBossFireLaser;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.lighting.FlickerLightComponent;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;


/**
 * The factory for all the entities related to the final boss
 */
public class FinalBossFactory {


    public static Entity createFinalBossPhaseManager(Entity boss, Entity darkness,
                                                     Entity healthBackground, Entity healthForeground) {

        Entity phaseManager = new Entity()
                .addComponent(new BossHealthListener(boss, darkness, healthBackground, healthForeground));

        return phaseManager;
    }


    /**
     * Creates the darkness for level 4
     * @param target the entity that the enemies attack
     * @param gameArea the level 4 game area
     * @return the darkness entity
     */
    public static Entity createDarkness(Entity target, GridPoint2 bounds, GameArea gameArea) {

        TextureRenderComponent texture = new TextureRenderComponent("images/placeholder.png");
        texture.setLayer(1);


        Entity darkness = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new DisposingComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE, PhysicsLayer.PLAYER).setDensity(100))
                .addComponent(texture);

        MultiAITaskComponent aiComponent =
                new MultiAITaskComponent()
                        .addTask(new ItemSpawner(bounds));
//                        .addTask(new SpawnerSpawner(target, bounds));

        darkness.setScale(50, 7);
        darkness.addComponent(aiComponent);


        return darkness;
    }

    /**
     * createBossHead
     * @param target Should be the player entity as AI is dependent on the player
     * @return the boss entity
     */
    public static Entity createBossHead(Entity target, float bounds, Level4 gameArea, Entity darkness) {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/Final_Boss/boss_head.atlas", TextureAtlas.class));
            animator.addAnimation("default", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("left", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("right", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("angry_left", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("angry_right", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("float_left", 0.3f, Animation.PlayMode.LOOP);
        animator.addAnimation("float_right", 0.3f, Animation.PlayMode.LOOP);
        animator.addAnimation("angry_float_left", 0.3f, Animation.PlayMode.LOOP);
        animator.addAnimation("angry_float_right", 0.3f, Animation.PlayMode.LOOP);

        //System.out.println(animator.getLayer());

        Entity bossHead = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent(new Vector2(3, 3)))
                .addComponent(new ColliderComponent().setDensity(1000))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(animator)
                .addComponent(new CombatStatsComponent(100, 0))
                .addComponent(new DisposingComponent())
                .addComponent(new BossAnimationController())
                .addComponent(new FlickerLightComponent(Color.WHITE, Color.WHITE, Color.WHITE,
                        Color.WHITE, 2f, 0, -1.5f))
                .addComponent(new LaserListener());

        Entity healthBackground = createHealthBarBackground();
        Entity healthForeground = createHealthBarForeground();


        MultiAITaskComponent aiComponent =
                new MultiAITaskComponent()
                        .addTask(new BossMovementTask(bounds))
                        .addTask(new FinalBossFireLaser(target))
                        .addTask(new FinalBossHealthBar(gameArea, bossHead, healthBackground, healthForeground));

        bossHead.addComponent(aiComponent);

        gameArea.spawnEntity(createFinalBossPhaseManager(bossHead, darkness, healthBackground, healthForeground));

        bossHead.getComponent(AnimationRenderComponent.class).scaleEntity();
        bossHead.setScale(new Vector2(5.3f, 4f));
        bossHead.getComponent(FlickerLightComponent.class).turnOnLights();
        //bossHead.getComponent(FlickerLightComponent.class).changeOffsetX(-2f);
        
        return bossHead;
    }

    /**
     * Creates the beam that the boss head shoots
     * @return the beam
     */
    public static Entity createBeam() {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/Final_Boss/final_beam.atlas", TextureAtlas.class));
                animator.addAnimation("default", 0.1f, Animation.PlayMode.LOOP);

        AITaskComponent aiComponent =
                new AITaskComponent();

        Entity beam = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NPC).setDensity(100))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(aiComponent)
                .addComponent(new GhostAnimationController())
                .addComponent(animator)
                .addComponent(new CombatStatsComponent(1000, 1))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER))
                .addComponent(new LaserTimer());

        beam.getComponent(AnimationRenderComponent.class).scaleEntity();
        beam.setScale(new Vector2(3f, 15f));
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
                .addComponent(new TextureRenderComponent("images/Enemy_Assets/SpawnerEnemy/spawnerEgg.png"))
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new CombatStatsComponent(5, 0));;

        spawner.setScale(1f, 1f);
        return spawner;
    }

    /**
     * Creates the image for the health bar background
     * @return the health bar background
     */
    public static Entity createHealthBarBackground() {

        TextureRenderComponent image = new TextureRenderComponent("images/Final_Boss/healthbar_background.png");
        image.setUnlit();

        Entity healthBar = new Entity()
                .addComponent(image);

        healthBar.setScale(10f,0.5f);

        return healthBar;

    }

    /**
     * Creates the image for the health bar foreground
     * @return the health bar foreground
     */
    public static Entity createHealthBarForeground() {

       TextureRenderComponent image = new TextureRenderComponent("images/Final_Boss/healthbar_foreground.png");
       image.setUnlit();

        Entity healthForeground = new Entity()
                .addComponent(image);

        healthForeground.setScale(10f, 0.5f);

        return healthForeground;
    }
}
