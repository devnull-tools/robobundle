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
import atatec.robocode.Field;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.parts.AimingSystem;
import atatec.robocode.util.Drawer;
import robocode.Rules;

import static atatec.robocode.event.Events.DRAW;
import static java.awt.Color.RED;

/** @author Marcelo Guimarães */
public class PredictionAimingSystem implements AimingSystem {

  private final Bot bot;
  private Point predictedLocation;

  public PredictionAimingSystem(Bot bot) {
    this.bot = bot;
  }

  public void execute() {
    double bulletSpeed = Rules.getBulletSpeed(bot.gun().power());
    Point botLocation = bot.location();
    if (bot.radar().hasLockedTarget()) {
      Enemy enemy = bot.radar().target();
      Angle enemyHeading = enemy.heading();
      double enemyVelocity = enemy.velocity();

      double deltaTime = 0;
      Field battleField = bot.radar().battleField();
      predictedLocation = enemy.location();
      while ((++deltaTime) * bulletSpeed < botLocation.bearingTo(predictedLocation).distance()) {
        predictedLocation = predictedLocation.plus(new Point(
          enemyHeading.sin() * enemyVelocity,
          enemyHeading.cos() * enemyVelocity
        ));
        // check bounds
        if (!battleField.isOnField(predictedLocation)) {
          // out of bounds
          predictedLocation = battleField.normalize(predictedLocation);
          break;
        }
      }
      predictedLocation = battleField.normalize(predictedLocation);
      bot.gun().aimTo(predictedLocation);
    } else {
      predictedLocation = null;
    }
  }

  @When(DRAW)
  public void draw(Drawer drawer) {
    if (predictedLocation != null) {
      drawer.draw(RED).cross().at(predictedLocation);
    }
  }

}
