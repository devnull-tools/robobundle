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

package atatec.robocode.parts.movement;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.event.Events;
import atatec.robocode.parts.MovingSystem;
import robocode.HitRobotEvent;

import static java.lang.Math.random;

/** @author Marcelo Guimarães */
public class FollowEnemyMovingSystem implements MovingSystem {

  private static final double MOVEMENT_LENGTH = 100;

  private final Bot bot;

  public FollowEnemyMovingSystem(Bot bot) {
    this.bot = bot;
  }

  public void execute() {
    double ahead = MOVEMENT_LENGTH * random();
    Enemy target = bot.radar().target();
    if (target != null) {
      bot.body().moveAndTurn(ahead, target.bearing().plus(Angle.inDegrees(22.5)));
    }
  }

  @When(Events.HIT_ROBOT)
  public void onHitRobot(HitRobotEvent event) {
    double ahead = MOVEMENT_LENGTH * random();
    bot.body().move(-ahead);
  }

}
