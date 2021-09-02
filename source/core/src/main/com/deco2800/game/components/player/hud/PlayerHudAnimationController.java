package com.deco2800.game.components.player.hud;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;


/**
 * This class listens to PlayerStatsDisplay and renders correct sprite state and
 * animations when events are triggered.
 * CURRENTLY UNSED AS OF SPRINT 1
 */
public class PlayerHudAnimationController extends Component{
    AnimationRenderComponent hudAnimator;

    @Override
    public void create() {
        super.create();
        hudAnimator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("shortEmpty", this::spriteShortEmpty);
        entity.getEvents().addListener("shortRegen", this::animateShortRegen);
        entity.getEvents().addListener("shortGreen", this::spriteShortGreen);
        entity.getEvents().addListener("mediumEmpty", this::spriteMediumEmpty);
        entity.getEvents().addListener("mediumRegen", this::animateMediumRegen);
        entity.getEvents().addListener("mediumOrange", this::spriteMediumOrange);
        entity.getEvents().addListener("mediumGreen", this::spriteMediumGreen);
        entity.getEvents().addListener("longEmpty", this::spriteLongEmpty);
        entity.getEvents().addListener("longRegen", this::animateLongRegen);
        entity.getEvents().addListener("longRed", this::spriteLongRed);
        entity.getEvents().addListener("longOrange", this::spriteLongOrange);
        entity.getEvents().addListener("longGreen", this::spriteLongGreen);

    }

    void spriteShortEmpty() {hudAnimator.startAnimation("shortEmpty");}

    void animateShortRegen() {hudAnimator.startAnimation("shortRegen");}

    void spriteShortGreen() {hudAnimator.startAnimation("shortGreen");}

    void spriteMediumEmpty() {hudAnimator.startAnimation("mediumEmpty");}

    void animateMediumRegen() {hudAnimator.startAnimation("mediumRegen");}

    void spriteMediumOrange() {hudAnimator.startAnimation("mediumOrange");}

    void spriteMediumGreen() {hudAnimator.startAnimation("mediumGreen");}

    void spriteLongEmpty() {hudAnimator.startAnimation("longEmpty");}

    void animateLongRegen() {hudAnimator.startAnimation("longRegen");}

    void spriteLongRed() {hudAnimator.startAnimation("longRed");}

    void spriteLongOrange() {hudAnimator.startAnimation("longOrange");}

    void spriteLongGreen() {hudAnimator.startAnimation("longGreen");}
}
