package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.components.tasks.FinalBossFireLaser.FinalBossFireLaser;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.FinalBossFactory;
import com.deco2800.game.entities.factories.ItemFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;

public class SpawnerSpawner extends DefaultTask implements Task {
    private Entity target;
    private long endTimeTask;
    private GridPoint2 bounds;

    public SpawnerSpawner(Entity target, GridPoint2 bounds) {
        this.bounds = new GridPoint2(bounds.x, bounds.y - 27);
        this.target = target;
    }
    @Override
    public void start(){
        endTimeTask = ServiceLocator.getTimeSource().getTime() + 2000;
    }

    @Override
    public void update() {
        if(ServiceLocator.getTimeSource().getTime() >= endTimeTask) {
            System.out.println(bounds);
            GridPoint2 randomPos = RandomUtils.random(new GridPoint2(0, 0), bounds);
            ServiceLocator.getGameArea().spawnEntityAt(FinalBossFactory.createLightSpawner(target, ServiceLocator.getGameArea()), randomPos, false, false);
            endTimeTask = ServiceLocator.getTimeSource().getTime() + 100;
        }
    }



}
