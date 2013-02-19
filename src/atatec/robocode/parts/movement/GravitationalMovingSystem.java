package atatec.robocode.parts.movement;

import atatec.robocode.Bot;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.GravityPoint;
import atatec.robocode.calc.Point;
import atatec.robocode.calc.TemporaryGravityPoint;
import atatec.robocode.event.Events;
import atatec.robocode.parts.MovingSystem;
import atatec.robocode.util.Drawer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static atatec.robocode.event.Events.ADD_GRAVITY_POINT;

/** @author Marcelo Varella Barca Guimarães */
public class GravitationalMovingSystem implements MovingSystem {

  private final Bot bot;
  private Collection<GravityPoint> fixedPoints = new HashSet<GravityPoint>(100);
  private Collection<TemporaryGravityPoint> temporaryPoints = new HashSet<TemporaryGravityPoint>(100);
  private Point forcePoint;

  public GravitationalMovingSystem(Bot bot) {
    this.bot = bot;
  }

  @When(ADD_GRAVITY_POINT)
  public GravitationalMovingSystem add(GravityPoint point) {
    bot.log("Adding gravity point: %s", point);
    fixedPoints.add(point);
    return this;
  }

  @When(ADD_GRAVITY_POINT)
  public GravitationalMovingSystem add(TemporaryGravityPoint point) {
    bot.log("Adding temp gravity point: %s", point);
    temporaryPoints.add(point);
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
      } else {
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
    bot.body().moveTo(forcePoint, 10);
  }

  @When(Events.DRAW)
  public void drawForcePoint(Drawer drawer) {
    if (forcePoint != null) {
      drawer.draw(Color.MAGENTA).marker().at(forcePoint);
    }
  }

  @When(Events.DRAW)
  public void drawTemporaryGravityPoints(Drawer drawer) {
    bot.log("Drawing %d temporary gravity points", temporaryPoints.size());
    for (TemporaryGravityPoint gravityPoint : temporaryPoints) {
      if (!gravityPoint.expired()) {
        GravityPoint point = gravityPoint.point();
        drawer.draw(Color.ORANGE).marker().at(point);
        drawer.draw(Color.ORANGE).string(gravityPoint.duration()).at(point);
        drawer.draw(Color.ORANGE).string("%.2f", point.value()).at(point.up(15));
      }
    }
  }

}
