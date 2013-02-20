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

package atatec.robocode.calc;

import atatec.robocode.util.GravityPointBuilder;

import static atatec.robocode.calc.BotMath.areEquals;
import static atatec.robocode.calc.BotMath.toBigecimal;

/** @author Marcelo Varella Barca Guimarães */
public class Point {

  public static final Point ORIGIN = new Point(0, 0);

  private final double x, y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }

  public Point left(double amount) {
    return new Point(x - amount, y);
  }

  public Point right(double amount) {
    return new Point(x + amount, y);
  }

  public Point up(double amount) {
    return new Point(x, y + amount);
  }

  public Point down(double amount) {
    return new Point(x, y - amount);
  }

  public java.awt.Point toAwtPoint() {
    return new java.awt.Point((int) x, (int) y);
  }

  public Point plus(Point other) {
    return new Point(x + other.x, y + other.y);
  }

  public Point minus(Point other) {
    return new Point(x - other.x, y - other.y);
  }

  public Position bearingTo(Point other) {
    return new Position(this, other);
  }

  public GravityPointBuilder gravitational() {
    return GravityPointBuilder.gravityPoint().at(this);
  }

  public GravityPointBuilder antiGravitational() {
    return GravityPointBuilder.antiGravityPoint().at(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Point point = (Point) o;

    return areEquals(x, point.x) && areEquals(y, point.y);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 37 * result + toBigecimal(x).hashCode();
    result = 37 * result + toBigecimal(y).hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("(%.5f , %.5f)", x, y);
  }

}
