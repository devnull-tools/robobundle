/************************************************************************************
 * The MIT License                                                                  *
 *                                                                                  *
 * Copyright (c) 2013 Marcelo Guimarães <ataxexe at gmail dot com>                  *
 * -------------------------------------------------------------------------------- *
 * Permission  is hereby granted, free of charge, to any person obtaining a copy of *
 * this  software  and  associated documentation files (the "Software"), to deal in *
 * the  Software  without  restriction,  including without limitation the rights to *
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of *
 * the  Software, and to permit persons to whom the Software is furnished to do so, *
 * subject to the following conditions:                                             *
 *                                                                                  *
 * The  above  copyright notice and this permission notice shall be included in all *
 * copies or substantial portions of the Software.                                  *
 *                            --------------------------                            *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS *
 * FOR  A  PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR *
 * COPYRIGHT  HOLDERS  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER *
 * IN  AN  ACTION  OF  CONTRACT,  TORT  OR  OTHERWISE,  ARISING  FROM, OUT OF OR IN *
 * CONNECTION  WITH  THE  SOFTWARE  OR  THE  USE OR OTHER DEALINGS IN THE SOFTWARE. *
 ************************************************************************************/

package atatec.robocode.robots;

import atatec.robocode.BaseBot;
import atatec.robocode.Enemy;
import atatec.robocode.Field;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.BulletTrajectory;
import atatec.robocode.calc.GravityPoint;
import atatec.robocode.calc.Point;
import atatec.robocode.condition.BotConditions;
import atatec.robocode.condition.Function;
import atatec.robocode.condition.StrengthBasedLockCondition;
import atatec.robocode.event.EnemyFireEvent;
import atatec.robocode.event.EnemyScannedEvent;
import atatec.robocode.parts.aiming.PredictionAimingSystem;
import atatec.robocode.parts.firing.EnergyBasedFiringSystem;
import atatec.robocode.parts.movement.GravitationalMovingSystem;
import atatec.robocode.parts.scanner.EnemyLockScanningSystem;
import atatec.robocode.plugin.Avoider;
import atatec.robocode.plugin.BulletPaint;
import atatec.robocode.plugin.BulletStatistics;
import atatec.robocode.plugin.Dodger;
import atatec.robocode.plugin.EnemyScannerInfo;
import atatec.robocode.plugin.EnemyTracker;
import atatec.robocode.util.GravityPointBuilder;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static atatec.robocode.event.Events.BULLET_FIRED;
import static atatec.robocode.event.Events.BULLET_HIT;
import static atatec.robocode.event.Events.BULLET_HIT_BULLET;
import static atatec.robocode.event.Events.BULLET_MISSED;
import static atatec.robocode.event.Events.BULLET_NOT_FIRED;
import static atatec.robocode.event.Events.ENEMY_FIRE;
import static atatec.robocode.event.Events.ENEMY_SCANNED;
import static atatec.robocode.event.Events.HIT_BY_BULLET;
import static atatec.robocode.event.Events.HIT_ROBOT;
import static atatec.robocode.event.Events.HIT_WALL;
import static atatec.robocode.event.Events.NEAR_TO_ENEMY;
import static atatec.robocode.event.Events.NEAR_TO_WALL;
import static atatec.robocode.event.Events.ROUND_STARTED;
import static atatec.robocode.event.Events.TARGET_UNLOCKED;
import static atatec.robocode.parts.movement.GravitationalMovingSystem.ADD_GRAVITY_POINT;
import static atatec.robocode.util.GravityPointBuilder.antiGravityPoint;
import static atatec.robocode.util.GravityPointBuilder.gravityPoint;

/** @author Marcelo Guimarães */
public class Nexus extends BaseBot {

  private int wallGPointsDistance = 40;
  private int avoidDistance = 100;

  private int fireSkipToChangeTarget = 50;

  private int hitByBulletUntilUnlock = 3;

  private int maxMissesInARow = 5;

  private double avoidingPower = 2500;

  private int movementLength = 10;

  private EnemyTracker enemyTracker = new EnemyTracker(this, 150);

  private BotConditions conditions = new BotConditions(this);

