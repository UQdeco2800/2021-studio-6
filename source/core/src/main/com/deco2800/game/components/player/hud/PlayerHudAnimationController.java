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

    public void setter() {
        statsDisplay = this.entity.getComponent(PlayerStatsDisplay.class);
        hudAnimator = statsDisplay.getDashAnimator();
        entity.getEvents().addListener("dashBar", this::dashStart);
        entity.getEvents().addListener("start", this::start);
    }

    void start() {hudAnimator.startAnimation("dashbarFull");
    }

    void dashStart() {hudAnimator.startAnimation("dashbar");
    }
}
