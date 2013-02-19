package atatec.robocode;

import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.calc.Position;

/** @author Marcelo Varella Barca Guimarães */
public interface Enemy {
  Position position();

  double distance();

  double energy();

  Angle heading();

  Angle bearing();

  double velocity();

  String name();

  Point location();

  boolean isMoving();

  boolean isStopped();
}
