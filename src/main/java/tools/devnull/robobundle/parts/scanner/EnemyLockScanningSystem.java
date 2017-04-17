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

package tools.devnull.robobundle.parts.scanner;

import tools.devnull.robobundle.Bot;
import tools.devnull.robobundle.Enemy;
import tools.devnull.robobundle.annotation.When;
import tools.devnull.robobundle.calc.Angle;
import tools.devnull.robobundle.condition.LockCondition;
import tools.devnull.robobundle.event.EnemyScannedEvent;
import tools.devnull.robobundle.parts.ScanningSystem;
import robocode.RobotDeathEvent;

import java.util.LinkedList;
import java.util.List;

import static tools.devnull.robobundle.event.Events.*;

/** @author Marcelo Guimarães */
public class EnemyLockScanningSystem implements ScanningSystem {

  private final Bot bot;

  private Angle turnAmount = Angle.TWO_PI;
  private boolean changeTarget = true; //always change target on first scan
  private boolean scanBattleField;

  private List<LockCondition> lockConditions = new LinkedList<LockCondition>();

  public EnemyLockScanningSystem(Bot bot) {
    this.bot = bot;
  }

  public EnemyLockScanningSystem addLockCondition(LockCondition condition) {
    this.bot.plug(condition);
    this.lockConditions.add(condition);
    return this;
  }

  @Override
  public void execute() {
    bot.radar().turn(turnAmount);
  }

  public EnemyLockScanningSystem scanBattleField() {
    scanBattleField = true;
    return this;
  }

  @When(ENEMY_SCANNED)
  public void onEnemyScanned(EnemyScannedEvent event) {
    Enemy enemy = event.enemy();
    bot.log("Enemy spotted at %s", enemy.position());
    if (canLock(enemy)) {
      bot.log("Locking %s", enemy.name());
      bot.radar().setTarget(enemy);
      changeTarget = false;
    }
    if (!scanBattleField) {
      // this will keep the radar on the target enemy
      turnAmount = turnAmount.inverse();
    }
  }

  @When(ROBOT_DEATH)
  public void onRobotDeath(RobotDeathEvent event) {
    if (bot.radar().hasTargetSet()) {
      if (event.getName().equals(bot.radar().target().name())) {
        changeTarget();
      }
    }
  }

  @When(TARGET_UNSET)
  public void changeTarget() {
    changeTarget = true;
    if (bot.radar().hasTargetSet()) {
      bot.radar().unsetTarget();
    }
  }

  private boolean canLock(Enemy enemy) {
    if (changeTarget) {
      for (LockCondition condition : lockConditions) {
        if (condition.canLock(enemy)) {
          return true;
        }
      }
    } else if (bot.radar().hasTargetSet()) {
      // locks the target if is the same last seen
      return bot.radar().target().name().equals(bot.radar().lastSeenEnemy().name());
    }
    //always lock if there is no condition
    return lockConditions.isEmpty();
  }

}
