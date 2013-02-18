package atatec.robocode.robots;

import atatec.robocode.AbstractBot;
import atatec.robocode.behaviour.BulletPainter;
import atatec.robocode.behaviour.Dodger;
import atatec.robocode.parts.aiming.DirectAimingSystem;
import atatec.robocode.parts.aiming.PredictionAimingSystem;
import atatec.robocode.parts.firing.EnergyBasedFiringSystem;
import atatec.robocode.parts.movement.EnemyCircleMovingSystem;
import atatec.robocode.parts.movement.FleeEnemyMovingSystem;
import atatec.robocode.parts.movement.FollowEnemyMovingSystem;
import atatec.robocode.parts.scanner.EnemyLockScanningSystem;

import java.awt.Color;

import static atatec.robocode.condition.Conditions.enemyIsAtMost;
import static atatec.robocode.condition.Conditions.enemyIsAtRange;
import static atatec.robocode.condition.Conditions.enemyIsMoving;

/** @author Marcelo Varella Barca Guimarães */
public class Chronos extends AbstractBot {

  protected void configure() {
    body().setColor(new Color(39, 40, 34));
    gun().setColor(new Color(54, 151, 255));
    radar().setColor(new Color(39, 40, 34));

    moveAllPartsSeparated();

    gun().aimingBehaviour()
      .use(new PredictionAimingSystem(this))
      .when(enemyIsMoving());

    gun().aimingBehaviour()
      .use(new DirectAimingSystem(this))
      .inOtherCases();

    gun().firingBehaviour()
      .use(new EnergyBasedFiringSystem(this));

    radar().scanningBehaviour()
      .use(new EnemyLockScanningSystem(this));

    body().movingBehaviour()
      .use(new EnemyCircleMovingSystem(this))
      .when(enemyIsAtRange(100, 400));

    body().movingBehaviour()
      .use(new FleeEnemyMovingSystem(this))
      .when(enemyIsAtMost(100));

    body().movingBehaviour()
      .use(new FollowEnemyMovingSystem(this))
      .inOtherCases();

    behaveAs(new Dodger(this));
    behaveAs(new BulletPainter(this)
      .use(new Color(255, 84, 84)).forStrong()
      .use(new Color(253, 151, 31)).forMedium()
      .use(new Color(54, 151, 255)).forWeak());
  }

  protected void battle() {
    while (true) {
      log("***********************************");
      radar().scan();
      body().move();
      gun().aim();
      if (radar().lockedTarget() != null) {
        gun().fire();
      }
      execute();
    }
  }

}
