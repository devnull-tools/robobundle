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
import atatec.robocode.calc.Point;
import atatec.robocode.util.Drawer;

import static atatec.robocode.event.Events.DRAW;
import static java.awt.Color.GREEN;
import static java.awt.Color.ORANGE;
import static java.awt.Color.YELLOW;

/** @author Marcelo Varella Barca Guimarães */
public class EnemyScannerInfo {

  private final Bot bot;

  private boolean showAttributes;

  public EnemyScannerInfo(Bot bot) {
    this.bot = bot;
  }

  public EnemyScannerInfo showAttributes() {
    this.showAttributes = true;
    return this;
  }

  @When(DRAW)
  public void paint(Drawer drawer) {
    for (Enemy enemy : bot.radar().knownEnemies()) {
      paintEnemy(drawer, enemy);
    }
  }

  private void paintEnemy(Drawer drawer, Enemy enemy) {
    Point location = enemy.location();
    if(enemy == bot.radar().lockedTarget()) {
      drawer.draw(GREEN).circle().at(location);
      drawer.draw(ORANGE).string(enemy.name()).at(bot.location().down(18));
    } else {
      drawer.draw(YELLOW).circle().at(location);
    }
    if(showAttributes) {
      drawer.draw(GREEN).string("%.3f", enemy.distance()).at(enemy.location());
      drawer.draw(GREEN).string("%.3f", enemy.velocity()).at(enemy.location().down(15));
      drawer.draw(GREEN).string("%s", enemy.location()).at(enemy.location().down(30));
    }
  }

}
