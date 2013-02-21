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
import atatec.robocode.event.EnemyScannedEvent;
import atatec.robocode.event.Events;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** @author Marcelo Varella Barca Guimarães */
public class EnemyHistory {

  private Map<String, List<Enemy>> enemyHistory;

  private int historySize;

  private final Bot bot;

  public EnemyHistory(Bot bot) {
    this(bot, 20);
  }

  public EnemyHistory(Bot bot, int historySize) {
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
    history.add(0, enemy);
    if(history.size() > historySize) {
      history.remove(history.size());
    }
  }

  public List<Enemy> historyFor(Enemy enemy) {
    if (!enemyHistory.containsKey(enemy.name())) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(enemyHistory.get(enemy.name()));
  }

}
