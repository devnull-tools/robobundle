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

package tools.devnull.robocode.parts.radar;

import tools.devnull.robocode.*;
import tools.devnull.robocode.annotation.When;
import tools.devnull.robocode.calc.Angle;
import tools.devnull.robocode.event.EnemyScannedEvent;
import tools.devnull.robocode.event.Events;
import tools.devnull.robocode.event.TargetSetEvent;
import tools.devnull.robocode.event.TargetUnsetEvent;
import tools.devnull.robocode.parts.BasePart;
import tools.devnull.robocode.parts.DefaultConditionalCommand;
import tools.devnull.robocode.parts.Radar;
import tools.devnull.robocode.parts.ScanningSystem;
import tools.devnull.robocode.parts.scanner.DefaultScanningSystem;
import robocode.RobotDeathEvent;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcelo Guimarães
 */
public class DefaultRadar extends BasePart implements Radar {

  private final DefaultConditionalCommand<ScanningSystem> scanningSystem;

  private Enemy target;

  private Enemy lastSeen;

  private Map<String, Enemy> enemies = new HashMap<String, Enemy>();

  public DefaultRadar(BaseBot bot) {
    super(bot);
    this.scanningSystem = new DefaultConditionalCommand<ScanningSystem>(bot);
    this.scanningSystem.use(new DefaultScanningSystem());
  }

  @Override
  public void setColor(Color color) {
    bot.setRadarColor(color);
  }

  public ConditionalCommand<ScanningSystem> forScanning() {
    return scanningSystem;
  }

  @Override
  public int enemiesCount() {
    return bot.getOthers();
  }

  @Override
  public boolean isHeadToHead() {
    return enemiesCount() == 1;
  }

  @Override
  public Field battleField() {
    return new BattleField(
      bot.getBattleFieldWidth(),
      bot.getBattleFieldHeight()
    );
  }

  @Override
  public long time() {
    return bot.getTime();
  }

  public void scan() {
    scanningSystem.execute();
  }

  public Enemy target() {
    return target;
  }

  public Enemy lastSeenEnemy() {
    return lastSeen;
  }

  public boolean hasTargetSet() {
    return target != null;
  }

  public Collection<Enemy> knownEnemies() {
    return Collections.unmodifiableCollection(enemies.values());
  }

  public void setTarget(Enemy e) {
    this.target = e;
    this.bot.broadcast(Events.TARGET_SET, new TargetSetEvent(target));
  }

  @Override
  public void unsetTarget() {
    this.target = null;
    this.bot.broadcast(Events.TARGET_UNSET, new TargetUnsetEvent(target));
  }

  @Override
  public Enemy enemy(String name) {
    return enemies.get(name);
  }

  @Override
  public Angle heading() {
    return new Angle(bot.getRadarHeadingRadians());
  }

  @Override
  public Angle turnRemaining() {
    return new Angle(bot.getRadarTurnRemainingRadians());
  }

  @Override
  public void turn(Angle angle) {
    bot.setTurnRadarRightRadians(angle.radians());
  }

  @When(Events.ENEMY_SCANNED)
  public void onEnemyScanned(EnemyScannedEvent event) {
    lastSeen = event.enemy();
    enemies.put(lastSeen.name(), lastSeen);
  }

  @When(Events.ROBOT_DEATH)
  public void onRobotDeath(RobotDeathEvent event) {
    enemies.remove(event.getName());
  }

}
