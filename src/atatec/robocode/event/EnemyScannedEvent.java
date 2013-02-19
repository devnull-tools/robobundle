package atatec.robocode.event;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.ScannedEnemy;
import robocode.ScannedRobotEvent;

/** @author Marcelo Varella Barca Guimarães */
public class EnemyScannedEvent {

  private final Enemy enemy;

  public EnemyScannedEvent(Bot bot, ScannedRobotEvent event) {
    this.enemy = new ScannedEnemy(bot, event);
  }

  public Enemy enemy() {
    return enemy;
  }

}
