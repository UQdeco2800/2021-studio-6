package com.deco2800.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.assets.AssetManager;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class VolumeTest {

    @Test
    void setSfxVolume() {
        AssetManager assetManager = spy(AssetManager.class);
        ResourceService resourceService = new ResourceService(assetManager);

        resourceService.setSfxVolume(0.2f);
        assertEquals(0.2f, resourceService.getSfxVolume());
    }

    @Test
    void setMusicVolume() {
        AssetManager assetManager = spy(AssetManager.class);
        ResourceService resourceService = new ResourceService(assetManager);

        resourceService.setMusicVolume(0.2f);
        assertEquals(0.2f, resourceService.getMusicVolume());
    }
}
