package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;

/**
 * Task that forces the NPC to do nothing as the entity is dead
 */
public class DeadTask extends DefaultTask implements PriorityTask {
  @Override
  public int getPriority() {
    return 100;
  }
}
