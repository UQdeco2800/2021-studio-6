package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.lighting.Lighting;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DarknessDetectionComponent extends Component{

    private static final Logger logger = LoggerFactory.getLogger(DarknessDetectionComponent.class);
    // this is the amount of light that needs to be around the entity to be in light. Higher needs more light.
    public static final String EVENT_IN_LIGHT = "inLight";
    public static final String EVENT_NOT_LIGHT = "inShadow";

    Lighting lightingService = ServiceLocator.getLightingService();
    private boolean isInLight = false;
    private boolean prevIsInLight = false;

    /**
     * Called when the entity is created and registered. Initial logic such as calls to GetComponent
     * should be made here, not in the constructor which is called before an entity is finished.
     */
    @Override
    public void create() {
        super.create();
    }

    /**
     * Called once per frame of the game, and should be used for most component logic. Not called if
     * component is disabled.
     */
    @Override
    public void update() {
        super.update();

        // Minor offset is added otherwise it considers player in light when standing still
        float entityPosX = entity.getCenterPosition().x + 0.01f;
        float entityPosY = entity.getCenterPosition().y + 0.01f;
        prevIsInLight = isInLight;

        isInLight = lightingService.getRayHandler().pointAtLight(entityPosX, entityPosY);

        if (prevIsInLight != isInLight) {
            if (isInLight) {
                entity.getEvents().trigger(EVENT_IN_LIGHT);
            } else {
                entity.getEvents().trigger(EVENT_NOT_LIGHT);
            }
        }
    }

    /**
     * Called when the component is disposed. Dispose of any internal resources here.
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    public boolean isInLight(){
        return isInLight;
    }
}
