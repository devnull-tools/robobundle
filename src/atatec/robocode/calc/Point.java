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

/**
 * A class that defines a point with two dimensions (x, y).
 * <p/>
 * All instances of this class are immutable.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class Point {

  /** Holds the (0,0) point. */
  public static final Point ORIGIN = new Point(0, 0);

  private final double x, y;

  /**
   * Creates a new point with the given coordinates
   *
   * @param x the <code>x</code> coordinate
   * @param y the <code>y</code> coordinate
   */
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the <code>x</code> coordinate
   *
   * @return the <code>x</code> coordinate
   */
  public final double x() {
    return x;
  }

  /**
   * Returns the <code>y</code> coordinate
   *
   * @return the <code>y</code> coordinate
   */
  public final double y() {
    return y;
  }

  /**
   * Returns a new point to the left of this.
   *
   * @param amount the distance to this point
   *
   * @return a new point to the left of this.
   */
  public Point left(double amount) {
    return new Point(x - amount, y);
  }

  /**
   * Returns a new point to the right of this.
   *
   * @param amount the distance to this point
   *
   * @return a new point to the right of this.
   */
  public Point right(double amount) {
    return new Point(x + amount, y);
  }

  /**
   * Returns a new point above this.
   *
   * @param amount the distance to this point
   *
   * @return a new point above this.
   */
  public Point up(double amount) {
    return new Point(x, y + amount);
  }

  /**
   * Returns a new point bellow this.
   *
   * @param amount the distance to this point
   *
   * @return a new point bellow this.
   */
  public Point down(double amount) {
    return new Point(x, y - amount);
  }

  /**
   * Sums this point's coordinates with another point's coordinates.
   *
   * @param other the other point
   *
   * @return a point (x + other.x, y + other.y)
   */
  public Point plus(Point other) {
    return new Point(x + other.x, y + other.y);
  }

  /**
   * Subtracts this point's coordinates with another point's coordinates.
   *
   * @param other the other point
   *
   * @return a point (x - other.x, y - other.y)
   */
  public Point minus(Point other) {
    return new Point(x - other.x, y - other.y);
  }

  /**
   * Computes the bearing to another point.
   *
   * @param other the other point to compute the bearing
   *
   * @return the bearing to the given point
   */
  public Position bearingTo(Point other) {
    return new Position(this, other);
  }

  /**
   * Starts to create a {@link GravityPoint gravity point} using this point's location.
   *
   * @return a builder for creating the gravity point.
   */
  public GravityPointBuilder gravitational() {
    return GravityPointBuilder.gravityPoint().at(this);
  }

  /**
   * Starts to create an {@link GravityPoint anti-gravity point} using this point's
   * location.
   *
   * @return a builder for creating the anti-gravity point.
   */
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
