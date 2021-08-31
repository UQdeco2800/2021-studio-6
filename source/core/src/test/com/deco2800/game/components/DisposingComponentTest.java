package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.World;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class DisposingComponentTest {
    @Mock GameTime gameTime;
    @Mock World world;

    @Test
    void shouldBeAddedForDisposalOrRemoval() {
        Entity entity = new Entity();
        entity.addComponent(new DisposingComponent());

    }
}
