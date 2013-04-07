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

package atatec.robocode.condition;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.event.Events;
import atatec.robocode.util.Drawer;

import java.awt.Color;
import java.util.Collection;

/** @author Marcelo Guimarães */
public class StrengthBasedLockCondition implements LockCondition {

  private final Bot bot;
  private final Function<Enemy, Double> strengthFunction;

  public StrengthBasedLockCondition(Bot bot, Function<Enemy, Double> strengthFunction) {
    this.bot = bot;
    this.strengthFunction = strengthFunction;
  }

  @Override
  public boolean canLock(Enemy enemy) {
    if (bot.radar().hasTargetSet()) {
      double lastSeenStr = strengthFunction.evaluate(enemy);
      double lockedStr = strengthFunction.evaluate(bot.radar().target());
      return lastSeenStr < lockedStr;
    }
    return true;
  }

  @When(Events.DRAW)
  public void drawStrength(Drawer drawer) {
    Collection<Enemy> enemies = bot.radar().knownEnemies();
    for (Enemy enemy : enemies) {
      drawer.draw(Color.LIGHT_GRAY
      ).string("%.4f", strengthFunction.evaluate(enemy)).at(enemy.location());
    }
  }

}
