package com.deco2800.game.components.player;


import com.badlogic.gdx.graphics.Color;
import com.deco2800.game.lighting.PointLightComponent;


public class PlayerLightingComponent extends PointLightComponent {

    public PlayerLightingComponent (Color color, float distance, float offsetx, float offsety) {
        super(color, distance,offsetx,offsety);

    }

    @Override
    public void create() {
        super.create();

        entity.getEvents().addListener("toggleTorch", this::TorchState);
    }

    private void TorchState() {

    }
}
