package com.deco2800.game.components.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.components.DarknessDetectionComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Creates a glowing red eye animation for the entity
 */
public class GlowingEyesComponent extends AnimationRenderComponent {
    private Boolean visible;
    private Boolean active;

    /**
     * Creates a glowing red eye animation for the entity
     * @param atlas - the texture atlas that contains the stationary and walking animations
     */
    public GlowingEyesComponent(TextureAtlas atlas) {
        super(atlas);

        addAnimation("left-glow", 0.1f, Animation.PlayMode.LOOP);
        addAnimation("right-glow", 0.1f, Animation.PlayMode.LOOP);
        addAnimation("front-glow", 0.1f, Animation.PlayMode.LOOP);
        addAnimation("back-glow", 0.1f, Animation.PlayMode.LOOP);
        addAnimation("left-run-glow", 0.1f, Animation.PlayMode.LOOP);
        addAnimation("right-run-glow", 0.1f, Animation.PlayMode.LOOP);
        addAnimation("front-run-glow", 0.1f, Animation.PlayMode.LOOP);
        addAnimation("back-run-glow", 0.1f, Animation.PlayMode.LOOP);
        active = true;
    }

    /**
     * Calls the super create and turns the animation on/off depending if the entity starts in the dark
     */
    @Override
    public void create() {
        super.create();
        if (this.entity.getComponent(DarknessDetectionComponent.class).isInLight()) {
            visible = false;
        } else {
            visible = true;
        }
    }

    /**
     * Completely turns off the component
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Turn the red eyes off
     */
    public void displayOff() {
        visible = false;
    }

    /**
     * Turn the red eyes on
     */
    public void displayOn() {
        visible = true;
    }

    /**
     * Draws the red eyes at the location and size relative to the entity
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (visible && active) {
            super.draw(batch);
        }
    }
}
