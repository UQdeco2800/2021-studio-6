package com.deco2800.game.components.player.hud;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.PlayerInterfaceDisplay;
import com.deco2800.game.rendering.IndependentAnimator;

public class PlayerHudAnimationController extends Component{

    private PlayerInterfaceDisplay statsDisplay;
    private IndependentAnimator hudAnimator;

    @Override
    public void create() {
        super.create();
    }

    /**
     * Setter has to be called after creation from wherever the Controller was
     * instanced to get around some annoying issues.
     */
    public void setter() {
        statsDisplay = this.entity.getComponent(PlayerInterfaceDisplay.class);
        hudAnimator = statsDisplay.getDashAnimator();
        entity.getEvents().addListener("abilityCooldown", this::abilityStart);
        entity.getEvents().addListener("dispose", this::disposeAnimation);
        hudAnimator.startAnimation("dashbarFull");
    }

    /**
     * Starts the animation for the ability cooldown on the hud.
     */
    void abilityStart() {
        hudAnimator.startAnimation("dashbar");
    }

    void disposeAnimation() {
        hudAnimator.dispose();
    }
}
