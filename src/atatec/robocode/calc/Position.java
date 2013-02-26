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

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/** @author Marcelo Varella Barca Guimarães */
public class Position {

  private final double distance;

  private final Angle angle;

  public Position(Angle angle, double distance) {
    this.angle = angle;
    this.distance = distance;
  }

  public Position(Point p1, Point p2) {
    this.distance = distance(p1, p2);
    Point p3 = new Point(p1.x(), p1.y() + 10);
    /*Triangle t = new Triangle(distance, distance(p1, p3), distance(p3, p2));
    Angle angle = t.angle(Triangle.ID.C);
    if (Double.isNaN(angle.radians())) {
      angle = Angle.ZERO;
    }*/
    Angle angle = p1.viewAngle(p2, p3);
    //using robocode convention
    this.angle = (p2.x() >= p1.x() ? angle : angle.inverse());
  }

  public double distance() {
    return distance;
  }

  public Angle angle() {
    return angle;
  }

  private double distance(Point p1, Point p2) {
    return sqrt(pow(p1.x() - p2.x(), 2) + pow(p1.y() - p2.y(), 2));
  }

  public String toString() {
    return String.format("(%.2f, %s)", distance, angle);
  }

}
