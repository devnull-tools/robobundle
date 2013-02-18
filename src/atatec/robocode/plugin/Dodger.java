package atatec.robocode.plugin;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.event.EnemyFireEvent;
import atatec.robocode.event.EnemyScannedEvent;
import atatec.robocode.event.Events;
import robocode.BulletHitEvent;

/** @author Marcelo Varella Barca Guimarães */
public class Dodger {

  private final Bot bot;

  private Enemy lastEnemy;

  public Dodger(Bot bot) {
    this.bot = bot;
  }

  @When(Events.ENEMY_SCANNED)
  public void onEnemyScanned(EnemyScannedEvent event) {
    Enemy enemy = event.enemy();
    if (lastEnemy != null && lastEnemy.name().equals(enemy.name())) {
      if (lastEnemy.energy() > enemy.energy()) { //assumes a bullet fired
        bot.events().send(Events.ENEMY_FIRE, new EnemyFireEvent(enemy));
      }
    }
    lastEnemy = enemy;
  }

  @When(Events.BULLET_HIT)
  public void onBulletHit(BulletHitEvent event) {
    // removes the last enemy registry because if the bullet hits the target, its energy
    // will drop a bit and may cause a wrong interpretation of a bullet being fired
    if(lastEnemy != null && event.getName().equals(lastEnemy.name())) {
      lastEnemy = null;
    }
  }

}
