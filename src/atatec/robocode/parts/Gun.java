package atatec.robocode.parts;

import atatec.robocode.Condition;
import atatec.robocode.ConditionalSystem;
import atatec.robocode.calc.Point;

import java.awt.Color;

/** @author Marcelo Varella Barca Guimarães */
public interface Gun extends Part {

  Gun aimTo(Point point);

  void fire(double power);

  Gun aim();

  void fireIfTargetLocked();

  void fireIf(Condition condition);

  void fire();

  double power();

  double heat();

  double coolingRate();

  ConditionalSystem<AimingSystem> aimingSystem();

  ConditionalSystem<FiringSystem> firingSystem();

  void setBulletColor(Color color);

}
