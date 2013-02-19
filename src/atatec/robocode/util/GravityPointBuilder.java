package atatec.robocode.util;

import atatec.robocode.calc.GravityPoint;
import atatec.robocode.calc.Point;

/** @author Marcelo Varella Barca Guimarães */
public class GravityPointBuilder {

  private Point point;
  private int mod;

  private GravityPointBuilder(int mod) {
    this.mod = mod;
  }

  public GravityPointBuilder at(Point point) {
    this.point = point;
    return this;
  }

  public GravityPoint withValue(double value) {
    return new GravityPoint(point, mod * value);
  }

  public static GravityPointBuilder antiGravityPoint() {
    return new GravityPointBuilder(-1);
  }

  public static GravityPointBuilder gravityPoint() {
    return new GravityPointBuilder(1);
  }

}
