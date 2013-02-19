package atatec.robocode.calc;

/** @author Marcelo Varella Barca Guimarães */
public class GravityPoint extends Point {

  private final double value;

  public GravityPoint(Point location, double value) {
    super(location.x(), location.y());
    this.value = value;
  }

  public GravityPoint(double x, double y, double value) {
    super(x, y);
    this.value = value;
  }

  public double value(){
    return value;
  }

  public Point force(Point reference) {
    Position bearing = bearingTo(reference);
    Angle angle = Angle.inRadians(Math.PI / 2 - Math.atan2(y() - reference.y(), x() - reference.x()));
    double distance = bearing.distance();
    double force = BotMath.areEquals(distance, 0) ?
      value : value / Math.pow(distance, 2);
    return new Point(angle.sin() * force, angle.cos() * force);
  }

  public TemporaryGravityPoint during(int time) {
    return new TemporaryGravityPoint(this, time);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    GravityPoint that = (GravityPoint) o;

    if (Double.compare(that.value, value) != 0) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    long temp;
    temp = value != +0.0d ? Double.doubleToLongBits(value) : 0L;
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s | %.4f", super.toString(), value);
  }
}

