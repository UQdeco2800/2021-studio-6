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
    private static final float ANIMATION_PER_TICK = 0.001f;
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

    private void nextAnimationYOffset() {
        if (animationDescending) {
            if (offsetY > minOffsetY) {
                offsetY -= ANIMATION_PER_TICK;
            } else {
                animationDescending = false;
            }
        } else {
            if (offsetY < maxOffsetY) {
                offsetY += ANIMATION_PER_TICK;
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
