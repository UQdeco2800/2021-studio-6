package com.deco2800.game.components.story;


import com.deco2800.game.components.story.stories.TestCutscene;

import java.util.EnumMap;

public class StoryManager {

    private final EnumMap<StoryNames, CutSceneConfig> scenesConfigs;

    private static StoryBase loadedStory;

    public StoryManager(){
        scenesConfigs = new EnumMap<>(StoryNames.class);

        scenesConfigs.put(StoryNames.TEST, new TestCutscene());
    }

    public void loadCutScene(StoryNames name){
        CutSceneConfig config = scenesConfigs.get(StoryNames.TEST);
        CutScene cutScene = new CutScene(config);
        cutScene.create();
        loadedStory = cutScene;
    }

}
