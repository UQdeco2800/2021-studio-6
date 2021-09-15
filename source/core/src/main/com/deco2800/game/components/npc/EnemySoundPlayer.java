package com.deco2800.game.components.npc;

import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.Component;
import com.badlogic.gdx.audio.Sound;

public class EnemySoundPlayer extends Component {

    public static void playSound(String assetPath) {
        Sound sound = ServiceLocator.getResourceService().getAsset(assetPath, Sound.class);
        //sound.setVolume(sound.play(), 0.8f);
        sound.play();
    }
}
