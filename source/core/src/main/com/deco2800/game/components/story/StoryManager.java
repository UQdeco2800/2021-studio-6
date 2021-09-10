package com.deco2800.game.components.story;


import com.deco2800.game.components.Component;
import com.deco2800.game.components.story.stories.TestCutscene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.EnumMap;

/**
 * Singleton component that manages the loading and display of story elements
 */
public class StoryManager extends Component {

    private static StoryManager manager = null;

    private final Logger logger = LoggerFactory.getLogger(StoryManager.class);
    private final EnumMap<StoryNames, CutSceneConfig> scenesConfigs = new EnumMap<>(StoryNames.class);

    public static final String ADVANCE_LISTENER = "advanceStory";
    private boolean displaying = false;

    private StoryBase loadedStory;

    private StoryManager(){
        scenesConfigs.put(StoryNames.TEST, new TestCutscene());
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener(ADVANCE_LISTENER, this::advance);
    }

    /**
     * Loads a cutscene in preparation for display
     * @param name cutscene id
     */
    public void loadCutScene(StoryNames name){
        CutSceneConfig config = scenesConfigs.get(StoryNames.TEST);
        CutScene cutScene = new CutScene(config);
        cutScene.create();
        loadedStory = cutScene;
    }

    /**
     * Displays a story that is loaded
     * @return null if no story is loaded, else the displayed story
     */
    public StoryBase displayStory() {

        if (!isLoaded()) {
            return null;
        }

        loadedStory.display();
        displaying = true;
        return loadedStory;
    }

    /**
     * If a story is being displayed
     * @return if the story is displayed
     */
    public boolean isDisplaying(){
        return displaying;
    }

    /**
     * Advances the story by one.
     */
    public void advance() {

        if (!isLoaded()) {
            return;
        }

        if (!loadedStory.isDead()) {
            loadedStory.advance();
        } else {
            disposeLoadedStory();
        }
    }

    /**
     * Disposes a loaded story
     */
    public void disposeLoadedStory() {
        loadedStory.dispose();
        loadedStory = null;
        displaying = false;
    }

    /**
     * Returns the singleton instance
     * @return the StoryManager instance
     */
    public static StoryManager getInstance(){
        if (manager == null) {
            manager = new StoryManager();
        }
        return manager;
    }

    /**
     * Returns if a story is loaded or not
     * @return story is loaded or not
     */
    private boolean isLoaded(){
        if (loadedStory == null) {
            logger.error("No story loaded");
            return false;
        } else {
            return true;
        }
    }

}
