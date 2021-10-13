package com.deco2800.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Creates a glowing red eye image for the entity
 */
public class GlowingEyesComponent extends RenderComponent {
    private final Texture glowingEyesTexture;
    private Boolean visible;
    private CombatStatsComponent combatStatsComponent;

    /**
     * Creates a glowing red eye image for the entity
     */
    public GlowingEyesComponent(String imageFileName) {
        glowingEyesTexture = ServiceLocator.getResourceService().getAsset(imageFileName, Texture.class);
        this.visible = true;
    }

    /**
     * Links the combat stats component to detect whether the enemy is dead
     */
    public void initialise() {
        combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
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
        if (visible && combatStatsComponent != null && !combatStatsComponent.isDead()) {
            Vector2 npcPos = entity.getPosition();
            Vector2 npcScale = entity.getScale();

            batch.draw(
                glowingEyesTexture,
                npcPos.x,
                npcPos.y,
                npcScale.x/2,
                npcScale.y/2,
                npcScale.x,
                npcScale.y,
                1.0f,
                1.0f,
                0f,
                0,
                0,
                glowingEyesTexture.getWidth(),
                glowingEyesTexture.getHeight(),
                false,
                false);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        glowingEyesTexture.dispose();
    }
}
