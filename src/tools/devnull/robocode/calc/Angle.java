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

package tools.devnull.robocode.calc;

import robocode.util.Utils;

import static tools.devnull.robocode.calc.BotMath.areEquals;
import static tools.devnull.robocode.calc.BotMath.toBigecimal;

/** @author Marcelo Guimarães */
public class Angle {

  public static final Angle ZERO = new Angle(0);

  public static final Angle PI = new Angle(Math.PI);

  public static final Angle TWO_PI = new Angle(Math.PI * 2);

  public static final Angle PI_OVER_TWO = new Angle(Math.PI / 2);

  public static final Angle MINUS_PI_OVER_TWO = new Angle(-Math.PI / 2);

  public static final Angle PI_OVER_FOUR = new Angle(Math.PI / 4);

  public static final Angle MINUS_PI_OVER_FOUR = new Angle(-Math.PI / 4);

  private final double radians;

  public Angle(double radians) {
    this.radians = radians;
  }

  public double radians() {
    return radians;
  }

  public double degrees() {
    return Math.toDegrees(radians);
  }

  public Angle plus(Angle angle) {
    return new Angle(radians + angle.radians);
  }

  public Angle plus(double angle) {
    return plus(new Angle(angle));
  }

  public Angle minus(Angle angle) {
    return new Angle(radians - angle.radians);
  }

  public Angle minus(double angle) {
    return minus(new Angle(angle));
  }

  public Angle inverse() {
    return new Angle(-radians);
  }

  public static double cos(Angle angle) {
    return Math.cos(angle.radians());
  }

  public static double sin(Angle angle) {
    return Math.sin(angle.radians());
  }

  public static double tan(Angle angle) {
    return Math.tan(angle.radians());
  }

  public Angle toRight() {
    return new Angle(Math.abs(radians));
  }

  public Angle toLeft() {
    return new Angle(-Math.abs(radians));
  }

  public boolean isNorth() {
    return radians >= MINUS_PI_OVER_TWO.radians() && radians <= PI_OVER_TWO.radians();
  }

  public boolean isSouth() {
    return !isNorth();
  }

  public boolean isEast() {
    return radians >= 0 && radians <= Math.PI;
  }

  public boolean isWest() {
    return !isEast();
  }

  public Angle relative() {
    return new Angle(Utils.normalRelativeAngle(radians));
  }

  public Angle absolute() {
    return new Angle(Utils.normalAbsoluteAngle(radians));
  }

  @Override
  public String toString() {
    return String.format("%.6fd", degrees());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Angle angle = (Angle) o;

    return areEquals(angle.radians(), radians());
  }

  @Override
  public int hashCode() {
    return toBigecimal(radians).hashCode();
  }

  public static Angle inDegrees(double degrees) {
    return new Angle(Math.toRadians(degrees));
  }

  public static Angle inRadians(double radians) {
    return new Angle(radians);
  }

}
