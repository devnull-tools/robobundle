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

  public GravityPoint pull() {
    duration--;
    if (duration < 0) {
      return null;
    }
    return point;
  }

}
