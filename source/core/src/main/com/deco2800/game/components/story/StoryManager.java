package com.deco2800.game.components.story;


import com.badlogic.gdx.utils.Logger;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.story.stories.TestCutscene;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.LoggerFactory;


import java.util.EnumMap;

public class StoryManager extends Component {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(StoryManager.class);
    private final EnumMap<StoryNames, CutSceneConfig> scenesConfigs;

    public static final String ADVANCE_LISTENER = "advanceStory";

    private static StoryBase loadedStory;

    public StoryManager(){
        scenesConfigs = new EnumMap<>(StoryNames.class);

        scenesConfigs.put(StoryNames.TEST, new TestCutscene());
        entity.getEvents().addListener(ADVANCE_LISTENER, this::advance);
    }

    public void loadCutScene(StoryNames name){
        CutSceneConfig config = scenesConfigs.get(StoryNames.TEST);
        CutScene cutScene = new CutScene(config);
        cutScene.create();
        loadedStory = cutScene;
    }

    public StoryBase displayStory() {
        if (loadedStory == null) {
            logger.error("No story loaded");
            return null;
        }
        loadedStory.display();
        return loadedStory;
    }

    public void advance() {
        loadedStory.advance();
    }

}
