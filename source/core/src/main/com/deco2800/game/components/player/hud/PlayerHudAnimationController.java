package com.deco2800.game.components.player.hud;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.rendering.IndependentAnimator;

public class PlayerHudAnimationController extends Component{

    private PlayerStatsDisplay statsDisplay;
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
        statsDisplay = this.entity.getComponent(PlayerStatsDisplay.class);
        hudAnimator = statsDisplay.getDashAnimator();
        entity.getEvents().addListener("dashBar", this::dashStart);
        entity.getEvents().addListener("start", this::start);
        entity.getEvents().addListener("dispose", this::disposeAnimation);
        hudAnimator.startAnimation("dashbarFull");
    }

    void start() {hudAnimator.startAnimation("dashbarFull");
    }

    void dashStart() {
        hudAnimator.startAnimation("dashbar");
    }

    void disposeAnimation() {
        hudAnimator.dispose();
    }
}
