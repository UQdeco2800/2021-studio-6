package com.deco2800.game.components.story;


import com.deco2800.game.components.Component;
import com.deco2800.game.components.story.stories.TestCutscene;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.EnumMap;

public class StoryManager extends Component {

    private static StoryManager manager = null;

    private final Logger logger = LoggerFactory.getLogger(StoryManager.class);
    private final EnumMap<StoryNames, CutSceneConfig> scenesConfigs = new EnumMap<>(StoryNames.class);

    public static final String ADVANCE_LISTENER = "advanceStory";
    private boolean displaying = false;

    private StoryBase loadedStory;
    private int storyIndex;

    private StoryManager(){
        scenesConfigs.put(StoryNames.TEST, new TestCutscene());
    }

    @Override
    public void create() {
        super.create();
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
        displaying = true;
        return loadedStory;
    }

    public boolean isDisplaying(){
        return displaying;
    }

    public void advance() {
        loadedStory.advance();
    }

    public static StoryManager getInstance(){
        if (manager == null) {
            manager = new StoryManager();
        }
        return manager;
    }

}
