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

package tools.devnull.robobundle.parts;

import tools.devnull.robobundle.ConditionalCommand;
import tools.devnull.robobundle.calc.Angle;
import tools.devnull.robobundle.calc.Point;

/**
 * Interface that defines a robot body.
 * <p/>
 * The body may use a {@link MovingSystem} to tell how the
 * robot should move. The behaviour of any system can be changed at runtime
 * based on giving {@link tools.devnull.robobundle.condition.Condition conditions}.
 *
 * @author Marcelo Guimarães
 */
public interface Body extends Part {

  /**
   * Turns the body to the given angle
   *
   * @param angle the angle that the body should face after turning
   * @return a reference to this object
   */
  Body turnTo(Angle angle);

  /**
   * Turns the body to face the given point
   *
   * @param point the point that the body should face after turning
   * @return a reference to this object
   */
  Body turnTo(Point point);

  /**
   * Moves the robot
   *
   * @param distance the distance to move
   */
  void move(double distance);

  /**
   * Moves the robot in an angular movement
   *
   * @param distance the distance to move
   * @param angle    the angle to turn
   */
  void moveAndTurn(double distance, Angle angle);

  /**
   * Moves the robot using an active {@link MovingSystem}
   */
  void move();

  /**
   * Moves the robot to a given point in the battlefield.
   *
   * @param point  the point that the body should be located after moving
   * @param amount the amount to move
   */
  void moveTo(Point point, double amount);

  /**
   * Gets the robot's energy
   *
   * @return the robot's energy
   */
  double energy();

  /**
   * Gets the robot's velocity
   *
   * @return the robot's velocity
   */
  double velocity();

  /**
   * Gets the register for the robot's {@link MovingSystem}
   *
   * @return the register for the robot's {@link MovingSystem}
   */
  ConditionalCommand<MovingSystem> forMoving();

}
