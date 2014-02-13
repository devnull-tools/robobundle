/************************************************************************************
 * The MIT License                                                                  *
 *                                                                                  *
 * Copyright (c) 2013 Marcelo Guimarães <ataxexe at gmail dot com>                  *
 * -------------------------------------------------------------------------------- *
 * Permission  is hereby granted, free of charge, to any person obtaining a copy of *
 * this  software  and  associated documentation files (the "Software"), to deal in *
 * the  Software  without  restriction,  including without limitation the rights to *
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of *
 * the  Software, and to permit persons to whom the Software is furnished to do so, *
 * subject to the following conditions:                                             *
 *                                                                                  *
 * The  above  copyright notice and this permission notice shall be included in all *
 * copies or substantial portions of the Software.                                  *
 *                            --------------------------                            *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS *
 * FOR  A  PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR *
 * COPYRIGHT  HOLDERS  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER *
 * IN  AN  ACTION  OF  CONTRACT,  TORT  OR  OTHERWISE,  ARISING  FROM, OUT OF OR IN *
 * CONNECTION  WITH  THE  SOFTWARE  OR  THE  USE OR OTHER DEALINGS IN THE SOFTWARE. *
 ************************************************************************************/

package atatec.robocode.parts;

import atatec.robocode.ConditionalCommand;
import atatec.robocode.Enemy;
import atatec.robocode.Field;

import java.util.Collection;

/**
 * Interface that defines a robot radar.
 * <p/>
 * A radar may use a {@link ScanningSystem} to manipulate
 * the radar movement.
 *
 * @author Marcelo Guimarães
 */
public interface Radar extends Part {

  /**
   * Gets the current target. Note that the current target may
   * not be a locked target.
   *
   * @return the current target
   */
  Enemy target();

  /**
   * Sets an enemy as the current target. If the radar
   * already has a target, it will be replaced by the
   * new one.
   *
   * @param enemy the enemy to set as target.
   */
  void setTarget(Enemy enemy);

  /**
   * Removes the current target
   */
  void unsetTarget();

  /**
   * Gets the last scanned enemy. The enemy returned may
   * not be the current target.
   *
   * @return the last scanned enemy.
   */
  Enemy lastSeenEnemy();

  /**
   * Checks if the radar has a target set
   *
   * @return <code>true</code> if the radar has a target set
   */
  boolean hasTargetSet();

  /**
   * Retrieves the scanned enemies. Note that some of them may have
   * out of date informations.
   *
   * @return the scanned enemies.
   */
  Collection<Enemy> knownEnemies();

  /**
   * Gets the register for the radar's {@link ScanningSystem}
   *
   * @return the register for the radar's {@link ScanningSystem}
   */
  ConditionalCommand<ScanningSystem> forScanning();

  /**
   * Uses an active {@link ScanningSystem} to move the radar
   */
  void scan();

  /**
   * Count the number of enemies in the battle field. This number
   * does not depends on scanned robots.
   *
   * @return the number of enemies in the battle field.
   */
  int enemiesCount();

  /**
   * Checks if the battle is a head to head.
   *
   * @return <code>true</code> if the battle field has only one enemy
   */
  boolean isHeadToHead();

  /**
   * Returns the current battle field
   *
   * @return the current battle field
   */
  Field battleField();

  /**
   * Returns the game time of the current round
   */
  long time();

  /**
   * Returns the last information about an enemy
   *
   * @param name enemy's name
   * @return the last information about the enemy
   */
  Enemy enemy(String name);

}
