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

package atatec.robocode;

import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.calc.Position;

/**
 * Interface that defines an enemy bot.
 *
 * @author Marcelo Guimarães
 */
public interface Enemy extends Localizable {

  /**
   * @return the target's position on the battlefield
   */
  Position position();

  /**
   * @return the distance to the enemy
   */
  double distance();

  /**
   * @return the target energy
   */
  double energy();

  /**
   * @return the heading of the enemy
   */
  Angle heading();

  /**
   * @return the bearing to the enemy
   */
  Angle bearing();

  /**
   * @return the bearing plus the body heading
   */
  Angle absoluteBearing();

  /**
   * @return the velocity of the enemy
   */
  double velocity();

  /**
   * Calculates the velocity in a direction perpendicular to
   * the reference bot. If the enemy is moving in clockwise,
   * this method will return a poitive number, if the enemy
   * is moving in a counter-clockwise, a negative number
   * will be returned.
   *
   * @return the lateral velocity of the enemy
   */
  double lateralVelocity();

  /**
   * @return the enemy name
   */
  String name();

  /**
   * @return the enemy location
   */
  Point location();

  /**
   * Checks if the enemy is moving based on it velocity
   *
   * @return <code>true</code> if the enemy is moving
   */
  boolean isMoving();

  /**
   * Checks if the enemy is stopped based on it velocity
   *
   * @return <code>true</code> if the enemy is stopped
   */
  boolean isStopped();

  /**
   * @return the turn when this enemy was scanned
   */
  long when();

}
