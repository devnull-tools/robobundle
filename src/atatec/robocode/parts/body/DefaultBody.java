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

package atatec.robocode.parts.body;

import atatec.robocode.BaseBot;
import atatec.robocode.ConditionalCommand;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.calc.Position;
import atatec.robocode.parts.BasePart;
import atatec.robocode.parts.Body;
import atatec.robocode.parts.DefaultConditionalCommand;
import atatec.robocode.parts.MovingSystem;

import java.awt.Color;

/** @author Marcelo Varella Barca Guimarães */
public class DefaultBody extends BasePart implements Body {

  private final DefaultConditionalCommand<MovingSystem> movingSystem;

  private final BaseBot bot;

  public DefaultBody(BaseBot bot) {
    this.bot = bot;
    this.movingSystem = new DefaultConditionalCommand<MovingSystem>(bot);
  }

  @Override
  public void setColor(Color color) {
    bot.setBodyColor(color);
  }

  @Override
  public void move(double distance) {
    if (distance < 0) {
      moveBack(-distance);
    } else {
      moveAhead(distance);
    }
  }

  public void move() {
    movingSystem.execute();
  }

  public ConditionalCommand<MovingSystem> forMoving() {
    return movingSystem;
  }

  @Override
  public void moveAndTurn(double distance, Angle angle) {
    turn(angle);
    move(distance);
  }

  @Override
  public Body turnTo(Angle angle) {
    bot.setTurnRightRadians(heading().plus(angle).radians());
    return this;
  }

  @Override
  public Body turnTo(Point point) {
    Position position = bot.location().bearingTo(point);
    turn(position.angle().minus(bot.body().heading()));
    return this;
  }

  @Override
  public void moveTo(Point point) {
    moveTo(point, bot.location().bearingTo(point).distance());
  }

  @Override
  public void moveTo(Point point, double distance) {
    Position position = bot.location().bearingTo(point);
    Angle angle = position.angle().minus(bot.body().heading()).relative();
    bot.log("Angle: %s", angle);
    if (angle.radians() > Math.PI / 2) {
      angle = Angle.PI.minus(angle).inverse();
      distance = -distance;
    } else if (angle.radians() < -Math.PI / 2) {
      angle = Angle.PI.plus(angle).inverse();
      distance = -distance;
    }
    turn(angle);
    move(distance);
  }

  @Override
  public Angle heading() {
    return new Angle(bot.getHeadingRadians());
  }

  @Override
  public Angle turnRemaining() {
    return new Angle(bot.getTurnRemainingRadians());
  }

  @Override
  protected void turnLeft(Angle angle) {
    bot.log("Body Turn Left: %.4f", angle.degrees());
    bot.setTurnLeftRadians(angle.radians());
  }

  @Override
  protected void turnRight(Angle angle) {
    bot.log("Body Turn Right: %.4f", angle.degrees());
    bot.setTurnRightRadians(angle.radians());
  }

  protected void moveAhead(double distance) {
    bot.log("Ahead: %.4f", distance);
    bot.setAhead(distance);
  }

  protected void moveBack(double distance) {
    bot.log("Back: %.4f", distance);
    bot.setBack(distance);
  }

  @Override
  public double energy() {
    return bot.getEnergy();
  }

  @Override
  public double velocity() {
    return bot.getVelocity();
  }

}
