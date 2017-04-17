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

package tools.devnull.robobundle.plugin;

import tools.devnull.robobundle.Bot;
import tools.devnull.robobundle.BotStatistics;
import tools.devnull.robobundle.Enemy;
import tools.devnull.robobundle.Statistics;
import tools.devnull.robobundle.annotation.When;
import tools.devnull.robobundle.event.BulletFiredEvent;
import tools.devnull.robobundle.event.Events;
import robocode.Bullet;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcelo Guimarães
 */
public class DefaultBotStatistics implements BotStatistics {

  private Bot bot;

  private Map<String, BulletStatistic> statisticsMap;

  private Map<Bullet, String> bullets;

  public DefaultBotStatistics() {
    this.statisticsMap = new HashMap<>();
    this.bullets = new HashMap<>();
  }

  public void setBot(Bot bot) {
    this.bot = bot;
    if (!statisticsMap.containsKey(bot.name())) {
      this.statisticsMap.put(bot.name(), new BulletStatistic());
    }
  }

  private BulletStatistic get(String name) {
    if (!statisticsMap.containsKey(name)) {
      statisticsMap.put(name, new BulletStatistic());
    }
    return statisticsMap.get(name);
  }

  @Override
  public Statistics forEnemy(Enemy enemy) {
    return get(enemy.name());
  }

  @Override
  public Statistics overall() {
    return get(bot.name());
  }

  @When(Events.BULLET_FIRED)
  public void registerBulletFired(BulletFiredEvent event) {
    if (bot.radar().hasTargetSet()) {
      String name = bot.radar().target().name();
      get(name).fires++;
      get(bot.name()).fires++;
      bullets.put(event.bullet(), name);
    }
  }

  @When(Events.BULLET_MISSED)
  public void registerBulletMissed(BulletMissedEvent event) {
    String name = bullets.get(event.getBullet());
    if (name != null) {
      get(name).misses++;
    }
    get(bot.name()).misses++;
  }

  @When(Events.BULLET_HIT)
  public void registerBulletHit(BulletHitEvent event) {
    String name = bullets.get(event.getBullet());
    if (name != null) {
      get(name).hits++;
    }
    get(bot.name()).hits++;
  }

  @When(Events.HIT_BY_BULLET)
  public void registerBulletToked(HitByBulletEvent event) {
    String name = event.getName();
    if (name != null) {
      get(name).taken++;
    }
    get(bot.name()).taken++;
  }

  @When(Events.ROUND_ENDED)
  public void logStatistics() {
    bot.log("-----------------------");
    bot.log("Statistics:");
    bot.log("Overall Accuracy: %.2f %%", overall().accuracy() * 100);
    for (Map.Entry<String, BulletStatistic> entry : statisticsMap.entrySet()) {
      if (!entry.getKey().equals(bot.name())) {
        bot.log("Accuracy for %s: %.2f",
          entry.getKey(), entry.getValue().accuracy() * 100
        );
      }
    }
    bot.log("-----------------------");
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
