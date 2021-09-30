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
    private float offsety;
    private Texture speechIconTexture;
    private Boolean visible;

    /**
     * Creates a speech bubble above an entity
     */
    public SpeechIconComponent(float offsety) {
        speechIconTexture = ServiceLocator.getResourceService().getAsset("images/dialogue/raw/npc_indicator.png", Texture.class);;
        this.visible = true;
        this.offsety = offsety;
    }

    /**
     * Turn the speech bubble off
     */
    public void displayOff() {
        visible = false;
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

            batch.draw(
                speechIconTexture,
                npcPos.x,
                npcPos.y + offsety,
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
