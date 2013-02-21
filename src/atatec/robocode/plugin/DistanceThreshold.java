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

package atatec.robocode.plugin;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.event.BulletFiredEvent;
import atatec.robocode.event.Events;
import atatec.robocode.util.Drawer;
import robocode.Bullet;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/** @author Marcelo Varella Barca Guimarães */
public class DistanceThreshold {

  private final Bot bot;

  private double baseDistance;
  private double stepPoint;

  private Map<Bullet, String> bullets;

  private Map<String, AtomicLong> modifiers;

  public DistanceThreshold(Bot bot) {
    this(bot, 100, 10);
  }

  public DistanceThreshold(Bot bot, double baseDistance, double stepPoint) {
    this.bot = bot;
    this.baseDistance = baseDistance;
    this.stepPoint = stepPoint;
    this.bullets = new HashMap<Bullet, String>();
    this.modifiers = new HashMap<String, AtomicLong>();
  }

  @When(Events.BULLET_FIRED)
  public void registerBulletFired(BulletFiredEvent event) {
    String name = bot.radar().lockedTarget().name();
    bullets.put(event.bullet(), name);
    if (!modifiers.containsKey(name)) {
      modifiers.put(name, new AtomicLong());
    }
  }

  @When(Events.BULLET_MISSED)
  public void registerBulletMissed(BulletMissedEvent event) {
    String name = bullets.get(event.getBullet());
    modifiers.get(name).decrementAndGet();
  }

  @When(Events.BULLET_HIT)
  public void registerBulletHit(BulletHitEvent event) {
    String name = bullets.get(event.getBullet());
    modifiers.get(name).incrementAndGet();
  }

  public double maximumDistanceTo(Enemy enemy) {
    if (modifiers.containsKey(enemy.name())) {
      return baseDistance + (stepPoint * modifiers.get(enemy.name()).get());
    }
    return baseDistance;
  }

  @When(Events.DRAW)
  public void draw(Drawer drawer) {
    Collection<Enemy> enemies = bot.radar().knownEnemies();
    for (Enemy enemy : enemies) {
      drawer.draw(Color.PINK).string(maximumDistanceTo(enemy)).at(enemy.location());
    }
  }

}
