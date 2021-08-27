package com.deco2800.game.components;

import com.deco2800.game.services.ServiceLocator;

/**
 * Used to manage entities that are required to be updated or diposed of outside of physics time step
 */
public class DisposingComponent extends Component {

    public DisposingComponent() {
    }

    /**
     * Register entity to be disposed of in physics engine - outside of stepping function
     */
    public void toBeDisposed() {
        System.out.println(entity);
        ServiceLocator.getPhysicsService().getPhysics().addToDisposeQueue(entity);
    }

    /**
     * Register entity to be reused, so that entity does not need to be recreated repeatedly
     */
    public void toBeReused() {
        ServiceLocator.getPhysicsService().getPhysics().addToReuseQueue(entity);
    }
}
