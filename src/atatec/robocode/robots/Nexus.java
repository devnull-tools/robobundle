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
import atatec.robocode.calc.Point;
import atatec.robocode.condition.BotConditions;
import atatec.robocode.condition.Function;
import atatec.robocode.condition.StrengthBasedLockCondition;
import atatec.robocode.event.EnemyFireEvent;
import atatec.robocode.event.EnemyScannedEvent;
import atatec.robocode.parts.aiming.PredictionAimingSystem;
import atatec.robocode.parts.firing.AccuracyBasedFiringSystem;
import atatec.robocode.parts.movement.GravitationalMovingSystem;
import atatec.robocode.parts.scanner.EnemyLockScanningSystem;
import atatec.robocode.plugin.Avoider;
import atatec.robocode.plugin.BulletPaint;
import atatec.robocode.plugin.BulletStatistics;
import atatec.robocode.plugin.Dodger;
import atatec.robocode.plugin.EnemyScannerInfo;
import atatec.robocode.plugin.EnemyTracker;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static atatec.robocode.event.Events.BULLET_FIRED;
import static atatec.robocode.event.Events.BULLET_HIT;
import static atatec.robocode.event.Events.BULLET_HIT_BULLET;
import static atatec.robocode.event.Events.BULLET_MISSED;
import static atatec.robocode.event.Events.BULLET_NOT_FIRED;
import static atatec.robocode.event.Events.ENEMY_FIRE;
import static atatec.robocode.event.Events.ENEMY_SCANNED;
import static atatec.robocode.event.Events.GUN_AIMED;
import static atatec.robocode.event.Events.HIT_BY_BULLET;
import static atatec.robocode.event.Events.HIT_ROBOT;
import static atatec.robocode.event.Events.HIT_WALL;
import static atatec.robocode.event.Events.NEAR_TO_ENEMY;
import static atatec.robocode.event.Events.NEAR_TO_WALL;
import static atatec.robocode.event.Events.ROUND_STARTED;
import static atatec.robocode.event.Events.TARGET_UNSET;
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

  private int movementLength = 5;

  private EnemyTracker enemyTracker = new EnemyTracker(this, 150);

  private BotConditions conditions = new BotConditions(this);

  private Map<String, Double> strengthMap = new HashMap<String, Double>();

  private Function<Enemy, Double> enemyStrength = new Function<Enemy, Double>() {
    @Override
    public Double evaluate(Enemy enemy) {
      return strengthMap.get(enemy.name());
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
      .use(new AccuracyBasedFiringSystem(this));

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
    if (++hitsByBullet == hitByBulletUntilUnlock) {
      radar().unsetTarget();
      hitsByBullet = 0;
    }
  }

  int enemies = 0;

  @When(ENEMY_SCANNED)
  public void enemyScanned(EnemyScannedEvent event) {
    if (enemies < radar().enemiesCount()) {
      radar().unsetTarget();
    }
    enemies = radar().enemiesCount();
    if (event.enemy().energy() < 10) {
      radar().setTarget(event.enemy());
    }
  }

  @When(TARGET_UNSET)
  public void resetEnemyCount() {
    enemies = 0;
  }

  @When(ENEMY_FIRE)
  public void onEnemyFire(EnemyFireEvent event) {
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
          .strong()
          .during(duration)
          .delay(delay++)
      );
    }
  }

  @When(HIT_ROBOT)
  public void hitRobot(HitRobotEvent event) {
    log("Hit robot at %s", event.getBearingRadians());
    Enemy enemy = radar().enemy(event.getName());
    if (enemy != null) { // use information from radar
      Point point = enemy.location();
      events().send(ADD_GRAVITY_POINT,
        antiGravityPoint()
          .at(point)
          .strong()
          .during(5)
      );
    }
  }

  @When(NEAR_TO_ENEMY)
  public void avoidEnemy(Enemy enemy) {
    events().send(ADD_GRAVITY_POINT,
      antiGravityPoint()
        .at(enemy.location())
        .strongest()
        .during(2)
    );
  }

  @When(HIT_WALL)
  public void hitWall(HitWallEvent event) {
    Field battleField = radar().battleField();
    events().send(ADD_GRAVITY_POINT,
      gravityPoint()
        .at(battleField.center())
        .strongest()
        .during(10)
    );
  }

  @When(ROUND_STARTED)
  public void addCenterPoint() {
    Field field = radar().battleField();
    events().send(ADD_GRAVITY_POINT,
      field.center()
        .antiGravitational()
        .normal()
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
        wallPoint.antiGravitational().strong()
      );
    }
  }

  @When(NEAR_TO_WALL)
  public void onNearToWall(Point wallPoint) {
    events().send(ADD_GRAVITY_POINT,
      wallPoint.antiGravitational()
        .strongest()
        .during(1)
    );
    events().send(ADD_GRAVITY_POINT,
      radar().battleField().center().gravitational()
        .strongest()
        .during(1)
    );
  }

  protected void onNextTurn() {
    log("***********************************");
    addEnemyPoints();
    addMovementPoints();
    radar().scan();
    body().move();
    gun().aim();
  }

  @When(GUN_AIMED)
  public void gunAimed() {
    gun().fire();
  }

  private int fireSkip = 0;

  @When(BULLET_NOT_FIRED)
  public void registerBulletNotFired() {
    if (++fireSkip > fireSkipToChangeTarget && !radar().isHeadToHead()) {
      radar().unsetTarget();
    }
  }

  @When(BULLET_FIRED)
  public void registerBulletFired() {
    fireSkip = 0;
  }

  @When(ENEMY_SCANNED)
  public void calculateEnemyStrength(EnemyScannedEvent event) {
    Enemy enemy = event.enemy();
    double patternStr = 1.0;
    List<Enemy> history = enemyTracker.historyFor(enemy).fromOldest();
    for (Enemy hist : history) {
      //when enemy is stopped, the patternStr will not be increased
      patternStr += hist.lateralVelocity() / 100;
    }

    patternStr = Math.abs(patternStr);

    double strength = (1 - statistics().of(enemy).accuracy()) *
      (patternStr * (enemy.energy() + statistics().of(enemy).taken() * 2)
        + Math.sqrt(enemy.distance()));

    strengthMap.put(enemy.name(), strength);
  }

  private void addEnemyPoints() {
    for (Enemy enemy : radar().knownEnemies()) {
      events().send(ADD_GRAVITY_POINT,
        enemy.location()
          .antiGravitational()
          .normal()
          .during(1)
      );
    }
  }

  private void addMovementPoints() {
    if (radar().hasTargetSet()) {
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
          .weak()
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
      radar().unsetTarget();
      misses = 0;
    }
  }

}
