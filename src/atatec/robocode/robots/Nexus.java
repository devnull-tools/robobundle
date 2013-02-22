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
import atatec.robocode.Condition;
import atatec.robocode.Enemy;
import atatec.robocode.Field;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.GravityPoint;
import atatec.robocode.calc.Point;
import atatec.robocode.condition.BotConditions;
import atatec.robocode.event.EnemyFireEvent;
import atatec.robocode.parts.aiming.PredictionAimingSystem;
import atatec.robocode.parts.firing.EnergyBasedFiringSystem;
import atatec.robocode.parts.movement.EnemyCircleMovingSystem;
import atatec.robocode.parts.movement.FollowEnemyMovingSystem;
import atatec.robocode.parts.movement.GravitationalMovingSystem;
import atatec.robocode.parts.scanner.EnemyLockScanningSystem;
import atatec.robocode.plugin.Avoider;
import atatec.robocode.plugin.BulletPaint;
import atatec.robocode.plugin.DistanceThreshold;
import atatec.robocode.plugin.Dodger;
import atatec.robocode.plugin.EnemyHistory;
import atatec.robocode.plugin.EnemyScannerInfo;
import atatec.robocode.util.GravityPointBuilder;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import static atatec.robocode.condition.Conditions.all;
import static atatec.robocode.condition.Conditions.any;
import static atatec.robocode.event.Events.BULLET_FIRED;
import static atatec.robocode.event.Events.BULLET_NOT_FIRED;
import static atatec.robocode.event.Events.ENEMY_FIRE;
import static atatec.robocode.event.Events.HIT_BY_BULLET;
import static atatec.robocode.event.Events.HIT_ROBOT;
import static atatec.robocode.event.Events.HIT_WALL;
import static atatec.robocode.event.Events.NEAR_TO_ENEMY;
import static atatec.robocode.event.Events.NEAR_TO_WALL;
import static atatec.robocode.event.Events.ROUND_STARTED;
import static atatec.robocode.parts.movement.GravitationalMovingSystem.ADD_GRAVITY_POINT;
import static atatec.robocode.parts.movement.GravitationalMovingSystem.LOW_ENFORCING;
import static atatec.robocode.util.GravityPointBuilder.antiGravityPoint;
import static atatec.robocode.util.GravityPointBuilder.gravityPoint;

/** @author Marcelo Varella Barca Guimarães */
public class Nexus extends BaseBot {

  private double lowEnforcingValue = 0.4;
  private int wallGPointsDistance = 40;
  private int avoidDistance = 80;

  private int minimumStoppedTurns = 15;

  private int fireSkipToChangeTarget = 50;

  private double avoidingPower = 3000;

  private boolean isLowEnforcing = false;

  private EnemyHistory enemyHistory;

  private DistanceThreshold distanceThreshold;

  private Condition lowEnforcing = new Condition() {
    @Override
    public boolean evaluate() {
      return isLowEnforcing;
    }
  };

  private BotConditions conditions = new BotConditions(this);

  protected void configure() {
    body().setColor(new Color(39, 40, 34));
    gun().setColor(new Color(166, 226, 46));
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
        .scanBattleField())
      .inOtherCases();

    body().forMoving()
      .use(new EnemyCircleMovingSystem(this))
      .when(
        all(lowEnforcing, conditions.enemy().isAtMost(400))
      )

      .use(new FollowEnemyMovingSystem(this))
      .when(lowEnforcing)

      .use(new GravitationalMovingSystem(this)
        .lowEnforcingAt(lowEnforcingValue))
      .inOtherCases();

    enemyHistory = new EnemyHistory(this);
    distanceThreshold = new DistanceThreshold(this);

    plug(new Dodger(this));
    plug(new Avoider(this)
      .notifyAt(avoidDistance));

    plug(new EnemyScannerInfo(this));
    plug(enemyHistory);
    plug(distanceThreshold);

    plug(new BulletPaint(this)
      .use(new Color(255, 84, 84)).forStrong()
      .use(new Color(253, 151, 31)).forMedium()
      .use(new Color(54, 151, 255)).forWeak());
  }

  @When(LOW_ENFORCING)
  public void lowEnforcing() {
    isLowEnforcing = true;
    body().move();
    isLowEnforcing = false;
  }

  @When(HIT_BY_BULLET)
  public void hitByBullet() {
    events().send(ADD_GRAVITY_POINT,
      GravityPointBuilder
        .antiGravityPoint()
        .at(location())
        .withValue(300)
        .during(10)
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
        .withValue(3000)
        .during(1)
    );
  }

  @When(NEAR_TO_ENEMY)
  public void avoidEnemy(Enemy enemy) {
    events().send(ADD_GRAVITY_POINT,
      antiGravityPoint()
        .at(enemy.location())
        .withValue(avoidingPower)
        .during(1)
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

  private int fireSkip = 0;

  protected void onNextTurn() {
    log("***********************************");
    addEnemyPoints();
    radar().scan();
    body().move();
    gun().aim().fireIf(
      all(
        conditions.radar().hasLockedTarget(),
        any(
          distanceThreshold.targetAtGoodDistance(),
          enemyHistory.targetStopedFor(minimumStoppedTurns)
        )
      )
    );
  }

  @When(BULLET_NOT_FIRED)
  public void registerBulletNotFired() {
    if (++fireSkip > fireSkipToChangeTarget && radar().enemiesCount() > 1) {
      radar().unlockTarget();
    }
  }

  @When(BULLET_FIRED)
  public void registerBulletFired() {
    fireSkip = 0;
  }

  private void addEnemyPoints() {
    for (Enemy enemy : radar().knownEnemies()) {
      GravityPoint point;
      if (enemy.equals(radar().lockedTarget())) {
        point = enemy.location().gravitational().withValue(enemy.distance() * 2);
      } else {
        point = enemy.location().antiGravitational().withValue(enemy.distance() / 2);
      }
      events().send(ADD_GRAVITY_POINT,
        point.during(1)
      );
    }
  }


}
