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

import atatec.robocode.Field;

import java.util.ArrayList;
import java.util.List;

/** @author Marcelo Guimarães */
public class BulletTrajectory {

  private final Field battleField;
  private Point from;
  private double distanceBetweenPoints = 5;

  public BulletTrajectory(Field battleField) {
    this.battleField = battleField;
  }

  public BulletTrajectory from(Point from) {
    this.from = from;
    return this;
  }

  public BulletTrajectory distancing(double distanceBetweenPoints) {
    this.distanceBetweenPoints = distanceBetweenPoints;
    return this;
  }

  public List<Point> to(Point target) {
    Position bearing = from.bearingTo(target);
    List<Point> points = new ArrayList<Point>(100);
    points.add(from);
    Point point = from;
    while (battleField.isOnField(point)) {
      point = new Point(
        point.x() + distanceBetweenPoints * bearing.angle().absolute().sin(),
        point.y() + distanceBetweenPoints * bearing.angle().absolute().cos()
      );
      points.add(point);
    }

    return points;
  }

}
