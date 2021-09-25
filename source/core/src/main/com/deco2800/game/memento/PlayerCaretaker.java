package com.deco2800.game.memento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the caretaker. It is used to enable originator (Player.java) to save and/or restore itself if required. It
 * also maintains the history of all memento objects created. When a restoration of a specific memento is called, all
 * memento objects created after that specific memento in history will no longer exist. It essentially takes a snapshot
 * of all created originator.
 */
public class PlayerCaretaker {
    private static final Logger logger = LoggerFactory.getLogger(PlayerCaretaker.class);
    protected Map<Integer, Map<String, PlayerMemento>> mementoHistory =
            new HashMap<Integer, Map<String, PlayerMemento>>();

    /**
     * Adds memento into a map that is linked to unique ids that are linked to another map of unique meesage
     * strings which are uniquely linked to specific history of all memento objects created
     * @param id uniquely linked to unique strings which will be linked to a list of all memento objects created
     *           previously
     * @param mementoMessage unique strings which are used to extract related history list of memento objects
     * @param memento object that holds internal state of originator
     */
    public void addMemento(int id, String mementoMessage, PlayerMemento memento) {
        if (mementoMessage != null && mementoMessage.trim().length() != 0 && memento != null) {

            Map<String, PlayerMemento> mementoMessageMap = mementoHistory.get(id);
            if (mementoMessageMap == null) {
                mementoMessageMap = new HashMap<String, PlayerMemento>();
                mementoHistory.put(id, mementoMessageMap);
            }

            mementoMessageMap.put(mementoMessage, memento);
            logger.info("Player ID = " + memento.getId() +  " stored with message = " + mementoMessage + ".\n");
        }
    }

    /**
     *
     * @param id which is unique that is used to retrieve relevant originator object
     * @param mementoMessage which is also unique to retrieve relevant history list of memento objects created
     * @return specific past memento object created in the past
     */
    public PlayerMemento getMemento(int id, String mementoMessage) {
        PlayerMemento memento = null;
        if (mementoMessage != null && mementoMessage.trim().length() != 0) {
            Map<String, PlayerMemento> mementoMessageMap = mementoHistory.get(id);

            if (mementoMessageMap != null) {
                memento = mementoMessageMap.get(mementoMessage);

                if (memento != null) {
                    logger.info("Player ID = " + memento.getId() +  " restored with message = " + mementoMessage + ".\n");
                } else {
                    logger.info("Not able ot find memento");
                }
            }
        }
        return memento;
    }
}
