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

package tools.devnull.robobundle.condition;

import tools.devnull.robobundle.Bot;
import tools.devnull.robobundle.Enemy;
import tools.devnull.robobundle.annotation.When;
import tools.devnull.robobundle.calc.Point;
import tools.devnull.robobundle.event.Events;
import tools.devnull.robobundle.util.Drawer;

import java.awt.*;
import java.util.Collection;
import java.util.function.Function;

/**
 * @author Marcelo Guimarães
 */
public class StrengthBasedLockCondition implements LockCondition {

  private enum LockMode {
    STRONGER {
      @Override
      public boolean canLock(double lockedStr, double candidateStr) {
        return candidateStr > lockedStr;
      }
    },
    WEAKER {
      @Override
      public boolean canLock(double lockedStr, double candidateStr) {
        return candidateStr < lockedStr;
      }
    };

    public abstract boolean canLock(double lockedStr, double candidateStr);
  }

  private final Bot bot;
  private LockMode mode = LockMode.STRONGER;
  private Function<Enemy, Double> strengthFunction = Enemy::energy;

  public StrengthBasedLockCondition(Bot bot) {
    this.bot = bot;
  }

  public StrengthBasedLockCondition use(Function<Enemy, Double> strengthFunction) {
    this.strengthFunction = strengthFunction;
    return this;
  }

  public StrengthBasedLockCondition lockWeaker() {
    this.mode = LockMode.WEAKER;
    return this;
  }

  public StrengthBasedLockCondition lockStronger() {
    this.mode = LockMode.STRONGER;
    return this;
  }

  @Override
  public boolean canLock(Enemy enemy) {
    double candidateEnemyStr = strengthFunction.apply(enemy);
    double lockedEnemyStr = strengthFunction.apply(bot.radar().target());
    return mode.canLock(lockedEnemyStr, candidateEnemyStr);
  }

  @When(Events.DRAW)
  public void drawStrength(Drawer drawer) {
    Collection<Enemy> enemies = bot.radar().knownEnemies();
    Point point;
    for (Enemy enemy : enemies) {
      point = enemy.location().right(25);
      if (!bot.radar().battleField().isOnField(point.right(30))) {
        point = enemy.location().left(60);
      }
      drawer.draw(Color.RED
      ).string("%.3f", strengthFunction.apply(enemy)).at(point);
    }
  }

}
