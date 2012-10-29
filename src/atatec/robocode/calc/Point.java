package atatec.robocode.calc;

import atatec.robocode.util.GravityPointBuilder;

import static atatec.robocode.calc.BotMath.areEquals;
import static atatec.robocode.calc.BotMath.convert;

/** @author Marcelo Varella Barca Guimarães */
public class Point {

  public static final Point ORIGIN = new Point(0, 0);

  private final double x, y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }

  public Point plus(Point other) {
    return new Point(x + other.x, y + other.y);
  }

  public Point minus(Point other) {
    return new Point(x - other.x, y - other.y);
  }

  public Position bearingTo(Point other) {
    return new Position(this, other);
  }

  public Position absoluteBearingTo(Point other) {
    Position position = bearingTo(other);
    double xo = other.x - x;
    double yo = other.y - y;
    double distance = position.distance();
    if (xo > 0 && yo > 0) {
      return new Position(Angle.inRadians(Math.asin(xo / distance)), distance);
    }
    if (xo > 0 && yo < 0) {
      return new Position(Angle.inRadians(Math.PI - Math.asin(xo / distance)), distance);
    }
    if (xo < 0 && yo < 0) {
      return new Position(Angle.inRadians(Math.PI + Math.asin(-xo / distance)), distance);
    }
    if (xo < 0 && yo > 0) {
      return new Position(Angle.inRadians(2.0 * Math.PI - Math.asin(-xo / distance)), distance);
    }
    return new Position(Angle.ZERO, distance);
  }

  public GravityPointBuilder gravitational() {
    return GravityPointBuilder.gravityPoint().at(this);
  }

  public GravityPointBuilder antiGravitational() {
    return GravityPointBuilder.antiGravityPoint().at(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Point point = (Point) o;

    return areEquals(x, point.x) && areEquals(y, point.y);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 37 * result + convert(x).hashCode();
    result = 37 * result + convert(y).hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("(%.5f , %.5f)", x, y);
  }

}
