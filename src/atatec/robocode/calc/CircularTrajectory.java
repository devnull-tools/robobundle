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

import java.util.ArrayList;
import java.util.List;

/** @author Marcelo Guimarães */
public class CircularTrajectory {

  private Point center;
  private int direction = 1;
  private double distanceBetweenPoints = 5;

  public CircularTrajectory() {
  }

  public CircularTrajectory at(Point origin) {
    this.center = origin;
    return this;
  }

  public CircularTrajectory reversed() {
    this.direction = -1;
    return this;
  }

  public CircularTrajectory distancing(double distance) {
    this.distanceBetweenPoints = distance;
    return this;
  }

  public List<Point> from(Point from) {
    List<Point> points = new ArrayList<Point>(100);
    points.add(from);
    double radius = center.distanceTo(from);
    Angle t = center.bearingTo(from).angle();
    double perimeter = Math.PI * 2 * radius;
    int numberOfPoints = (int) (perimeter / distanceBetweenPoints);
    double angleStep = direction * (Math.PI * 2) / numberOfPoints;
    while (--numberOfPoints > 0) {
      points.add(new Point(
        center.x() + (radius * t.sin()),
        center.y() + (radius * t.cos())
      ));
      t = t.plus(angleStep);
    }
    return points;
  }

}