  private Function<Enemy, Double> enemyStrength = new Function<Enemy, Double>() {
    @Override
    public Double eval(Enemy enemy) {
      double patternStr = 1.0;
      List<Enemy> history = enemyTracker.historyFor(enemy).fromOldest();
      Enemy last = null;
      Point location = location();
      for (Enemy hist : history) {
        if (last != null) {
          //when enemy is stopped, the patterStr will not be increased
          patternStr += location.angleOfView(hist.location(), last.location()).radians()
            * Math.abs(hist.distance() - last.distance());
        }
        last = hist;
      }
      return (2 - statistics().of(enemy).accuracy()) + Math.pow(statistics().of(enemy).taken(), 2)
        * (patternStr * enemy.energy() * Math.pow(enemy.distance(), 2));
    }
  };

  private BulletStatistics statistics() {
    String entryName = "statistics";
    if (!storage().hasValueFor(entryName)) {
      storage().store(entryName, new BulletStatistics(this));
    }
    return storage().retrieve(entryName);
  }

  protected void configure() {
    body().setColor(new Color(39, 40, 34));
    gun().setColor(new Color(249, 38, 114));
    radar().setColor(new Color(39, 40, 34));

    gun().forAiming()
      .use(new PredictionAimingSystem(this));

    gun().forFiring()
      .use(new EnergyBasedFiringSystem(this)
        .fireMaxAt(80)
        .fireMinAt(30));

    radar().forScanning()
      .use(new EnemyLockScanningSystem(this))
      .when(conditions.radar().headToHeadBattle())

      .use(new EnemyLockScanningSystem(this)
        .scanBattleField()
        .addLockCondition(plug(new StrengthBasedLockCondition(this, enemyStrength)))
      )
      .inOtherCases();

    body().forMoving().use(new GravitationalMovingSystem(this));

    plug(new Dodger(this));
    plug(new Avoider(this)
      .notifyAt(avoidDistance));

    plug(new EnemyScannerInfo(this));
    plug(enemyTracker);
    plug(statistics());

    plug(new BulletPaint(this)
      .use(new Color(255, 84, 84)).forStrong()
      .use(new Color(253, 151, 31)).forMedium()
      .use(new Color(54, 151, 255)).forWeak());
  }

  int hitsByBullet = 0;

  @When(HIT_BY_BULLET)
  public void hitByBullet() {
    events().send(ADD_GRAVITY_POINT,
      GravityPointBuilder
        .antiGravityPoint()
        .at(location())
        .withValue(300)
        .during(10)
    );
    if (++hitsByBullet == hitByBulletUntilUnlock) {
      radar().unlock();
      hitsByBullet = 0;
    }
  }

  int enemies = 0;

  @When(ENEMY_SCANNED)
  public void enemyScanned(EnemyScannedEvent event) {
    if (enemies < radar().enemiesCount()) {
      radar().unlock();
    }
    enemies = radar().enemiesCount();
    if (event.enemy().energy() < 10) {
      radar().lock(event.enemy());
    }
  }

  @When(TARGET_UNLOCKED)
  public void resetEnemyCount() {
    enemies = 0;
  }

  @When(ENEMY_FIRE)
  public void enemyFire(EnemyFireEvent event) {
    Enemy enemy = event.enemy();
    log("Enemy %s probably fired a bullet at %s. Adding anti-gravity pull.",
      enemy.name(), enemy.position());
    List<Point> bulletTrajectory = new BulletTrajectory(radar().battleField())
      .from(enemy.location()).to(location());
    int duration = 5;
    int delay = 0;
    int speed = (int) Math.floor(event.bulletSpeed() / 4);
    for (int i = speed * 3; i < bulletTrajectory.size(); i += speed) {
      events().send(ADD_GRAVITY_POINT,
        antiGravityPoint()
          .at(bulletTrajectory.get(i))
          .withValue(200)
          .during(duration)
          .delay(delay++)
      );
    }
  }

  @When(HIT_ROBOT)
  public void hitRobot(HitRobotEvent event) {
    log("Hit robot at %s", event.getBearingRadians());
    Enemy enemy = radar().enemy(event.getName());
    Point point;
    if (enemy != null) { // use information from radar
      point = enemy.location();
    } else {
      point = location();
    }
    events().send(ADD_GRAVITY_POINT,
      antiGravityPoint()
        .at(point)
        .withValue(avoidingPower)
        .during(5)
    );
  }

