package atatec.robocode.robots;

import atatec.robocode.BaseBot;
import atatec.robocode.parts.aiming.DirectAimingSystem;
import atatec.robocode.parts.aiming.PredictionAimingSystem;
import atatec.robocode.parts.firing.EnergyBasedFiringSystem;
import atatec.robocode.parts.movement.EnemyCircleMovingSystem;
import atatec.robocode.parts.movement.FollowEnemyMovingSystem;
import atatec.robocode.parts.scanner.EnemyLockScanningSystem;
import atatec.robocode.plugin.BulletPaint;
import atatec.robocode.plugin.Dodger;
import atatec.robocode.plugin.EnemyScannerInfo;

import java.awt.Color;

import static atatec.robocode.condition.Conditions.enemyIsAtMost;
import static atatec.robocode.condition.Conditions.enemyIsMoving;

/** @author Marcelo Varella Barca Guimarães */
public class Chronos extends BaseBot {

  protected void configure() {
    body().setColor(new Color(39, 40, 34));
    gun().setColor(new Color(54, 151, 255));
    radar().setColor(new Color(39, 40, 34));

    independentMovement();

    gun().aimingBehaviour()
      .use(new PredictionAimingSystem(this))
      .when(enemyIsMoving())

      .use(new DirectAimingSystem(this))
      .inOtherCases();

    gun().firingBehaviour()
      .use(new EnergyBasedFiringSystem(this));

    radar().scanningSystem()
      .use(new EnemyLockScanningSystem(this));

    body().movingBehaviour()
      .use(new EnemyCircleMovingSystem(this))
      .when(enemyIsAtMost(400))

      .use(new FollowEnemyMovingSystem(this))
      .inOtherCases();

    plug(new Dodger(this));

    plug(new EnemyScannerInfo(this)
      .showAttributes());

    plug(new BulletPaint(this)
      .use(new Color(255, 84, 84)).forStrong()
      .use(new Color(253, 151, 31)).forMedium()
      .use(new Color(54, 151, 255)).forWeak());
  }

  protected void battle() {
    while (true) {
      log("***********************************");
      radar().scan();
      body().move();
      if (radar().hasLockedTarget()) {
        gun().fire();
      }
      execute();
    }
  }

}
