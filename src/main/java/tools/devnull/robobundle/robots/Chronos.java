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

package tools.devnull.robobundle.robots;

import tools.devnull.robobundle.BaseBot;
import tools.devnull.robobundle.condition.BotConditions;
import tools.devnull.robobundle.condition.TargetConditions;
import tools.devnull.robobundle.parts.aiming.DirectAimingSystem;
import tools.devnull.robobundle.parts.aiming.LinearPredictionAimingSystem;
import tools.devnull.robobundle.parts.firing.EnergyBasedFiringSystem;
import tools.devnull.robobundle.parts.movement.EnemyCircleMovingSystem;
import tools.devnull.robobundle.parts.movement.FollowEnemyMovingSystem;
import tools.devnull.robobundle.parts.scanner.EnemyLockScanningSystem;
import tools.devnull.robobundle.plugin.BulletPaint;

import java.awt.*;

/** @author Marcelo Guimarães */
public class Chronos extends BaseBot {

  protected void configure() {
    body().setColor(new Color(39, 40, 34));
    gun().setColor(new Color(54, 151, 255));
    radar().setColor(new Color(39, 40, 34));

    TargetConditions target = new BotConditions(this).target();

    gun().forAiming()
      .use(new LinearPredictionAimingSystem(this))
      .when(target.isMoving())

      .use(new DirectAimingSystem(this))
      .asDefault();

    gun().forFiring()
      .use(new EnergyBasedFiringSystem(this));

    radar().forScanning()
      .use(new EnemyLockScanningSystem(this));

    body().forMoving()
      .use(new EnemyCircleMovingSystem(this))
      .when(target.isClose())

      .use(new FollowEnemyMovingSystem(this))
      .asDefault();

    plug(new BulletPaint(this)
      .use(new Color(255, 84, 84)).forStrong()
      .use(new Color(253, 151, 31)).forMedium()
      .use(new Color(54, 151, 255)).forWeak());
  }

  protected void onNextTurn() {
    log("***********************************");
    radar().scan();
    body().move();
    gun().aim().fireIfTargetSet();
  }

}
