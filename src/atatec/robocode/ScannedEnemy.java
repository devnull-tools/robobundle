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

import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.calc.Position;
import robocode.ScannedRobotEvent;

import static atatec.robocode.calc.Angle.cos;
import static atatec.robocode.calc.Angle.sin;

/**
 * @author Marcelo Guimarães
 */
public final class ScannedEnemy implements Enemy {

  private String name;
  private Position position;
  private double distance;
  private double energy;
  private double velocity;
  private Angle heading;
  private Angle bearing;
  private Angle absoluteBearing;
  private Point location;
  private long time;

  public ScannedEnemy(Bot bot, ScannedRobotEvent event) {
    this.position = new Position(
      new Angle(event.getBearingRadians()),
      event.getDistance()
    );
    this.distance = event.getDistance();
    this.energy = event.getEnergy();
    this.heading = new Angle(event.getHeadingRadians());
    this.bearing = new Angle(event.getBearingRadians());
    this.velocity = event.getVelocity();
    this.name = event.getName();
    absoluteBearing = bot.body().heading().plus(bearing);
    Point botLocation = bot.location();
    double enemyX = botLocation.x() + distance * sin(absoluteBearing);
    double enemyY = botLocation.y() + distance * cos(absoluteBearing);
    this.location = new Point(enemyX, enemyY);
    this.time = event.getTime();
  }

  @Override
  public Position position() {
    return position;
  }

  @Override
  public double distance() {
    return distance;
  }

  @Override
  public double energy() {
    return energy;
  }

  @Override
  public Angle heading() {
    return heading;
  }

  @Override
  public Angle bearing() {
    return bearing;
  }

  @Override
  public double velocity() {
    return velocity;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Point location() {
    return location;
  }

  @Override
  public long when() {
    return time;
  }

  @Override
  public Angle absoluteBearing() {
    return absoluteBearing;
  }

  @Override
  public boolean isMoving() {
    return velocity() > 0;
  }

  @Override
  public boolean isStopped() {
    return !isMoving();
  }

  @Override
  public double lateralVelocity() {
    return velocity() * sin(heading().minus(absoluteBearing()));
  }

}
