package atatec.robocode.robots;

import atatec.robocode.AbstractBot;
import atatec.robocode.Enemy;
import atatec.robocode.Field;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.GravityPoint;
import atatec.robocode.calc.Point;
import atatec.robocode.event.EnemyFireEvent;
import atatec.robocode.parts.aiming.PredictionAimingSystem;
import atatec.robocode.parts.firing.EnergyBasedFiringSystem;
import atatec.robocode.parts.movement.GravitationalMovingSystem;
import atatec.robocode.parts.scanner.EnemyLockScanningSystem;
import atatec.robocode.plugin.BulletPaint;
import atatec.robocode.plugin.Dodger;
import atatec.robocode.plugin.EnemyScannerInfo;
import atatec.robocode.util.GravityPointBuilder;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import static atatec.robocode.condition.Conditions.headToHeadBattle;
import static atatec.robocode.event.Events.ADD_GRAVITY_POINT;
import static atatec.robocode.event.Events.ENEMY_FIRE;
import static atatec.robocode.event.Events.HIT_BY_BULLET;
import static atatec.robocode.event.Events.HIT_ROBOT;
import static atatec.robocode.event.Events.HIT_WALL;
import static atatec.robocode.event.Events.ROUND_STARTED;
import static atatec.robocode.util.GravityPointBuilder.antiGravityPoint;
import static atatec.robocode.util.GravityPointBuilder.gravityPoint;

/** @author Marcelo Varella Barca Guimarães */
public class Nexus extends AbstractBot {

  protected void configure() {
    body().setColor(new Color(39, 40, 34));
    gun().setColor(new Color(166, 226, 46));
    radar().setColor(new Color(39, 40, 34));

    independentMovement();

    gun().aimingBehaviour()
      .use(new PredictionAimingSystem(this));

    gun().firingBehaviour()
      .use(new EnergyBasedFiringSystem(this)
        .fireMaxAt(80)
        .fireMinAt(30));

    radar().scanningBehaviour()
      .use(new EnemyLockScanningSystem(this))
      .when(headToHeadBattle())

      .use(new EnemyLockScanningSystem(this).scanBattleField())
      .inOtherCases();

    body().movingBehaviour()
      .use(new GravitationalMovingSystem(this));

    plug(new Dodger(this));

    plug(new EnemyScannerInfo(this)
      .showAttributes());

    plug(new BulletPaint(this)
      .use(new Color(255, 84, 84)).forStrong()
      .use(new Color(253, 151, 31)).forMedium()
      .use(new Color(54, 151, 255)).forWeak());
  }

  @When(HIT_BY_BULLET)
  public void hitByBullet() {
    events().send(ADD_GRAVITY_POINT,
      GravityPointBuilder
        .antiGravityPoint()
        .at(location())
        .withValue(30)
        .during(5)
    );
  }

  @When(ENEMY_FIRE)
  public void enemyFire(EnemyFireEvent event) {
    Enemy enemy = event.enemy();
    log("Enemy %s probably fired a bullet at %s. Adding anti-gravity pull.",
      enemy.name(), enemy.position());
    events().send(ADD_GRAVITY_POINT,
      antiGravityPoint()
        .at(enemy.location())
        .withValue(200)
        .during((int) enemy.distance() / 3)
    );
    events().send(ADD_GRAVITY_POINT,
      antiGravityPoint()
        .at(location())
        .withValue(1000)
        .during((int) enemy.distance() / 3)
    );
  }

  @When(HIT_ROBOT)
  public void hitHobot(HitRobotEvent event) {
    log("Adding anti-gravity points to avoid %s", event.getName());
    events().send(ADD_GRAVITY_POINT,
      antiGravityPoint()
        .at(location())
        .withValue(100)
        .during(10)
    );
  }

  @When(HIT_WALL)
  public void hitWall(HitWallEvent event) {
    log("Adding gravity pull to avoid wall");
    Field battleField = radar().battleField();
    events().send(ADD_GRAVITY_POINT,
      gravityPoint()
        .at(battleField.center())
        .withValue(battleField.diagonal())
        .during(10)
    );
  }

  @When(ROUND_STARTED)
  public void addCenterPoint() {
    log("Adding center antigravity point");
    Field field = radar().battleField();
    events().send(ADD_GRAVITY_POINT,
      field.center()
        .antiGravitational()
        .withValue(field.diagonal() / 2)
    );
  }

  @When(ROUND_STARTED)
  public void addWallGravityPoints() {
    log("Adding gravity points to avoid walls");
    Field battleField = radar().battleField();
    Set<Point> wallPoints = new HashSet<Point>(1000);

    // bottom wall points
    Point point = battleField.downLeft();
    for (int i = 0; i < battleField.width(); i += 10) {
      wallPoints.add(point.right(i));
    }
    // right wall points
    point = battleField.downRight();
    for (int i = 0; i < battleField.height(); i += 10) {
      wallPoints.add(point.up(i));
    }
    // upper wall points
    point = battleField.upLeft();
    for (int i = 0; i < battleField.width(); i += 10) {
      wallPoints.add(point.right(i));
    }
    // left wall points
    point = battleField.downLeft();
    for (int i = 0; i < battleField.height(); i += 10) {
      wallPoints.add(point.up(i));
    }

    for (Point wallPoint : wallPoints) {
      events().send(ADD_GRAVITY_POINT,
        wallPoint.antiGravitational().withValue(battleField.diagonal() / 2)
      );
    }
  }

  protected void battle() {
    while (true) {
      log("***********************************");
      addEnemyPoints();
      radar().scan();
      body().move();
      gun().aim();
      if (radar().hasLockedTarget()) {
        gun().fire();
      }
      execute();
    }
  }

  private void addEnemyPoints() {
    for (Enemy enemy : radar().knownEnemies()) {
      GravityPoint point;
      if (enemy.equals(radar().lockedTarget())) {
        point = enemy.location().gravitational().withValue(enemy.distance() * 2);
      } else {
        point = enemy.location().antiGravitational().withValue(enemy.distance());
      }
      events().send(ADD_GRAVITY_POINT,
        point.during(1)
      );
    }
  }


}
