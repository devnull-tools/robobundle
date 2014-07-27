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

import tools.devnull.robocode.Localizable;
import tools.devnull.robocode.util.GravityPointBuilder;

import static tools.devnull.robocode.calc.Angle.cos;
import static tools.devnull.robocode.calc.Angle.sin;
import static tools.devnull.robocode.calc.BotMath.areEquals;
import static tools.devnull.robocode.calc.BotMath.toBigecimal;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * A class that defines a point with two dimensions (x, y).
 * <p/>
 * All instances of this class are immutable.
 *
 * @author Marcelo Guimarães
 */
public class Point implements Localizable {

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
   * @return a new point to the left of this.
   */
  public Point left(double amount) {
    return new Point(x - amount, y);
  }

  /**
   * Returns a new point to the right of this.
   *
   * @param amount the distance to this point
   * @return a new point to the right of this.
   */
  public Point right(double amount) {
    return new Point(x + amount, y);
  }

  /**
   * Returns a new point above this.
   *
   * @param amount the distance to this point
   * @return a new point above this.
   */
  public Point up(double amount) {
    return new Point(x, y + amount);
  }

  /**
   * Returns a new point bellow this.
   *
   * @param amount the distance to this point
   * @return a new point bellow this.
   */
  public Point down(double amount) {
    return new Point(x, y - amount);
  }

  /**
   * Sums this point's coordinates with another point's coordinates.
   *
   * @param other the other point
   * @return a point (x + other.x, y + other.y)
   */
  public Point plus(Point other) {
    return new Point(x + other.x, y + other.y);
  }

  /**
   * Subtracts this point's coordinates with another point's coordinates.
   *
   * @param other the other point
   * @return a point (x - other.x, y - other.y)
   */
  public Point minus(Point other) {
    return new Point(x - other.x, y - other.y);
  }

  /**
   * Computes the bearing to another point.
   *
   * @param other the other point to compute the bearing
   * @return the bearing to the given point
   */
  public Position bearingTo(Point other) {
    return new Position(this, other);
  }

  /**
   * Calculates the distance from this point to the given point
   *
   * @param other the point to calculate the distance
   * @return the distance between this point and the given one.
   */
  public double distanceTo(Point other) {
    return distance(this, other);
  }

  /**
   * Moves this point using the given angle and value
   *
   * @param angle the angle to head
   * @param value the value to move
   * @return the moved point
   */
  public Point move(Angle angle, double value) {
    return this.plus(new Point(value * sin(angle), value * cos(angle)));
  }

  @Override
  public Point location() {
    return this;
  }

  /**
   * Calculates the angle that covers the two given points using this point as the
   * reference
   */
  public Angle angleOfView(Point p2, Point p3) {
    Angle angle;
    double a = distance(this, p2);
    double b = distance(this, p3);
    double c = distance(p2, p3);
    //cosine rule
    double cosGama = (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b);
    angle = new Angle(Math.acos(cosGama));
    if (Double.isNaN(angle.radians())) {
      angle = Angle.ZERO;
    }
    return angle;
  }

  private static double distance(Point p1, Point p2) {
    return sqrt(pow(p1.x() - p2.x(), 2) + pow(p1.y() - p2.y(), 2));
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
