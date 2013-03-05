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

package atatec.robocode;

import atatec.robocode.calc.Point;
import atatec.robocode.event.BulletFiredEvent;
import atatec.robocode.event.DefaultEventRegistry;
import atatec.robocode.event.EnemyScannedEvent;
import atatec.robocode.event.EventRegistry;
import atatec.robocode.parts.Body;
import atatec.robocode.parts.DefaultStorage;
import atatec.robocode.parts.Gun;
import atatec.robocode.parts.Radar;
import atatec.robocode.parts.Storage;
import atatec.robocode.parts.body.DefaultBody;
import atatec.robocode.parts.gun.DefaultGun;
import atatec.robocode.parts.radar.DefaultRadar;
import atatec.robocode.util.Drawer;
import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.Bullet;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

import java.awt.Graphics2D;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static atatec.robocode.event.Events.BATTLE_ENDED;
import static atatec.robocode.event.Events.BULLET_FIRED;
import static atatec.robocode.event.Events.BULLET_HIT;
import static atatec.robocode.event.Events.BULLET_HIT_BULLET;
import static atatec.robocode.event.Events.BULLET_MISSED;
import static atatec.robocode.event.Events.DEATH;
import static atatec.robocode.event.Events.DRAW;
import static atatec.robocode.event.Events.ENEMY_SCANNED;
import static atatec.robocode.event.Events.HIT_BY_BULLET;
import static atatec.robocode.event.Events.HIT_ROBOT;
import static atatec.robocode.event.Events.HIT_WALL;
import static atatec.robocode.event.Events.NEXT_TURN;
import static atatec.robocode.event.Events.PAINT;
import static atatec.robocode.event.Events.ROBOT_DEATH;
import static atatec.robocode.event.Events.ROUND_ENDED;
import static atatec.robocode.event.Events.ROUND_STARTED;
import static atatec.robocode.event.Events.WIN;

/**
 * A base class that provides a default abstraction to creating first class robots.
 *
 * @author Marcelo Guimarães
 */
public abstract class BaseBot extends AdvancedRobot implements Bot {

  private Gun gun;

  private Body body;

  private Radar radar;

  private EventRegistry eventRegistry = new DefaultEventRegistry(this);

  private boolean roundEnded = false;

  private static Map<String, Storage> persistentStorage =
    new ConcurrentHashMap<String, Storage>();

  /** Initializes {@link Gun}, {@link Radar} and {@link Body} */
  protected void initializeParts() {
    this.gun = new DefaultGun(this);
    this.body = new DefaultBody(this);
    this.radar = new DefaultRadar(this);
  }

  /** Configures the bot behaviours. All configuration must be done here. */
  protected abstract void configure();

  /**
   * Sets up the bot and put it to battle.
   * <p/>
   * Every part is adjusted to turn independently, than the {@link #initializeParts()} is
   * called. After it, the bot parts and the bot itself are {@link #plug(Object)
   * registered} and the {@link #configure()} method is called.
   * <p/>
   * When the configuration is done, a {@link atatec.robocode.event.Events#ROUND_STARTED}
   * event is send and the {@link #onRoundStarted()} is called.
   */
  public final void run() {
    setAdjustGunForRobotTurn(true);
    setAdjustRadarForGunTurn(true);
    setAdjustRadarForRobotTurn(true);

    if (!persistentStorage.containsKey(getName())) {
      persistentStorage.put(getName(), new DefaultStorage());
    }

    initializeParts();

    events().register(body);
    events().register(gun);
    events().register(radar);
    events().register(this);

    configure();

    events().send(ROUND_STARTED);

    onRoundStarted();
  }

  /**
   * Called when each round was started. Override this method if you want to change the
   * default behaviour.
   * <p/>
   * By default, this method maintains a loop until the round ends and, for each step,
   * calls {@link #onNextTurn()} and sends a {@link atatec.robocode.event.Events#NEXT_TURN}
   * event.
   */
  protected void onRoundStarted() {
    while (!roundEnded) {
      onNextTurn();
      events().send(NEXT_TURN);
      execute();
    }
  }

  /**
   * Do the robot's movements. Override this method if you want to use the default
   * behaviour of {@link #onRoundStarted()}.
   * <p/>
   * You may manipulate any part of the robot
   */
  protected void onNextTurn() {

  }

  @Override
  public Storage storage() {
    return persistentStorage.get(getName());
  }

  @Override
  public final Point location() {
    return new Point(getX(), getY());
  }

  @Override
  public final Gun gun() {
    return gun;
  }

  @Override
  public final Body body() {
    return body;
  }

  @Override
  public final Radar radar() {
    return radar;
  }

  @Override
  public final EventRegistry events() {
    return eventRegistry;
  }

  @Override
  public void fire(double power) {
    if (getGunHeat() == 0) {
      Bullet bullet = super.setFireBullet(power);
      if (bullet != null) {
        eventRegistry.send(BULLET_FIRED, new BulletFiredEvent(bullet));
      }
    }
  }

  @Override
  public final void log(Object message, Object... params) {
    out.printf(message.toString(), params);
    out.println();
  }

  @Override
  public void log(Throwable throwable) {
    throwable.printStackTrace(out);
  }

  @Override
  public final void onScannedRobot(ScannedRobotEvent event) {
    eventRegistry.send(ENEMY_SCANNED, new EnemyScannedEvent(this, event));
    eventRegistry.send(ENEMY_SCANNED, event);
  }

  @Override
  public final void onBulletHit(BulletHitEvent event) {
    eventRegistry.send(BULLET_HIT, event);
  }

  @Override
  public final void onBulletHitBullet(BulletHitBulletEvent event) {
    eventRegistry.send(BULLET_HIT_BULLET, event);
  }

  @Override
  public final void onBulletMissed(BulletMissedEvent event) {
    eventRegistry.send(BULLET_MISSED, event);
  }

  @Override
  public final void onHitByBullet(HitByBulletEvent event) {
    eventRegistry.send(HIT_BY_BULLET, event);
  }

  @Override
  public final void onRobotDeath(RobotDeathEvent event) {
    eventRegistry.send(ROBOT_DEATH, event);
  }

  @Override
  public final void onHitRobot(HitRobotEvent event) {
    eventRegistry.send(HIT_ROBOT, event);
  }

  @Override
  public final void onHitWall(HitWallEvent event) {
    eventRegistry.send(HIT_WALL, event);
  }

  @Override
  public final void onPaint(Graphics2D g) {
    eventRegistry.send(PAINT, g);
    eventRegistry.send(DRAW, new Drawer(g));
  }

  @Override
  public final void onDeath(DeathEvent event) {
    eventRegistry.send(DEATH, event);
  }

  @Override
  public final void onWin(WinEvent event) {
    eventRegistry.send(WIN, event);
  }

  @Override
  public final void onRoundEnded(RoundEndedEvent event) {
    roundEnded = true;
    eventRegistry.send(ROUND_ENDED, event);
  }

  @Override
  public final void onBattleEnded(BattleEndedEvent event) {
    eventRegistry.send(BATTLE_ENDED);
    persistentStorage.clear();
  }

  @Override
  public final <E> E plug(E plugin) {
    eventRegistry.register(plugin);
    return plugin;
  }

}
