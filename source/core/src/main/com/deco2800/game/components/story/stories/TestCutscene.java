package com.deco2800.game.components.story.stories;
import com.deco2800.game.components.story.CutSceneConfig;

public class TestCutscene extends CutSceneConfig {
    public static final String[] dialogueTextRaw = {"Hello World!", "This is me speaking", "I love the world"};
    public static final String[] imagePathsRaw = {"title-screen/0001.png", "title-screen/0021.png"};
    public static final String musicPathRaw = "sounds/title-screen-music.mp3";

    public TestCutscene(){
        dialogueText = dialogueTextRaw;
        imagePaths = imagePathsRaw;
        musicPath = musicPathRaw;
    }
}
