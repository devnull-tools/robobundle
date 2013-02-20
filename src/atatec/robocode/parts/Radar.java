package atatec.robocode.parts;

import atatec.robocode.ConditionalSystem;
import atatec.robocode.Enemy;
import atatec.robocode.Field;

import java.util.Collection;

/** @author Marcelo Varella Barca Guimarães */
public interface Radar extends Part {

  Enemy lockedTarget();

  boolean hasLockedTarget();

  Collection<Enemy> knownEnemies();

  void lockTarget(Enemy enemy);

  void unlockTarget();

  ConditionalSystem<ScanningSystem> scanningSystem();

  void scan();

  int enemiesCount();

  Field battleField();

  long time();
}
