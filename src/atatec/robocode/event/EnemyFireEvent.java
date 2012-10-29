package atatec.robocode.event;

import atatec.robocode.Enemy;

/** @author Marcelo Varella Barca Guimarães */
public class EnemyFireEvent {

  private final Enemy enemy;

  public EnemyFireEvent(Enemy enemy) {
    this.enemy = enemy;
  }

  public Enemy enemy() {
    return enemy;
  }

}
