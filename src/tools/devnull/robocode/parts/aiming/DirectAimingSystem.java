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

package tools.devnull.robocode.parts.aiming;

import tools.devnull.robocode.Bot;
import tools.devnull.robocode.Enemy;
import tools.devnull.robocode.annotation.When;
import tools.devnull.robocode.calc.Angle;
import tools.devnull.robocode.calc.Point;
import tools.devnull.robocode.parts.AimingSystem;
import tools.devnull.robocode.util.Drawer;

import static tools.devnull.robocode.event.Events.DRAW;
import static java.awt.Color.RED;

/** @author Marcelo Guimarães */
public class DirectAimingSystem implements AimingSystem {

  private final Bot bot;
  private Point enemyLocation;

  public DirectAimingSystem(Bot bot) {
    this.bot = bot;
  }

  public void execute() {
    Enemy enemy = bot.radar().target();
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
