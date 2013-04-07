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

package atatec.robocode.parts;

import atatec.robocode.condition.Condition;
import atatec.robocode.ConditionalCommand;
import atatec.robocode.calc.Point;

import java.awt.Color;

/**
 * Interface that defines a robot gun.
 * <p/>
 * A gun can use an {@link AimingSystem} to point the gun to the right direction and a
 * {@link FiringSystem} to fire the gun using the best power. The behaviour of any system
 * can be changed at runtime based on giving {@link Condition conditions}.
 *
 * @author Marcelo Guimarães
 */
public interface Gun extends Part {

  /**
   * Points the gun to aim on the given point.
   *
   * @param point the point that the gun should aim
   *
   * @return a reference to this object.
   */
  Gun aimTo(Point point);

  void fire(double power);

  Gun aim();

  void fireIfTargetSet();

  void fireIf(Condition condition);

  void fire();

  double power();

  double heat();

  double coolingRate();

  ConditionalCommand<AimingSystem> forAiming();

  ConditionalCommand<FiringSystem> forFiring();

  void setBulletColor(Color color);

}
