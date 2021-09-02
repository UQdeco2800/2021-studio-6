package com.deco2800.game.components.player.hud;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create each bar of health
 *
 * Each bar has Empty, Regen, and Green state
 * Medium has an additional Orange state
 * Long has Orange and Red state.
 *
 * Currently Unused
 */
public class PlayerHudFactory {
    public static Entity createShortBar(Entity target) {
        Entity shortBar = createBaseBar(target);
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/hud/22hpbar.atlas", TextureAtlas.class));
        animator.addAnimation("shortEmpty", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shortRegen", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shortGreen", 0.1f, Animation.PlayMode.NORMAL);

        shortBar
            .addComponent(animator)
            .addComponent(new PlayerHudAnimationController());

        shortBar.getComponent(AnimationRenderComponent.class).scaleEntity();
        return shortBar;


    }


    public static Entity createBaseBar(Entity target) {
        Entity baseBar = new Entity();

        return baseBar;
    }
}
