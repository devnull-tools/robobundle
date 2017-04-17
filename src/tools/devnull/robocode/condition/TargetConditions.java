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

package tools.devnull.robocode.condition;

import tools.devnull.robocode.Enemy;
import tools.devnull.robocode.parts.Radar;

/** @author Marcelo Guimarães */
public class TargetConditions {

  private final Radar radar;
  private double closerDistance;

  public TargetConditions(Radar radar) {
    double diagonal = radar.battleField().diagonal();
    this.radar = radar;
    this.closerDistance = diagonal / 4;
  }

  public Condition isMoving() {
    return new TargetCondition(radar) {
      @Override
      public boolean evaluate(Enemy enemy) {
        return enemy.isMoving();
      }
    };
  }

  public Condition isStopped() {
    return new TargetCondition(radar) {
      @Override
      public boolean evaluate(Enemy enemy) {
        return enemy.isStopped();
      }
    };
  }

  public Condition isClose() {
    return isAtMost(closerDistance);
  }

  public Condition isAtLeastAt(final double distance) {
    return new TargetCondition(radar) {
      public boolean evaluate(Enemy enemy) {
        return enemy != null && enemy.distance() > distance;
      }
    };
  }

  public Condition isAtMost(final double distance) {
    return new TargetCondition(radar) {
      public boolean evaluate(Enemy enemy) {
        return enemy != null && enemy.distance() < distance;
      }
    };
  }

  public Condition isBetween(final double minimum, final double maximum) {
    return new TargetCondition(radar) {
      public boolean evaluate(Enemy enemy) {
        double distance = enemy == null ? 0 : enemy.distance();
        return distance >= minimum && distance < maximum;
      }
    };
  }

}
