package com.deco2800.game.components.story;


import com.deco2800.game.components.Component;
import com.deco2800.game.components.story.stories.*;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.EnumMap;

/**
 * Singleton component that manages the loading and display of story elements
 */
public class StoryManager extends Component {

    private static StoryManager manager = null;

    private final Logger logger = LoggerFactory.getLogger(StoryManager.class);
    private final EnumMap<StoryNames, StoryConfig> scenesConfigs = new EnumMap<>(StoryNames.class);

    public static final String ADVANCE_LISTENER = "advanceStory";
    private boolean displaying = false;
    private boolean prevPauseState = false;

    private StoryBase loadedStory;
    private StoryNames loadedStoryName = StoryNames.NONE;

    private StoryManager(){
        scenesConfigs.put(StoryNames.TEST, new TestCutscene());
        scenesConfigs.put(StoryNames.INTRO_DIALOGUE, new IntroDialogue());
        scenesConfigs.put(StoryNames.PROLOGUE, new PrologueCutScene());
        scenesConfigs.put(StoryNames.EPILOGUE, new EpilogueCutScene());
        scenesConfigs.put(StoryNames.TOWN_GUIDE, new TownGuideDialogue());
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

        StoryConfig config = scenesConfigs.get(name);

        if (config == null) {
            logger.error("Story does not exist");
            return;
        }

        loadedStoryName = name;
        loadedStory = config.initialiseStory();
        loadedStory.create();
    }

    /**
     * Displays a story that is loaded and pauses the game
     * @return null if no story is loaded, else the displayed story
     */
    public StoryBase displayStory() {

        if (!isLoaded()) {
            return null;
        }

        try{
            loadedStory.display();
        } catch (Exception ignored) {
            logger.error("Story has not been created");
        }

        prevPauseState = ServiceLocator.getTimeSource().isPaused();
        ServiceLocator.getTimeSource().pause();

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

        if (!loadedStory.advance()) {
            disposeLoadedStory();
        }
    }

    /**
     * Disposes a loaded story and returns to the previous pause state
     */
    public void disposeLoadedStory() {
        loadedStory.dispose();
        loadedStory = null;
        displaying = false;

        if (!prevPauseState) {
            ServiceLocator.getTimeSource().unpause();
        }
        entity.getEvents().trigger("story-finished:" + loadedStoryName);
        loadedStoryName = StoryNames.NONE;
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
     * The map of story configs
     * @return the map of stories
     */
    public EnumMap<StoryNames, StoryConfig> getScenesConfigs() {
        return scenesConfigs;
    }


    /**
     * The loaded story
     * @return the loaded story
     */
    public StoryBase getLoadedStory() {
        return loadedStory;
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
