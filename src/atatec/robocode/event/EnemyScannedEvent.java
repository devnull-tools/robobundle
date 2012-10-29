package atatec.robocode.event;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import robocode.ScannedRobotEvent;

/** @author Marcelo Varella Barca Guimarães */
public class EnemyScannedEvent {

  private final Enemy enemy;

  public EnemyScannedEvent(Bot bot, ScannedRobotEvent event) {
    this.enemy = new Enemy(bot, event);
  }

  public Enemy enemy() {
    return enemy;
  }

}
