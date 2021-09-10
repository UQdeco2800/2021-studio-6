package com.deco2800.game.components.story;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.components.dialoguebox.Dialogue;

import java.util.List;

public abstract class CutSceneConfig {
    public Dialogue dialogue;
    public Music music;
    public List<Image> images;

    protected abstract void create();
}
