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
import tools.devnull.robocode.annotation.When;
import tools.devnull.robocode.calc.Point;
import tools.devnull.robocode.event.Events;
import tools.devnull.robocode.util.Drawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/** @author Marcelo Guimarães */
public class Avoider {

  private final Bot bot;
  private int threshold;
  private Point wallPoint;
  private Collection<Enemy> closestEnemies;

  public Avoider(Bot bot) {
    this.bot = bot;
    this.threshold = 40;
    this.closestEnemies = new ArrayList<Enemy>();
  }

  public Avoider notifyAt(int threshold) {
    this.threshold = threshold;
    return this;
  }

  @When(Events.NEXT_TURN)
  public void checkWalls() {
    wallPoint = bot.radar().battleField().closestBorderPoint(bot.location());
    if (isBotNearTo(wallPoint)) {
      bot.broadcast(Events.NEAR_TO_WALL, wallPoint);
    }
  }

  @When(Events.NEXT_TURN)
  public void checkEnemies() {
    Collection<Enemy> enemies = bot.radar().knownEnemies();
    closestEnemies.clear();
    for (Enemy enemy : enemies) {
      if (isBotNearTo(enemy.location())) {
        bot.broadcast(Events.NEAR_TO_ENEMY, enemy);
        closestEnemies.add(enemy);
      }
    }
  }

  private boolean isBotNearTo(Point point) {
    return point.bearingTo(bot.location()).distance() <= threshold;
  }

  @When(Events.DRAW)
  public void markClosestWall(Drawer drawer) {
    if (wallPoint != null) {
      drawer.draw(Color.RED).marker().at(wallPoint);
    }
  }

  @When(Events.DRAW)
  public void markClosestEnemies(Drawer drawer) {
    for (Enemy enemy : closestEnemies) {
      drawer.draw(Color.RED).marker().at(enemy.location());
    }
  }

}
