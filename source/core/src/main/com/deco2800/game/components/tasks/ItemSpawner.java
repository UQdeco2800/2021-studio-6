package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.entities.factories.ItemFactory;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;

public class ItemSpawner extends DefaultTask implements Task {

    private long endTimeTask;
    private GridPoint2 bounds;
    private int timeStop = 3000;

    public ItemSpawner(GridPoint2 bounds) {
        this.bounds = new GridPoint2(bounds.x, bounds.y - 27);
    }
    @Override
    public void start(){
        endTimeTask = ServiceLocator.getTimeSource().getTime() + timeStop;
    }

    @Override
    public void update() {
        if(ServiceLocator.getTimeSource().getTime() >= endTimeTask) {
            System.out.println(bounds);
            GridPoint2 randomPos = RandomUtils.random(new GridPoint2(0, 0), bounds);
            ServiceLocator.getGameArea().spawnEntityAt(ItemFactory.createAmmoPickup(1), randomPos, true, true);
            endTimeTask = ServiceLocator.getTimeSource().getTime() + timeStop;
        }
    }



}