  @When(NEAR_TO_ENEMY)
  public void avoidEnemy(Enemy enemy) {
    events().send(ADD_GRAVITY_POINT,
      antiGravityPoint()
        .at(enemy.location())
        .withValue(avoidingPower)
        .during(2)
    );
  }

  @When(HIT_WALL)
  public void hitWall(HitWallEvent event) {
    Field battleField = radar().battleField();
    events().send(ADD_GRAVITY_POINT,
      gravityPoint()
        .at(battleField.closestWallPointTo(location()))
        .withValue(2000)
        .during(10)
    );
  }

  @When(ROUND_STARTED)
  public void addCenterPoint() {
    Field field = radar().battleField();
    events().send(ADD_GRAVITY_POINT,
      field.center()
        .antiGravitational()
        .withValue(field.diagonal() / 2)
    );
  }

  @When(ROUND_STARTED)
  public void addWallGravityPoints() {
    Field battleField = radar().battleField();
    Set<Point> wallPoints = new HashSet<Point>(1000);

    // bottom wall points
    Point point = battleField.downLeft();
    for (int i = 0; i < battleField.width(); i += wallGPointsDistance) {
      wallPoints.add(point.right(i));
    }
    // right wall points
    point = battleField.downRight();
    for (int i = 0; i < battleField.height(); i += wallGPointsDistance) {
      wallPoints.add(point.up(i));
    }
    // upper wall points
    point = battleField.upLeft();
    for (int i = 0; i < battleField.width(); i += wallGPointsDistance) {
      wallPoints.add(point.right(i));
    }
    // left wall points
    point = battleField.downLeft();
    for (int i = 0; i < battleField.height(); i += wallGPointsDistance) {
      wallPoints.add(point.up(i));
    }

    for (Point wallPoint : wallPoints) {
      events().send(ADD_GRAVITY_POINT,
        wallPoint.antiGravitational().withValue(battleField.diagonal() / 2)
      );
    }
  }

  @When(NEAR_TO_WALL)
  public void avoidWall(Point wallPoint) {
    events().send(ADD_GRAVITY_POINT,
      wallPoint.antiGravitational()
        .withValue(avoidingPower)
        .during(1)
    );
  }

  protected void onNextTurn() {
    log("***********************************");
    addEnemyPoints();
    addMovementPoints();
    radar().scan();
    body().move();
    gun().aim().fireIfTargetLocked();
  }

  private int fireSkip = 0;

  @When(BULLET_NOT_FIRED)
  public void registerBulletNotFired() {
    if (++fireSkip > fireSkipToChangeTarget && !radar().isHeadToHead()) {
      radar().unlock();
    }
  }

  @When(BULLET_FIRED)
  public void registerBulletFired() {
    fireSkip = 0;
  }

  private void addEnemyPoints() {
    for (Enemy enemy : radar().knownEnemies()) {
      GravityPoint point;
      if (enemy.equals(radar().target()) && getEnergy() > enemy.energy()) {
        point = enemy.location().gravitational().withValue(enemy.distance());
      } else {
        point = enemy.location().antiGravitational().withValue(-enemy.distance());
      }
      events().send(ADD_GRAVITY_POINT,
        point.during(1)
      );
    }
  }

  private void addMovementPoints() {
    if (radar().hasLockedTarget()) {
      Enemy target = radar().target();
      double radius = target.distance();
      double perimeter = Math.PI * 2 * radius;
      int numberOfPoints = (int) (perimeter / 5);
      double angleStep = Angle.inRadians(radar().time() >> 4).cos() * (Math.PI * 2) / numberOfPoints;
      Angle t = target.location().bearingTo(this.location()).angle().plus(angleStep * movementLength);
      Point movementPoint = new Point(
        target.location().x() + (radius * t.sin()),
        target.location().y() + (radius * t.cos())
      );
      events().send(ADD_GRAVITY_POINT,
        movementPoint.gravitational()
          .withValue(1000)
          .during(1)
      );
    }
  }

  private int misses = 0;

  @When({BULLET_HIT, BULLET_HIT_BULLET})
  public void hit() {
    misses = 0;
  }

  @When(BULLET_MISSED)
  public void miss() {
    if (misses++ > maxMissesInARow && !radar().isHeadToHead()) {
      radar().unlock();
      misses = 0;
    }
  }

}
