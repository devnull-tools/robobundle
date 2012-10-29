package atatec.robocode.parts.movement;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.Field;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.GravityPoint;
import atatec.robocode.calc.Point;
import atatec.robocode.calc.TemporaryGravityPoint;
import atatec.robocode.event.Events;
import atatec.robocode.parts.MovingSystem;
import atatec.robocode.util.GravityPointBuilder;
import robocode.HitWallEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static atatec.robocode.util.GravityPointBuilder.gravityPoint;

/** @author Marcelo Varella Barca Guimarães */
public class GravityMovingSystem implements MovingSystem {

  private final Bot bot;
  private Collection<GravityPoint> fixedPoints = new HashSet<GravityPoint>(100);
  private Collection<TemporaryGravityPoint> temporaryPoints = new HashSet<TemporaryGravityPoint>(100);

  private int midPointCount;
  private double midpointPower;

  public GravityMovingSystem(Bot bot) {
    this.bot = bot;
  }

  @When(Events.ADD_GRAVITY_POINT)
  public GravityMovingSystem add(GravityPoint point) {
    fixedPoints.add(point);
    return this;
  }

  @When(Events.ADD_TEMPORARY_GRAVITY_POINT)
  public GravityMovingSystem add(TemporaryGravityPoint point) {
    temporaryPoints.add(point);
    return this;
  }

  @Override
  public void move() {
    Collection<GravityPoint> gPoints = new ArrayList<GravityPoint>(fixedPoints);
    Iterator<TemporaryGravityPoint> iterator = temporaryPoints.iterator();
    while (iterator.hasNext()) {
      TemporaryGravityPoint temporaryGravityPoint = iterator.next();
      GravityPoint point = temporaryGravityPoint.point();
      if (point == null) {
        iterator.remove();
      } else {
        gPoints.add(point);
      }
    }
    initializePoints(gPoints);
    move(gPoints);
  }

  private void initializePoints(Collection<GravityPoint> gPoints) {
    Point location = bot.location();
    Field battleField = bot.radar().battleField();
    for (Enemy enemy : bot.radar().knownEnemies()) {
      gPoints.add(enemy.location().antiGravitational().withValue(40));
    }
    Point[] wallPoints = new Point[]{
      new Point(battleField.width(), location.y()),
      new Point(0, location.y()),
      new Point(location.x(), battleField.height()),
      new Point(location.x(), 0),
      new Point(location.x(), 0),
      battleField.upRight(),
      battleField.downLeft(),
      battleField.downRight(),
      battleField.upLeft(),
    };
    GravityPoint gravityPoint;
    for (Point wallPoint : wallPoints) {
      gravityPoint = wallPoint.antiGravitational().withValue(35);
      gPoints.add(gravityPoint);
    }
    addRandomPoints(gPoints, battleField);
  }

  private void addRandomPoints(Collection<GravityPoint> gPoints, Field battleField) {
    midPointCount++;
    if (midPointCount % 5 == 0) {
      midpointPower = (Math.random() * 20) - 10;
    }
    Point mod = new Point(
      (Math.random() * 30) - 15,
      (Math.random() * 30) - 15
    );
    gPoints.add(
      new GravityPointBuilder()
        .at(battleField.center().plus(mod))
        .withValue(midpointPower)
    );
  }

  private void move(Collection<GravityPoint> points) {
    Point location = bot.location();
    Point forcePoint = location;
    for (GravityPoint point : points) {
      forcePoint = forcePoint.plus(point.force(location));
    }
    bot.log("Location: %s", location);
    bot.log("Forced Location: %s", forcePoint);
    double move = 10;
    bot.body().moveTo(forcePoint, move);
  }

  @When(Events.HIT_WALL)
  public void onHitWall(HitWallEvent event) {
    bot.log("Adding gravity point to avoid wall");
    Field battleField = bot.radar().battleField();
    add(gravityPoint().at(battleField.center()).withValue(100).during(20));
  }

}
