package atatec.robocode;

import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.calc.Position;
import robocode.ScannedRobotEvent;

/** @author Marcelo Varella Barca Guimarães */
public class Enemy {

  private final String name;
  private final Position position;
  private final double distance;
  private final double energy;
  private final double velocity;
  private final Angle heading;
  private final Angle bearing;
  private final Point location;

  public Enemy(Bot bot, ScannedRobotEvent event) {
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
    Angle absoluteBearing = bot.body().heading().plus(bearing);
    Point botLocation = bot.location();
    double enemyX = botLocation.x() + distance * absoluteBearing.sin();
    double enemyY = botLocation.y() + distance * absoluteBearing.cos();
    this.location = new Point(enemyX, enemyY);
  }

  public Position position() {
    return position;
  }

  public double distance() {
    return distance;
  }

  public double energy() {
    return energy;
  }

  public Angle heading() {
    return heading;
  }

  public Angle bearing() {
    return bearing;
  }

  public double velocity() {
    return velocity;
  }

  public String name() {
    return name;
  }

  public Point location() {
    return location;
  }

  public boolean isMoving() {
    return velocity > 0;
  }

  public boolean isStopped() {
    return !isMoving();
  }

}
