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

package tools.devnull.robobundle.parts.movement;

import tools.devnull.robobundle.Bot;
import tools.devnull.robobundle.Enemy;
import tools.devnull.robobundle.calc.Angle;
import tools.devnull.robobundle.parts.MovingSystem;

import static java.lang.Math.cos;
import static java.lang.Math.random;

/** @author Marcelo Guimarães */
public class EnemyCircleMovingSystem implements MovingSystem {

  private static final double MOVEMENT_LENGTH = 75;

  private final Bot bot;

  public EnemyCircleMovingSystem(Bot bot) {
    this.bot = bot;
  }

  public void execute() {
    double ahead = cos(bot.radar().time() >> 4) * MOVEMENT_LENGTH * random();
    if (bot.radar().hasTargetSet()) {
      Enemy enemy = bot.radar().target();
      Angle heading = bot.body().heading();
      Angle angle = enemy.bearing().plus(Angle.PI_OVER_TWO);
      bot.log("Turning %s", angle);
      bot.log("Body is at %s", heading);
      bot.body().moveAndTurn(ahead, angle);
    }
  }

}
