package com.deco2800.game.components;

import com.deco2800.game.lighting.Lighting;
import com.deco2800.game.services.ServiceLocator;


public class DarknessDetectionComponent extends Component{

    // this is the amount of light that needs to be around the entity to be in light. Higher needs more light.
    public static final String EVENT_IN_LIGHT = "inLight";
    public static final String EVENT_NOT_LIGHT = "inShadow";

    private Lighting lightingService = ServiceLocator.getLightingService();
    private boolean isInLight = false;

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
        boolean prevIsInLight = isInLight;

        isInLight = lightingService.getRayHandler().pointAtLight(entityPosX, entityPosY);

        if (prevIsInLight != isInLight) {
            if (isInLight) {
                entity.getEvents().trigger(EVENT_IN_LIGHT);
            } else {
                entity.getEvents().trigger(EVENT_NOT_LIGHT);
            }
        }
    }

    public boolean isInLight(){
        return isInLight;
    }
}
