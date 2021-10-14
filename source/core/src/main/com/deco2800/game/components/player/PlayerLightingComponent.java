package com.deco2800.game.components.player;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.lighting.PointLightComponent;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.services.ServiceLocator;

/**
 * Component to toggle player lighting
 */
public class PlayerLightingComponent extends PointLightComponent {

    private IndependentAnimator torchAnimator;

    public PlayerLightingComponent (Color color, float distance, float offsetx, float offsety) {
        super(color, distance,offsetx,offsety);
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("torchOn", this::torchOn);
        entity.getEvents().addListener("torchOff", this::torchOff);
        entity.getEvents().addListener("torchTick", this::torchTick);
        PlayerTorchAnimationController torch = this.entity.getComponent(PlayerTorchAnimationController.class);
        torchAnimator =
            new IndependentAnimator(
                ServiceLocator.getResourceService()
                    .getAsset("images/playeritems/tourch/torch.atlas", TextureAtlas.class), false);
        torchAnimator.addAnimation("left", 0.2f, Animation.PlayMode.LOOP);
        torchAnimator.addAnimation("back", 0.2f, Animation.PlayMode.LOOP);
        torchAnimator.addAnimation("right", 0.2f, Animation.PlayMode.LOOP);
        torchAnimator.addAnimation("front", 0.2f, Animation.PlayMode.LOOP);
        torchAnimator.setCamera(true);
        torchAnimator.setScale(1, 1);
        torch.setter();
    }

    /**
     * Turns the lighting off
     */
    private void torchOff() {
        this.getPointLight().setActive(false);
    }

    /**
     * Turns the lighting on
     */
    private void torchOn() {
        this.getPointLight().setActive(true);
    }

    /**
     * Reduces lighting distance as the torch runs out
     */
    private void torchTick(int ticks) {
        float distance = (float) ticks;
        distance = distance/10;
        this.getPointLight().setDistance(distance);

    }




    /**
     * Used to get the torch animator for use in the animation controller
     * @return the torchAnimator IndependentAnimator
     */
    public IndependentAnimator getTorchAnimator() {
        return torchAnimator;
    }
}
