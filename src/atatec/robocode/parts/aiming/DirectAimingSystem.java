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

package atatec.robocode.parts.aiming;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.parts.AimingSystem;
import atatec.robocode.util.Drawer;

import static atatec.robocode.event.Events.DRAW;
import static java.awt.Color.RED;

/** @author Marcelo Guimarães */
public class DirectAimingSystem implements AimingSystem {

  private final Bot bot;
  private Point enemyLocation;

  public DirectAimingSystem(Bot bot) {
    this.bot = bot;
  }

  public void execute() {
    Enemy enemy = bot.radar().locked();
    if (enemy != null) {
      enemyLocation = enemy.location();
      Angle angle = enemy.bearing()
        .plus(bot.body().heading())
        .minus(bot.gun().heading());
      bot.gun().turn(angle);
    }
  }

  @When(DRAW)
  public void paint(Drawer drawer) {
    if (enemyLocation != null) {
      drawer.draw(RED).cross().at(enemyLocation);
    }
  }

}
