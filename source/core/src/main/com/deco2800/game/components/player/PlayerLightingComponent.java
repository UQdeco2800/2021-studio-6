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

    private boolean torchState;
    private IndependentAnimator torchAnimator;
    private int torchTime;

    public PlayerLightingComponent (Color color, float distance, float offsetx, float offsety) {
        super(color, distance,offsetx,offsety);
        torchState = true;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggleTorch", this::ToggleTorchState);
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
        torchTime = 100;
    }

    /**
     * Toggles the state of the torch to allow player to toggle lighting
     */
    /**
     * Toggles the torch state
     */
    public void ToggleTorchState() {
        torchState = !torchState;
        this.getPointLight().setActive(torchState);
    }

    /**
     * Used to get the torch animator for use in the animation controller
     * @return the torchAnimator IndependentAnimator
     */
    public IndependentAnimator getTorchAnimator() {
        return torchAnimator;
    }
}
