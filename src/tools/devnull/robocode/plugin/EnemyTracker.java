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

package tools.devnull.robocode.plugin;

import tools.devnull.robocode.Bot;
import tools.devnull.robocode.Enemy;
import tools.devnull.robocode.EnemyHistory;
import tools.devnull.robocode.annotation.When;
import tools.devnull.robocode.condition.Condition;
import tools.devnull.robocode.event.EnemyScannedEvent;
import tools.devnull.robocode.event.Events;
import tools.devnull.robocode.util.Drawer;

import java.util.*;

import static tools.devnull.robocode.util.Drawer.Mode.TRANSPARENT;
import static java.awt.Color.LIGHT_GRAY;

/** @author Marcelo Guimarães */
public class EnemyTracker {

  private Map<String, List<Enemy>> enemyHistory;

  private int historySize;

  private final Bot bot;

  public EnemyTracker(Bot bot) {
    this(bot, 20);
  }

  public EnemyTracker(Bot bot, int historySize) {
    this.enemyHistory = new HashMap<String, List<Enemy>>();
    this.bot = bot;
    this.historySize = historySize;
  }

  @When(Events.ENEMY_SCANNED)
  public void registerEnemy(EnemyScannedEvent event) {
    Enemy enemy = event.enemy();
    if (!enemyHistory.containsKey(enemy.name())) {
      enemyHistory.put(enemy.name(), new LinkedList<Enemy>());
    }
    List<Enemy> history = enemyHistory.get(enemy.name());
    history.add(enemy);
    if (history.size() > historySize) {
      history.remove(0);
    }
  }

  public EnemyHistory historyFor(final Enemy enemy) {
    return new EnemyHistory() {

      @Override
      public List<Enemy> fromOldest() {
        if (!enemyHistory.containsKey(enemy.name())) {
          return Collections.emptyList();
        }
        return Collections.unmodifiableList(enemyHistory.get(enemy.name()));
      }

      @Override
      public List<Enemy> fromLatest() {
        if (!enemyHistory.containsKey(enemy.name())) {
          return Collections.emptyList();
        }
        List<Enemy> list = new ArrayList<Enemy>(enemyHistory.get(enemy.name()));
        Collections.reverse(list);
        return Collections.unmodifiableList(list);
      }
    };
  }

  @When(Events.DRAW)
  public void drawHistory(Drawer drawer) {
    Collection<Enemy> enemies = bot.radar().knownEnemies();
    for (Enemy enemy : enemies) {
      int i = 0;
      for (Enemy enemyHistory : historyFor(enemy).fromLatest()) {
        drawer.draw(TRANSPARENT, LIGHT_GRAY).circle().at(enemyHistory.location());
        if (++i == 10) {
          break;
        }
      }
    }
  }

  public boolean isEnemyStopped(Enemy enemy) {
    for (Enemy enemyHistory : historyFor(enemy).fromOldest()) {
      if (enemyHistory.isMoving()) {
        return false;
      }
    }
    return true;
  }

  public Condition targetStoped() {
    return new Condition() {
      @Override
      public boolean evaluate() {
        if (bot.radar().hasTargetSet()) {
          Enemy target = bot.radar().target();
          return isEnemyStopped(target);
        }
        return false;
      }
    };
  }

}
