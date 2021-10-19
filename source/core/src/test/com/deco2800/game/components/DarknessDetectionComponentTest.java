package com.deco2800.game.components;

import com.badlogic.box2dlights.box2dLight.RayHandler;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.lighting.Lighting;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class DarknessDetectionComponentTest {

    Entity testBody;

    @Mock Lighting lighting;
    @Mock RayHandler rayHandler;


    @BeforeEach
    void setUp() {
        ServiceLocator.registerLightingService(lighting);
        Mockito.when(lighting.getRayHandler()).thenReturn(rayHandler);

        testBody = new Entity()
                .addComponent(new DarknessDetectionComponent());
        testBody.create();
    }

    @Test
    void trueIfInLight(){
        testBody.setPosition(0f, 0f);
        Mockito.when(rayHandler.pointAtLight(testBody.getCenterPosition().x + 0.01f,
                testBody.getCenterPosition().y + 0.01f)).thenReturn(true);
        testBody.update();
        assertTrue(testBody.getComponent(DarknessDetectionComponent.class).isInLight());
    }

    @Test
    void falseIfNotInLight(){
        testBody.setPosition(5f, 2f);
        Mockito.when(rayHandler.pointAtLight(testBody.getCenterPosition().x + 0.01f,
                testBody.getCenterPosition().y + 0.01f)).thenReturn(false);
        testBody.update();
        assertFalse(testBody.getComponent(DarknessDetectionComponent.class).isInLight());
    }


}