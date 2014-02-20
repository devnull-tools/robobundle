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

import java.awt.*;

/**
 * @author Marcelo Guimarães
 */
public class DefaultBody extends BasePart implements Body {

  private final DefaultConditionalCommand<MovingSystem> movingSystem;

  public DefaultBody(BaseBot bot) {
    super(bot);
    this.movingSystem = new DefaultConditionalCommand<MovingSystem>(bot);
  }

  @Override
  public void setColor(Color color) {
    bot.setBodyColor(color);
  }

  @Override
  public void move(double distance) {
    bot.log("Ahead: %.4f", distance);
    bot.setAhead(distance);
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
  public void moveTo(Point point, double amount) {
    Point location = bot.location();
    Point head = location.move(heading(), location.distanceTo(point));
    Angle angle = location.angleOfView(head, point);
    if (isAtLeft(point)) {
      angle = angle.inverse();
    }
    if (isInBack(point)) {
      angle = Angle.PI.minus(angle).inverse();
      amount = -amount;
    }
    turn(angle);
    move(amount);
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
  public void turn(Angle angle) {
    bot.log("Body Turn : %.4f", angle.degrees());
    bot.setTurnRightRadians(angle.radians());
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
