package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BulletFactory;

public class BulletFire extends Component {
    private Entity target;

    public void BulletFire(Entity target) {
        this.target = target;
    }
    public void fire() {
        BulletFactory.createBullet(this.entity, target);
    }


}
