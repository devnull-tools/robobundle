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

package tools.devnull.robobundle.robots;

import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import tools.devnull.robobundle.BaseBot;
import tools.devnull.robobundle.Enemy;
import tools.devnull.robobundle.Field;
import tools.devnull.robobundle.annotation.When;
import tools.devnull.robobundle.calc.BulletTrajectory;
import tools.devnull.robobundle.calc.Point;
import tools.devnull.robobundle.condition.BotConditions;
import tools.devnull.robobundle.condition.StrengthBasedLockCondition;
import tools.devnull.robobundle.event.EnemyFireEvent;
import tools.devnull.robobundle.event.EnemyScannedEvent;
import tools.devnull.robobundle.parts.aiming.LinearPredictionAimingSystem;
import tools.devnull.robobundle.parts.firing.AccuracyBasedFiringSystem;
import tools.devnull.robobundle.parts.movement.GravitationalMovingSystem;
import tools.devnull.robobundle.parts.scanner.EnemyLockScanningSystem;
import tools.devnull.robobundle.plugin.Avoider;
import tools.devnull.robobundle.plugin.BulletPaint;
import tools.devnull.robobundle.plugin.Dodger;
import tools.devnull.robobundle.plugin.EnemyScannerInfo;
import tools.devnull.robobundle.plugin.EnemyTracker;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static tools.devnull.robobundle.event.Events.BULLET_FIRED;
import static tools.devnull.robobundle.event.Events.BULLET_HIT;
import static tools.devnull.robobundle.event.Events.BULLET_HIT_BULLET;
import static tools.devnull.robobundle.event.Events.BULLET_MISSED;
import static tools.devnull.robobundle.event.Events.BULLET_NOT_FIRED;
import static tools.devnull.robobundle.event.Events.ENEMY_FIRE;
import static tools.devnull.robobundle.event.Events.ENEMY_SCANNED;
import static tools.devnull.robobundle.event.Events.GUN_AIMED;
import static tools.devnull.robobundle.event.Events.HIT_BY_BULLET;
import static tools.devnull.robobundle.event.Events.HIT_ROBOT;
import static tools.devnull.robobundle.event.Events.HIT_WALL;
import static tools.devnull.robobundle.event.Events.NEAR_TO_ENEMY;
import static tools.devnull.robobundle.event.Events.NEAR_TO_WALL;
import static tools.devnull.robobundle.event.Events.ROUND_STARTED;
import static tools.devnull.robobundle.event.Events.TARGET_UNSET;
import static tools.devnull.robobundle.util.GravityPointBuilder.antiGravityPoint;
import static tools.devnull.robobundle.util.GravityPointBuilder.gravityPoint;

/**
 * @author Marcelo Guimarães
 */
public class Nexus extends BaseBot {

  private int wallGPointsDistance = 40;
  private int avoidDistance = 100;

  private int fireSkipToChangeTarget = 50;

  private int hitByBulletUntilUnlock = 3;

  private int maxMissesInARow = 5;

  private EnemyTracker enemyTracker = new EnemyTracker(this, 150);

  private BotConditions conditions = new BotConditions(this);

  private Map<String, Double> strengthMap = new HashMap<String, Double>();

  private Function<Enemy, Double> enemyStrength = enemy -> strengthMap.get(enemy.name());

  private GravitationalMovingSystem gravitationalMovingSystem;

