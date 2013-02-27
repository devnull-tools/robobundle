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

package atatec.robocode;

import atatec.robocode.calc.Point;
import robocode.Robot;

/** @author Marcelo Guimarães */
public class BattleField implements Field {

  private final double height;

  private final double width;

  private final double botSize = 18.0;

  public BattleField(Robot robot) {
    height = robot.getBattleFieldHeight();
    width = robot.getBattleFieldWidth();
  }

  @Override
  public double height() {
    return height;
  }

  @Override
  public double width() {
    return width;
  }

  @Override
  public Point center() {
    return new Point(width / 2, height / 2);
  }

  @Override
  public Point downRight() {
    return new Point(width, 0);
  }

  @Override
  public Point upRight() {
    return new Point(width, height);
  }

  @Override
  public Point upLeft() {
    return new Point(0, height);
  }

  @Override
  public Point downLeft() {
    return new Point(botSize, botSize);
  }

  @Override
  public double diagonal() {
    return downLeft().bearingTo(upRight()).distance();
  }

  @Override
  public boolean isOnField(Point p) {
    return p.x() < 0
      || p.y() < 0
      || p.x() > width
      || p.y() > height;
  }

  @Override
  public Point normalize(Point p) {
    if (isOnField(p)) {
      return p;
    }
    double x = Math.min(Math.max(botSize, p.x()), width - botSize);
    double y = Math.min(Math.max(botSize, p.y()), height - botSize);
    return new Point(x, y);
  }

  @Override
  public Point closestWallPointTo(Point point) {
    Point p = normalize(point);
    Point a, b;
    if (p.x() < width / 2) {
      // on left side
      a = new Point(0, p.y());
    } else {
      // on right side
      a = new Point(width, p.y());
    }
    if (p.y() < height / 2) {
      b = new Point(p.x(), 0);
    } else {
      b = new Point(p.x(), height);
    }
    return p.bearingTo(a).distance() < p.bearingTo(b).distance() ? a : b;
  }

}
