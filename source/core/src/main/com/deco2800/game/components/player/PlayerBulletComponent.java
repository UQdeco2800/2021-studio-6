package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.physics.PhysicsLayer;


/**
 * Equips player's bullet with components that allows bullet to make an
 * impact in game area
 */
public class PlayerBulletComponent extends Component {

    short targetLayer = PhysicsLayer.NPC;
}
