package atatec.robocode.calc;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/** @author Marcelo Varella Barca Guimarães */
public class Position {

  private final double distance;

  private final Angle angle;

  public Position(Angle angle, double distance) {
    this.angle = angle;
    this.distance = distance;
  }

  public Position(Point p1, Point p2) {
    this.distance = distance(p1, p2);
    Point p3 = new Point(p1.x(), p1.y() + 10);
    Triangle t = new Triangle(distance, distance(p1, p3), distance(p3, p2));
    Angle angle = t.angle(Triangle.ID.C);
    //using robocode convention
    this.angle = (p2.x() >= p1.x() ? angle : angle.inverse());
  }

  public double distance() {
    return distance;
  }

  public Angle angle() {
    return angle;
  }

  private double distance(Point p1, Point p2) {
    return sqrt(pow(p1.x() - p2.x(), 2) + pow(p1.y() - p2.y(), 2));
  }

  public String toString() {
    return String.format("(%.2f, %s)", distance, angle);
  }

}
