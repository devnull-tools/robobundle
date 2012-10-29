package atatec.robocode.calc;

/** @author Marcelo Varella Barca Guimarães */
public class Movement {

  private Angle angle = Angle.ZERO;

  private double distance;

  private boolean absoluteAngle;

  public Movement() {

  }

  public Movement ahead(double distance) {
    this.distance = distance;
    return this;
  }

  public boolean isAbsoluteAngle() {
    return absoluteAngle;
  }

  public Movement turnTo(Angle angle) {
    this.absoluteAngle = true;
    this.angle = angle;
    return this;
  }

  public Movement turn(Angle angle) {
    this.absoluteAngle = false;
    this.angle = angle;
    return this;
  }

  public Angle angle() {
    return angle;
  }

  public double distance() {
    return distance;
  }

}
