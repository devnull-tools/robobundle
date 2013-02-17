package atatec.robocode.parts;

import atatec.robocode.Enemy;
import atatec.robocode.Field;
import atatec.robocode.behaviour.Behaviour;

import java.util.Collection;

/** @author Marcelo Varella Barca Guimarães */
public interface Radar extends Part, ScanningSystem {

  Enemy lockedTarget();

  boolean hasLockedTarget();

  Collection<Enemy> knownEnemies();

  void lockTarget(Enemy enemy);

  Behaviour<ScanningSystem> scanningBehaviour();

  int enemiesCount();

  Field battleField();

  long time();

}
