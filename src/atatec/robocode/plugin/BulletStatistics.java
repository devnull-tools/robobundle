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
import atatec.robocode.parts.Statistics;
import robocode.Bullet;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;

import java.util.HashMap;
import java.util.Map;

/** @author Marcelo Guimarães */
public class BulletStatistics {

  private final Bot bot;

  private Map<String, BulletStatistic> statisticsMap;

  private Map<Bullet, String> bullets;

  public BulletStatistics(Bot bot) {
    this.bot = bot;
    this.statisticsMap = new HashMap<String, BulletStatistic>();
    this.bullets = new HashMap<Bullet, String>();
  }

  private BulletStatistic get(String name) {
    if (!statisticsMap.containsKey(name)) {
      statisticsMap.put(name, new BulletStatistic());
    }
    return statisticsMap.get(name);
  }

  public Statistics of(Enemy enemy) {
    return get(enemy.name());
  }

  @When(Events.BULLET_FIRED)
  public void registerBulletFired(BulletFiredEvent event) {
    if (bot.radar().hasTargetSet()) {
      get(event.bullet().getName()).fires++;
    }
  }

  @When(Events.BULLET_MISSED)
  public void registerBulletMissed(BulletMissedEvent event) {
    String name = bullets.get(event.getBullet());
    get(name).misses++;
  }

  @When(Events.BULLET_HIT)
  public void registerBulletHit(BulletHitEvent event) {
    String name = bullets.get(event.getBullet());
    get(name).hits++;
  }

  @When(Events.HIT_BY_BULLET)
  public void registerBulletToked(HitByBulletEvent event) {
    get(event.getName()).taken++;
  }

  private class BulletStatistic implements Statistics {

    private int fires;
    private int hits;
    private int misses;
    private int taken;

    @Override
    public double accuracy() {
      if (fires > 0) {
        return hits == 0 ? 0 : (double) hits / fires;
      }
      return 0;
    }

    @Override
    public int fired() {
      return fires;
    }

    @Override
    public int hit() {
      return hits;
    }

    @Override
    public int missed() {
      return misses;
    }

    @Override
    public int taken() {
      return taken;
    }
  }

}
