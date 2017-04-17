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

package tools.devnull.robobundle.parts.movement;

import tools.devnull.robobundle.Bot;
import tools.devnull.robobundle.annotation.When;
import tools.devnull.robobundle.calc.GravityPoint;
import tools.devnull.robobundle.calc.Point;
import tools.devnull.robobundle.calc.TemporaryGravityPoint;
import tools.devnull.robobundle.event.Events;
import tools.devnull.robobundle.parts.MovingSystem;
import tools.devnull.robobundle.util.Drawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static java.lang.Math.random;

/** @author Marcelo Guimarães */
public class GravitationalMovingSystem implements MovingSystem {

  public static final String LOW_ENFORCING = "GravitationalMovingSystem.LOW_ENFORCING";

  private final Bot bot;
  private Collection<GravityPoint> fixedPoints = new HashSet<GravityPoint>(100);
  private Collection<TemporaryGravityPoint> temporaryPoints = new HashSet<TemporaryGravityPoint>(100);
  private Point forcePoint;
  private double lowEnforcing;
  private boolean drawTemporaryPoints;

  public GravitationalMovingSystem(Bot bot) {
    this.bot = bot;
  }

  public GravitationalMovingSystem lowEnforcingAt(double forcePointDistance) {
    lowEnforcing = forcePointDistance;
    return this;
  }

  public GravitationalMovingSystem add(GravityPoint point) {
    bot.log("Adding gravity point: %s", point);
    fixedPoints.add(point);
    return this;
  }

  public GravitationalMovingSystem add(TemporaryGravityPoint point) {
    bot.log("Adding temp gravity point: %s", point);
    temporaryPoints.add(point);
    return this;
  }

  public GravitationalMovingSystem drawTemporaryPoints() {
    drawTemporaryPoints = true;
    return this;
  }

  @Override
  public void execute() {
    Collection<GravityPoint> gPoints = new ArrayList<GravityPoint>(fixedPoints);
    Iterator<TemporaryGravityPoint> iterator = temporaryPoints.iterator();
    while (iterator.hasNext()) {
      TemporaryGravityPoint temporaryGravityPoint = iterator.next();
      GravityPoint point = temporaryGravityPoint.pull();
      if (temporaryGravityPoint.expired()) {
        iterator.remove();
      } else if (!temporaryGravityPoint.delayed()) {
        gPoints.add(point);
      }
    }
    move(gPoints);
  }

  private void move(Collection<GravityPoint> points) {
    Point location = bot.location();
    forcePoint = location;
    for (GravityPoint point : points) {
      forcePoint = forcePoint.plus(point.force(location));
    }
    bot.log("Location: %s", location);
    bot.log("Forced Location: %s", forcePoint);
    if (isLowEnforcing()) {
      bot.broadcast(LOW_ENFORCING, forcePoint);
      forcePoint = null;
      return;
    }
    bot.body().moveTo(forcePoint, 10 + (random() * 50));
  }

  private boolean isLowEnforcing() {
    return bot.location().bearingTo(forcePoint).distance() <= lowEnforcing;
  }

  @When(Events.DRAW)
  public void drawForcePoint(Drawer drawer) {
    if (forcePoint != null) {
      drawer.draw(Color.MAGENTA).circle().at(forcePoint);
      drawer.draw(Color.RED).marker().at(forcePoint);
    }
  }

  @When(Events.DRAW)
  public void drawTemporaryGravityPoints(Drawer drawer) {
    if (drawTemporaryPoints) {
      for (TemporaryGravityPoint temporaryPoint : temporaryPoints) {
        if (!temporaryPoint.expired() && !temporaryPoint.delayed()) {
          drawer.draw(Color.ORANGE).cross().at(temporaryPoint.point());
        }
      }
    }
  }

}
