package atatec.robocode.robots;

import atatec.robocode.AbstractBot;

import java.awt.Color;

import static atatec.robocode.conditions.Conditions.enemyIsAtMost;
import static atatec.robocode.conditions.Conditions.enemyIsAtRange;
import static atatec.robocode.conditions.Conditions.enemyIsMoving;

/** @author Marcelo Varella Barca Guimarães */
public class Chronos extends AbstractBot {

  protected void configure() {
    setBodyColor(new Color(39, 40, 34));
    setGunColor(new Color(54, 151, 255));
    setRadarColor(new Color(39, 40, 34));

    moveAllPartsSeparated();

    gun().aimingBehaviour()
      .use(predictionAiming())
      .when(enemyIsMoving())

      .use(directAiming())
      .inOtherCases();

    gun().firingBehaviour()
      .use(energyBasedFiring());

    radar().scanningBehaviour()
      .use(enemyLockScanning());

    body().movingBehaviour()
      .use(circleEnemyMoving())
      .when(enemyIsAtRange(100, 400))

      .use(fleeEnemyMoving())
      .when(enemyIsAtMost(100))

      .use(followEnemyMoving())
      .inOtherCases();

    behaveAs(dodger());
    behaveAs(
      bulletPainter()
        .use(new Color(255, 84, 84)).forStrong()
        .use(new Color(253, 151, 31)).forMedium()
        .use(new Color(54, 151, 255)).forWeak()
    );
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
