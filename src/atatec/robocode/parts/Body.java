package atatec.robocode.parts;

import atatec.robocode.behaviour.Behaviour;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;

/** @author Marcelo Varella Barca Guimarães */
public interface Body extends Part, MovingSystem {

  Body turnTo(Angle angle);

  Body turnTo(Point point);

  void moveTo(Point point);

  void move(double distance);

  void moveAndTurn(double distance, Angle angle);

  double energy();

  double velocity();

  Behaviour<MovingSystem> movingBehaviour();

  void moveTo(Point point, double distance);
}
