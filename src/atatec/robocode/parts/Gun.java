package atatec.robocode.parts;

import atatec.robocode.calc.Point;

/** @author Marcelo Varella Barca Guimarães */
public interface Gun extends Part, AimingSystem, FiringSystem {

  Gun aimTo(Point point);

  void fire(double power);

  double heat();

  double coolingRate();

  Behaviour<AimingSystem> aimingBehaviour();

  Behaviour<FiringSystem> firingBehaviour();

}
