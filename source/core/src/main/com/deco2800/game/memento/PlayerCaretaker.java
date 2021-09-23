package com.deco2800.game.memento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PlayerCaretaker {
    private static final Logger logger = LoggerFactory.getLogger(PlayerCaretaker.class);
    protected Map<Integer, Map<String, PlayerMemento>> mementoHistory =
            new HashMap<Integer, Map<String, PlayerMemento>>();

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
