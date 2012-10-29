package atatec.robocode.robots;

import atatec.robocode.AbstractBot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.event.EnemyFireEvent;
import atatec.robocode.event.Events;
import atatec.robocode.util.GravityPointBuilder;
import robocode.HitRobotEvent;

import java.awt.Color;

import static atatec.robocode.conditions.Conditions.headToHeadBattle;
import static atatec.robocode.util.GravityPointBuilder.antiGravityPoint;

/** @author Marcelo Varella Barca Guimarães */
public class Nexus extends AbstractBot {

  protected void configure() {
    setBodyColor(Color.GRAY);
    setGunColor(Color.LIGHT_GRAY);
    setRadarColor(Color.BLACK);

    moveAllPartsSeparated();

    gun().aimingBehaviour()
      .use(predictionAiming());

    gun().firingBehaviour()
      .use(
        energyBasedFiring()
          .fireMaxAt(80)
          .fireMinAt(30)
      );

    radar().scanningBehaviour()
      .use(enemyLockScanning())
      .when(headToHeadBattle())

      .use(
        enemyLockScanning()
          .scanBattleField()
      ).inOtherCases();

    body().movingBehaviour()
      .use(gravityMoving());


    behaveAs(dodger());
    behaveAs(
      bulletPainter()
        .use(Color.RED).forStrong()
        .use(Color.ORANGE).forMedium()
        .use(Color.BLACK).forWeak()
    );
  }

  @When(Events.HIT_BY_BULLET)
  public void hitByBullet() {
    events().send(Events.ADD_TEMPORARY_GRAVITY_POINT,
      GravityPointBuilder
        .antiGravityPoint()
        .at(location())
        .withValue(10)
        .during(5)
    );
  }

  @When(Events.ENEMY_FIRE)
  public void enemyFire(EnemyFireEvent event) {
    Enemy enemy = event.enemy();
    log("Enemy %s probably fired a bullet at %s. Adding anti-gravity point.",
      enemy.name(), enemy.position());
    events().send(Events.ADD_TEMPORARY_GRAVITY_POINT,
      antiGravityPoint()
        .at(enemy.location())
        .withValue(65)
        .during(20)
    );
    events().send(Events.ADD_TEMPORARY_GRAVITY_POINT,
      antiGravityPoint()
        .at(location())
        .withValue(20)
        .during(20));
  }

  @When(Events.HIT_ROBOT)
  public void hitHobot(HitRobotEvent event) {
    log("Adding anti-gravity points to avoid %s", event.getName());
    events().send(Events.ADD_TEMPORARY_GRAVITY_POINT,
      antiGravityPoint()
        .at(location())
        .withValue(30)
        .during(10)
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
