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

package tools.devnull.robocode.util;

import tools.devnull.robocode.calc.GravityPoint;
import tools.devnull.robocode.calc.Point;

/**
 * @author Marcelo Guimarães
 */
public class GravityPointBuilder {

  private static double WEAKEST_VALUE = 1;
  private static double WEAK_VALUE = 10;
  private static double NORMAL_VALUE = 30;
  private static double STRONG_VALUE = 60;
  private static double STRONGEST_VALUE = 1000;

  private Point point;
  private int mod;

  private GravityPointBuilder(int mod) {
    this.mod = mod;
  }

  public GravityPointBuilder at(Point point) {
    this.point = point;
    return this;
  }

  public GravityPoint weakest() {
    return withValue(WEAKEST_VALUE);
  }

  public GravityPoint weak() {
    return withValue(WEAK_VALUE);
  }

  public GravityPoint normal() {
    return withValue(NORMAL_VALUE);
  }

  public GravityPoint strong() {
    return withValue(STRONG_VALUE);
  }

  public GravityPoint strongest() {
    return withValue(STRONGEST_VALUE);
  }

  public GravityPoint withValue(double value) {
    return new GravityPoint(point, mod * value);
  }

  public static GravityPointBuilder antiGravityPoint() {
    return new GravityPointBuilder(-1);
  }

  public static GravityPointBuilder gravityPoint() {
    return new GravityPointBuilder(1);
  }

}
