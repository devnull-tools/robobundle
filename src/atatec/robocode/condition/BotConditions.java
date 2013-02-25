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
import atatec.robocode.Condition;
import atatec.robocode.Enemy;
import atatec.robocode.calc.Point;

import java.util.Collection;

/** @author Marcelo Varella Barca Guimarães */
public class BotConditions {

  private final Bot bot;

  public BotConditions(Bot bot) {
    this.bot = bot;
  }

  public TargetConditions target() {
    return new TargetConditions(bot.radar());
  }

  public RadarConditions radar() {
    return new RadarConditions(bot.radar());
  }

  public GunConditions gun() {
    return new GunConditions(bot.gun());
  }

  public BodyConditions body() {
    return new BodyConditions(bot.body());
  }

  public StatisticsConditions statistics() {
    return new StatisticsConditions(bot.statistics());
  }

  public Condition nextToWall(final double distance) {
    return new Condition() {
      @Override
      public boolean evaluate() {
        Point wall = bot.radar().battleField().closestWallPointTo(bot.location());
        return wall.bearingTo(bot.location()).distance() <= distance;
      }
    };
  }

  public Condition nextToEnemy(final double distance) {
    return new Condition() {
      @Override
      public boolean evaluate() {
        Collection<Enemy> enemies = bot.radar().knownEnemies();
        for (Enemy enemy : enemies) {
          if (enemy.distance() <= distance) {
            return true;
          }
        }
        return false;
      }
    };
  }

}
