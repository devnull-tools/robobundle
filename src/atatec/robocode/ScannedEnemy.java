package atatec.robocode;

import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.calc.Position;
import robocode.ScannedRobotEvent;

/** @author Marcelo Varella Barca Guimarães */
public class ScannedEnemy extends BaseEnemy {

  private final String name;
  private final Position position;
  private final double distance;
  private final double energy;
  private final double velocity;
  private final Angle heading;
  private final Angle bearing;
  private final Point location;

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
    Angle absoluteBearing = bot.body().heading().plus(bearing);
    Point botLocation = bot.location();
    double enemyX = botLocation.x() + distance * absoluteBearing.sin();
    double enemyY = botLocation.y() + distance * absoluteBearing.cos();
    this.location = new Point(enemyX, enemyY);
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

}
