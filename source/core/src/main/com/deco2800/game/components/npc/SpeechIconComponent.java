package com.deco2800.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Creates a speech bubble above an entity
 */
public class SpeechIconComponent extends RenderComponent {
    private static final float ANIMATION_RANGE = 0.03f;
    private static final float ANIMATION_PER_TICK = 0.003f;
    private float offsetY;
    private float minOffsetY;
    private float maxOffsetY;
    private boolean animationDescending;
    private Texture speechIconTexture;
    private Boolean visible;

    /**
     * Creates a speech bubble above an entity
     */
    public SpeechIconComponent(float offsetY) {
        speechIconTexture = ServiceLocator.getResourceService().getAsset("images/dialogue/raw/npc_indicator.png", Texture.class);;
        this.visible = true;
        this.offsetY = offsetY;
        this.maxOffsetY = offsetY;
        this.minOffsetY = offsetY - ANIMATION_RANGE;
        this.animationDescending = true;
    }

    /**
     * Turn the speech bubble off
     */
    public void displayOff() {
        visible = false;
    }

    /**
     * Adjusts the y offset to the next y offset in the animation. The animation causes the speech bubble to bob up
     * and down above the entity.
     */
    private void nextAnimationYOffset() {
        // calculates the ratio from the top and bottom of the range. This is used for animation tweening.
        float currentProgressRatioFromTop = (offsetY - minOffsetY)/(maxOffsetY - minOffsetY);
        float currentProgressRatioFromBottom = (maxOffsetY - offsetY)/(maxOffsetY - minOffsetY);

        // calculates the ratio to the closest edge of the animation range. This is used for animation tweening.
        float minimalProgressRatio = Math.min(currentProgressRatioFromTop, currentProgressRatioFromBottom);

        if (animationDescending) {
            if (offsetY > minOffsetY) {
                offsetY -= ANIMATION_PER_TICK * Math.max(minimalProgressRatio, 0.2);
            } else {
                animationDescending = false;
            }
        } else {
            if (offsetY < maxOffsetY) {
                offsetY += ANIMATION_PER_TICK * Math.max(minimalProgressRatio, 0.2);
            } else {
                animationDescending = true;
            }
        }
    }

    /**
     * Draws the speech bubble at the location and size relative to the entity
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (visible) {
            Vector2 npcPos = entity.getPosition();
            Vector2 npcScale = entity.getScale();

            nextAnimationYOffset();

            batch.draw(
                speechIconTexture,
                npcPos.x,
                npcPos.y + offsetY,
                npcScale.x/2,
                npcScale.y/2,
                npcScale.x,
                npcScale.y,
                1.0f,
                1.0f,
                0f,
                0,
                0,
                speechIconTexture.getWidth(),
                speechIconTexture.getHeight(),
                false,
                false);
        }
    }


}
