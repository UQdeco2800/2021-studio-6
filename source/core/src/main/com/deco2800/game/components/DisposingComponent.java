package com.deco2800.game.components;

import com.deco2800.game.services.ServiceLocator;

/**
 * Used to manage entities that are required to be updated or diposed of outside of time step
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
}
