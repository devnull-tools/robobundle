package atatec.robocode.calc;

/** @author Marcelo Varella Barca Guimarães */
public class TemporaryGravityPoint {

  private int duration;
  private GravityPoint point;

  public TemporaryGravityPoint(GravityPoint point, int duration) {
    this.duration = duration;
    this.point = point;
  }

  public GravityPoint point() {
    return point;
  }

  public int duration() {
    return duration;
  }

  public GravityPoint pull() {
    duration--;
    return point;
  }

  public boolean expired() {
    return duration < 0;
  }

  @Override
  public String toString() {
    return String.format("%s | %d", point.toString(), duration);
  }

}
