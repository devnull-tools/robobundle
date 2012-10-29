package atatec.robocode.parts.scanner;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.event.EnemyScannedEvent;
import atatec.robocode.event.Events;
import atatec.robocode.parts.ScanningSystem;
import robocode.RobotDeathEvent;

/** @author Marcelo Varella Barca Guimarães */
public class EnemyLockScanningSystem implements ScanningSystem {

  private final Bot bot;

  private Angle turnAmount = Angle.TWO_PI;
  private boolean changeTarget = true;
  private boolean scanBattleField;
  private boolean lockClosestEnemy;

  public EnemyLockScanningSystem(Bot bot) {
    this.bot = bot;
  }

  @Override
  public void scan() {
    bot.radar().turn(turnAmount);
  }

  public EnemyLockScanningSystem scanBattleField() {
    scanBattleField = true;
    return this;
  }

  public EnemyLockScanningSystem lockClosestEnemy() {
    lockClosestEnemy = true;
    return this;
  }

  @When(Events.ENEMY_SCANNED)
  public void onEnemyScanned(EnemyScannedEvent event) {
    Enemy enemy = event.enemy();
    bot.log("Enemy spotted at %s", enemy.position());
    if (canLock(enemy)) {
      bot.log("Locking %s", enemy.name());
      bot.radar().lockTarget(enemy);
      changeTarget = false;
    }
    if (!scanBattleField) {
      turnAmount = turnAmount.inverse();
    }
  }

  @When(Events.ROBOT_DEATH)
  public void onRobotDeath(RobotDeathEvent event) {
    if (event.getName().equals(bot.radar().lockedTarget().name())) {
      changeTarget = true;
    }
  }

  @When(Events.CHANGE_TARGET)
  public void changeTarget() {
    changeTarget = true;
  }

  private boolean canLock(Enemy enemy) {
    Enemy lockedEnemy = bot.radar().lockedTarget();
    if (lockedEnemy == null) {
      return true;
    }
    if (changeTarget) {
      return true;
    }
    if (lockedEnemy.name().equals(enemy.name())) {
      return true;
    }
    if (lockClosestEnemy && lockedEnemy.distance() > enemy.distance()) {
      return true;
    }
    return false;
  }
}
