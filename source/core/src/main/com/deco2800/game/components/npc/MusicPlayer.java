package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;
import com.badlogic.gdx.audio.Music;

public class MusicPlayer extends Component {

    public void playSound(String assetPath) {
        Music music = ServiceLocator.getResourceService().getAsset(assetPath, Music.class);
        music.setVolume(0f);
        music.play();
    }
}
