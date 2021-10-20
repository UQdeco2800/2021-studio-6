package com.deco2800.game.physics;

public class PhysicsLayer {
  public static final short NONE = 0;
  public static final short DEFAULT = (1 << 0);
  public static final short PLAYER = (1 << 1);
  // Terrain obstacle, e.g. trees
  public static final short OBSTACLE = (1 << 2);

  // For invisible wall around map
  public static final short WALL = (1 << 9);

  // NPC (Non-Playable Character) colliders
  public static final short NPC = (1 << 3);
  public static final short ALL = ~0;
  public static final short WEAPON = (1 << 5);
  public static final short ITEM = (1 << 6);

  // Safehouse object
  public static final short SAFEHOUSE = (1 << 4);
  // Paraphernalia
  public static final short PARAPHERNALIA = (1 << 4);

  public static final short ENEMYBULLET = (1 << 7);

  //Friendly NPC
  public static final short FRIENDLY_NPC = (1 << 8);

  public static boolean contains(short filterBits, short layer) {
    return (filterBits & layer) != 0;
  }

  private PhysicsLayer() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