  protected void configure() {
    body().setColor(new Color(39, 40, 34));
    gun().setColor(new Color(249, 38, 114));
    radar().setColor(new Color(39, 40, 34));

    gun().forAiming()
        .use(new LinearPredictionAimingSystem(this));

    gun().forFiring()
        .use(new AccuracyBasedFiringSystem(this));

    radar().forScanning()
        .use(new EnemyLockScanningSystem(this))
        .when(conditions.radar().headToHeadBattle())

        .use(new EnemyLockScanningSystem(this)
            .scanBattleField()
            .addLockCondition(new StrengthBasedLockCondition(this)
                .use(enemyStrength)
                .lockWeaker())
        )
        .asDefault();

    gravitationalMovingSystem = new GravitationalMovingSystem(this);
    body().forMoving().use(gravitationalMovingSystem);

    plug(new Dodger(this));
    plug(new Avoider(this)
        .notifyAt(avoidDistance));

    plug(new EnemyScannerInfo(this));
    plug(enemyTracker);

    plug(new BulletPaint(this)
        .use(new Color(255, 84, 84)).forStrong()
        .use(new Color(253, 151, 31)).forMedium()
        .use(new Color(54, 151, 255)).forWeak());
  }

  private int hitsByBullet = 0;

  @When(HIT_BY_BULLET)
  public void hitByBullet() {
    if (++hitsByBullet == hitByBulletUntilUnlock) {
      radar().unsetTarget();
      hitsByBullet = 0;
    }
  }

  private int enemies = 0;

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
      gravitationalMovingSystem.add(
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
      gravitationalMovingSystem.add(
          antiGravityPoint()
              .at(point)
              .strong()
              .during(5)
      );
    }
  }

  @When(NEAR_TO_ENEMY)
  public void avoidEnemy(Enemy enemy) {
    gravitationalMovingSystem.add(
        antiGravityPoint()
            .at(enemy.location())
            .strongest()
            .during(2)
    );
  }

  @When(HIT_WALL)
  public void hitWall(HitWallEvent event) {
    Field battleField = radar().battleField();
    gravitationalMovingSystem.add(
        gravityPoint()
            .at(battleField.center())
            .strongest()
            .during(10)
    );
  }

  @When(ROUND_STARTED)
  public void addCenterPoint() {
    Field field = radar().battleField();
    gravitationalMovingSystem.add(
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
    Point point = battleField.bottomLeft();
    for (int i = 0; i < battleField.width(); i += wallGPointsDistance) {
      wallPoints.add(point.right(i));
    }
    // right wall points
    point = battleField.bottomRight();
    for (int i = 0; i < battleField.height(); i += wallGPointsDistance) {
      wallPoints.add(point.up(i));
    }
    // upper wall points
    point = battleField.topLeft();
    for (int i = 0; i < battleField.width(); i += wallGPointsDistance) {
      wallPoints.add(point.right(i));
    }
    // left wall points
    point = battleField.bottomLeft();
    for (int i = 0; i < battleField.height(); i += wallGPointsDistance) {
      wallPoints.add(point.up(i));
    }

    for (Point wallPoint : wallPoints) {
      gravitationalMovingSystem.add(
          wallPoint.antiGravitational().strong()
      );
    }
  }

  @When(NEAR_TO_WALL)
  public void onNearToWall(Point wallPoint) {
    gravitationalMovingSystem.add(
        wallPoint.antiGravitational()
            .strongest()
            .during(1)
    );
    gravitationalMovingSystem.add(
        radar().battleField().center().gravitational()
            .strongest()
            .during(1)
    );
  }

  protected void onNextTurn() {
    log("***********************************");
    addEnemyPoints();
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
    List<Enemy> history = enemyTracker.dataFor(enemy).history();
    for (Enemy hist : history) {
      //when enemy is stopped, the patternStr will not be increased
      patternStr += hist.lateralVelocity() / 100;
    }

    patternStr = Math.abs(patternStr);

    double strength = (1 - statistics().forEnemy(enemy).accuracy()) *
        (patternStr * (enemy.energy() + statistics().forEnemy(enemy).taken() * 2)
            + Math.sqrt(enemy.distance()));

    strengthMap.put(enemy.name(), strength);
  }

  private void addEnemyPoints() {
    for (Enemy enemy : radar().knownEnemies()) {
      gravitationalMovingSystem.add(
          enemy.location()
              .antiGravitational()
              .normal()
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
