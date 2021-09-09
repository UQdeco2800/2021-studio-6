package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.PlayerCombatStatsComponent;
import com.deco2800.game.components.player.hud.PlayerHealthAnimationController;
import com.deco2800.game.components.player.hud.PlayerHudAnimationController;
import com.deco2800.game.components.player.hud.PlayerHudFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.IndependentAnimator;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  Table tableHealth;
  private Image heartImage;
  private Label woundLabel;
  private Label healthLabel;
  private Image healthBar;
  private IndependentAnimator dashAnimator;
  private IndependentAnimator healthAnimator;

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    healthAnimator =
        new IndependentAnimator(
            ServiceLocator.getResourceService()
                .getAsset("images/hud/health.atlas", TextureAtlas.class), 10, (float) -2.7, 3, 1);
    healthAnimator.addAnimation("health1", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health2", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health3", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health4", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health5", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health6", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health7", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health8", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health9", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health10", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health11", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health12", 0.1f, Animation.PlayMode.NORMAL);
    healthAnimator.addAnimation("health13", 0.1f, Animation.PlayMode.NORMAL);

    dashAnimator =
        new IndependentAnimator(
            ServiceLocator.getResourceService()
                .getAsset("images/hud/dashbar.atlas", TextureAtlas.class), 10, (float) -2, 3, (float) 0.5);
    dashAnimator.addAnimation("dashbar", 0.04f, Animation.PlayMode.NORMAL);
    dashAnimator.addAnimation("dashbarFull", 0.1f, Animation.PlayMode.NORMAL);


    entity.getEvents().addListener("updateWound", this::updatePlayerWoundUI);
    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
    entity.getEvents().addListener("firstHit", this::removeHealth);
    addActors();
    setAnimations();
  }

  public void setAnimations() {
    PlayerHudAnimationController setHud = this.entity.getComponent(PlayerHudAnimationController.class);
    PlayerHealthAnimationController setHealth = this.entity.getComponent(PlayerHealthAnimationController.class);
    setHud.setter();
    setHealth.setter();
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f);

    tableHealth = new Table();
    tableHealth.top().left();
    tableHealth.setFillParent(true);
    tableHealth.padTop(150f);

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));


    healthBar = new Image(ServiceLocator.getResourceService().getAsset
        ("images/hud/healthFull.png", Texture.class));

    // Health text
    int wounds = entity.getComponent(PlayerCombatStatsComponent.class).getWoundState();
    int health = entity.getComponent(PlayerCombatStatsComponent.class).getHealth();
    CharSequence woundText = String.format("Wounds: %d", wounds);
    CharSequence healthText = String.format("Health: %d", health);
    woundLabel = new Label(woundText, skin, "large");
    healthLabel = new Label(healthText, skin, "large");

    table.add(healthLabel);
    table.add(heartImage).size(heartSideLength).pad(6);
    table.add(woundLabel);
    table.padLeft(6f);

    tableHealth.add(healthBar).size(110f, 32f);
    stage.addActor(table);
    stage.addActor(tableHealth);
  }


  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's wound state on the ui.
   * @param wound player wound state
   */
  public void updatePlayerWoundUI(int wound) {
    CharSequence text = String.format("Wounds: %d", wound);
    woundLabel.setText(text);
  }

  /**
   * Updates the player's state health on the ui.
   * @param health player state health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
  }

  /**
   * Gets the animator for the dash interface
   * @return IndependentAnimator for the dash
   */
  public IndependentAnimator getDashAnimator() {
    return dashAnimator;
  }

  /**
   * Gets the animator for the health interface
   * @return IndependentAnimator for the health bar
   */
  public IndependentAnimator getHealthAnimator() {
    return healthAnimator;
  }

  /**
   * Temp function to remove original health bar
   */
  void removeHealth() {
    if (healthBar != null) {
      healthBar.remove();
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    healthBar.remove();
    tableHealth.remove();
    table.remove();
  }
}
