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
    private final Texture glowingEyesTextureLeft;
    private final Texture glowingEyesTextureRight;
    private final Texture glowingEyesTextureFront;
    private final Texture glowingEyesTextureBack;
    private Texture glowingEyesTextureCurrent;
    private Boolean visible;
    private CombatStatsComponent combatStatsComponent;

    /**
     * Creates a glowing red eye image for the entity
     */
    public GlowingEyesComponent(String imageFileNameLeft, String imageFileNameRight, String imageFileNameFront, String imageFileNameBack) {
        glowingEyesTextureLeft = ServiceLocator.getResourceService().getAsset(imageFileNameLeft, Texture.class);
        glowingEyesTextureRight = ServiceLocator.getResourceService().getAsset(imageFileNameRight, Texture.class);
        glowingEyesTextureFront = ServiceLocator.getResourceService().getAsset(imageFileNameFront, Texture.class);
        glowingEyesTextureBack = ServiceLocator.getResourceService().getAsset(imageFileNameBack, Texture.class);
        glowingEyesTextureCurrent = glowingEyesTextureFront;
        this.visible = true;
    }

    @Override
    public void create() {
        super.create();
        this.entity.getEvents().addListener("left", this::facingLeft);
        this.entity.getEvents().addListener("right", this::facingRight);
        this.entity.getEvents().addListener("front", this::facingFront);
        this.entity.getEvents().addListener("back", this::facingBack);
    }

    /**
     * Links the combat stats component to detect whether the enemy is dead
     */
    public void initialise() {
        combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
    }

    private void facingRight() {
        glowingEyesTextureCurrent = glowingEyesTextureRight;
    }

    private void facingLeft() {
        glowingEyesTextureCurrent = glowingEyesTextureLeft;
    }

    private void facingFront() {
        glowingEyesTextureCurrent = glowingEyesTextureFront;
    }

    private void facingBack() {
        glowingEyesTextureCurrent = glowingEyesTextureBack;
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
                glowingEyesTextureCurrent,
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
                glowingEyesTextureCurrent.getWidth(),
                glowingEyesTextureCurrent.getHeight(),
                false,
                false);
        }
    }

    /**
     * Adds the texture to be disposed
     */
    @Override
    public void dispose() {
        super.dispose();
        glowingEyesTextureLeft.dispose();
        glowingEyesTextureRight.dispose();
        glowingEyesTextureFront.dispose();
        glowingEyesTextureBack.dispose();
    }
}
