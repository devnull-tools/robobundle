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

/** @author Marcelo Varella Barca Guimarães */
public class GravityPoint extends Point {

  private final double value;

  public GravityPoint(Point location, double value) {
    super(location.x(), location.y());
    this.value = value;
  }

  public GravityPoint(double x, double y, double value) {
    super(x, y);
    this.value = value;
  }

  public double value(){
    return value;
  }

  public Point force(Point reference) {
    Position bearing = bearingTo(reference);
    Angle angle = Angle.inRadians(Math.PI / 2 - Math.atan2(y() - reference.y(), x() - reference.x()));
    double distance = bearing.distance();
    double force = BotMath.areEquals(distance, 0) ?
      value : value / Math.pow(distance, 2);
    return new Point(angle.sin() * force, angle.cos() * force);
  }

  public TemporaryGravityPoint during(int time) {
    return new TemporaryGravityPoint(this, time);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    GravityPoint that = (GravityPoint) o;

    if (Double.compare(that.value, value) != 0) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    long temp;
    temp = value != +0.0d ? Double.doubleToLongBits(value) : 0L;
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s | %.4f", super.toString(), value);
  }
}

