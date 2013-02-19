package atatec.robocode.parts;

import atatec.robocode.Conditional;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;

/** @author Marcelo Varella Barca Guimarães */
public interface Body extends Part {

  Body turnTo(Angle angle);

  Body turnTo(Point point);

  void moveTo(Point point);

  void move(double distance);

  void moveAndTurn(double distance, Angle angle);

  void move();

  double energy();

  double velocity();

  Conditional<MovingSystem> movingBehaviour();

  void moveTo(Point point, double distance);
}
